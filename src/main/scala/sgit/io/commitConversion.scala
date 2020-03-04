package sgit.io

import better.files._
import sgit.objects.Commit

class commitConversion {

  def createTreeFile (commits : List[Commit]) : Unit = {
    val commitPath: String = ".sgit/objects/tree/"
    if(commits.nonEmpty) {
      val commit : Commit = commits.head
      if(!commitPath.contains(commit.key)){
        val newFileInObject = createObject.createFile(isDirectory = false, commitPath + commit.key)
        if (newFileInObject) {
          (commitPath + commit.key).toFile.appendText(commit.content)
        }
        createTreeFile(commits.tail)
      }
    }
  }
}
