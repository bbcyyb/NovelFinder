package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler._

object CrawlerActor {
    case class Crawing(url: String, basicUrl: String)

    val crawler = new Crawler2
}

class CrawlerActor(storerRef: ActorRef) extends Actor {

    def receive = {
        case CrawlerActor.Crawing(url: String, basicUrl: String) => {
            val content = CrawlerActor.crawler.crawl(url)
            val actorRef = context.actorOf(Props(new ParserActor),  "ParserActor_baslc")
            actorRef ! ParserActor.Parsing(content._1, content._2, basicUrl)
        }
    }
}