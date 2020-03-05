package sgit

import scopt.OParser
import sgit.io.{blobConversion, init, refsConversion}
import sgit.localChange.{add, commit, diff, status}
import sgit.objects.{Blob, BlobAndContent, Commit, Ref, Tree}

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
    case Some(config) => determineMode(config.command, config.option, config.files, config.message)
    case None => println("")
  }

  /**
   * Dispatch according to the command written by the user
   * @param command : written command
   * @param option : command option
   * @param files : files argument
   */
  def determineMode(command: String, option: String, files: Seq[String], message: String): Unit = {
    command match {
      case "init" => { init.init() }
      case "status" => status.sgitStatus()
      case "diff" => diff.sgitDiff()
      case "commit" => { commit.sgitCommit(message) }
      case "add" => add.addFiles(files)
      case _=> println("Error, write a good command 2")
    }
  }
}
