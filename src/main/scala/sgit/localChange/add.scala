package sgit.localChange

import sgit.io.{blobConversion, utilities, indexConversion}
import sgit.objects.{Blob, BlobAndContent}
import sgit.objectManipulation.blobManipulation

object add {

  /** PF method
   *
   * @param blobs the blob list
   * @param blobAndContents the blobAndContent list
   * @return true if blobs and blobAndContents contained the same blobs
   */
  @scala.annotation.tailrec
  def isBlobACListEqual(blobs : List[Blob], blobAndContents: List[BlobAndContent] ) : Boolean = {
    if(blobs.length != blobAndContents.length) return false
    if(blobs.isEmpty && blobAndContents.isEmpty) return true
    val blob = blobs.head
    val newBlobAndContents: List[BlobAndContent] = removeBlobAC(blob, blobAndContents)
    isBlobACListEqual(blobs.tail, newBlobAndContents)
  }

  /** PF method
   *
   * @param blob the blob to remove
   * @param blobAndContents list of blob
   * @return blobAndContents without the blob
   */
  def removeBlobAC(blob : Blob, blobAndContents: List[BlobAndContent]): List[BlobAndContent] ={
    if(blobAndContents.isEmpty) List.empty
    else{
      val blobAndContent = blobAndContents.head
      if(blob.key.equals(blobAndContent.key) && blob.path.equals(blobAndContent.path)){
        println(blobAndContents.tail)
        blobAndContents.tail
      } else {
        blobAndContent::removeBlobAC(blob, blobAndContents.tail)
      }
    }
  }

  /** PF method
   * Remove the blob to the List if it contained it
   *
   * @param blob the blob to remove
   * @param blobs the blob list
   * @return blobs list without the blob, or blobs list unaltered if blobs doesn't contained blob
   */
  def removeBlob(blob : Blob, blobs: List[Blob]): List[Blob] ={
    if(blobs.isEmpty) List.empty
    else{
      val blobAndContent = blobs.head
      if(blob.key.equals(blobAndContent.key) && blob.path.equals(blobAndContent.path)){
        blobs.tail
      } else {
        blobAndContent::removeBlob(blob, blobs.tail)
      }
    }
  }

  /** PF method
   *
   * @param path the path to search in blob list
   * @param blobs the blob list
   * @return a blobAC that has the right path
   */
  @scala.annotation.tailrec
  def pathInBlobACList(path: String, blobs: List[BlobAndContent]) : Option[BlobAndContent] = {
    if(blobs.isEmpty)None
    else{
      val blob = blobs.head
      if (blob.path.equals(path)) Some(blob)
      else pathInBlobACList(path, blobs.tail)
    }
  }


  /** NOT PF method
   *
   * Main add method
   * @param files = files to add
   */
  @scala.annotation.tailrec
  def addFiles(files : Seq[String]): Unit = {
    if(!utilities.isFilePresent(".sgit")){
      println("Git init first")
    }else{
      if(files.nonEmpty){
        val file = files.head
        val indexBlob: List[Blob] = indexConversion.indexToBlobList
        val wdBlob : List[BlobAndContent] = blobConversion.wdToBlobACList
        file match {
          case "." =>
            if(isBlobACListEqual(indexBlob, wdBlob)){
              println("Everything is up to date")
            } else {
              blobConversion.createBlobFiles(wdBlob)
              indexConversion.blobACListToIndexFile(wdBlob)
            }
          case _ =>
            val pathInWD : Option[BlobAndContent] = pathInBlobACList(file, wdBlob)
            if(pathInWD.isDefined){ //IN WD
              val blobAc : BlobAndContent = pathInWD.get
              if( !blobManipulation.blobInList( blobAc, indexBlob)){
                //BLOB NOT IN INDEX
                val isInIndex : Option[Blob] = blobManipulation.pathInBlobList(blobAc.path,indexBlob)
                if(isInIndex.isDefined){
                  //PATH IN INDEX (Modified/Update)
                  blobConversion.createBlobFiles(List(blobAc))
                  val blobToAdd = blobManipulation.blobACToBlob(blobAc)
                  val blobToRemove : Blob = isInIndex.get
                  val newindexBlob = blobToAdd::removeBlob(blobToRemove, indexBlob)
                  indexConversion.blobListToIndexFile(newindexBlob)
                }else{
                  //PATH NOT IN INDEX (Untracked)
                  blobConversion.createBlobFiles(List(blobAc))
                  val blobToAdd = blobManipulation.blobACToBlob(blobAc)
                  val newindexBlob = blobToAdd::indexBlob
                  indexConversion.blobListToIndexFile(newindexBlob)
                }
              }else{
                //IN WD and IN INDEX
                println(blobAc.path + " already staged")
              }
            }else{ //NOT IN WD
              val isInWd : Option[Blob] = blobManipulation.pathInBlobList(file, indexBlob)
              if(isInWd.isDefined){ //Modified/Delete
                val newindexBlob = removeBlob(isInWd.get, indexBlob)
                indexConversion.blobListToIndexFile(newindexBlob)
              }else {//NOT IN WD and NOT IN INDEX
                println(file + " doesn't exist")
              }
            }
            addFiles(files.tail)
        }
      }
    }
  }

}
