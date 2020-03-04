package sgit

import scopt.OParser
import sgit.io.init
import sgit.localChange.{add, commit, status}
import sgit.objects.{Blob, BlobAndContent, Commit, Tree}
import sgit.io.blobConversion

/*import sgit.create._
import sgit.localChange.{add, commit}
import sgit.Tree
import sgit.io.readFile
import sgit.io.createObject
import sgit.io.writeFile*/


import better.files._
import sgit.io.utilities
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
      case "commit" => {
        /*val b1 : Blob = new Blob("B1", "d1/d2/d3/f5")
        val b2 : Blob = new Blob("B2", "b2")
        val t1: Tree = new Tree("t1", List(b1, b2), List.empty)
        val t2: Tree = new Tree("t2", List(b1), List(t1))
        val t3: Tree = new Tree("t3", List(b1,b2), List(t1, t2))
        println(t1.toString)
        println(t2.toString)
        println(t3.toString)
        val c1 : Commit = new Commit(t3, None, "Bonjour")
        println("KEY : ")
        println(c1.key)
        println(c1.content)
        println("WRITE TREE")
        println(commit.writeTree(List(b1,b2), List.empty).content)
        val indexBlob: List[Blob] = indexConversion.indexToBlobList
        println(commit.writeTree(indexBlob, List.empty))*/
        println(utilities.getHEAD)

      }
      case "add" => add.addFiles(files)
      case _=> println("Error, write a good command 2")
    }
  }
}
