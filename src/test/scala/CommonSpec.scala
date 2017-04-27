package org.kevin.app.bookcrawler

import org.scalatest.FunSpec

class CommonSpec extends FunSpec {
  describe("File Path Test") {
    val testFilePath = "~/Document/Novel/dzz.txt"

    it("getFileNameWithoutExtension") {
        val result = Common.getFileNameWithoutExtension(testFilePath)
        assert(result == "~/Document/Novel/dzz")
    }

    it("getExtension") {
        val result = Common.getExtension(testFilePath)
        assert(result == "txt")
    }

    it("getIncreasingFileName") {
        val result = Common.getIncreasingFileName(testFilePath, 3)
        assert(result == "~/Document/Novel/dzz_3.txt")
    }
  }
}