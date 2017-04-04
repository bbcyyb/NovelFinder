package org.kevin.app.bookcrawler

abstract class AbstractWebsite(novelTitleSet: Set[String]) {
    abstract class WebsiteInner(title: String, basicUrl: String) {
        private val outputPath = s"/Users/ky54/Documents/Novel/${title}.txt"

        def scopeFilter(url: String): Boolean = {
            return url.contains(basicUrl)
        }

        def processCore = {
            val crawler = new Crawler(this.scopeFilter)
            //抓取所有的链接和html内容
            val linksAndContent = crawler.crawl(basicUrl)
            //将获取html中文本内容抽取出来
            crawler.parse(linksAndContent, this.extractTitleAndContent)
            //将文本内容存储在文件中
            crawler.store(linksAndContent, this.outputPath)
        }

        def extractTitleAndContent(html:String): String 
    }

    private val novelMap: Map[String, String] = Map(
                                 "大主宰" -> "http://www.7caimi.com/xiaoshuo/13/"
                                ,"斗罗大陆" -> "http://www.7caimi.com/xiaoshuo/15/"
                                ,"莽荒纪" -> "http://www.7caimi.com/xiaoshuo/14/"
                                ,"十方神王" -> "http://www.7caimi.com/xiaoshuo/11/")
    private var novelNodeSet: Set[WebsiteInner] = novelTitleSet.filter{ novelMap.contains(_) }
                                                                .map{m => generateWebsiteInnerObject(m, novelMap(m)) }

    def generateWebsiteInnerObject(title: String, basicUrl: String): WebsiteInner

    def process = {
        novelNodeSet.foreach(_.processCore)
    }
}