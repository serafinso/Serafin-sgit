package sgit.objectManipulation

import sgit.objects.Blob

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
}
