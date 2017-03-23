package org.kevin.app.bookcrawler

object Main {
  def main(args: Array[String]) {
    val url = "http://www.7caimi.com/xiaoshuo/13/"

    val crawler = new Crawler2(url)
    var result = crawler.getPageFromRemote(url)
    val ss = crawler.fetchLinks(result._2)
    println(ss.getClass)

  }
}
