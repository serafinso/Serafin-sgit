package sgit.localChange

import better.files._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import sgit.io.{indexConversion, init}
import sgit.objectManipulation.blobManipulation

class statusTest extends FunSpec with BeforeAndAfter with Matchers  {
  //STAGED
  describe("If the user use the status command and one file is staged"){
    it("it should print this staged file") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        status.sgitStatus()
      }
      assert(stream.toString.contains("Changes to be committed:"))
      assert(stream.toString.contains("Test1"))
      file.delete()
      sgitDirectory.delete()
    }
  }

  //MODIFIED : UPDATE
  describe("If the user use the status command and one file is modified (update)"){
    it("it should print this update file") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)

      add.addFiles(Seq("Test1"))
      file.overwrite("Bonjour")
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        status.sgitStatus()
      }
      assert(stream.toString.contains("Changes not staged for commit:") && stream.toString.contains("modified:"))
      assert(stream.toString.contains("Test1"))
      file.delete()
      sgitDirectory.delete()
    }
  }

  //MODIFIED : DELETE
  describe("If the user use the status command and one file is modifies (delete)"){
    it("it should print this deleted file") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      file.delete()
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        status.sgitStatus()
      }
      assert(stream.toString.contains("Changes not staged for commit:") && stream.toString.contains("deleted:"))
      assert(stream.toString.contains("Test1"))
      sgitDirectory.delete()
    }
  }

  //UNTRACKED
  describe("If the user use the status command and one file is untracked"){
    it("it should print this untracked file") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)

      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        status.sgitStatus()
      }
      assert(stream.toString.contains("Untracked files:"))
      assert(stream.toString.contains("Test1"))

      file.delete()
      sgitDirectory.delete()
    }
  }

  //EVERYTHING UP TO DATE
}
