package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import org.kevin.app.bookcrawler.{Common}

object MasterActor {  
    case class Starting(basicUrl: String)

    case class Ending()

    def propsCrawlerActor(masterPath: String): Props = Props(new CrawlerActor(masterPath))

    var masterRefPath: String = ""
}

class MasterActor extends Actor {

    def receive = {

        case MasterActor.Ending() => {
            println("Master received")
            println("System Shutdown!")
            context.system.shutdown
            // self ! PoisonPill
        }

        case MasterActor.Starting(basicUrl: String) => {
            val actorRef = context.actorOf(MasterActor.propsCrawlerActor(self.path.toString), name = "CrawlerActor_Basic")
            actorRef ! CrawlerActor.Crawling(basicUrl, basicUrl)
            Common.log(s"${self.path.name} : Crawling => ${actorRef.path.name} %% url: ${basicUrl} | basicUrl: ${basicUrl}")
        }
    }
}