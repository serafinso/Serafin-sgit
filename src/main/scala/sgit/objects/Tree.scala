package sgit.objects

import sgit.io.utilities.getKeySha1FromString

class Tree(pathT : String, blobs: List[Blob], trees: List[Tree]){

  val path : String = pathT
  val treesT : List[Tree] = trees

  val content : String  = {
    val blobsSorted : List[Blob] = blobs.sortBy(b=>b.key)
    val treesSorted : List[Tree] = trees.sortBy(t=>t.key)
    val blobString : List[String] = blobsSorted.map(blob => "blob " + blob.key + " " + blob.path)
    val treeString : List[String] = treesSorted.map(tree => "tree " + tree.key + " " + tree.path)
    blobString.foldLeft(""){(acc, s) => acc + s + "\n"} + treeString.foldLeft(""){(acc, s) => acc + s  + "\n" }
  }

  val key : String = {
    getKeySha1FromString(content)
  }

  def sameTree(otherTree : Tree): Boolean = {
    key == otherTree.key && path == otherTree.path
  }

  override def toString : String = {
    val c = content
    val k = key
    "key : " + k + "\n" + "path : " + path + "\n" + c
  }
}
