package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorContext, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler.{Crawler2, Common}
import java.util.UUID

object ParserActor {
    // 解析当前html content中的内容和链接
    case class Parsing(url: String, htmlString: String, basicUrl: String)
    // 如果当前URL还未被爬取过，那么交由CrawlerActor继续爬取
    case class UrlNonExisting(url: String, basicUrl: String)

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
                 Common.log(s"${self.path.name} : Collecting => ${ParserActor.storerActorRef.path.name} %% url: ${url} | section.title: ${title} | selectin.content: ~)")
            }

            for(a <- alinks) {
                ParserActor.storerActorRef ! StorerActor.Checking(a, basicUrl)
                Common.log(s"${self.path.name} : Checking => ${ParserActor.storerActorRef.path.name} %% url: ${a} | basicUrl: ${basicUrl}")
            }
        }
        case ParserActor.UrlNonExisting(url: String, basicUrl: String) => {
            val uuid = UUID.randomUUID().toString()
            val actorRef = context.actorOf(ParserActor.propsCrawlerActor(masterRefPath), name = s"CrawlerActor_${uuid}")
            actorRef ! CrawlerActor.Crawling(url, basicUrl)
            Common.log(s"${self.path.name} : Crawling => ${actorRef.path.name} %% url: ${url} | basicUrl: ${basicUrl}")
        }
    }
}