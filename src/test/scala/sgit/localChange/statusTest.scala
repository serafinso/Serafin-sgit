package sgit.localChange

import better.files._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import sgit.io.{init, utilities}
import sgit.objectManipulation.blobManipulation

class statusTest extends FunSpec with BeforeAndAfter with Matchers  {
  //STAGED UPDATE
  describe("If the user use the status command and one file is staged"){
    it("it should print this staged file") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      commit.sgitCommit("-m \"First Commit\"")
      file.overwrite("modified")
      add.addFiles(Seq("Test1"))
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        status.sgitStatus()
      }
      assert(stream.toString.contains("Changes to be committed:"))
      assert(stream.toString.contains("\tupdated:   Test1"))
      file.delete()
      sgitDirectory.delete()
    }
  }

  //STAGED NEW FILE
  describe("If the user use the status command and one file is staged"){
    it("it should print this new file staged") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
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
      assert(stream.toString.contains("\tnew files:   Test1"))
      file.delete()
      sgitDirectory.delete()
    }
  }

  //MODIFIED : UPDATE
  describe("If the user use the status command and one file is modified (update)"){
    it("it should print this update file") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
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
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
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
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
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
  describe("If the user use the status command after one add . and a commit"){
    it("it should print 'nothing to commit, working tree clean'") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("."))
      commit.sgitCommit("-m \"Test1\"")
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {status.sgitStatus()}
      assert(stream.toString.contains("nothing to commit, working tree clean"))
      file.delete()
      sgitDirectory.delete()
    }
  }
}
