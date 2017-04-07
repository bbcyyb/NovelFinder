package org.kevin.app.bookcrawler

import scala.collection.JavaConversions._
import scala.collection.mutable
import java.net.URL
import org.jsoup.Jsoup

class Crawler2 {

    private def getHostBase(url: String) = {
        val uri = new URL(url)
        val portPart = if(uri.getPort() == -1 || uri.getPort() == 80) "" else ":" + uri.getPort()
        uri.getProtocol() + "://" + uri.getHost() + portPart
    }

    def crawl(basicUrl: String) = {
        val html = Jsoup.connect(basicUrl).get().html
        val host = getHostBase(basicUrl)
        (host, html)
    }

    def parse(hostName: String, htmlString: String, linkFilter: (String => Boolean) = (url: String) => true): (String, String, List[String]) = {

        val html = Jsoup.parse(htmlString)
        val c = html.select("div.chapter").first
        val title = if(c != null) c.select("h1").first.text else ""
        val content = if(c != null) c.select("div").first.html else ""
        val alinks = html.select("a[href]").map(_.attr("href")).filter(
            link => !link.startsWith("javascript:")
            ).map(
            link =>
            link match {
                case link if link.startsWith("//") => "http://" + link
                case link if link.startsWith("/") => hostName + link
                case link if link.startsWith("http://") || link.startsWith("https://") => link
                case _ => link
            }
            ).filter(linkFilter).toList
        
        return (title, content, alinks)
    }

    def store = {

    }
}