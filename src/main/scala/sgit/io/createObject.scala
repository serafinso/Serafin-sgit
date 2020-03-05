package sgit.io

import better.files._

object createObject {

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



}
