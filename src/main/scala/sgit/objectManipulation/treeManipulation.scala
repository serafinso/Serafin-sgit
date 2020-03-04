package sgit.objectManipulation


import sgit.objects.{Blob, Tree}

object treeManipulation {

  /**
   *
   * @param tree
   * @param list
   * @return
   */
  def isTreeInList(tree: Tree, list: List[Tree]) : Boolean = {
    if(list.isEmpty) false
    else (
      if(tree.sameTree(list.head)) true
      else isTreeInList(tree, list.tail)
      )
  }

  /**
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


  def getTreeWithPath(path:String, trees: List[Tree]) : List[Tree] = {
    if (trees.isEmpty) trees
    else {
      if (trees.head.path.contains(path)) trees.head::getTreeWithPath(path, trees.tail)
      else getTreeWithPath(path, trees.tail)
    }
  }
}
