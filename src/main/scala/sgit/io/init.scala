package sgit.io

object init {

  /** NOT PF method
   * The init method
   */
  def init() : Unit = {
    println("Initialization of the .sgit folder")
    if (!utilities.isFilePresent(".sgit")) {
      createObject.createFile(isDirectory = true, ".sgit")
      createObject.createFile(isDirectory = false,  ".sgit/HEAD")
      createObject.createFile(isDirectory = true,  ".sgit/objects")
      createObject.createFile(isDirectory = true, ".sgit/objects/blob")
      createObject.createFile(isDirectory = true, ".sgit/objects/tree")
      createObject.createFile(isDirectory = true, ".sgit/objects/commit")
      createObject.createFile(isDirectory = false, ".sgit/index")
      createObject.createFile(isDirectory = true, ".sgit/refs")
      createObject.createFile(isDirectory = true, ".sgit/refs/heads")
      createObject.createFile(isDirectory = true, ".sgit/refs/tags")
      println("End of the initialization")
    } else {
      println("The repository .sgit already exist")
    }
  }
}
