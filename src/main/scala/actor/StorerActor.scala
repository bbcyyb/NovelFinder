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

class StorerActor(masterRefPath: String) extends Actor {

    val list = new mutable.ListBuffer[String]()
    val map = new mutable.HashMap[String,(String, String)]()

    def receive = {
        case StorerActor.Collecting(url: String, section: (String, String)) => {
            if(!map.contains(url)) {
                map += (url -> section)
            }
        }
        case StorerActor.Cycling => {
            
        }
        case StorerActor.Saving(linksAndContent: mutable.HashMap[String, String]) => {

            context.actorSelection(masterRefPath) ! MasterActor.Ending()
        }        
    }
}
