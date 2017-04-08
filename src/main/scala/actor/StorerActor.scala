package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import scala.collection.mutable
import org.kevin.app.bookcrawler._

object StorerActor {
    case class Saving(linksAndContent: mutable.HashMap[String, String])
    case class Collecting(url: String, section: (String, String))
    case class Cycling()

    val crawler = new Crawler2
}

class StorerActor(masterRef: ActorRef) extends Actor {

    val list = new mutable.ListBuffer[String]()
    val map = new mutable.HashMap[String,(String, String)]()

    def receive = {
        case actorName: String => {
            list += (actorName + ",")
            println(list)
            if(list.size == 3){
                println(s"[StorerActor] ${masterRef}")
                masterRef ! "over"
            }
        }
        case StorerActor.Collecting(url: String, section: (String, String)) => {
            if(!map.containsKey(url)) {
                map += (url -> section)
            }
        }
        case StorerActor.Cycling => {
            
        }
        case StorerActor.Saving(linksAndContent: mutable.HashMap[String, String]) => {

        }        
    }
}
