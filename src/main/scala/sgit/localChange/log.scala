package sgit.localChange

import sgit.io.{blobConversion, commitConversion}
import sgit.objects.{Blob, Commit}

object log {

  def printCommit(commit : Commit) : Unit = {
    println(
      Console.YELLOW + "commit "+ commit.key +" ("
        + Console.CYAN + " HEAD -> "
        + Console.GREEN +"master"
        + Console.YELLOW+")"
        + Console.BLACK + "\n\n    " + commit.messageC + "\n"
    )
  }

  def simpleLog() : Unit = {
    val optLastCommit : Option[Commit] = commitConversion.getLastCommit
    if (optLastCommit.isDefined){
      val allCommit = commitConversion.getAllCommitFromLast(optLastCommit.get)
      allCommit.reverse.foreach(c=> printCommit(c))
    }else {
      println("fatal: your current branch 'master' does not have any commits yet")
    }
  }

  def logP(): Unit = {
    val optLastCommit : Option[Commit] = commitConversion.getLastCommit
    if (optLastCommit.isDefined){
      val allCommit = commitConversion.getAllCommitFromLast(optLastCommit.get)
      allCommit.reverse.foreach(c=> {
        printCommit(c)
        println("diff files")
      })
    }else {
      println("fatal: your current branch 'master' does not have any commits yet")
    }
  }
}
