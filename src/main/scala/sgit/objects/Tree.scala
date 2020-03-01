package sgit.objects

case class Tree(keyTree: String, pathTree: String){

  def sameTree(otherTree : Tree): Boolean = { keyTree == otherTree.keyTree && pathTree == otherTree.pathTree}

}