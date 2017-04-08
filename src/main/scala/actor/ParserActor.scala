package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler._

object ParserActor {
    case class Parsing(hostName: String, htmlString: String, basicUrl: String)

    val crawler = new Crawler2
}

class ParserActor(storerRef: ActorRef) extends Actor {
    
    def receive = {
        
        case ParserActor.Parsing(hostName: String, htmlString: String, basicUrl: String) => {
            val result = ParserActor.crawler.parse(hostName, htmlString, f => f.contains(basicUrl) && f != basicUrl)
            val title = result._1
            val content = result._2
            val alinks = result._3

            if(!title.isEmpty && !content.isEmpty) {
                storerRef ! StorerActor.Collecting(title, content)
            }
        }
    }
}