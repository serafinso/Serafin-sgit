package sgit.objects

/** The tree key object used to get the Tree of the .sgit/objects/tree
 *
 * @param blobs the blobs children of the Tree
 * @param treesTuple the tree tuple contained teh key tree and the path tree
 */
case class TreeKey ( blobs: List[Blob], treesTuple: List[(String, String)]){
//treesTuple = (key : String, path : String)

  override def toString : String = {
    blobs.map(l => println(l)).foldLeft(""){(acc, s) => acc + s  + "\n" } + treesTuple.map(l => println(l)).foldLeft(""){(acc, s) => acc + s  + "\n" }
  }
}
