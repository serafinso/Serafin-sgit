package sgit.io

import better.files._
import sgit.objects.{Blob, BlobAndContent}
import utilities._

import scala.annotation.tailrec

object blobConversion {

  /**
   * Gets the file path from the root of the working directory.
   * @param file the file to relativize the path from
   * @return an option containing the path, or none if the path doesn't exist.
   */
  def shortFilePath(file: File): Option[String] = {
    if(!".sgit".toFile.exists || !file.exists) return None
    val rootDir = ".".toFile
    Some(rootDir.relativize(file).toString)
  }

  /**
   *
   * @return
   */
  def wdToBlobACList : List[BlobAndContent] = {
    val wdFiles : Option[List[File]] = utilities.getAllWDFiles
    if(wdFiles.isEmpty){
      List.empty
    } else {
      wdFilesToBlobAndContentList(wdFiles.get)
    }
  }



  /**
   *
   * @param files
   * @return
   */
  def wdFilesToBlobAndContentList(files : List[File]) : List[BlobAndContent] = {
      if(files.isEmpty) List.empty
      else {
        val file : File = files.head
        if(file.isDirectory){
          wdFilesToBlobAndContentList(files.tail)
        }
        else{
          val blob : BlobAndContent = new BlobAndContent(file.sha1, shortFilePath(file).get, file.contentAsString )
          blob::wdFilesToBlobAndContentList(files.tail)
        }
      }
  }

  /**
   *
   * @return
   */
  def wdToBlobList : List[Blob] = {
    val wdFiles : Option[List[File]] = utilities.getAllWDFiles
    if(wdFiles.isEmpty){
      List.empty
    } else {
      wdFilesToBlobList(wdFiles.get)
    }
  }



  /**
   *
   * @param files
   * @return
   */
  def wdFilesToBlobList(files : List[File]) : List[Blob] = {
    if(files.isEmpty) List.empty
    else {
      val file : File = files.head
      if(file.isDirectory){
        wdFilesToBlobList(files.tail)
      }
      else{
        val blob : Blob = new Blob(file.sha1, shortFilePath(file).get )
        blob::wdFilesToBlobList(files.tail)
      }
    }
  }

  /**
   *
   * @param files
   * @return
   */
  def filesToBlobList(files : List[File]) : List[Blob] = {
    if(files.isEmpty) List.empty
    else {
      val file : File = files.head
      if(file.isDirectory){
        filesToBlobList(files.tail)
      }
      else{
        val blob : Blob = new Blob(file.sha1, shortFilePath(file).get)
        blob::filesToBlobList(files.tail)
      }
    }
  }

  /**
   *
   * @param blobs
   */
  @scala.annotation.tailrec
  def createBlobFiles(blobs : List[BlobAndContent]) : Unit = {
    val blobPath: String = ".sgit/objects/blob/"
    if(blobs.nonEmpty) {
      val blob : BlobAndContent = blobs.head
      if(!blobPath.contains(blob.key)){
        val newFileInObject = createObject.createFile(isDirectory = false, blobPath + blob.key)
        if (newFileInObject) {
          (blobPath + blob.key).toFile.appendText(blob.content)
        }
        createBlobFiles(blobs.tail)
      }
    }
  }







}
