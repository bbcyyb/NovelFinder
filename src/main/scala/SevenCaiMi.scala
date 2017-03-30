package org.kevin.app.bookcrawler

class SevenCaiMi(novelTitleSet: Set[String]) {
    class SevenCaiMiInner(title: String, basicUrl: String) {
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
            val linksAndContent = crawler.crawl(basicUrl)
            //将获取html中文本内容抽取出来
            crawler.parse(linksAndContent, this.extractTitleAndContent)
            //将文本内容存储在文件中
            crawler.store(linksAndContent, this.outputPath)
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
                                                                            new SevenCaiMiInner(novelTitle, novelMap(novelTitle)) 
                                                                        }

    def process = {
        novelNodeSet.foreach(_.processCore)
    }
}