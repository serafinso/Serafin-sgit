package sgit.objectManipulation

import sgit.objects.{Blob, BlobAndContent}

object blobManipulation {

  /**
   *
   * @param blobAndContent
   * @return
   */
  def blobACToBlob(blobAndContent: BlobAndContent) : Blob = {
    Blob(blobAndContent.key, blobAndContent.path)
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
   * @param path
   * @param blobs
   * @return
   */
  def getBlobWithPath(path:String, blobs: List[Blob]) : List[Blob] = {
    if (blobs.isEmpty) blobs
    else {
      if (blobs.head.path.contains(path)) blobs.head::getBlobWithPath(path, blobs.tail)
      else getBlobWithPath(path, blobs.tail)
    }
  }

  /** Remove the blob to the List if it contained it otherwise return blobs unaltered
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
  @scala.annotation.tailrec
  def removeBlobPathFromL1(l1: List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l2.isEmpty) l1
    else{
      removeBlobPathFromL1(removeBlobWithTheSamePath( l2.head, l1), l2.tail)
    }
  }

  @scala.annotation.tailrec
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

  @scala.annotation.tailrec
  def asBlobSamePathDiffKey(blob : Blob, list: List[Blob]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(!blob.key.equals(list.head.key) && blob.path.equals(list.head.path)) true
      else asBlobSamePathDiffKey(blob, list.tail)
    }
  }
}
