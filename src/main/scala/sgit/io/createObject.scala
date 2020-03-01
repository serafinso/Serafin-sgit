package sgit.io

import better.files._
import java.nio.file.{Files, Paths}
import sgit.objects.{Blob, Tree}

object createObject {

  /**
   *
   * @param asDirectory
   * @param name
   * @return
   */
  def createFile(asDirectory: Boolean, name: String ) : Boolean = {
    if (!Files.exists(Paths.get(name))){
      val file1: File = name
        .toFile
        .createIfNotExists(asDirectory, false)
      true
    } else {
      false
    }
  }

  def createBlob(file: File) : Blob = {
    val root = ".sgit/".toFile.parent
    val blobPath: String = ".sgit/objects/blob/"
    //Creation of blob in blob directory if not exist
    if(!blobPath.contains(file.sha1)){
      val newFileInObject = createObject.createFile(false, blobPath + file.sha1)
      if (newFileInObject) {
        (blobPath + file.sha1).toFile.appendText(file.contentAsString)
      }
    }
    Blob(file.sha1, root.relativize(file).toString)
  }

  /**
   * Create new objects blob if they don't already exist
   * @param files a sequence of files
   * @return all the blob object corresponding to the files
   */
  def createObjectBlob(files :Seq[File]) : Seq[Blob] = {
    files.map((file: File) => {
      val root = ".sgit/".toFile.parent
      val blobPath: String = ".sgit/objects/blob/"
      //Creation of blob in blob directory if not exist
      if(!blobPath.contains(file.sha1)){
        val newFileInObject = createObject.createFile(false, blobPath + file.sha1)
        if (newFileInObject) {
          (blobPath + file.sha1).toFile.appendText(file.contentAsString)
        }
      }
      Blob(file.sha1, root.relativize(file).toString)
    })
  }

}
