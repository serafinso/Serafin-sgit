package sgit.create

import java.nio.file.{Files, Paths}

import sgit.io.createObject


object init {
  /*
  Create the .sgit folder
   */
  def init() : Unit = {
    if (!Files.exists(Paths.get(".sgit"))) {
      createObject.createFile(true, ".sgit")
      createObject.createFile(false, ".sgit/HEAD")
      createObject.createFile(true, ".sgit/objects")
      createObject.createFile(true, ".sgit/objects/blob")
      createObject.createFile(true, ".sgit/objects/tree")
      createObject.createFile(true, ".sgit/objects/commit")
      createObject.createFile(false, ".sgit/index")
      createObject.createFile(true, ".sgit/refs")
      createObject.createFile(true, ".sgit/refs/heads")
      createObject.createFile(true, ".sgit/refs/tags")
    } else {
      println("The repository .sgit already exist")
    }
  }
}
