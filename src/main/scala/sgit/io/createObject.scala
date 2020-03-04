package sgit.io

import better.files._
import java.nio.file.{Files, Paths}
import sgit.io.utilities._

import sgit.objects.Blob

object createObject {

  /**
   *
   * @param isDirectory
   * @param name
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



}
