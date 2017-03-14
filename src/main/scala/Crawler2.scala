package org.kevin.app.bookcrawler
import java.net.{HttpURLConnection, SocketTimeoutException, URL}
import java.io.{BufferedReader, InputStreamReader}
import java.util.HashSet
import java.util.concurrent._
import scala.collection.JavaConversions._

class Crawler2(startPage:String, outputPath: String = "./crawl.txt", filter: (String => Boolean) = (url: String) => true){

  private val crawledPool = new HashSet[String]

  private val CONN_TIME_OUT = 10 * 1000

  private val READ_TIME_OUT = 15 * 1000

    def crawl(): Unit = {
        // val linksAndContent = doCrawlPages(startPage);
        //
        // linksAndContent.foreach(entry => {linksAndContent += (entry._1 -> extractTitleAndContent(entry._2))});
        //
        // storeContent(linkAndContent, outputPath);
    }

    def getPageFromRemote(url: String): (Int, String, Map[String, String]) = {
        var uri = new URL(url);

        var conn:HttpURLConnection = null;
        var status:Int = 0;
        var data:String = "";
        var headers:Map[String, String] = null;
        try{
            conn = uri.openConnection().asInstanceOf[HttpURLConnection];
            conn.setConnectTimeout(this.CONN_TIME_OUT);
            conn.setReadTimeout(this.READ_TIME_OUT);
            var stream = conn.getInputStream();
            var bufferedReader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            var strBuf = new StringBuilder();
            var line = bufferedReader.readLine();
            while(line != null){
                strBuf.append(line);
                line = bufferedReader.readLine();
            }

            data = strBuf.toString();
            status = conn.getResponseCode();

            headers = conn.getHeaderFields().toMap.map {
                head => (head._1, head._2.mkString(","));
            };
        } catch{
            case e:SocketTimeoutException => println(e.getStackTrace)
            case e2:Exception => println(e2.getStackTrace)
        } finally {
            if(conn != null){
                conn.disconnect;
            }
            crawledPool.add(url)
        }

        return (status, data, headers)
    }
}
