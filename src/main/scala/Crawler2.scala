package org.kevin.app.bookcrawler

import scala.collection.JavaConversions._
import scala.collection.mutable
import java.net.URL
import java.io._
import org.jsoup.Jsoup

class Crawler2 {

    private val TIME_OUT: Int = 18000

    private def getHostBase(url: String) = {
        val uri = new URL(url)
        val portPart = if(uri.getPort() == -1 || uri.getPort() == 80) "" else ":" + uri.getPort()
        uri.getProtocol() + "://" + uri.getHost() + portPart
    }

    def crawl(basicUrl: String) = {
        var html: String = s"url: ${basicUrl} 访问失败"
        // 三次访问，如果失败，则不再继续处理
        for(i <- (1 to 3)) {
            try {
                html = Jsoup.connect(basicUrl).maxBodySize(0).timeout(TIME_OUT).get().html
            } catch {
                case e: Exception => println(s"exception caught: ${e}, url: ${basicUrl}")
            }
        }
        html
    }

    def parse(currentUrl: String, htmlString: String, linkFilter: (String => Boolean) = (url: String) => true): (String, String, List[String]) = {

        val hostName = getHostBase(currentUrl)
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

    def store(linksAndContent: mutable.HashMap[String, String], outputPath: String) = {

        var counter: Int = 0
        var file: File = null
        do {
            val increasedOutputPath = Common.getIncreasingFileName(outputPath, counter)
            file = new File(increasedOutputPath)    
        } while(file.exists())

        //TODO: 由于上面的逻辑，文件必定不存在，所以这里需要先创建文件，再打开句柄

        val writer = new BufferedWriter(new FileWriter(file))

        var iter = linksAndContent.keySet.toList.sorted.iterator
        while(iter.hasNext) {
            val value = linksAndContent.get(iter.next).get
            writer.write(value)
        }

        writer.close()
    }
}