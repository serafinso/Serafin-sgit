package sgit.localChange

import sgit.io.{blobConversion, indexConversion}
import sgit.objects.{Blob, BlobAndContent}
import sgit.localChange.add


object status {

  /** Remove th blob to the List if it contained it
   *
   * @param blob
   * @param blobs
   * @return blobAndContents without the blob blob
   */
  def removeBlobWithTheSamePath(blob : Blob, blobs: List[Blob]): List[Blob] ={
    if(blobs.isEmpty) List.empty
    else{
      val blobAndContent = blobs.head
      if( blob.path.equals(blobAndContent.path)){
        blobs.tail
      } else {
        blobAndContent::removeBlobWithTheSamePath(blob, blobs.tail)
      }
    }
  }

  /** Return l1 without the blob that have one blob in l2 with the same path
   *
   * @param l1
   * @param l2
   * @return
   */
  def removeBlobPathFromL1(l1: List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l2.isEmpty) l1
    else{
      removeBlobPathFromL1(removeBlobWithTheSamePath( l2.head, l1), l2.tail)
    }
  }

  def isBlobInList(blob: Blob, list: List[Blob]) : Boolean = {
    if(list.isEmpty) false
    else (
      if(blob.sameBlob(list.head)) true
      else isBlobInList(blob, list.tail)
    )
  }

  def diffList(l1: List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l1.isEmpty) l1
    else{
      if(isBlobInList(l1.head, l2)) diffList(l1.tail, l2)
      else l1.head :: diffList(l1.tail, l2)
    }
  }

  def asBlobSamePathDiffKey(blob : Blob, list: List[Blob]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(!blob.key.equals(list.head.key) && blob.path.equals(list.head.path)) true
      else asBlobSamePathDiffKey(blob, list.tail)
    }
  }

  def getUpdateAndStaged(wdBlob: List[Blob], indexBlob: List[Blob]) : List[Blob] = {
    if(wdBlob.isEmpty) List.empty
    else {
      if(asBlobSamePathDiffKey(wdBlob.head, indexBlob)) wdBlob.head::getUpdateAndStaged(wdBlob.tail, indexBlob)
      else getUpdateAndStaged(wdBlob.tail, indexBlob)
    }
  }



  def sgitStatus() : Unit  = {
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    //println(wdBlob)
    //println(indexBlob)

    val untrackedAndStaged : List[Blob] = removeBlobPathFromL1(wdBlob, indexBlob)
    //println(untrackedAndStaged)

    val deleteNotStaged : List[Blob] = removeBlobPathFromL1(indexBlob, wdBlob)
    //println(deleteAndStaged)

    val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
    //println(updateAndStaged)

    val update : List[Blob] = diffList(updateAndStaged, indexBlob)
    val untracked : List[Blob] = diffList(untrackedAndStaged, indexBlob)
    if(update.nonEmpty){
      println("File Update : "+ update)
    }
    if (deleteNotStaged.nonEmpty){
      println("File Delete : " + deleteNotStaged)
    }
    if(untracked.nonEmpty){
      println("File Untracked : " + untracked)
    }


  }

}
