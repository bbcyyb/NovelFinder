package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler.{AbstractProcessor, Common}
import java.util.UUID

object CrawlerActor {
    case class Crawling(url: String)

    def propsParserActor(processor: AbstractProcessor, masterPath: String): Props = Props(new ParserActor(processor, masterPath))
}

class CrawlerActor(processor: AbstractProcessor, masterRefPath: String) extends Actor {

    def receive = {

        case CrawlerActor.Crawling(url: String) => {
            val content = processor.crawl(url)
            val uuid = UUID.randomUUID().toString()
            val actorRef = context.actorOf(CrawlerActor.propsParserActor(processor, masterRefPath), s"ParserActor_${uuid}")
            actorRef ! ParserActor.Parsing(url, content)
        }
    }
}