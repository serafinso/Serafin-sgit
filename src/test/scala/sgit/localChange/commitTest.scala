package sgit.localChange

import better.files._
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import sgit.io.{init, utilities}
import sgit.io.objectConversion.{blobConversion, commitConversion}
import sgit.objectManipulation.blobManipulation
import sgit.objects.{Blob, Commit}

class commitTest extends FunSpec with BeforeAndAfter with Matchers {
  describe("If the user use the commit command and one file is staged"){
    it("it should commit this staged file") {
      if(utilities.isFilePresent(".sgit")) ".sgit".toFile.delete()
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      commit.sgitCommit("-m \"First commit\"")
      val optLastCommit : Option[Commit] = commitConversion.getLastCommit
      assert(optLastCommit.isDefined)
      val blobs : Option[List[Blob]] = blobConversion.getBlobFromCommit(optLastCommit.get)
      assert(blobs.isDefined)
      assert(blobManipulation.isBlobInList(blobConversion.filesToBlobList(List(file)).head, blobs.get))
      file.delete()
      sgitDirectory.delete()
    }
  }
}
