package sgit.scopts

case class Config(
                   command: String = "",
                   option : String = "",
                   files: Seq[String] = Seq()
                 )
