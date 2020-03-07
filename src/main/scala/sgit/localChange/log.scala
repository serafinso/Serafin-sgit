package sgit.localChange

import sgit.io.{blobConversion, commitConversion}
import sgit.objectManipulation.blobManipulation
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

  def getCommitInTuple (commits : List[Commit]) : List[(Commit,Commit)] = {
    if(commits.isEmpty || commits.tail.isEmpty) return List.empty
    else (commits.head, commits.tail.head)::getCommitInTuple(commits.tail)
  }

  def printAdd (b: Blob) : Unit = {
    val content : Option[String] = blobConversion.getBlobContent(b)
    println(
      "diff --git a/"+ b.path +"b/" +b.path +"\n"+
        "new file \n" +
        "index 0000000.."+ b.key.substring(0,7) + "\n" +
        "--- /dev/null\n"+
        "+++ b/"+ b.path + "\n"+
      Console.CYAN + "@@ -0,0 +1,"+content.get.split("\n").length +" @@"+ Console.BLACK

    )
    if(content.isDefined)
      content.get.split("\n").foreach(line =>
        println(Console.GREEN + "-" + line + Console.BLACK)
      )
  }

  def logP(): Unit = {
    val optLastCommit : Option[Commit] = commitConversion.getLastCommit
    if (optLastCommit.isDefined){
      val allCommit = commitConversion.getAllCommitFromLast(optLastCommit.get).reverse
      val commitTuple : List[(Commit, Commit)] = getCommitInTuple(allCommit)
      //ATTENTION PAS DE HEAD
      val firstCommit = allCommit.head
      commitTuple.reverse.foreach(commitTuple => {
        printCommit(commitTuple._2)
        val oldBlobs : Option[List[Blob]] = blobConversion.getBlobFromCommit(commitTuple._1)
        val newBlobs : Option[List[Blob]] = blobConversion.getBlobFromCommit(commitTuple._2)
        if(oldBlobs.isDefined && newBlobs.isDefined){
          diff.diffListBlob(oldBlobs.get, newBlobs.get)
          val addedBlob : List[Blob] = blobManipulation.diffList(newBlobs.get, oldBlobs.get)
          addedBlob.foreach(a => printAdd(a))
        }
        println()
      })

      printCommit(firstCommit)
      val firstCommitBlobs : Option[List[Blob]] = blobConversion.getBlobFromCommit(firstCommit)
      firstCommitBlobs.get.foreach(a => printAdd(a))
    }else {
      println("fatal: your current branch 'master' does not have any commits yet")
    }
  }
}
