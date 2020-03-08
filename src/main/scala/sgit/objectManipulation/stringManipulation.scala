package sgit.objectManipulation

object stringManipulation {
  /** PF method
   *
   * @param blob blob to search
   * @param list blob list
   * @return
   */
  @scala.annotation.tailrec
  def isBlobInList(blob: String, list: List[String]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(blob.equals(list.head)) true
      else isBlobInList(blob, list.tail)
    }
  }

  /** PF method
   *
   * @param l1 the first blob list
   * @param l2 the second blob list
   * @return l1 blob list without the blobs in l2
   */
  def diffListString(l1: List[String], l2: List[String]) : List[String] = {
    if(l1.isEmpty) l1
    else{
      if(isBlobInList(l1.head, l2)) diffListString(l1.tail, l2)
      else l1.head :: diffListString(l1.tail, l2)
    }
  }

  /** PF method
   *
   * @param s the string
   * @param list the list of tuple(int, string) (int : line numero, string : line content)
   * @return the list without the string
   */
  def removeOneTimeStringFromList(s : String, list: List[(Int,String)]) : List[(Int,String)] ={
    if(list.isEmpty) list
    else {
      if(s.equals(list.head._2)) list.tail
      else list.head :: removeOneTimeStringFromList(s, list.tail)
    }
  }

  /** PF method
   *
   * @param s blob to search
   * @param list blob list
   * @return
   */
  @scala.annotation.tailrec
  def isStringInListTuple(s: String, list: List[(Int,String)]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(s.equals(list.head._2)) true
      else isStringInListTuple(s, list.tail)
    }
  }

  /** PF method
   *
   * @param l1 the first blob list
   * @param l2 the second blob list
   * @return remove one time the l2 list string from the l1 if it exist
   */
  def diffTupleListString(l1: List[(Int,String)], l2: List[(Int,String)]) : List[(Int,String)] = {
    if(l1.isEmpty) l1
    else{
      if(isStringInListTuple(l1.head._2, l2)) {
        diffTupleListString(l1.tail, removeOneTimeStringFromList(l1.head._2, l2))
      }
      else l1.head :: diffTupleListString(l1.tail, l2)
    }
  }

  /** PF method
   *
   * @param s string
   * @param counter start with 1
   * @return
   */
  def splitStringWithCounter(s : List[String], counter : Int) : List[(Int, String)] = {
    if(s.isEmpty) List.empty
    else (counter, s.head)::splitStringWithCounter(s.tail, counter+1)
  }

  /** PF method
   *
   * @param text text to be reconstruct with the added lines and deleted lines
   * @param newT new text
   * @param old old text
   * @param counter the counter of lines
   * @return text reconstruct with the added lines and deleted lines
   */
  def getPrintDiff(text : List[String],newT : List[(Int, String)], old : List[(Int, String)], counter: Int) : List[String] = {
    if(text.nonEmpty){
      if(old.nonEmpty && old.head._1 == counter) (Console.RED +"-"+ old.head._2 + Console.BLACK)::getPrintDiff(text, newT, old.tail, counter)
      else {
        if(newT.nonEmpty && newT.head._1 == counter) (Console.GREEN + "+" + text.head + Console.BLACK)::getPrintDiff(text.tail, newT.tail, old, counter + 1)
        else text.head::getPrintDiff(text.tail, newT, old, counter + 1)
      }
    }else List.empty
  }

  /** PF method
   *
   * @param path the path
   * @return the path length
   */
  def getLengthPath(path : String): Int = {
    val splitBlob : Array[String] = path.split("/")
    splitBlob.length
  }

  /** PF method
   *
   * @param string path
   * @return path without the blob name at the end of path
   */
  def pathWithoutBlobName(string: String): String = {
    val split : String = string.split("/").dropRight(1).fold(""){(acc, s) => acc + s + "/"}
    split.slice(0, split.length - 1)
  }
}
