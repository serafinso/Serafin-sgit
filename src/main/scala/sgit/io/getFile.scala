package sgit.io

import java.nio.file.{Files, Path, Paths}

import better.files._

object getFile {

  /**
   * Get all the files located in the work directory without the files in .sgit
   * @return : Seq[File]
   */
  def getAllFileWorkDirectory: Seq[File] = {
    getWorkingDirectory.toFile
      .listRecursively
      .toSeq
      .filterNot(f => f.path.toString.contains(".sgit"))
      .filterNot(f => f.isDirectory)
  }

  /**
   * Get all the files containing in the path if path exist.
   * If the path lead to a file it will return a seq with one file otherwise it will return all the file containing in the directory.
   * @param path the path to retrive the files
   * @return
   */
  def getFileFromPath(path: String): Option[Seq[File]] = {
    if(Files.exists(Paths.get(path))){
      Some(path.toFile.listRecursively.toSeq)
    }else {
      None
    }
  }

  /**
   * Return the path of the working directory
   * @return
   */
  def getWorkingDirectory: String = {
    ".sgit/../WorkingDirectory"
  }

  /**
   *
   * @return
   */
  def getIndexPath: String = {
    ".sgit/index"
  }

  /**
   *
   * @param path where the file or directory should exist
   * @return true if the file or directory exist, false otherwise
   */
  def fileOrDirectoryExist(path: String): Boolean = {
    Files.exists(Paths.get(path))
  }

  /**
   *
   * @param pathDirectory the path of the directory to get
   * @return All the file containing in the directory at the path pathDirectory
   */
  def getAllFilesInDirectory(pathDirectory: String): Seq[File] ={
    if(fileOrDirectoryExist(pathDirectory) && pathDirectory.toFile.isDirectory){
      val seqFile = pathDirectory.toFile
        .listRecursively
        .toSeq
      seqFile.filterNot(f=>f.isDirectory)
    }else{
      println("Not a file")
      Seq()
    }
  }

  /**
   *
   * @return the current directory
   */
  def getPWD: Path = {
    Paths.get(".").toAbsolutePath
  }






}
