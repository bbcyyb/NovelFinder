package org.kevin.app.bookcrawler

import scala.collection.JavaConversions._
import scala.collection.mutable
import java.net.URL
import org.jsoup.Jsoup

class Crawler2 {

    private val protocol = "http://"

    private def getHostBase(url: String) = {
        val uri = new URL(url)
        val portPart = if(uri.getPort() == -1 || uri.getPort() == 80) "" else ":" + uri.getPort()
        uri.getProtocol() + "://" + uri.getHost() + portPart
    }

    def crawlAndParse(basicUrl: String, linkFilter: (String => Boolean) = (url: String) => true): (String, String, List[String]) = {

        val html = Jsoup.connect(basicUrl).get()

        val c = html.select("div.chapter").first
        var title = ""
        var content = ""
        if(c != null) {
            val title = c.select("h1").first.text
            val content = c.select("div").first.html
        }
        val alinks = html.select("a[href]").map(_.attr("href")).filter(
            link => !link.startsWith("javascript:")
            ).map(
            link =>
            link match {
                case link if link.startsWith("/") => getHostBase(basicUrl) + link
                case link if link.startsWith("http://") || link.startsWith("https://") => link
                case _ => link
            }
            ).filter(linkFilter).toList
        

        //.map(_.attr("href")).toList

        return (title, "content", alinks)
    }

    def store = {

    }
}