package sgit.io

import better.files._
import sgit.io.utilities.isFilePresent
import sgit.objects.{Blob, BlobAndContent}
object indexConversion {

  def getIndex : Option[File] = {
    if (isFilePresent(".sgit/index")) {Some(".sgit/index".toFile)}
    else None
  }


  def indexToBlobList : List[Blob] = {
    val indexFile : Option[File] = getIndex
    if(indexFile.isDefined){
      val line :List[String] = indexFile.get.contentAsString
        .replace("\r", "")
        .split("\n").toList
      if (line.head.equals("")) {
        List.empty
      } else {
        line.map(l => {
          val splitLine = l.split(" ")
          Blob(splitLine(0), splitLine(1))
        })
      }
    }else {
      println("Git init first")
      List.empty
    }
  }

  /**
   * Rewrite the index entirely with the Seq of Blob
   * @param blobList blob to add to the index
   */
  def blobACListToIndexFile(blobList : List[BlobAndContent]) : Unit = {
    ".sgit/index".toFile.overwrite("")
    blobList.map(blob => {
      ".sgit/index".toFile.appendLine( blob.key+ " " + blob.path)
    })
  }

  /**
   * Rewrite the index entirely with the Seq of Blob
   * @param blobList blob to add to the index
   */
  def blobListToIndexFile(blobList : List[Blob]) : Unit = {
    ".sgit/index".toFile.overwrite("")
    blobList.map(blob => {
      ".sgit/index".toFile.appendLine( blob.key+ " " + blob.path)
    })
  }

}
