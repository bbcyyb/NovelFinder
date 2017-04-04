package org.kevin.app.bookcrawler

object MovelFinder {
  def main(args: Array[String]): Unit = {
    //val url = "http://www.7caimi.com/xiaoshuo/13/"
    //val crawler = new Crawler(url, filter = uri => uri.contains(url))
    //crawler.crawl()
    
    new SevenCaiMi(Set("大主宰")).process
  }
}
