package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import scala.collection.mutable

object StorerActor {
    case class Saving(linksAndContent: mutable.HashMap[String, String])

    
}

class StorerActor(masterRef: ActorRef) extends Actor {

    val list = new mutable.ListBuffer[String]()
    println(masterRef)
    //val masterRef = context.actorSelection(masterPath)

    def receive = {
        case actorName: String => {
            list += (actorName + ",")
            println(list)
            if(list.size == 3){
                println(s"[StorerActor] ${masterRef}")
                masterRef ! "over"
            }
        }
        case StorerActor.Saving(linksAndContent: mutable.HashMap[String, String]) => {

        }
    }
}