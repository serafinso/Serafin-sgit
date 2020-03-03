package sgit.localChange

import sgit.io.{blobConversion, getFile, indexConversion}
import sgit.objects.{Blob, BlobAndContent}

object add {

  /**
   *
   * @param blobs
   * @param blobAndContents
   * @return
   */
  def isBlobACListEqual(blobs : List[Blob], blobAndContents: List[BlobAndContent] ) : Boolean = {
    if(blobs.length != blobAndContents.length) return false
    if(blobs.isEmpty && blobAndContents.isEmpty) return true
    val blob = blobs.head
    val newBlobAndContents: List[BlobAndContent] = removeBlobAC(blob, blobAndContents)
    isBlobACListEqual(blobs.tail, newBlobAndContents)
  }

  /**
   *
   * @param blob
   * @param blobAndContents
   * @return blobAndContents without the blob blob
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

  /** Remove th blob to the List if it contained it
   *
   * @param blob
   * @param blobs
   * @return blobAndContents without the blob blob
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

  /**
   *
   * @param path
   * @param blobs
   * @return
   */
  @scala.annotation.tailrec
  def pathInBlobACList(path: String, blobs: List[BlobAndContent]) : Option[BlobAndContent] = {
    if(blobs.isEmpty)None
    else{
      val blob = blobs.head
      if (blob.path.equals(path)) Some(blob)
      else (pathInBlobACList(path, blobs.tail))
    }
  }

  /**
   *
   * @param path
   * @param blobs
   * @return
   */
  @scala.annotation.tailrec
  def pathInBlobList(path: String, blobs: List[Blob]) : Option[Blob] = {
    if(blobs.isEmpty)None
    else{
      val blob = blobs.head
      if (blob.path.equals(path)) Some(blob)
      else (pathInBlobList(path, blobs.tail))
    }
  }

  /**
   *
   * @param blobToSearch
   * @param blobs
   * @return
   */
  @scala.annotation.tailrec
  def blobInList(blobToSearch: BlobAndContent, blobs: List[Blob]) : Boolean = {
    if(blobs.isEmpty) false
    else{
      val blob = blobs.head
      if (blobToSearch.key.equals(blob.key) && blobToSearch.path.equals(blob.path)) true
      else (blobInList(blobToSearch, blobs.tail))
    }
  }

  /**
   *
   */
  @scala.annotation.tailrec
  def addFiles(files : Seq[String]): Unit = {
    if(!getFile.isFilePresent(".sgit")){
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
              if( !blobInList( blobAc, indexBlob)){
                //BLOB NOT IN INDEX
                val isInIndex : Option[Blob] = pathInBlobList(blobAc.path,indexBlob)
                if(isInIndex.isDefined){
                  //PATH IN INDEX (Modified/Update)
                  blobConversion.createBlobFiles(List(blobAc))
                  val blobToAdd = blobConversion.blobACToBlob(blobAc)
                  val blobToRemove : Blob = isInIndex.get
                  val newindexBlob = blobToAdd::removeBlob(blobToRemove, indexBlob)
                  indexConversion.blobListToIndexFile(newindexBlob)
                }else{
                  //PATH NOT IN INDEX (Untracked)
                  blobConversion.createBlobFiles(List(blobAc))
                  val blobToAdd = blobConversion.blobACToBlob(blobAc)
                  val newindexBlob = blobToAdd::indexBlob
                  indexConversion.blobListToIndexFile(newindexBlob)
                }
              }else{
                //IN WD and IN INDEX
                println(blobAc.path + " already staged")
              }
            }else{ //NOT IN WD
              val isInWd : Option[Blob] = pathInBlobList(file, indexBlob)
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
