package sgit

import scopt.OParser
import sgit.io.init
import sgit.localChange.{add, status}
import sgit.io.blobConversion
import sgit.objects.{Blob, BlobAndContent}

/*import sgit.create._
import sgit.localChange.{add, commit}
import sgit.Tree
import sgit.io.readFile
import sgit.io.createObject
import sgit.io.writeFile*/


import better.files._
import sgit.io.getFile
import sgit.io.indexConversion
import scopts.{Parser,Config}


object Main extends App {

  OParser.parse(Parser.parser1, args, Config()) match {
    case Some(config) => determineMode(config.command, config.option, config.files)
    case None => println("")
  }
  /*val blob1 : Blob = Blob("RRRR1", "workingdirectory/r1")
  val tree : Tree = Tree("ggg1","working/d1" )
  writeFile.writeBlobLineInTree(blob1, tree)
  writeFile.writeTreeLineInTree(tree, tree)
  println(readFile.searchBlobInTree(blob1, tree))
  println(readFile.readPathIndex())*/
  //createObject.createTreeFromFile("WorkingDirectory/d1".toFile)
  //println("WorkingDirectory/d1".toFile.sha1)

  /**
   * Dispatch according to the command written by the user
   * @param command : written command
   * @param option : command option
   * @param files : files argument
   */
  def determineMode(command: String, option: String, files: Seq[String]): Unit = {
    command match {
    case "init" => {
      init.init()
    }

      // init.init()
      case "status" => status.sgitStatus()
      case "diff" => println("diff")
      case "commit" => println("commit")
      case "add" => add.addFiles(files)
      case _=> println("Error, write a good command 2")
    }
  }
}
