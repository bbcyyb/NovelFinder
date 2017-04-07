package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, Props, PoisonPill}

object CrawlerActor {
    case class Crawing(url: String)
}

class CrawlerActor extends Actor {

    def receive = {
        case "hello" => {
            println("Crawler receive hello message")
            println("Crawler say hi, how are you to Master")
            val actorRef = context.actorOf(Props[ParserActor],  "ParserActor")
            actorRef ! "hello"
        }
        case "over" => {
            println("Master received")
            self ! PoisonPill
        }
        case CrawlerActor.Crawing(url: String) => {

        }
    }
}