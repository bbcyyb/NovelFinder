package org.kevin.app.bookcrawler

object Main {
  def main(args: Array[String]) {
    val url = "http://www.7caimi.com/xiaoshuo/13/"
    val crawler = new Crawler2(url, filter = uri => uri.contains(url))
    crawler.crawl()
  }
}
