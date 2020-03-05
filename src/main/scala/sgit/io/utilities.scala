package sgit.io

import java.nio.file.{Files, Paths}
import better.files._

object utilities {

  /** NOT PF method
   *
   * @return the working directory File
   */
  def getWD: File = ".sgit".toFile.parent

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
        println("This is your first commit")
        headFile.overwrite("refs/heads/master")
        Some("First commit")
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
   * Rewrite the head entirely text
   * @param text text to add to the head file
   */
  def headUpdate(text : String) : Unit = {
    if(isFilePresent(".sgit/HEAD")){
      ".sgit/HEAD".toFile.overwrite(text)
    }else {
      println("git init first")
    }

  }

}
