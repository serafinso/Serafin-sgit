package sgit.io

import better.files._
import sgit.objects.Tree

object treeConversion {


  def createTreeFile (trees : List[Tree]) : Unit = {
    val treePath: String = ".sgit/objects/tree/"
    if(trees.nonEmpty) {
      val tree : Tree = trees.head
      if(!treePath.contains(tree.key)){
        val newFileInObject = createObject.createFile(isDirectory = false, treePath + tree.key)
        if (newFileInObject) {
          (treePath + tree.key).toFile.appendText(tree.content)
        }
        createTreeFile(trees.tail)
      }
    }
  }


}
