package sgit.io

import org.scalatest._
import java.nio.file.{Files, Paths}
import better.files._

class initTest extends FunSpec{
  describe("If the user enter the sgit init command in the terminal") {
    it("A folder should be created in this directory.") {
      init.init()
      val sgitDirectory = ".sgit".toFile
      if(Files.exists(Paths.get(".sgit"))) {
        val _ : File = ".sgit"
          .toFile
          .delete()
      }
      init.init()
      assert(Files.exists(Paths.get(".sgit")))
      assert(Files.exists(Paths.get(".sgit/objects")))
      assert(Files.exists(Paths.get(".sgit/HEAD")))
      assert(Files.exists(Paths.get((".sgit/index"))))
      assert(Files.exists(Paths.get(".sgit/refs")))
      assert(Files.exists(Paths.get((".sgit/refs/heads"))))
      assert(Files.exists(Paths.get(".sgit/refs/tags")))
      sgitDirectory.delete()
    }
  }
}
