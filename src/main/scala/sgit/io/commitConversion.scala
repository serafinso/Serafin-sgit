package sgit.io

import better.files._
import sgit.io.utilities.isFilePresent
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

  def getCommit(s: String) : Option[String] = {
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
        val commit : Option[String] = getCommit(line(1).split(" ")(2))
        val message = line(2).split(" ")(2)
        Some(new Commit(tree, commit, message))
      }
    }else {
      println("Commit doesn't exist")
      None
    }
  }

  def getRefByName(name : String) : Option[Ref] = {
    if (isFilePresent(".sgit/refs/heads/" + name)){
      val headFile : File = (".sgit/refs/heads/" + name).toFile
      val line : String = headFile.contentAsString
      if (line.equals("")) { //PREMIER COMMIT
        println("ERREUR with last commit")
        None
      } else {
        Some(Ref(line, name))
      }
    }else {
      println("ERREUR with last commit")
      None
    }
  }




}
