package org.kevin.app.bookcrawler

import org.jsoup.Jsoup
import scala.collection.JavaConversions._


class SevenCaiMi2Processor(crawler: Crawler2) extends AbstractProcessor(crawler) {

    override var storePath = "/Users/ky54/Documents/Novel/Jueshishenyi.txt"

    override protected def extractContentAndLinks(html: String): (String, String, List[String]) = {
        val htmlObj = Jsoup.parse(html)
        val c = htmlObj.select("div.chapter").first
        val title = if(c != null) c.select("h1").first.text else ""
        val content = if(c != null) c.select(".chapter div").first.html else ""
        val alinks = htmlObj.select("a[href]").map(_.attr("href")).toList
        return (title, content, alinks)
    }

    override def purifyTitleAndContent(title: String, content: String): String = {
        val contentAfter = content.replaceAll("(<br />|<br>)+","\n")
                                .replaceAll("&nbsp;+|\t+", "")
                                .replaceAll("""<[^>]*>""", "")

            s"${title}\n${contentAfter}\n\n"
    }
}