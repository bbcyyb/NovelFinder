package org.kevin.app.bookcrawler

object Main {
  def main(args: Array[String]) {
    //val url = "http://www.7caimi.com/xiaoshuo/13/"
    //val crawler = new Crawler2(url, filter = uri => uri.contains(url))
    //crawler.crawl()

    new SevenCaiMi(Set("大主宰","斗罗大陆","莽荒纪","十方神王")).process
  }
}
