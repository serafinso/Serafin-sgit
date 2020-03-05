package sgit.localChange

import sgit.io.{blobConversion, indexConversion}
import sgit.localChange.status.getUpdateAndStaged
import sgit.objectManipulation.blobManipulation
import sgit.objects.Blob

object diff {

  /** NOT PF method
   * Main diff method
   */
  def sgitDiff(): Unit = {
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
    val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
    val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
    if(updateNotStaged.nonEmpty) {
      println("File Updated : ")
      updateNotStaged.foreach(b => println(
        "diff --git a/"+ b.path +" b/" +b.path +"\n"+
          "index "+ b.key +"\n" +
          "--- a/" + b.path +"\n" +
          "+++ b/" + b.path
      ))
    }
    if(deleteNotStaged.nonEmpty){
      deleteNotStaged.foreach(b => println(
        "diff --git a/"+ b.path +"b/" +b.path +"\n"+
          "deleted file \n" +
          "index "+ b.key
      ))
    }
  }
}
