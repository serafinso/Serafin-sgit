package sgit.localChange

import better.files._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import sgit.io.{init, utilities}

class diffTest extends FunSpec with BeforeAndAfter with Matchers{
  describe("If the user use the diff command is used and one file before staged and commit is modified"){
    it("it should print the diff between the file before the commit and after the modification ") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      commit.sgitCommit("-m \"First Commit\"")
      file.overwrite("modified")
      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        diff.sgitDiff()
      }
      assert(stream.toString.contains(Console.CYAN + "@@ -1,1 +1,1 @@"+ Console.BLACK))
      file.delete()
      sgitDirectory.delete()
    }
  }
}
