package sgit.commitHistory

import better.files._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import sgit.io.{init, utilities}
import sgit.localChange.{add, commit, diff}

class logTest extends FunSpec with BeforeAndAfter with Matchers {
  describe("If the user use the log command and one file before staged and commit is modified"){
    it("it should print the commit ") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      commit.sgitCommit("-m \"First Commit\"")
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        log.simpleLog()
      }
      assert(stream.toString.contains("First Commit"))
      file.delete()
      sgitDirectory.delete()
    }
  }
  describe("If the user use the logp command and one file before staged and commit is modified"){
    it("it should print the diff between the commit ") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      file.overwrite("Hello")
      add.addFiles(Seq("Test1"))
      commit.sgitCommit("-m \"First Commit\"")
      file.overwrite("Bonjour")
      add.addFiles(Seq("Test1"))
      commit.sgitCommit("-m \"Second Commit\"")
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        log.logP()
      }
      assert(stream.toString.contains("First Commit"))
      assert(stream.toString.contains("Second Commit"))
      assert(stream.toString.contains(Console.RED +"-"+ "Hello" + Console.BLACK))
      assert(stream.toString.contains(Console.GREEN +"+"+ "Bonjour" + Console.BLACK))

      file.delete()
      sgitDirectory.delete()
    }
  }
}
