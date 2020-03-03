package sgit.io

import java.nio.file.{Files, Path, Paths}
import better.files._

object getFile {

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

  def getIndex : Option[File] = {
    if (isFilePresent(".sgit/index")) {Some(".sgit/index".toFile)}
    else None
  }

}
