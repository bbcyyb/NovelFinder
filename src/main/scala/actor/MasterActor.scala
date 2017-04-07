package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, Props, PoisonPill}

object MasterActor {
    case class Starting(basicUrl: String)
}

class MasterActor extends Actor {

    def receive = {
        case MasterActor.Starting(basicUrl: String) => {

        }
    }
}