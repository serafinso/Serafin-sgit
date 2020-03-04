package sgit.objects

import better.files._
import java.security.{ MessageDigest, SecureRandom }
import sgit.io.utilities.getKeySha1FromString

class Commit(tree: String, parent : Option[String], message : String){

  val content : String  = {
    "tree : " + tree + "\nparent : " + parent + "\nmessage : " + message
  }

  val key : String = {
    getKeySha1FromString(content)
  }

  val isParentNull : Boolean = { parent.isDefined }
}

