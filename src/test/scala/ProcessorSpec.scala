package org.kevin.app.bookcrawler

import org.scalatest.{FunSpec, PrivateMethodTester}

class ProcessorSpec extends FunSpec with PrivateMethodTester {
  describe("SevenCaiMi2Processor class") {
    val processor = new SevenCaiMi2Processor(new Crawler2)
    assert(processor != null)

    describe("Purify html content") {
      val htmlContent = """
&nbsp;&nbsp;&nbsp;&nbsp;烈日如炎，灼热的阳光从天空上倾洒下来，令得整片大地都是处于一片蒸腾之中，杨柳微垂，收敛着枝叶，恹恹不振。
<br><br>&nbsp;&nbsp;&nbsp;&nbsp;在那一片投射着被柳树枝叶切割而开的明亮光斑的空地中，数百道身影静静盘坐，这是一群略显青涩的少年少女，而此时，他们都是面目认真的微闭着双目，鼻息间的呼吸，呈现一种极有节奏之感，而随着呼吸的吐纳，他们的周身，仿佛是有着肉眼难辨的细微光芒出现。
<br><br>&nbsp;&nbsp;&nbsp;&nbsp;微风悄然的吹拂而来，衣衫飘动，倒是略显壮观。<br> <h1></h1><script aaa="123">结束
      """

      val theFunction = PrivateMethod[String]('purifyTitleAndContent)
      val result = processor invokePrivate theFunction("test title", htmlContent)

      it("Should not contains <br>, <br >, &nbsp; and any html tags") {
        assert(result.indexOf("<br>") == -1)
        assert(result.indexOf("<br >") == -1)
        assert(result.indexOf("&nbsp;") == -1)
        assert(result.indexOf("</h1>") == -1)

        println(result)
      }
    }

    describe("Extract html content and links") {
      val htmlDocument = """
<html>
  <head></head>
  <body>
    <div class="chapter">
      <h1>第1章 北灵院</h1>
      <div>         
      &nbsp;&nbsp;&nbsp;&nbsp;烈日如炎，灼热的阳光从天空上倾洒下来，令得整片大地都是处于一片蒸腾之中，杨柳微垂，收敛着枝叶，恹恹不振。<br><br>&nbsp;&nbsp;&nbsp;&nbsp;在那一片投射着被柳树枝叶切割而开的明亮光斑的空地中，数百道身影静静盘坐，这是一群略显青涩的少年少女，而此时，他们都是面目认真的微闭着双目，鼻息间的呼吸，呈现一种极有节奏之感，而随着呼吸的吐纳，他们的周身，仿佛是有着肉眼难辨的细微光芒出现。
      </div>
    </div>
    <div class="tool">
      <a id="btnMenu" class="btn-purple" href="javascript:void(0);" onclick="BookReader.ReadMode.Menu()">菜单</a>
      <a href="" class="btn-purple" title="上一页" target="_self" hidefocus="true"><i class="i-prev"></i>上一页</a>
      <a href="/xiaoshuo/13/" title="返回目录" class="btn-orange" target="_self" hidefocus="true">返回目录</a>
      <a href="/xiaoshuo/13/16342.html" class="btn-purple" target="_self" title="下一页" hidefocus="true">下一页<i class="i-next"></i></a>
    </div>
  </body>
</html>
      """

      val theFunction = PrivateMethod[(String, String, List[String])]('extractContentAndLinks)
      val result = processor invokePrivate theFunction(htmlDocument)
      val title = result._1
      val content = result._2
      val alinks = result._3


      it("Should get title and isn't empty.") {
        assert(title == "第1章 北灵院")
      }

      it("Should get whole content and don't contain title") {
        assert(!content.isEmpty)
        assert(content.indexOf(title) == -1)
      }

      it("Should has 4 alinks") {
        assert(alinks.size == 4)
      }

      println(result._1)
      println(result._2)
      println(result._3)
    }
  }
}