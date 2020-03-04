package sgit.objects

import better.files._
import java.security.{ MessageDigest, SecureRandom }
import sgit.io.utilities.getKeySha1FromString

class Commit(tree: Tree, parent : Option[Commit], message : String){

  val content : String  = {
    "tree : " + tree.key + "\nparent : " + parent + "\nmessage : " + message
  }

  val key : String = {
    getKeySha1FromString(content)
  }

  val isParentNull : Boolean = { parent.isDefined }
}

