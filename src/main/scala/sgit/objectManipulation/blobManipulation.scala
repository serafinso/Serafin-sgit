package sgit.objectManipulation

import sgit.objects.{Blob, Tree}

object blobManipulation {

  /** PF method
   *
   * @param path the path to search in blob list
   * @param blobs the blob list
   * @return a blob that has the right path
   */
  @scala.annotation.tailrec
  def pathInBlobList(path: String, blobs: List[Blob]) : Option[Blob] = {
    if(blobs.isEmpty)None
    else{
      val blob = blobs.head
      if (blob.path.equals(path)) Some(blob)
      else pathInBlobList(path, blobs.tail)
    }
  }

  /** PF method
   *
   * @param blobs the blob list
   * @param blobAndContents the blobAndContent list
   * @return true if blobs and blobAndContents contained the same blobs
   */
  @scala.annotation.tailrec
  def isBlobListEqual(blobs : List[Blob], blobAndContents: List[Blob] ) : Boolean = {
    if(blobs.length != blobAndContents.length) return false
    if(blobs.isEmpty && blobAndContents.isEmpty) return true
    val blob = blobs.head
    val newBlobs: List[Blob] = removeBlob(blob, blobAndContents)
    isBlobListEqual(blobs.tail, newBlobs)
  }

  /** PF method
   *
   * @param blob the blob to remove
   * @param blobs list of blob
   * @return blobAndContents without the blob
   */
  def removeBlob(blob : Blob, blobs: List[Blob]): List[Blob] ={
    if(blobs.isEmpty) List.empty
    else{
      val blobHead = blobs.head
      if(blob.key.equals(blobHead.key) && blob.path.equals(blobHead.path)){
        blobs.tail
      } else {
        blobHead::removeBlob(blob, blobs.tail)
      }
    }
  }

  /** PF method
   * Remove the blob to the List if it contained it otherwise return blobs unaltered
   *
   * @param blob blob to be removed
   * @param blobs blob list
   * @return blobs without the blob blob
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

  /** PF method
   * Return l1 without the blob that have one blob in l2 with the same path
   *
   * @param l1 the first list
   * @param l2 the second list
   * @return remove the blob having the same path in l2 from l1
   */
  @scala.annotation.tailrec
  def removeBlobPathFromL1(l1: List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l2.isEmpty) l1
    else{
      removeBlobPathFromL1(removeBlobWithTheSamePath( l2.head, l1), l2.tail)
    }
  }

  /** PF method
   *
   * @param blob blob to search
   * @param list blob list
   * @return
   */
  @scala.annotation.tailrec
  def isBlobInList(blob: Blob, list: List[Blob]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(blob.sameBlob(list.head)) true
      else isBlobInList(blob, list.tail)
    }
  }

  /** PF method
   *
   * @param l1 the first blob list
   * @param l2 the second blob list
   * @return l1 blob list without the blobs in l2
   */
  def diffList(l1: List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l1.isEmpty) l1
    else{
      if(isBlobInList(l1.head, l2)) diffList(l1.tail, l2)
      else l1.head :: diffList(l1.tail, l2)
    }
  }

  /** PF method
   *
   * @param l1 the first blob list
   * @param l2 the second blob list
   * @return l1 blob list without the blobs in l2
   */
  def interList(l1: List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l1.isEmpty) l1
    else{
      if(isBlobInList(l1.head, l2)) l1.head ::interList(l1.tail, l2)
      else interList(l1.tail, l2)
    }
  }

  /** PF method
   *
   * @param wdBlob = the list of Blobs in the working directory
   * @param indexBlob = the list of Blobs in the index file
   * @return WD blobs that have any index blob with the same pathname but a different key
   */
  def samePathDiffKeyL1(wdBlob: List[Blob], indexBlob: List[Blob]) : List[Blob] = {
    if(wdBlob.isEmpty) List.empty
    else {
      if(blobManipulation.asBlobSamePathDiffKey(wdBlob.head, indexBlob)) wdBlob.head::samePathDiffKeyL1(wdBlob.tail, indexBlob)
      else samePathDiffKeyL1(wdBlob.tail, indexBlob)
    }
  }

  /** PF method
   *
   * @param blob blob to compare
   * @param list the blob list
   * @return true if there is a blob in the list that has the same path but a different key
   */
  @scala.annotation.tailrec
  def asBlobSamePathDiffKey(blob : Blob, list: List[Blob]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(!blob.key.equals(list.head.key) && blob.path.equals(list.head.path)) true
      else asBlobSamePathDiffKey(blob, list.tail)
    }
  }

  /** PF method
   *
   * @param tree the tree
   * @return All the children blob in the tree and in its children trees
   */
  def getBlobsFromTree(tree : Tree) : List[Blob] = {
    if(tree.treesT.isEmpty) tree.blobsT
    else {
      getBlobsFromTree(tree.treesT.head):::getBlobsFromTree(new Tree(tree.path, tree.blobsT, tree.treesT.tail))
    }
  }

  /** PF method
   *
   * @param path path to search
   * @param blobs blob list
   * @return the blob that contained the right path
   */
  def getBlobWithPath(path:String, blobs: List[Blob]) : List[Blob] = {
    if (blobs.isEmpty) blobs
    else {
      if (blobs.head.path.contains(path)) blobs.head::getBlobWithPath(path, blobs.tail)
      else getBlobWithPath(path, blobs.tail)
    }
  }

  /** PF method
   *
   * @param blobs the blob list
   * @param max the length max (to use this method the max should be initializate at 0)
   * @return the max length on the blob list
   */
  @scala.annotation.tailrec
  def maxLenghtBlobPath(blobs : List[Blob], max: Int): Int = {
    if(blobs.isEmpty) max
    else {
      val lenght : Int = stringManipulation.getLengthPath(blobs.head.path)
      if (lenght > max) maxLenghtBlobPath(blobs.tail, lenght)
      else maxLenghtBlobPath(blobs.tail, max)
    }
  }

  /** PF method
   *
   * @param l1 the first list
   * @param l2 the second list
   * @return the blobs in l2 that as the same path of one blob in l1
   */
  def getBlobsSamePathL1(l1:List[Blob], l2: List[Blob]) : List[Blob] = {
    if(l1.isEmpty || l2.isEmpty) l1
    else{
      val optBlob : Option[Blob] = pathInBlobList(l1.head.path, l2)
      if(optBlob.isDefined) optBlob.get::getBlobsSamePathL1(l1.tail, l2)
      else getBlobsSamePathL1(l1.tail, l2)
    }
  }

  /** PF method
   *
   * @param l1 first list to combine
   * @param l2 second list to combine
   * @return combine the to list in tuple list
   */
  def listToTuple (l1:List[Blob], l2: List[Blob]) : List[(Blob, Blob)] = {
    if(l1.size != l2.size || l1.isEmpty) List.empty
    else Tuple2(l1.head, l2.head)::listToTuple(l1.tail, l2.tail)
  }

  /** PF method
   *
   * @param string path
   * @return the blob name on the path
   */
  def lastOnPath(string: String) : String = {
    val split : Array[String] = string.split("/")
    split.last
  }

  /** PF method
   *
   * @param blobs blob list
   * @param length int
   * @return the blob path having the path with the right length
   */
  @scala.annotation.tailrec
  def getPath(blobs : List[Blob], length : Int) : String = {
    if(blobs.isEmpty) "ERREUR"
    else {
      if(stringManipulation.getLengthPath(blobs.head.path) == length) stringManipulation.pathWithoutBlobName(blobs.head.path)
      else getPath(blobs.tail, length)
    }
  }
}
