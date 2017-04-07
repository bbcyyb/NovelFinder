package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, Props, PoisonPill}
import scala.collection.mutable

object StorerActor {
    case class Saving(linksAndContent: mutable.HashMap[String, String])
}

class StorerActor extends Actor {

    def receive = {
        case StorerActor.Saving(linksAndContent: mutable.HashMap[String, String]) => {

        }
    }
}