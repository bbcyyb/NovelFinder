package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import scala.collection.mutable
import org.kevin.app.bookcrawler.{Crawler2, Common}

object StorerActor {
    case class Saving(linksAndContent: mutable.HashMap[String, String])
    case class Collecting(url: String, section: (String, String))
    case class Checking(url: String, basicUrl: String)
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
                Common.log(s"-------->   Current map pool has ${map.size} records.")
            }
        }

        case StorerActor.Cycling => {
            
        }

        case StorerActor.Checking(url: String, basicUrl: String) => {
            if(!map.contains(url)) {
                sender() ! ParserActor.UrlNonExisting(url, basicUrl)
            } else {
                Common.log(s"${self.path.name} : has already Existed in => ${sender().path.name} %% url: ${url} | basicUrl： ${basicUrl})")
            }
        }

        case StorerActor.Saving(linksAndContent: mutable.HashMap[String, String]) => {

            context.actorSelection(masterRefPath) ! MasterActor.Ending()
        }        
    }
}
