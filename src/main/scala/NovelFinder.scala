package org.kevin.app.bookcrawler

import org.kevin.app.bookcrawler.actor._

import akka.actor.{Actor, ActorSystem, Props, PoisonPill}

object MovelFinder {
  def main(args: Array[String]): Unit = {
    val crawler = new Crawler2
    val basicUrl = "http://www.7caimi.com/xiaoshuo/9/"
    // val crawlResult = crawler.crawl(basicUrl)
    // val result = crawler.parse(crawlResult._1, crawlResult._2, f => f.contains(basicUrl) && f != basicUrl)
    // println(result)
    
    //new SevenCaiMi(Set("大主宰")).process

        val system = ActorSystem("ActorSystem")
        val processor: AbstractProcessor = new SevenCaiMi2Processor(new Crawler2)
        val actorRef = system.actorOf(Props(new MasterActor(processor)), name = "MyActor")
        actorRef ! MasterActor.Starting(basicUrl)

        //system.shutdown
        Common.log("system runing")
  }
}
