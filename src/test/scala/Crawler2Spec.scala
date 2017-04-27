package org.kevin.app.bookcrawler

import org.scalatest.FunSpec

class Crawler2Spec extends FunSpec{
  describe("send http request"){
    it("can create a new Crawler2 object"){
      val obj = new Crawler2
      assert(obj != null)
    }

    it("send http request"){
      val startPage = "http://www.7caimi.com/xiaoshuo/13/"
      val obj = new Crawler2
      val result = obj.crawl(startPage)

      assert(result != null)
    }
  }

}
