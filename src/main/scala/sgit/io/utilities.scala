package sgit.io

import java.nio.file.{Files, Path, Paths}

import better.files._
import sgit.io.indexConversion.getIndex
import sgit.objects.Blob

object utilities {

  /**
   * @return the working directory File
   */
  def getWD: File = ".sgit".toFile.parent

  /**
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

  /**
   *
   * @param folderName
   * @return
   */

  def isFilePresent(folderName: String): Boolean = Files.exists(Paths.get(folderName))

  def getKeySha1FromString(s : String): String ={
    val md = java.security.MessageDigest.getInstance("SHA-1")
    md.digest(s.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }


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

  /**
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
