package org.kevin.app.bookcrawler

import org.scalatest.FunSpec

class Crawler2Spec extends FunSpec{
  describe("send http request"){
    it("can create a new Crawler2 object"){
      val obj = new Crawler2("http://www.7caimi.com/xiaoshuo/13/")
      assert(obj != null)
    }

    it("send http request"){
      val startPage = "http://www.7caimi.com/xiaoshuo/13/"
      val obj = new Crawler2(startPage)
      val result = obj.getPageFromRemote(startPage)

      assert(result != null)
    }

    it("parse links from html contenT"){
      val startPage = "http://www.7caimi.com/xiaoshuo/13/"
      val obj = new Crawler2(startPage)
      obj.fetchLinks(startPage)

      assert(obj != null)
    }
  }

}
