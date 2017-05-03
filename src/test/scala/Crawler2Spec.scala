package org.kevin.app.bookcrawler

import org.scalatest.{FunSpec, PrivateMethodTester}

class Crawler2Spec extends FunSpec with PrivateMethodTester {
  describe("Crawler2 class") {
    val obj = new Crawler2
    assert(obj != null)

    describe("Crawl a http url") {
      val startPage = "http://www.7caimi.com/xiaoshuo/13/"
      val result = obj.crawl(startPage)
      it("Should return a non-empty content") {
        assert(result != null)
      }
    }

    val startPage = "http://www.7caimi.com/xiaoshuo/13/"
    describe(s"Get a hostname from ${startPage}") { 
      val expected = "http://www.7caimi.com"
      val theFunction = PrivateMethod[String]('getHostBase)
      val result = obj invokePrivate theFunction(startPage)
      it(s"should equal ${expected}") {
        assert(result == expected)
      }
    }
  }
}
