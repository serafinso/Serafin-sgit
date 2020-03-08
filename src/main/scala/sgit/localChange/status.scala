package sgit.localChange

import better.files._
import sgit.io.objectConversion.{blobConversion, indexConversion}
import sgit.io.utilities
import sgit.objects.Blob
import sgit.objectManipulation.blobManipulation

object status {

  /** NOT PF method
   * The main status method
   */
  def sgitStatus() : Unit  = {
    if(!utilities.isFilePresent(".sgit")) {
      println("Git init first")
    }else{
      val indexBlob: List[Blob] = indexConversion.indexToBlobList
      val wdBlob : List[Blob] = blobConversion.wdToBlobList
      val untracked: List[Blob] = blobManipulation.removeBlobPathFromL1(wdBlob, indexBlob)
      val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
      val updateAndMaybeStaged : List[Blob] = blobManipulation.samePathDiffKeyL1(wdBlob, indexBlob)
      val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndMaybeStaged, indexBlob)
      val optionCommited : Option[List[Blob]] = blobConversion.getLastCommitBlobs
      val commited : List[Blob] = {
        if(optionCommited.isDefined) optionCommited.get
        else List.empty
      }
      val updatedAndStage = blobManipulation.samePathDiffKeyL1(commited, indexBlob)
      val staged : List[Blob]= blobManipulation.diffList(indexBlob, commited)
      if((updateNotStaged.isEmpty && deleteNotStaged.isEmpty) && (staged.isEmpty && untracked.isEmpty) ){
        println("nothing to commit, working tree clean")
      }
      //STAGED
      if(staged.nonEmpty) {
        println("Changes to be committed: + \n")
        //staged.foreach (b =>  println(Console.GREEN +"\t   " + b.path + Console.BLACK) )
        blobManipulation.removeBlobPathFromL1(staged, updatedAndStage).foreach (b =>  println(Console.GREEN +"\tnew files:   " + b.path + Console.BLACK) )
        updatedAndStage.foreach (b =>  println(Console.GREEN +"\tupdated:   " + b.path + Console.BLACK) )
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
