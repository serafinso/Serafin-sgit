package sgit.localChange

import sgit.objectManipulation.{blobManipulation, treeManipulation}
import sgit.objects.{Blob, Tree}

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
      return new Tree("root", blobs, trees)
    }
    val maxLength : Int = maxLenghtPath(blobs, 0)
    if(maxLength == 1) {
      return new Tree("root", blobs, trees)
    }
    val pathSelected : String = getPath(blobs, maxLength)
    //println(pathSelected)
    val blobsToAdd: List[Blob] = blobManipulation.getBlobWithPath(pathSelected, blobs)
    val treesToAdd: List[Tree] = treeManipulation.getTreeWithPath(pathSelected, trees)
    val treeCreated : Tree = new Tree(pathSelected, blobsToAdd, treesToAdd)
    writeTree(blobManipulation.diffList(blobs, blobsToAdd), treeCreated::treeManipulation.diffList(trees,treesToAdd))
  }


}
