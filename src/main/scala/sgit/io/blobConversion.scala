package sgit.io

import better.files._
import sgit.io.utilities.isFilePresent
import sgit.objectManipulation.blobManipulation
import sgit.objects.{Blob, Commit, TreeKey}

object blobConversion {

  /** NOT PF method
   *
   * Gets the file path from the root of the working directory.
   * @param file the file to relativize the path from
   * @return an option containing the path, or none if the path doesn't exist.
   */
  def shortFilePath(file: File): Option[String] = {
    if(!".sgit".toFile.exists || !file.exists) return None
    val rootDir = ".".toFile
    Some(rootDir.relativize(file).toString)
  }

  /** NOT PF method
   *
   * @return the blob list contained in the index
   */
  def wdToBlobList : List[Blob] = {
    val wdFiles : Option[List[File]] = utilities.getAllWDFiles
    if(wdFiles.isEmpty){
      List.empty
    } else {
      filesToBlobList(wdFiles.get)
    }
  }

  /** PF method
   *
   * @param files to convert into blob list
   * @return the blob list
   */
  def filesToBlobList(files : List[File]) : List[Blob] = {
    if(files.isEmpty) List.empty
    else {
      val file : File = files.head
      if(file.isDirectory){
        filesToBlobList(files.tail)
      }
      else{
        val blob : Blob = Blob(file.sha1, shortFilePath(file).get)
        blob::filesToBlobList(files.tail)
      }
    }
  }

  /** NOT PF method
   * Create blobs files
   * @param blobs to be created
   */
  @scala.annotation.tailrec
  def createBlobFiles(blobs : List[Blob]) : Unit = {
    val blobPath: String = ".sgit/objects/blob/"
    if (blobs.nonEmpty) {
      if (!blobPath.contains(blobs.head.key)) {
        val content : Option[String] = getBlobContent(blobs.head)
        val newFileInObject = createObject.createFile(isDirectory = false, blobPath + blobs.head.key)
        if (newFileInObject && content.isDefined) {
          (blobPath + blobs.head.key).toFile.appendText(content.get)
        }
        createBlobFiles(blobs.tail)
      }
    }
  }

  /** NOT PF
   *
   * @param blob the blob
   * @return the content of the blob
   */
  def getBlobContent(blob: Blob) : Option[String] = {
    if(getBlobContentFromKeyInIndex(blob.key).isDefined) return getBlobContentFromKeyInIndex(blob.key)
    if(getBlobContentFromPathInWD(blob.path).isDefined) return getBlobContentFromPathInWD(blob.path)
    println("Blob doesn't exist")
    None
  }

  /** NOT PF method
   * Get the blob content
   * @param key blob key
   */
  def getBlobContentFromKeyInIndex(key : String) : Option[String] = {
    val blobPath: String = ".sgit/objects/blob/" + key
    if (isFilePresent(blobPath)){
      Some(blobPath.toFile.contentAsString)
    } else None
  }

  /** NOT PF method
   * Get the blob content
   * @param path blob path
   */
  def getBlobContentFromPathInWD(path : String) : Option[String] = {
    val blobPath= utilities.getWD + "/" + path
    if (isFilePresent(blobPath)){
      Some(blobPath.toFile.contentAsString)
    } else None
  }

  /** Not PF
   *
   * @param key the tree key
   * @return the blob list from a tree key
   */
  def getBlobsFromRootTreeKey(key : String) : Option[List[Blob]] = {
    val optionTree : Option[TreeKey] = treeConversion.getTreeByKey(key)
    if (optionTree.isDefined){
      Some(blobManipulation.getBlobsFromTree(treeConversion.getTree(optionTree.get.blobs, optionTree.get.treesTuple, List.empty, "root")))
    }else None
  }


  /** Not PF
   *
   * @return get all the blob from the last commit if the last commit exist
   */
  def getLastCommitBlobs: Option[List[Blob]] = {
    val optionCommit : Option[Commit] = commitConversion.getLastCommit
    if(optionCommit.isDefined){
      getBlobsFromRootTreeKey(optionCommit.get.treeC)
    }else None
  }

  def getBlobFromCommit(c : Commit) : Option[List[Blob]] = {
    getBlobsFromRootTreeKey(c.treeC)
  }


}
