package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, Props, PoisonPill}

object MasterActor {
    case class Starting(basicUrl: String)
}

class MasterActor extends Actor {

    def receive = {
        case MasterActor.Starting(basicUrl: String) => {

        }
        case "start" => {
            println("start MasterActor")
            val actorRef = context.actorOf(Props[CrawlerActor],"CrawlerActor")
            println("Master say hello to Crawler")
            actorRef ! "hello"
        }
        case "over" => {
            println("Master receive Crawler said")
            self ! PoisonPill
        }
    }
}