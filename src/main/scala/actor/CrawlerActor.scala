package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler._

object CrawlerActor {
    case class Crawling(url: String, basicUrl: String)

    def propsParserActor(masterPath: String): Props = Props(new ParserActor(masterPath))

    val crawler = new Crawler2
}

class CrawlerActor(masterRefPath: String) extends Actor {

    def receive = {

        case CrawlerActor.Crawling(url: String, basicUrl: String) => {
            val content = CrawlerActor.crawler.crawl(url)
            val actorRef = context.actorOf(CrawlerActor.propsParserActor(masterRefPath),  "ParserActor_baslc")
            actorRef ! ParserActor.Parsing(url, content, basicUrl)
        }
    }
}