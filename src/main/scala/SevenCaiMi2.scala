package org.kevin.app.bookcrawler


class SevenCaiMi2Processor(crawler: Crawler2) extends AbstractProcessor(crawler) {

    override var storePath = "/Users/ky54/Documents/Novel/DaZhuZai.txt"

    override protected def extractContentAndLinks(html: String): (String, String, List[String]) = {
        ("","",List[String]())
    }

    override def purifyTitleAndContent(title: String, content: String): String = {
        val contentAfter = content.replaceAll("<br />","\n")
                                .replaceAll("<br />|&nbsp;+|\t+", "")
                                .replaceAll("""<[a-zA-Z]+(\s+[a-zA-Z]+\s*=\s*("([^"]*)"|'([^']*)'))*\s*/>""", "")

            s"${title}\n${contentAfter}\n\n"
    }
}