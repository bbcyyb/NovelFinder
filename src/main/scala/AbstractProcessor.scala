package org.kevin.app.bookcrawler

import scala.collection.mutable

abstract class AbstractProcessor(crawler: Crawler2) {

    protected var storePath: String

    def crawl(basicUrl: String): String = {
        return crawler.crawl(basicUrl)
    }

    def parse(currentUrl: String, htmlString: String): (String, List[String]) = {
        val parseResult = crawler.parse(currentUrl, htmlString, scopeFilter, extractContentAndLinks)
        val title = parseResult._1
        val content = parseResult._2
        val alinks = parseResult._3
        var section = ""
        if(!title.isEmpty && !content.isEmpty) {
            section = purifyTitleAndContent(title, content)
        }

        (section, alinks)
    }

    def store(linksAndContent: mutable.HashMap[String, String]) = {
        crawler.store(linksAndContent, storePath)
    }

    protected def scopeFilter(url: String, basicUrl: String): Boolean = {
        return url.contains(basicUrl) && url != basicUrl
    }

    // 从html文档中提取标题，正文和所有的链接(无论是否正确和有效)
    protected def extractContentAndLinks(html: String): (String, String, List[String])

    // 精炼提取到的内容，去掉其中混杂的html标签和特殊字符
    protected def purifyTitleAndContent(title: String, content: String): String
}