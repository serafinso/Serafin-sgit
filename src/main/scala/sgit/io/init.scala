package sgit.io

object init {

  /** NOT PF method
   * The init method
   */
  def init() : Unit = {
    println("Initialization of the .sgit folder")
    if (!utilities.isFilePresent(".sgit")) {
      utilities.createFile(isDirectory = true, ".sgit")
      utilities.createFile(isDirectory = false,  ".sgit/HEAD")
      utilities.createFile(isDirectory = true,  ".sgit/objects")
      utilities.createFile(isDirectory = true, ".sgit/objects/blob")
      utilities.createFile(isDirectory = true, ".sgit/objects/tree")
      utilities.createFile(isDirectory = true, ".sgit/objects/commit")
      utilities.createFile(isDirectory = false, ".sgit/index")
      utilities.createFile(isDirectory = true, ".sgit/refs")
      utilities.createFile(isDirectory = true, ".sgit/refs/heads")
      utilities.createFile(isDirectory = true, ".sgit/refs/tags")
      println("End of the initialization")
    } else {
      println("The repository .sgit already exist")
    }
  }
}
