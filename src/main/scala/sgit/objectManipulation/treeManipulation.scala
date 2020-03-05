package sgit.objectManipulation

import sgit.objects.Tree

object treeManipulation {

  /** PF method
   *
   * @param tree
   * @param list
   * @return
   */
  @scala.annotation.tailrec
  def isTreeInList(tree: Tree, list: List[Tree]) : Boolean = {
    if(list.isEmpty) false
    else (
      if(tree.sameTree(list.head)) true
      else isTreeInList(tree, list.tail)
      )
  }

  /** PF method
   *
   * @param l1
   * @param l2
   * @return
   */
  def diffList(l1: List[Tree], l2: List[Tree]) : List[Tree] = {
    if(l1.isEmpty) l1
    else{
      if(isTreeInList(l1.head, l2)) diffList(l1.tail, l2)
      else l1.head :: diffList(l1.tail, l2)
    }
  }

  /** PF method
   *
   * @param path
   * @param trees
   * @return
   */
  def getTreeWithPath(path:String, trees: List[Tree]) : List[Tree] = {
    if (trees.isEmpty) trees
    else {
      if (trees.head.path.contains(path)) trees.head::getTreeWithPath(path, trees.tail)
      else getTreeWithPath(path, trees.tail)
    }
  }
}
