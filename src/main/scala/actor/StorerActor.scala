package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import scala.collection.mutable
import org.kevin.app.bookcrawler.{Crawler2, Common}

object StorerActor {
    case class Saving(linksAndContent: mutable.HashMap[String, String])
    case class Collecting(url: String, section: (String, String))
    case class Checking(url: String, basicUrl: String)

    val crawler = new Crawler2
    val saveAs: String = "~c/Documents/Novel/DaZhuZai.txt"
}

class StorerActor(masterRefPath: String) extends Actor {

    val list = new mutable.ListBuffer[String]()
    val map = new mutable.HashMap[String,(String, String)]()
    // 初始化为1，是 因为root的url也要算作一个
    var counter: Int = 1

    def receive = {
        case StorerActor.Collecting(url: String, section: (String, String)) => {
            if(!map.contains(url)) {
                map += (url -> section)
                Common.log(s"-------->   Current map pool has ${map.size} records.")
            }

            counter -= 1
            if(0 == counter) {
                self ! StorerActor.Saving
                Common.log(s"${self.path.name} : Saving => ${self.path.name}")
            } else if(counter < 0) {
                Common.log("Counter < 0")
            }
        }

        case StorerActor.Checking(url: String, basicUrl: String) => {
            if(!map.contains(url)) {
                counter += 1
                sender() ! ParserActor.UrlNonExisting(url, basicUrl)
            } else {
                Common.log(s"${self.path.name} : has already Existed in => ${sender().path.name} %% url: ${url} | basicUrl： ${basicUrl})")
            }
        }

        case StorerActor.Saving => {
            // crawler.store(map, )
            context.actorSelection(masterRefPath) ! MasterActor.Ending()
        }        
    }
}
