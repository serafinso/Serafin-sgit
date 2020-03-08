package sgit.localChange

import sgit.io.objectConversion.{blobConversion, indexConversion}
import sgit.io.utilities
import sgit.objects.Blob
import sgit.objectManipulation.blobManipulation

object add {

  /** NOT PF method
   *
   * Main add method
   * @param files = files to add
   */
  @scala.annotation.tailrec
  def addFiles(files : Seq[String]): Unit = {
    if(!utilities.isFilePresent(".sgit")) println("Git init first")
    else{
      if(files.nonEmpty){
        val file = files.head
        val indexBlob: List[Blob] = indexConversion.indexToBlobList
        val wdBlob : List[Blob] = blobConversion.wdToBlobList
        file match {
          case "." =>
            if(blobManipulation.isBlobListEqual(indexBlob, wdBlob)){
              println("Everything is up to date")
            } else {
              blobConversion.createBlobFiles(wdBlob)
              indexConversion.blobListToIndexFile(wdBlob)
            }
          case _ =>
            val pathInWD : Option[Blob] = blobManipulation.pathInBlobList(file, wdBlob)
            if(pathInWD.isDefined){ //IN WD
              val blob : Blob = pathInWD.get
              if( !blobManipulation.isBlobInList( blob, indexBlob)){
                //BLOB NOT IN INDEX
                val isInIndex : Option[Blob] = blobManipulation.pathInBlobList(blob.path,indexBlob)
                if(isInIndex.isDefined){
                  //PATH IN INDEX (Modified/Update)
                  blobConversion.createBlobFiles(List(blob))
                  val blobToRemove : Blob = isInIndex.get
                  val newindexBlob = blob::blobManipulation.removeBlob(blobToRemove, indexBlob)
                  indexConversion.blobListToIndexFile(newindexBlob)
                }else{
                  //PATH NOT IN INDEX (Untracked)
                  blobConversion.createBlobFiles(List(blob))
                  val newindexBlob = blob::indexBlob
                  indexConversion.blobListToIndexFile(newindexBlob)
                }
              }else{
                //IN WD and IN INDEX
                println(blob.path + " already staged")
              }
            }else{ //NOT IN WD
              val isInWd : Option[Blob] = blobManipulation.pathInBlobList(file, indexBlob)
              if(isInWd.isDefined){ //Modified/Delete
                val newindexBlob = blobManipulation.removeBlob(isInWd.get, indexBlob)
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
