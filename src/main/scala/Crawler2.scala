package org.kevin.app.bookcrawler

import scala.collection.JavaConversions._
import scala.collection.mutable
import java.net.URL
import java.io._
import org.jsoup.Jsoup

class Crawler2 {

    private val TIME_OUT: Int = 18000
    private var rootUrl: String = _

    private def getHostBase(url: String) = {
        val uri = new URL(url)
        val portPart = if(uri.getPort() == -1 || uri.getPort() == 80) "" else ":" + uri.getPort()
        uri.getProtocol() + "://" + uri.getHost() + portPart
    }

    def crawl(basicUrl: String) = {

        if(rootUrl == null) {
            rootUrl = basicUrl
        }

        var html: String = s"url: ${basicUrl} 访问失败"
        // 最多五次访问，如果失败，则不再继续处理
        for(i <- (1 to 5)) {
            try {
                html = Jsoup.connect(basicUrl).maxBodySize(0).timeout(TIME_OUT).get().html
            } catch {
                case e: Exception => println(s"exception caught: ${e}, url: ${basicUrl}")
            }
        }
        html
    }

    def parse(currentUrl: String, htmlString: String, linkFilter: ((String, String) => Boolean), extractContentAndLinks: String => (String, String, List[String])): (String, String, List[String]) = {

        val hostName = getHostBase(currentUrl)
        val extractResult = extractContentAndLinks(htmlString)
        val title = extractResult._1
        val content = extractResult._2
        val alinks = extractResult._3.filter(
            link => !link.startsWith("javascript:")
            ).map( link => link match {
                                case link if link.startsWith("//") => "http://" + link
                                case link if link.startsWith("/") => hostName + link
                                case link if link.startsWith("http://") || link.startsWith("https://") => link
                                case _ => link
                            }
            ).filter(linkFilter(_, rootUrl)).toList
        
        return (title, content, alinks)
    }

    def store(linksAndContent: mutable.HashMap[String, String], storePath: String) = {

        var counter: Int = 0
        var file: File = null
        var increasedOutputPath: String = ""
        do {
            increasedOutputPath = Common.getIncreasingFileName(storePath, counter)
            file = new File(increasedOutputPath)
            counter += 1    
        } while(file.exists())
        Common.log(s"current file path is ${increasedOutputPath}")
        file.createNewFile()
        val writer = new BufferedWriter(new FileWriter(file))

        var iter = linksAndContent.keySet.toList.sorted.iterator
        while(iter.hasNext) {
            val value = linksAndContent.get(iter.next).get
            writer.write(value)
        }

        writer.close()
    }
}