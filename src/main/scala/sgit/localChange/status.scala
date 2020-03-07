package sgit.localChange

import sgit.io.{blobConversion, indexConversion, utilities}
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
    if(!utilities.isFilePresent(".sgit")) {
      println("Git init first")
    }else{
      val indexBlob: List[Blob] = indexConversion.indexToBlobList
      val wdBlob : List[Blob] = blobConversion.wdToBlobList
      val untrackedAndStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(wdBlob, indexBlob)
      val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
      val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
      val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
      val untracked : List[Blob] = blobManipulation.diffList(untrackedAndStaged, indexBlob)
      val optionCommited : Option[List[Blob]] = blobConversion.getLastCommitBlobs
      val commited : List[Blob] = {
        if(optionCommited.isDefined) optionCommited.get
        else List.empty
      }
      val staged : List[Blob]= blobManipulation.diffList(indexBlob, commited)
      if((updateNotStaged.isEmpty && deleteNotStaged.isEmpty) && (staged.isEmpty && untracked.isEmpty) ){
        println("nothing to commit, working tree clean")
      }
      //STAGED
      if(staged.nonEmpty) {
        println("Changes to be committed:\n ")
        staged.foreach (b =>  println(Console.GREEN +"\tnew file:   " + b.path + Console.BLACK) )
        println()
      }
      //MODIFIED
      if(updateNotStaged.nonEmpty || deleteNotStaged.nonEmpty) {
        println("Changes not staged for commit:\n  (use \"sgit add <file>...\" to update what will be committed) : \n")
        updateNotStaged.foreach(b => println(Console.RED +"\tmodified:   " + b.path+ Console.BLACK))
        deleteNotStaged.foreach(b => println(Console.RED +"\tdeleted:   " + b.path + Console.BLACK))
        println()
      }

      //UNTRACKED
      if(untracked.nonEmpty) {
        println("Untracked files:\n  (use \"sgit add <file>...\" to include in what will be committed) \n")
        untracked.foreach (b => println(Console.RED +"\t" + b.path + Console.BLACK)  )
        println()
      }
    }
  }

}
