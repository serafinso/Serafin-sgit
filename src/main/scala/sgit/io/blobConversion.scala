package sgit.io

import better.files._
import sgit.io.utilities.isFilePresent
import sgit.localChange.commit
import sgit.objectManipulation.blobManipulation
import sgit.objects.{Blob, BlobAndContent, Commit, Tree, TreeKey}

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
   * @return the list of blobAC containing in the index
   */
  def wdToBlobACList : List[BlobAndContent] = {
    val wdFiles : Option[List[File]] = utilities.getAllWDFiles
    if(wdFiles.isEmpty){
      List.empty
    } else {
      filesToBlobAndContentList(wdFiles.get)
    }
  }

  /** PF method
   *
   * @param files the files to convert in blobAC list
   * @return the blobAC list contained in the working directory
   */
  def filesToBlobAndContentList(files : List[File]) : List[BlobAndContent] = {
      if(files.isEmpty) List.empty
      else {
        val file : File = files.head
        if(file.isDirectory){
          filesToBlobAndContentList(files.tail)
        }
        else{
          val blob : BlobAndContent = BlobAndContent(file.sha1, shortFilePath(file).get, file.contentAsString )
          blob::filesToBlobAndContentList(files.tail)
        }
      }
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
  def createBlobFiles(blobs : List[BlobAndContent]) : Unit = {
    val blobPath: String = ".sgit/objects/blob/"
    if (blobs.nonEmpty) {
      val blob: BlobAndContent = blobs.head
      if (!blobPath.contains(blob.key)) {
        val newFileInObject = createObject.createFile(isDirectory = false, blobPath + blob.key)
        if (newFileInObject) {
          (blobPath + blob.key).toFile.appendText(blob.content)
        }
        createBlobFiles(blobs.tail)
      }
    }
  }

  def getBlobContent(blob: Blob) : Option[String] = {
    if(getBlobContentFromKeyInIndex(blob.key).isDefined) return getBlobContentFromKeyInIndex(blob.key)
    if(getBlobContentFromPathInWD(blob.path).isDefined) return getBlobContentFromPathInWD(blob.path)
    println("Blob doesn't exist")
    None
  }

  /** NOT PF method
   * Create blobs files
   * @param key
   */
  def getBlobContentFromKeyInIndex(key : String) : Option[String] = {
    val blobPath: String = ".sgit/objects/blob/" + key
    if (isFilePresent(blobPath)){
      Some(blobPath.toFile.contentAsString)
    } else None
  }

  /** NOT PF method
   * Create blobs files
   * @param path
   */
  def getBlobContentFromPathInWD(path : String) : Option[String] = {
    val blobPath= utilities.getWD + "/" + path
    if (isFilePresent(blobPath)){
      Some(blobPath.toFile.contentAsString)
    } else None
  }

  /**
   *
   * @param treeKey
   * @return
   */
  def getBlobsFromRootTreeKey(treeKey : String) : Option[List[Blob]] = {
    val optionTree : Option[TreeKey] = treeConversion.getTreeByKey(treeKey)
    if (optionTree.isDefined){
      Some(blobManipulation.getBlobsFromTree(treeConversion.getTree(optionTree.get.blobs, optionTree.get.treesTuple, List.empty, "root")))
    }else None
  }


  /**
   *
   * @return
   */
  def getLastCommitBlobs: Option[List[Blob]] = {
    val optionCommit : Option[Commit] = commitConversion.getLastCommit
    if(optionCommit.isDefined){
      getBlobsFromRootTreeKey(optionCommit.get.treeC)
    }else None
  }


}
