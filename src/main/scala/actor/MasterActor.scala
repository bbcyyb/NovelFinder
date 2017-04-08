package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}

object MasterActor {
    case class Starting(basicUrl: String)

    def propsStorerActor(masterRef: ActorRef): Props = Props(new StorerActor(masterRef))

    def propsCrawlerActor(storerRef: ActorRef): Props = Props(new CrawlerActor(storerRef))
}

class MasterActor extends Actor {

    val storerActorRef = context.actorOf(MasterActor.propsStorerActor(self),"StorerActor")

    def receive = {

        case "start" => {
            println("start MasterActor")
            val actorRef = context.actorOf(Props[CrawlerActor],"CrawlerActor")
            println("Master say hello to Crawler")
            //actorRef ! "hello"
        }
        case "call the roll" => {
            println("[MasterActor] Master call the roll")
            (1 to 3).foreach{
                num =>
                println(num)
                val actorRef = context.actorOf(MasterActor.propsCrawlerActor(storerActorRef), name = s"CrawlerActor_${num}")
                println(s"[MasterActor] ${actorRef}")
                actorRef ! "call the roll"
            }
        }
        case "over" => {
            println("Master received")
            println("System Shutdown!")
            context.system.shutdown
            // self ! PoisonPill
        }

        case MasterActor.Starting(basicUrl: String) => {

        }
    }
}