package sgit.localChange

import sgit.io.{blobConversion, indexConversion}
import sgit.localChange.status.getUpdateAndStaged
import sgit.objectManipulation.{blobManipulation, stringManipulation}
import sgit.objects.Blob

object diff {

  def getDiffBlob(old : Blob, newb : Blob) : Unit = {
    val optOldContent : Option[String] = blobConversion.getBlobContent(old)
    val optNewContent : Option[String] = blobConversion.getBlobContent(newb)
    if(optNewContent.isDefined && optOldContent.isDefined){
      val linesO : List[String] = optOldContent.get.split("\n").toList
      val linesN : List[String] = optNewContent.get.split("\n").toList
      println("NEW : " + stringManipulation.diffListString(linesN, linesO))
      println("OLD : " + stringManipulation.diffListString(linesO, linesN))
    }
  }

  @scala.annotation.tailrec
  def getDiffListBlob(old : List[Blob], newL : List[Blob]) : Unit = {
    if(old.nonEmpty && newL.nonEmpty){
      getDiffBlob(old.head, newL.head)
      getDiffListBlob(old.tail, newL.tail)
    }
  }

  def listToTuple (l1:List[Blob], l2: List[Blob]) : List[Tuple2[Blob, Blob]] = {
    if(l1.size != l2.size || l1.isEmpty) List.empty
    else Tuple2(l1.head, l2.head)::listToTuple(l1.tail, l2.tail)
  }


  /** NOT PF method
   * Main diff method
   */
  def sgitDiff(): Unit = {
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
    val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
    val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
    val oldUpdate : List[Blob] = blobManipulation.getBlobsSamePathL1(updateAndStaged, indexBlob)

    //println(oldUpdate)
    //println(updateNotStaged)
    getDiffListBlob(oldUpdate, updateNotStaged)
    val tupleUpdate : List[(Blob, Blob)] = listToTuple(oldUpdate, updateNotStaged)
    //UPDATE
    if(tupleUpdate.nonEmpty) {
      println("File Updated : ")
      tupleUpdate.foreach( t => {
        val oContent : Option[String] = blobConversion.getBlobContent(t._1)
        val nContent : Option[String] = blobConversion.getBlobContent(t._2)
        println(
          "diff --git a/"+ t._1.path +" b/" +t._2.path +"\n"+
            "index "+ t._1.key +"\n" +
            "--- a/" + t._1.path +"\n" +
            "+++ b/" + t._2.path
        )
        if(oContent.isDefined)
          oContent.get.split("\n").foreach(line =>
            println(Console.RED + "-" + line + Console.BLACK)
          )
        if(nContent.isDefined)
          nContent.get.split("\n").foreach(line =>
            println(Console.GREEN + "-" + line + Console.BLACK)
          )
      })
    }

    //DELETED
    if(deleteNotStaged.nonEmpty){
      deleteNotStaged.foreach(b => {
        val content : Option[String] = blobConversion.getBlobContent(b)
        println(
          "diff --git a/"+ b.path +"b/" +b.path +"\n"+
            "deleted file \n" +
            "index "+ b.key
        )
        if(content.isDefined)
          content.get.split("\n").foreach(line =>
            println(Console.RED + "-" + line + Console.BLACK)
          )
      })
    }
  }
}
