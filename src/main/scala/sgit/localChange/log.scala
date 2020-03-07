package sgit.localChange

import sgit.io.commitConversion
import sgit.objects.Commit

object log {

  def simpleLog() : Unit = {
    val optLastCommit : Option[Commit] = commitConversion.getLastCommit
    if (optLastCommit.isDefined){
      val allCommit = commitConversion.getAllCommitFromLast(optLastCommit.get)
      allCommit.reverse.foreach(c => println(
        Console.YELLOW + "commit "+ c.key +" ("
          + Console.CYAN + " HEAD -> "
          + Console.GREEN +"master"
          + Console.YELLOW+")"
          + Console.BLACK + "\n\n    " + c.messageC + "\n"
      )
      )
    }else {
      println("fatal: your current branch 'master' does not have any commits yet")
    }
  }
}
