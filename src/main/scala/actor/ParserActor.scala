package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorContext, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler.{AbstractProcessor, Common}
import java.util.UUID

object ParserActor {
    // 解析当前html content中的内容和链接
    case class Parsing(url: String, htmlString: String)
    // 如果当前URL还未被爬取过，那么交由CrawlerActor继续爬取
    case class UrlNonExisting(url: String)

    var storerActorRef: ActorRef = null

    def propsStorerActor(processor: AbstractProcessor, masterPath: String) = Props(new StorerActor(processor, masterPath))
    def propsCrawlerActor(processor: AbstractProcessor, masterPath: String) = Props(new CrawlerActor(processor, masterPath))
}

class ParserActor(processor: AbstractProcessor, masterRefPath: String) extends Actor {

    def receive = {

        case ParserActor.Parsing(url: String, htmlString: String) => {
            val result = processor.parse(url, htmlString)
            val section = result._1
            val alinks = result._2

            if(ParserActor.storerActorRef == null) {
                ParserActor.storerActorRef = context.actorOf(ParserActor.propsStorerActor(processor, masterRefPath), "StorerActor")
            }

            if(!section.isEmpty) {
                ParserActor.storerActorRef ! StorerActor.Collecting(url, section)
            }

            for(a <- alinks.distinct) {
                ParserActor.storerActorRef ! StorerActor.Checking(a)
            }
        }

        case ParserActor.UrlNonExisting(url: String) => {
            val uuid = UUID.randomUUID().toString()
            val actorRef = context.actorOf(ParserActor.propsCrawlerActor(processor, masterRefPath), name = s"CrawlerActor_${uuid}")
            actorRef ! CrawlerActor.Crawling(url)
        }
    }
}