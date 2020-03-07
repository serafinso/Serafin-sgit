package sgit.io

import better.files._
import sgit.io.utilities.isFilePresent
import sgit.localChange.commit.lastOnPath
import sgit.objects.{Commit, Ref}

object commitConversion {

  @scala.annotation.tailrec
  def createCommitFile(commits : List[Commit]) : Unit = {
    val commitPath: String = ".sgit/objects/commit/"
    if(commits.nonEmpty) {
      val commit : Commit = commits.head
      if(!commitPath.contains(commit.key)){
        val newFileInObject = createObject.createFile(isDirectory = false, commitPath + commit.key)
        if (newFileInObject) {
          (commitPath + commit.key).toFile.appendText(commit.content)
        }
        createCommitFile(commits.tail)
      }
    }
  }

  def getCommitIfExist(s: String) : Option[String] = {
    if(s.equals("None")) None
    else Some(s)
  }

  def getCommitByKey(key : String) : Option[Commit] = {
    if (isFilePresent(".sgit/objects/commit/" + key)){
      val commitFile : File = (".sgit/objects/commit/" + key).toFile
      val line :List[String] = commitFile.contentAsString
        .replace("\r", "")
        .split("\n").toList
      if (line.length != 3) {
        println("Invalid Commit")
        None
      } else {
        val tree: String = line(0).split(" ")(2)
        val commit : Option[String] = getCommitIfExist(line(1).split(" ")(2))
        val message = line(2).split(" ").drop(2).fold(""){(acc, s) => acc + s + " "}
        Some(new Commit(tree, commit, message.substring(0, message.length-1)))
      }
    }else {
      println("Commit doesn't exist")
      None
    }
  }

  /**
   *
   * @return
   */
  def getLastCommit : Option[Commit] = {
    val head: Option[String] = utilities.getHEAD
    if(head.isDefined){
      if (head.get.equals("Initial commit")){
        println("On branch master \n\nNo commits yet\n")
        None
      }else{

        val refName = lastOnPath(head.get)
        val refHead: Option[Ref] = refsConversion.getRefByName(refName)

        if(refHead.isDefined){
          commitConversion.getCommitByKey(refHead.get.commitKey)
        }else{
          None
        }
      }
    }else{
      None
    }
  }

  def getAllCommitFromLast(lastCommit : Commit) : List[Commit] = {
    if(lastCommit.parentC.isDefined) {
      val newCommit:Option[Commit] = getCommitByKey(lastCommit.parentC.get)
      if(newCommit.isDefined) lastCommit::getAllCommitFromLast(newCommit.get)
      else {
        println("Invalid commit")
        List.empty
      }
    }
    else{
      List(lastCommit)
    }
  }

}
