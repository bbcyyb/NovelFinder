package org.kevin.app.bookcrawler

object MovelFinder {
  def main(args: Array[String]): Unit = {
    val crawler = new Crawler2
    val basicUrl = "http://www.7caimi.com/xiaoshuo/13/"
    val crawlResult = crawler.crawl(basicUrl)
    val result = crawler.parse(crawlResult._1, crawlResult._2, f => f.contains(basicUrl) && f != basicUrl)
    println(result)
    
    //new SevenCaiMi(Set("大主宰")).process
  }
}
