package sgit.localChange

import sgit.io.{blobConversion, indexConversion}
import sgit.objects.Blob
import sgit.objectManipulation.blobManipulation


object status {

  /** PF method
   *
   * @param wdBlob = the list of Blobs in the working directory
   * @param indexBlob = the list of Blobs in the index file
   * @return WD blobs that don't have any index blob with the same pathname
   */
  def getUpdateAndStaged(wdBlob: List[Blob], indexBlob: List[Blob]) : List[Blob] = {
    if(wdBlob.isEmpty) List.empty
    else {
      if(blobManipulation.asBlobSamePathDiffKey(wdBlob.head, indexBlob)) wdBlob.head::getUpdateAndStaged(wdBlob.tail, indexBlob)
      else getUpdateAndStaged(wdBlob.tail, indexBlob)
    }
  }


  /** NOT PF method
   * The main status method
   */
  def sgitStatus() : Unit  = {
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    val untrackedAndStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(wdBlob, indexBlob)
    val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
    val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
    val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
    val untracked : List[Blob] = blobManipulation.diffList(untrackedAndStaged, indexBlob)
    //TO DO changed unmodified
    val unmodified : List[Blob] = List.empty
    val staged : List[Blob] = blobManipulation.diffList(indexBlob, unmodified)
    if((updateNotStaged.isEmpty && deleteNotStaged.isEmpty) && (staged.isEmpty && untracked.isEmpty) ){
      println("Everything is up to date")
    }
    if(updateNotStaged.nonEmpty) {
      println("File Updated : ")
      updateNotStaged.foreach(b => println(b.path))
    }
    if(deleteNotStaged.nonEmpty){
      println("File Deleted : ")
      deleteNotStaged.foreach(b => println(b.path))
    }
    if(untracked.nonEmpty) {
      println("File Untracked : ")
      untracked.foreach (b => println("   " + b.path))
    }
    if(staged.nonEmpty) {
      println("File Staged : ")
      staged.foreach (b => println("   " + b.path))
    }
  }

}
