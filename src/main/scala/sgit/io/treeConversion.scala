package sgit.io

import better.files._
import sgit.io.commitConversion.getCommitIfExist
import sgit.io.indexConversion.getIndex
import sgit.io.utilities.isFilePresent
import sgit.objects.{Blob, Commit, Tree, TreeKey}

object treeConversion {

  /** NOT PF method
   * create trees files
   * @param trees to be create as trees files
   */
  @scala.annotation.tailrec
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


  def createTreeKeyFromString( lines : Array[String], blobs: List[Blob], trees : List[(String,String)]) : TreeKey = {
    if(lines.isEmpty) {
      TreeKey(blobs, trees)
    }else {
      if(lines.head.startsWith("blob"))createTreeKeyFromString(lines.tail, Blob(lines.head.split(" ")(1),lines.head.split(" ")(2))::blobs, trees)
      else createTreeKeyFromString(lines.tail, blobs, (lines.head.split(" ")(1),lines.head.split(" ")(2))::trees)
    }
  }

  def getTreeByKey(key:String) : Option[TreeKey] = {
    if (isFilePresent(".sgit/objects/tree/" + key)){
      val treeFile : File = (".sgit/objects/tree/" + key).toFile
      val lines :Array[String] = treeFile.contentAsString
        .replace("\r", "")
        .split("\n")
      if (lines.head.equals("")) {
        println("Invalid tree")
        None
      } else {
        Some(createTreeKeyFromString(lines, List.empty, List.empty))

      }
    }else {
      println("Tree doesn't exist")
      None
    }
  }

  /** not pf mmethod
   *
   * @param blobs blob list that are direct child of the tree
   * @param treesTuples tree list (in form of tuple)
   * @param trees trees creating during the method (start empty)
   * @param currentPath path of the current Tree being creating
   * @return
   */
   def getTree(blobs:List[Blob], treesTuples: List[(String,String)], trees: List[Tree], currentPath : String) : Tree = {
     if(treesTuples.isEmpty) new Tree(currentPath, blobs, trees)
     else{
       val optionChild : Option[TreeKey] = getTreeByKey(treesTuples.head._1)
       if(optionChild.isDefined){
         val childrenTree: Tree = getTree(optionChild.get.blobs, optionChild.get.treesTuple, List.empty, treesTuples.head._2)
         getTree(blobs, treesTuples.tail, childrenTree::trees, currentPath)
       }
       else getTree(blobs, treesTuples.tail, trees, currentPath)
     }
   }






}
