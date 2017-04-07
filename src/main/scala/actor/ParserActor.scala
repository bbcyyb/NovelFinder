package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, Props, PoisonPill}

object ParserActor {
    case class Parsing(hostName: String, htmlString: String)
}

class ParserActor extends Actor {
    
    def receive = {
        case "hello" => {
            println("ParserActor")
            println(self.parent)
            sender ! "over"
        }
        case ParserActor.Parsing(hostName: String, htmlString: String) => {

        }
    }
}