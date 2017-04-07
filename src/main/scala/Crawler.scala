package org.kevin.app.bookcrawler
import java.net.{HttpURLConnection, SocketTimeoutException, URL}
import java.io._
import java.util.concurrent._
import scala.collection.JavaConversions._
import java.util.HashSet
import scala.collection.mutable

class Crawler(scopeFilter: (String => Boolean) = (url: String) => true){

  private val crawledPool = new HashSet[String]

  private val linkRegex = """ (src|href)="([^"]+)"|(src|href)='([^']+)' """.trim.r

  private val CONN_TIME_OUT = 10 * 1000

  private val READ_TIME_OUT = 15 * 1000

    def getHostBase(url: String) = {
        val uri = new URL(url)
        val portPart = if(uri.getPort() == -1 || uri.getPort() == 80) "" else ":" + uri.getPort()
        uri.getProtocol() + "://" + uri.getHost() + portPart
    }

    def fetchLinks(html: String): Set[String] = {
        var list = for(m <- linkRegex.findAllIn(html).matchData if (m.group(1) != null || m.group(3) != null)) yield {
          if (m.group(1) != null) m.group(2) else m.group(4)
        }

        list.filter {
          link => !link.startsWith("#") && !link.startsWith("javascript:") && link != "" && !link.startsWith("mailto:")
        }.toSet
    }

    def getPageFromRemote(url: String): (Int, String, Map[String, String]) = {
        var uri = new URL(url)

        var conn:HttpURLConnection = null
        var status:Int = 0
        var data:String = ""
        var headers:Map[String, String] = null
        try{
            conn = uri.openConnection().asInstanceOf[HttpURLConnection]
            conn.setConnectTimeout(this.CONN_TIME_OUT)
            conn.setReadTimeout(this.READ_TIME_OUT)
            var stream = conn.getInputStream()
            var bufferedReader = new BufferedReader(new InputStreamReader(stream, "utf-8"))
            var strBuf = new StringBuilder()
            var line = bufferedReader.readLine()
            while(line != null){
              strBuf.append(line)
                line = bufferedReader.readLine()
            }

            data = strBuf.toString()
            status = conn.getResponseCode()

            headers = conn.getHeaderFields().toMap.map{
                h => (h._1, h._2.mkString(","))
            }
        } catch{
            case e: SocketTimeoutException => println(e.getStackTrace)
            case e2: Exception => println(e2.getStackTrace)
        } finally {
            if(conn != null){
                conn.disconnect;
            }
            crawledPool.add(url)
        }

        return (status, data, headers)
    }

    def gainCrawlLinks(parentUrl: String, html: String) = {
        val baseHost = getHostBase(parentUrl)
        val links = fetchLinks(html).map(
                link => 
                link match {
                    case link if link.startsWith("/") => baseHost + link
                    case link if link.startsWith("http:") || link. startsWith("https://") => link
                    case _ =>
                        val index = parentUrl.lastIndexOf("/")
                        parentUrl.substring(0, index) + "/" + link
                }
            ).filter {
                link => !crawledPool.contains(link) && this.scopeFilter(link)
            }

            //println("find " + links.size + "links at page " + parentUrl)
            links
    }

    def crawl(basicUrl: String): mutable.HashMap[String, String] = {
        //创建线程池
        val threadPool: ThreadPoolExecutor = new ThreadPoolExecutor(10, 200, 3
                                                , TimeUnit.SECONDS
                                                , new LinkedBlockingDeque[Runnable]()
                                                , new ThreadPoolExecutor.CallerRunsPolicy())

        //设置线程池相关属性
        threadPool.allowCoreThreadTimeOut(true)
        threadPool.setKeepAliveTime(6, TimeUnit.SECONDS)
        //存储该函数的返回值
        val result= new mutable.HashMap[String, String]()
        //用于存储每个页面符合条件的url, 该栈共享于多个线程
        val linksStack = mutable.Stack[String]()
        linksStack.push(basicUrl)

        try {
                //线程池中还有任务在进行
                do{
                    //link栈不空
                    while(!linksStack.isEmpty) {
                        val link = linksStack.pop()
                        val future = new FutureTask[(Int, String, Map[String, String])] (() => getPageFromRemote(link))
                        threadPool.execute(future)
                        //获取网页信息
                        val pageContent = future.get(this.READ_TIME_OUT, TimeUnit.SECONDS)._2
                        val tempLinks = gainCrawlLinks(link, pageContent)
                        tempLinks.filter(!crawledPool.contains(_)).foreach(linksStack.push(_))
                        result += (link -> pageContent)
                    }   
                    Thread.sleep(100)
                } while(threadPool.getActiveCount != 0)
            } finally {
                threadPool.shutdown()
            }

        result
    }

    def parse(linksAndContent: mutable.HashMap[String, String], extractTitleAndContent: String => String) = {
        //遍历所有的html内容，按照规则解析，生成干净的文本内容
        linksAndContent.foreach(entry => {linksAndContent += (entry._1 -> extractTitleAndContent(entry._2))}) 
    }

    def store(linksAndContent: mutable.HashMap[String, String], outputPath: String): Unit = {
        val writer = new BufferedWriter(new FileWriter(new File(outputPath)))

        var itor = linksAndContent.keySet.toList.sorted.iterator
        while(itor.hasNext) {
            val value = linksAndContent.get(itor.next).get
            writer.write(value)
        }

        // val values = linksAndContent.valuesIterator
        // while(values.hasNext){
        //     writer.write(values.next())
        // }

        writer.close()
    }
}
