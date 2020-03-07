package sgit.objects

case class TreeKey ( blobs: List[Blob], treesTuple: List[(String, String)]){
//treesTuple = (key : String, path : String)

  override def toString : String = {
    blobs.map(l => println(l)).foldLeft(""){(acc, s) => acc + s  + "\n" } + treesTuple.map(l => println(l)).foldLeft(""){(acc, s) => acc + s  + "\n" }
  }
}
