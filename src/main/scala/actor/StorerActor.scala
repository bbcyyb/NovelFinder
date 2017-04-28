package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, ActorPath, ActorRef, Props, PoisonPill}
import scala.collection.mutable
import org.kevin.app.bookcrawler.{Crawler2, Common}

object StorerActor {
    case class Saving(linksAndContent: mutable.HashMap[String, String])
    case class Collecting(url: String, section: String)
    case class Checking(url: String, basicUrl: String)

    val crawler = new Crawler2
    val saveAs: String = "~c/Documents/Novel/DaZhuZai.txt"
}

class StorerActor(masterRefPath: String) extends Actor {

    val list = new mutable.ListBuffer[String]()
    val map = new mutable.HashMap[String,String]()
    // 初始化为1，是 因为root的url也要算作一个
    var counter: Int = 1

    def receive = {
        case StorerActor.Collecting(url: String, section: String) => {

            Common.log(s"counter --: ${counter}")
            if(map.contains(url)) {
                counter -= 1
                map += (url -> section)
                Common.log(s"${self.path.name}  -------->   Current map pool has ${map.size} records. Counter size is ${counter}")
            }
            
            if(0 == counter) {
                self ! StorerActor.Saving
                Common.log(s"${self.path.name} : Saving => ${self.path.name}")
            } else if(counter < 0) {
                Common.log("Counter < 0")
            }
        }

        case StorerActor.Checking(url: String, basicUrl: String) => {
            if(!map.contains(url)) {
                map += (url -> "")
                counter += 1
                Common.log(s"counter ++: ${counter}")
                sender() ! ParserActor.UrlNonExisting(url, basicUrl)
            } else {
                //Common.log(s"${self.path.name} : has already Existed in => ${sender().path.name} %% url: ${url} | basicUrl： ${basicUrl})")
            }
        }

        case StorerActor.Saving => {
            StorerActor.crawler.store(map, StorerActor.saveAs)
            context.actorSelection(masterRefPath) ! MasterActor.Ending()
        }        
    }
}
