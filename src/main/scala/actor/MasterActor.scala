package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler._

object MasterActor {
    case class Starting(basicUrl: String)

    def propsStorerActor(masterRef: ActorRef): Props = Props(new StorerActor(masterRef))

    def propsCrawlerActor(storerRef: ActorRef): Props = Props(new CrawlerActor(storerRef))
}

class MasterActor extends Actor {

    val storerActorRef = context.actorOf(MasterActor.propsStorerActor(self),"StorerActor")

    def receive = {

        case "over" => {
            println("Master received")
            println("System Shutdown!")
            context.system.shutdown
            // self ! PoisonPill
        }

        case MasterActor.Starting(basicUrl: String) => {
            val actorRef = context.actorOf(MasterActor.propsCrawlerActor(storerActorRef), name = s"CrawlerActor_basic")
            actorRef ! CrawlerActor.Crawling(basicUrl, basicUrl)
        }
    }
}