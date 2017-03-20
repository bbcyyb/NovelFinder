package org.kevin.app.bookcrawler

object Main {
  def main(args: Array[String]) {
    val url = "http://www.7caimi.com/xiaoshuo/13/"

    val crawler = new Crawler2(url)
    // var result = crawler.getPageFromRemote(url)
    // crawler.fetchLinks()
    // println(result)

    (() => {
      var list = for(i <- 1 until 10) {
        i
      }

      println(list)
    })()
  }
}
