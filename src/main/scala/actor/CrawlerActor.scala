package org.kevin.app.bookcrawler.actor

import akka.actor.{Actor, Props, PoisonPill}

object CrawlerActor {
    case class Crawing(url: String)
}

class CrawlerActor extends Actor {

    def receive = {
        case CrawlerActor.Crawing(url: String) => {

        }
    }
}