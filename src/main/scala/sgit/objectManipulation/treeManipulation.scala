package sgit.objectManipulation

import sgit.objects.Tree

object treeManipulation {

  /** PF method
   *
   * @param tree tree to search
   * @param list the tree list
   * @return true if list contained the tree, false otherwise
   */
  @scala.annotation.tailrec
  def isTreeInList(tree: Tree, list: List[Tree]) : Boolean = {
    if(list.isEmpty) false
    else {
      if(tree.sameTree(list.head)) true
      else isTreeInList(tree, list.tail)
    }
  }

  /** PF method
   *
   * @param l1 the first tree list
   * @param l2 the second tree list
   * @return l1 tree list without the trees in l2
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
   * @param path the path to search in the tree list
   * @param trees the tree list
   * @return the tree list with the tree having the rigth path
   */
  def getTreeWithPath(path:String, trees: List[Tree]) : List[Tree] = {
    if (trees.isEmpty) trees
    else {
      if (trees.head.path.contains(path)) trees.head::getTreeWithPath(path, trees.tail)
      else getTreeWithPath(path, trees.tail)
    }
  }
}
