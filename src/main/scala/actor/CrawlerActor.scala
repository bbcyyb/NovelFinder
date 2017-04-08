package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}

object CrawlerActor {
    case class Crawing(url: String)
}

class CrawlerActor(storerRef: ActorRef) extends Actor {

    def receive = {
        case "hello" => {
            println("Crawler receive hello message")
            println("Crawler say hi, how are you to Master")
            val actorRef = context.actorOf(Props[ParserActor],  "ParserActor")
            actorRef ! "hello"
        }
        case "call the roll" => {
            println(s"[CrawlerActor] ${self}")
            storerRef ! self.toString

        }
        case "over" => {
            println("Thread.sleep(100)")
            Thread.sleep(100)
            println("Crawler received")
        }
        case CrawlerActor.Crawing(url: String) => {

        }
    }
}