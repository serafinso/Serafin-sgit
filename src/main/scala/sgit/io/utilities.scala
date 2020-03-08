package sgit.io

import java.nio.file.{Files, Paths}
import better.files._

object utilities {
  /** NOT PF method
   *
   * @param isDirectory true if the file to be created is a directory
   * @param name name of the file to be created
   * @return true if the file as been created, false otherwise
   */
  def createFile(isDirectory: Boolean, name : String) : Boolean = {
    if(utilities.isFilePresent(name)) false
    else {
      val _ : File = name
        .toFile
        .createIfNotExists(isDirectory, createParents = true)
      true
    }
  }

  /** NOT PF method
   *
   * @return the working directory File
   */
  def getWD: File = ".sgit".toFile.parent

  /** NOT PF method
   *
   * @return the working directory File
   */
  def getWDPath: String = ".sgit".toFile.parent.toString()

  /** NOT PF method
   *
   * Get all the files located in the work directory without the files in .sgit
   * @return : Seq[File]
   */
  def getAllWDFiles: Option[List[File]] = {
    val wd : File = getWD
    if(!wd.isDirectory && !".sgit".toFile.exists) None
    else Some(wd::wd.listRecursively
      .filterNot(file => file.pathAsString.contains(".sgit"))
      .filterNot(file => file.pathAsString.contains(".idea"))
      .filterNot(file => file.pathAsString.contains("project"))
      .filterNot(file => file.pathAsString.contains("src"))
      .filterNot(file => file.pathAsString.contains("target"))
      .filterNot(file => file.pathAsString.contains(".gitignore"))
      .filterNot(file => file.pathAsString.contains("build.sbt"))
      .filterNot(file => file.pathAsString.contains("README.md"))
      .filterNot(file => file.pathAsString.contains("serafin-git.iml"))
      .filterNot(file => file.pathAsString.contains(".git"))
      .filterNot(file => file.pathAsString.contains(".DS_Store"))
      .filterNot(file => file.pathAsString.contains("sgit.bash"))
      .toList)
  }

  /** NOT PF method
   *
   * @param folderName the folder name to search
   * @return true if the folderName is present
   */
  def isFilePresent(folderName: String): Boolean = Files.exists(Paths.get(folderName))

  /** PF method
   *
   * @param s the string to get the sha1 key
   * @return the sha1 key of s
   */
  def getKeySha1FromString(s : String): String ={
    val md = java.security.MessageDigest.getInstance("SHA-1")
    md.digest(s.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }

  /** NOT PF method
   *
   * @return the string contained in the HEAD file if it exist, None otherwise
   */
  def getHEAD : Option[String] = {
    if (isFilePresent(".sgit/HEAD")){
      val headFile : File = ".sgit/HEAD".toFile
      val line : String = headFile.contentAsString
      if (line.equals("")) { //PREMIER COMMIT
        Some("Initial commit")
      } else {
        Some(line)
      }
    }else {
      println("git init first")
      None
    }
  }

  /** NOT PF method
   *
   * @return the string contained in the HEAD file if it exist, None otherwise
   */
  def updtateHEAD(newHead: String) : Unit = {
    if (isFilePresent(".sgit/HEAD")){
      ".sgit/HEAD".toFile.overwrite(newHead)
    }else {
      println("Git init first")
    }
  }
}
