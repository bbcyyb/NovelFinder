package org.kevin.app.bookcrawler

import org.scalatest.FunSpec

class CommonSpec extends FunSpec {
  describe("Common static object") {
    val testFilePath = "/Users/ky54/Documents/Novel/DaZhuZai.txt"

    describe(s"Get fileName without extension from ${testFilePath}") {
        val expected = "/Users/ky54/Documents/Novel/DaZhuZai"
        val result = Common.getFileNameWithoutExtension(testFilePath)
        it(s"Should equal ${expected}") {
            assert(result == expected)
        }
    }

    describe(s"Get file extension from ${testFilePath}") {
        val expected = "txt"
        val result = Common.getExtension(testFilePath)
        it(s"Should equal ${expected}") {
            assert(result == expected)
        }
    }

    val id0 = 0
    describe(s"Get increasing fileName(id: ${id0}) from ${testFilePath}") {
        val expected = "/Users/ky54/Documents/Novel/DaZhuZai.txt"
        val result = Common.getIncreasingFileName(testFilePath, id0)
        it(s"Should equial ${expected}"){
            assert(result == expected)
        }
    }

    val id3 = 3
    describe(s"Get increasing fileName(id: ${id3}) from ${testFilePath}") {
        val expected = "/Users/ky54/Documents/Novel/DaZhuZai_3.txt"
        val result = Common.getIncreasingFileName(testFilePath, id3)
        it(s"Should equial ${expected}"){
            assert(result == expected)
        }
    }
  }
}