package sgit.localChange

import better.files.File
import sgit.io.{indexConversion, treeConversion, utilities}
import sgit.objectManipulation.{blobManipulation, treeManipulation}
import sgit.objects.{Blob, Commit, Tree}

object commit {

  def getLastCommit(){

  }

  def getLengthPath(blobPath : String): Int = {
    val splitBlob : Array[String] = blobPath.split("/")
    splitBlob.length
  }

  @scala.annotation.tailrec
  def maxLenghtPath(blobs : List[Blob], max: Int): Int = {
    if(blobs.isEmpty) max
    else {
      val lenght : Int = getLengthPath(blobs.head.path)
      if (lenght > max) maxLenghtPath(blobs.tail, lenght)
      else(maxLenghtPath(blobs.tail, max))
    }
  }

  def pathWithoutBlobName(string: String): String = {
    val split : String = string.split("/").dropRight(1).fold(""){(acc, s) => acc + s + "/"}
    split.slice(0, split.length - 1)
  }

  @scala.annotation.tailrec
  def getPath(blobs : List[Blob], length : Int) : String = {
    if(blobs.isEmpty) "ERREUR"
    else {
      if(getLengthPath(blobs.head.path) == length) pathWithoutBlobName(blobs.head.path)
      else(getPath(blobs.tail, length))
    }
  }

  def writeTree(blobs : List[Blob], trees : List[Tree]) : Tree = {
    if(blobs.isEmpty ){
      val treeCreated = new Tree("root", blobs, trees)
      treeConversion.createTreeFile(List(treeCreated))
      return treeCreated
    }
    val maxLength : Int = maxLenghtPath(blobs, 0)
    if(maxLength == 1) {
      val treeCreated = new Tree("root", blobs, trees)
      treeConversion.createTreeFile(List(treeCreated))
      return treeCreated
    }
    val pathSelected : String = getPath(blobs, maxLength)
    //println(pathSelected)
    val blobsToAdd: List[Blob] = blobManipulation.getBlobWithPath(pathSelected, blobs)
    val treesToAdd: List[Tree] = treeManipulation.getTreeWithPath(pathSelected, trees)
    val treeCreated : Tree = new Tree(pathSelected, blobsToAdd, treesToAdd)
    //WARNING NOT PF
    treeConversion.createTreeFile(List(treeCreated))
    writeTree(blobManipulation.diffList(blobs, blobsToAdd), treeCreated::treeManipulation.diffList(trees,treesToAdd))
  }

  def sgitCommit() : Unit = {
    val head : Option[String] = utilities.getHEAD
    if(head.isDefined){
      val headString : String = head.get
      val indexBlob: List[Blob] = indexConversion.indexToBlobList
      val tree : Tree = writeTree(indexBlob, List.empty)
      if (headString.equals("First commit")){
        val commit : Commit = new Commit(tree.key, None, null)
        //CREATE COMMIT
        //UPDATE ref
      } else {
        //GET commitKey
        //create commit : val commit : Commit = new Commit(tree.key, commitKey, null)
        //UPDATE ref
      }

    }
  }

}
