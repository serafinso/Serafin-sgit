package sgit

import scopt.OParser
import sgit.io.init
import sgit.localChange.{add, commit, diff, status}
import scopts.{Config, Parser}
import sgit.commitHistory.log
import sgit.objectManipulation.blobManipulation
import sgit.objects.Blob


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
      case "init" => init.init()
      case "status" => status.sgitStatus()
      case "diff" => diff.sgitDiff()
      case "commit" => {
        commit.sgitCommit(message)
      }
      case "add" => add.addFiles(files)
      case "log" => logOpt(option)
      case _=> println("Error, write a good command 2")
    }
  }

  /** Determine the log option
   *
   * @param option: the log option
   */
  def logOpt(option: String): Unit ={
    option match {
      case ""=> log.simpleLog()
      case "p" => commitHistory.log.logP()
      case "stat" => println("Not done")
    }
  }
}
