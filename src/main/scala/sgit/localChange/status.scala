package sgit.localChange

import sgit.io.{blobConversion, indexConversion}
import sgit.objects.{Blob, BlobAndContent}
import sgit.localChange.add
import sgit.objectManipulation.blobManipulation


object status {
  /**
   *
   * @param wdBlob
   * @param indexBlob
   * @return
   */
  def getUpdateAndStaged(wdBlob: List[Blob], indexBlob: List[Blob]) : List[Blob] = {
    if(wdBlob.isEmpty) List.empty
    else {
      if(blobManipulation.asBlobSamePathDiffKey(wdBlob.head, indexBlob)) wdBlob.head::getUpdateAndStaged(wdBlob.tail, indexBlob)
      else getUpdateAndStaged(wdBlob.tail, indexBlob)
    }
  }

  def sgitStatus() : Unit  = {
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    val untrackedAndStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(wdBlob, indexBlob)
    val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
    val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
    val update : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
    val untracked : List[Blob] = blobManipulation.diffList(untrackedAndStaged, indexBlob)
    if(update.nonEmpty) println("File Update : "+ update)
    if (deleteNotStaged.nonEmpty){println("File Delete : " + deleteNotStaged)}
    if(untracked.nonEmpty) {println("File Untracked : " + untracked)}
  }

}
