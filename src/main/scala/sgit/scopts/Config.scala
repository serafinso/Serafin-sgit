package sgit.scopts

/**
 *
 * @param command sgit command
 * @param option the option of sgit
 * @param files the several files to be added on the command
 * @param message message enter with the commit command
 */
case class Config(
                   command: String = "",
                   option : String = "",
                   files: Seq[String] = Seq(),
                   message : String = ""
                 )
