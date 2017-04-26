package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler.{Crawler2, Common}
import java.util.UUID

object CrawlerActor {
    case class Crawling(url: String, basicUrl: String)

    def propsParserActor(masterPath: String): Props = Props(new ParserActor(masterPath))

    val crawler = new Crawler2
}

class CrawlerActor(masterRefPath: String) extends Actor {

    def receive = {

        case CrawlerActor.Crawling(url: String, basicUrl: String) => {
            val content = CrawlerActor.crawler.crawl(url)
            val uuid = UUID.randomUUID().toString()
            val actorRef = context.actorOf(CrawlerActor.propsParserActor(masterRefPath), s"ParserActor_${uuid}")
            actorRef ! ParserActor.Parsing(url, content, basicUrl)
            Common.log(s"${self.path.name} : Parsing => ${actorRef.path.name} %% url: ${url}, htmlString: ~, basicUrl: ${basicUrl})")
        }
    }
}