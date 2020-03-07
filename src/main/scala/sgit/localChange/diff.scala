package sgit.localChange

import sgit.io.{blobConversion, indexConversion}
import sgit.localChange.status.getUpdateAndStaged
import sgit.objectManipulation.{blobManipulation, stringManipulation}
import sgit.objects.Blob

object diff {

  def getPrintDiff(text : List[String],newT : List[(Int, String)], old : List[(Int, String)], counter: Int) : List[String] = {
    if(text.nonEmpty){
      if(old.nonEmpty && old.head._1 == counter) (Console.RED +"-"+ old.head._2 + Console.BLACK)::getPrintDiff(text, newT, old.tail, counter)
      else {
        if(newT.nonEmpty && newT.head._1 == counter) (Console.GREEN + "+" + text.head + Console.BLACK)::getPrintDiff(text.tail, newT.tail, old, counter + 1)
        else text.head::getPrintDiff(text.tail, newT, old, counter + 1)
      }
    }else List.empty
  }

  /**
   *
   * @param s string
   * @param counter start with 1
   * @return
   */
  def splitStringWithCounter(s : List[String], counter : Int) : List[(Int, String)] = {
    if(s.isEmpty) List.empty
    else (counter, s.head)::splitStringWithCounter(s.tail, counter+1)
  }
  /**
   *
   * @param old
   * @param newb
   */
  def getDiffBlob(old : Blob, newb : Blob) : Unit = {
    val optOldContent : Option[String] = blobConversion.getBlobContent(old)
    val optNewContent : Option[String] = blobConversion.getBlobContent(newb)
    if(optNewContent.isDefined && optOldContent.isDefined){
      val linesO : List[String] = optOldContent.get.split("\n").toList
      val linesN : List[String] = optNewContent.get.split("\n").toList
      val oldNbLine = linesO.length

      println(Console.CYAN + "@@ -1,"+linesO.length+" +1," +linesN.length +" @@"+ Console.BLACK)
      val tupleLineO : List[(Int, String)] = splitStringWithCounter(linesO, 1)
      val tupleLineN : List[(Int, String)] = splitStringWithCounter(linesN, 1)
      val newLines = stringManipulation.diffTupleListString(tupleLineN, tupleLineO)
      val oldLines = stringManipulation.diffTupleListString(tupleLineO, tupleLineN)
      getPrintDiff(linesN,newLines, oldLines, 1).foreach(l => println(l))
    }
  }

  /**
   *
   * @param l1
   * @param l2
   */
  @scala.annotation.tailrec
  def getDiffListBlob(l1 : List[Blob], l2 : List[Blob]) : Unit = {
    if(l1.nonEmpty && l2.nonEmpty){
      getDiffBlob(l1.head, l2.head)
      getDiffListBlob(l1.tail, l2.tail)
    }
  }

  /**
   *
   * @param l1 first list to combine
   * @param l2 second list to combine
   * @return combine the to list in tuple list
   */
  def listToTuple (l1:List[Blob], l2: List[Blob]) : List[Tuple2[Blob, Blob]] = {
    if(l1.size != l2.size || l1.isEmpty) List.empty
    else Tuple2(l1.head, l2.head)::listToTuple(l1.tail, l2.tail)
  }


  def sgitDiff(): Unit ={
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    diffListBlob(indexBlob, wdBlob)
  }

  def printlnDeleted(b : Blob) : Unit = {
    val content : Option[String] = blobConversion.getBlobContent(b)
    println(
      "diff --git a/"+ b.path +"b/" +b.path +"\n"+
        "deleted file \n" +
        "index 0000000.."+ b.key.substring(0,7) + "\n" +
        "--- a/" +b.path+ "\n" +
        "+++ /dev/null"

    )
    if(content.isDefined)
      println(Console.CYAN + "@@ -0,"+content.get.split("\n").length+" +0,0 @@"+ Console.BLACK)
      content.get.split("\n").foreach(line =>
        println(Console.RED + "-" + line + Console.BLACK)
      )
  }


  /** NOT PF method
   * Main diff method
   */
  def diffListBlob(indexBlob : List[Blob], wdBlob : List[Blob]): Unit = {
    val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
    val updateAndStaged : List[Blob] = getUpdateAndStaged(wdBlob, indexBlob)
    val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
    val oldUpdate : List[Blob] = blobManipulation.getBlobsSamePathL1(updateAndStaged, indexBlob)
    //getDiffListBlob(oldUpdate, updateNotStaged)
    val tupleUpdate : List[(Blob, Blob)] = listToTuple(oldUpdate, updateNotStaged)
    //UPDATE
    if(tupleUpdate.nonEmpty) {
      println("File Updated : ")
      tupleUpdate.foreach( t => {
        println(
          "diff --git a/"+ t._1.path +" b/" +t._2.path +"\n"+
            "index 0000000.."+ t._1.key.substring(0,7) +"\n" +
            "--- a/" + t._1.path +"\n" +
            "+++ b/" + t._2.path
        )
        getDiffBlob(t._1,t._2)
        //If to much time the same line in file the method that compare the file doesn't work so much(the line aren't at a good position)
        //In this case print all the old file in red + all the new file in green

        /*if(oContent.isDefined)
          oContent.get.split("\n").foreach(line =>
            println(Console.RED + "-" + line + Console.BLACK)
          )
        if(nContent.isDefined)
          nContent.get.split("\n").foreach(line =>
            println(Console.GREEN + "-" + line + Console.BLACK)
          )*/
      })
    }

    //DELETED
    if(deleteNotStaged.nonEmpty){
      deleteNotStaged.foreach(b => {
        printlnDeleted(b)
      })
    }
  }
}
