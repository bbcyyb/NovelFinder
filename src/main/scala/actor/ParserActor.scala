package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorContext, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler._

object ParserActor {
    case class Parsing(url: String, htmlString: String, basicUrl: String)

    var _singleStoreProps: ActorRef = null
    def singleStoreProps(context: ActorContext, masterPath: String): ActorRef = {
        if(_singleStoreProps == null) {
            _singleStoreProps = context.actorOf(propsStorerActor(masterPath), "Store")
        }
        return _singleStoreProps
    }

    def propsStorerActor(masterPath: String) = Props(new StorerActor(masterPath))

    val crawler = new Crawler2
}

class ParserActor(masterRefPath: String) extends Actor {

    def receive = {

        case ParserActor.Parsing(url: String, htmlString: String, basicUrl: String) => {
            val result = ParserActor.crawler.parse(url, htmlString, f => f.contains(basicUrl) && f != basicUrl)
            val title = result._1
            val content = result._2
            val alinks = result._3

            if(!title.isEmpty && !content.isEmpty) {
                ParserActor.singleStoreProps(context, masterRefPath) ! StorerActor.Collecting(url, (title, content))
            }
        }
    }
}