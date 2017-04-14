package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorContext, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler._

object ParserActor {
    case class Parsing(url: String, htmlString: String, basicUrl: String)

    var storerActorRef: ActorRef = null

    def propsStorerActor(masterPath: String) = Props(new StorerActor(masterPath))
    def propsCrawlerActor(masterPath: String) = Props(new CrawlerActor(masterPath))

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
                if(ParserActor.storerActorRef == null) {
                    ParserActor.storerActorRef = context.actorOf(ParserActor.propsStorerActor(masterRefPath), "StorerActor")
                }
                 ParserActor.storerActorRef ! StorerActor.Collecting(url, (title, content))
            }

            for(a <- alinks) {
                val actorRef = context.actorOf(ParserActor.propsCrawlerActor(masterRefPath), name = "CrawlerActor_")
                actorRef ! CrawlerActor.Crawling(a, basicUrl)
            }
        }
    }
}