package org.kevin.app.bookcrawler

class SevenCaiMi(novelTitleSet: Set[String]) {
    class ServenCaiMiInner(t: String, b: String) {
        private val title = t
        private val basicUrl = b
        private val outputPath = s"/Users/ky54/Documents/Novel/${title}.txt"

        def output = {
            println(s"title:${title}  basicUrl:${basicUrl}")
        }

        def scopeFilter(url: String): Boolean = {
            return url.contains(basicUrl)
        }

        def processCore = {
            val crawler = new Crawler2(this.scopeFilter)
            //抓取所有的链接和html内容
            val linksAndContent = crawler.doCrawlPages(basicUrl)
            //遍历所有的html内容，按照规则解析，生成干净的文本内容
            linksAndContent.foreach(entry => {linksAndContent += (entry._1 -> extractTitleAndContent(entry._2))})
            //将文本内容存储在文件中
            crawler.storeContent(linksAndContent, this.outputPath)
        }

        def extractTitleAndContent(html:String): String = {
            val h1StartIndex = html.indexOf("<h1>")
            val h1EndIndex = html.indexOf("</h1>", h1StartIndex)
            val contentStartIndex = html.indexOf("<div>", h1EndIndex)
            val contentEndIndex = html.indexOf("</div>", contentStartIndex)
            if(h1StartIndex < 0 
                || h1EndIndex < 0
                || contentStartIndex < 0
                || contentEndIndex < 0){
                return ""
        }

        val title = html.substring(h1StartIndex + 4, h1EndIndex)
        val content = html.substring(contentStartIndex + 5, contentEndIndex)
                        .replaceAll("<br />","\n")
                        .replaceAll("<br />|&nbsp;+|\t+", "")
                        .replaceAll("""<[a-zA-Z]+(\s+[a-zA-Z]+\s*=\s*("([^"]*)"|'([^']*)'))*\s*/>""", "")

        s"${title}\n${content}\n\n"
    }
    }

    private val novelMap: Map[String, String] = Map(
                                 "大主宰" -> "http://www.7caimi.com/xiaoshuo/13/"
                                ,"斗罗大陆" -> "http://www.7caimi.com/xiaoshuo/15/"
                                ,"莽荒纪" -> "http://www.7caimi.com/xiaoshuo/14/"
                                ,"十方神王" -> "http://www.7caimi.com/xiaoshuo/11/")
    private var novelNodeSet: Set[ServenCaiMiInner] = novelTitleSet.filter{ novelMap.contains(_) }
                                                                   .map { novelTitle => 
                                                                            new ServenCaiMiInner(novelTitle, novelMap(novelTitle)) 
                                                                        }

    def process = {
        novelNodeSet.foreach(_.output)
    }
}