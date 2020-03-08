package sgit.localChange

import sgit.io.objectConversion.{blobConversion, indexConversion}
import sgit.objectManipulation.{blobManipulation, stringManipulation}
import sgit.objects.Blob

object diff {
  /** Not PF method
   *
   * @param old old blob
   * @param newb new blob
   * Print the difference between the two blobs
   */
  def printDiffBlob(old : Blob, newb : Blob) : Unit = {
    val optOldContent : Option[String] = blobConversion.getBlobContent(old)
    val optNewContent : Option[String] = blobConversion.getBlobContent(newb)
    if(optNewContent.isDefined && optOldContent.isDefined){
      val linesO : List[String] = optOldContent.get.split("\n").toList
      val linesN : List[String] = optNewContent.get.split("\n").toList
      println(Console.CYAN + "@@ -1,"+linesO.length+" +1," +linesN.length +" @@"+ Console.BLACK)
      val tupleLineO : List[(Int, String)] = stringManipulation.splitStringWithCounter(linesO, 1)
      val tupleLineN : List[(Int, String)] = stringManipulation.splitStringWithCounter(linesN, 1)
      val newLines = stringManipulation.diffTupleListString(tupleLineN, tupleLineO)
      val oldLines = stringManipulation.diffTupleListString(tupleLineO, tupleLineN)
      stringManipulation.getPrintDiff(linesN,newLines, oldLines, 1).foreach(l => println(l))
    }
  }

  /** NOT PF method
   *
   * @param l1 First list
   * @param l2 Second list
   * print the difference between the to list.
   */
  @scala.annotation.tailrec
  def printDiffListBlob(l1 : List[Blob], l2 : List[Blob]) : Unit = {
    if(l1.nonEmpty && l2.nonEmpty){
      printDiffBlob(l1.head, l2.head)
      printDiffListBlob(l1.tail, l2.tail)
    }
  }

  /** NOT PF method
   *
   * @param b the deleted blob to be printed
   * Print the deleted blob
   */
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
   * The main diff method
   */
  def sgitDiff(): Unit ={
    val indexBlob: List[Blob] = indexConversion.indexToBlobList
    val wdBlob : List[Blob] = blobConversion.wdToBlobList
    diffListBlob(indexBlob, wdBlob)
  }

  /** NOT PF method
   * Main diff method
   */
  def diffListBlob(indexBlob : List[Blob], wdBlob : List[Blob]): Unit = {
    val deleteNotStaged : List[Blob] = blobManipulation.removeBlobPathFromL1(indexBlob, wdBlob)
    val updateAndStaged : List[Blob] = blobManipulation.samePathDiffKeyL1(wdBlob, indexBlob)
    val updateNotStaged : List[Blob] = blobManipulation.diffList(updateAndStaged, indexBlob)
    val oldUpdate : List[Blob] = blobManipulation.getBlobsSamePathL1(updateAndStaged, indexBlob)
    val tupleUpdate : List[(Blob, Blob)] = blobManipulation.listToTuple(oldUpdate, updateNotStaged)
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
        printDiffBlob(t._1,t._2)
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
