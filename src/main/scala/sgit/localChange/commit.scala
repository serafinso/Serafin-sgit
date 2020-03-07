package sgit.localChange

import sgit.io.{commitConversion, indexConversion, refsConversion, treeConversion, utilities}
import sgit.objectManipulation.{blobManipulation, treeManipulation}
import sgit.objects.{Blob, Commit, Ref, Tree}

object commit {
  /** PF method
   *
   * @param blobPath the path
   * @return the path length
   */
  def getLengthPath(blobPath : String): Int = {
    val splitBlob : Array[String] = blobPath.split("/")
    splitBlob.length
  }

  /** PF method
   *
   * @param blobs the blob list
   * @param max the length max (to use this method the max should be initializate at 0)
   * @return the max length on the blob list
   */
  @scala.annotation.tailrec
  def maxLenghtPath(blobs : List[Blob], max: Int): Int = {
    if(blobs.isEmpty) max
    else {
      val lenght : Int = getLengthPath(blobs.head.path)
      if (lenght > max) maxLenghtPath(blobs.tail, lenght)
      else maxLenghtPath(blobs.tail, max)
    }
  }

  /** PF method
   *
   * @param string path
   * @return path without the blob name at the end of path
   */
  def pathWithoutBlobName(string: String): String = {
    val split : String = string.split("/").dropRight(1).fold(""){(acc, s) => acc + s + "/"}
    split.slice(0, split.length - 1)
  }

  /** PF method
   *
   * @param string path
   * @return the blob name on the path
   */
  def lastOnPath(string: String) : String = {
    val split : Array[String] = string.split("/")
    split.last
  }

  /** PF method
   *
   * @param blobs blob list
   * @param length int
   * @return the blob having the path with the right length
   */
  @scala.annotation.tailrec
  def getPath(blobs : List[Blob], length : Int) : String = {
    if(blobs.isEmpty) "ERREUR"
    else {
      if(getLengthPath(blobs.head.path) == length) pathWithoutBlobName(blobs.head.path)
      else getPath(blobs.tail, length)
    }
  }

  /** NOT PF method
   *
   * @param blobs blob list
   * @param trees tree list (the method should be used with the tree list empty)
   * @return the tree containing all the blob on the blob list
   */
  @scala.annotation.tailrec
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
    val blobsToAdd: List[Blob] = blobManipulation.getBlobWithPath(pathSelected, blobs)
    val treesToAdd: List[Tree] = treeManipulation.getTreeWithPath(pathSelected, trees)
    val treeCreated : Tree = new Tree(pathSelected, blobsToAdd, treesToAdd)
    //WARNING NOT PF
    treeConversion.createTreeFile(List(treeCreated))
    writeTree(blobManipulation.diffList(blobs, blobsToAdd), treeCreated::treeManipulation.diffList(trees,treesToAdd))
  }


  /** NOT PF method
   *
   * Main commit method
   * @param message the commit message
   */
  def sgitCommit(message: String) : Unit = {
    val head : Option[String] = utilities.getHEAD
    if(head.isDefined){ //init done
      val headString : String = head.get
      val indexBlob: List[Blob] = indexConversion.indexToBlobList
      if (indexBlob.isEmpty){
        println("nothing to commit")
      }else {
        val tree : Tree = writeTree(indexBlob, List.empty)
        if (headString.equals("Initial commit")){
          println("On branch master\n\n" + headString)
          utilities.updtateHEAD("refs/head/master")
          val commit : Commit = new Commit(tree.key, None, message)
          refsConversion.createOrUpdateRefFile(Ref(commit.key, "master")) //CREATE REF FILE
          commitConversion.createCommitFile(List(commit))//CREATE COMMIT FILE
        } else {
          //GET last commitKey
          val refName = lastOnPath(headString) //master
          val OptionCommitKey: Option[Ref] = refsConversion.getRefByName(refName)
          if(OptionCommitKey.isDefined){
            val lastCommit : Option[Commit] = commitConversion.getCommitByKey(OptionCommitKey.get.commitKey)
            if(lastCommit.get.treeC.equals(tree.key)){
              println("Everything is up to date")
            }else{
              val commit : Commit = new Commit(tree.key, Option(OptionCommitKey.get.commitKey), message)
              refsConversion.createOrUpdateRefFile(Ref(commit.key, refName)) //CREATE REF FILE
              commitConversion.createCommitFile(List(commit))//CREATE COMMIT FILE
              println("Succesfully commited")
            }
          }else{
            println("Last commit ref invalid")
          }
        }
      }
    }
  }

}
