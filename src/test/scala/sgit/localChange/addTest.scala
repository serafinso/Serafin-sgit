package sgit.localChange

import org.scalatest._
import better.files._
import sgit.io.{indexConversion, init}
import sgit.objectManipulation.blobManipulation
import sgit.objects.Blob


class addTest extends FunSpec with BeforeAndAfter with Matchers {
  describe("If the user add a file in the index"){
    it("it should add a line in the index") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      val expectedBlob: Blob = Blob(file.sha1, "Test1")
      add.addFiles(Seq("Test1"))
      assert(blobManipulation.isBlobInList(expectedBlob, indexConversion.indexToBlobList))
      file.delete()
      sgitDirectory.delete()
    }
  }

  describe("If the user update a file in the working directory"){
    it("it should update the associated line in the index") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      file.overwrite("Bonjour")
      val newBlob: Blob = Blob(file.sha1, "Test1")
      add.addFiles(Seq("Test1"))
      assert(blobManipulation.isBlobInList(newBlob, indexConversion.indexToBlobList))
      file.delete()
      sgitDirectory.delete()
    }
  }

  describe("If a file existing in the index and doesn't exist in the working directory is added"){
    it("it should delete the line related to that line in the index"){
      init.init()
      val sgitDirectory = ".sgit".toFile
      val file: File = "Test1"
        .toFile
        .createIfNotExists(asDirectory = false, createParents = false)
      add.addFiles(Seq("Test1"))
      val newBlob: Blob = Blob(file.sha1, "Test1")
      file.delete()
      add.addFiles(Seq("Test1"))
      assert(!blobManipulation.isBlobInList(newBlob, indexConversion.indexToBlobList))
      sgitDirectory.delete()
    }
  }
}

