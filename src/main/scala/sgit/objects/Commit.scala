package sgit.objects

import sgit.io.utilities.getKeySha1FromString

class Commit(tree: String, parent : Option[String], message : String){
  val parentC : Option[String] = parent
  val treeC : String = tree
  val messageC : String = message
  val content : String  = {
    if(parent.isDefined){
      "tree : " + tree + "\nparent : " + parent.get + "\nmessage : " + messageC
    }else{
      "tree : " + tree + "\nparent : None\nmessage : " + messageC
    }
  }

  val key : String = {
    getKeySha1FromString(content)
  }

  val isParentNull : Boolean = { parent.isDefined }

  override def toString: String = {
    "key : "+ key + "\n" + content
  }
}

