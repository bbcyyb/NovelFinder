package org.kevin.app.bookcrawler

object MovelFinder {
  def main(args: Array[String]): Unit = {
    val crawler = new Crawler2
    val basicUrl = "http://www.7caimi.com/xiaoshuo/13/"
    val result = crawler.crawlAndParse(basicUrl, f => f.contains(basicUrl))
    println(result)
    
    //new SevenCaiMi(Set("大主宰")).process
  }
}
