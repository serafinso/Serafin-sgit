package sgit.scopts

import scopt.OParser

object Parser {
  val builder = OParser.builder[Config]
  val parser1 = {
    import builder._
    OParser.sequence(
      programName("sgit"),
      head("sgit", "1.0"),
      cmd("init")
        .action((_, c) => c.copy( command = "init"))
        .text("Creating sgit folders"),
      cmd("add")
        .action((_, c) => c.copy(command = "add"))
        .text("Add file contents to the index.")
        .children(
          arg[String]("<file>... or regex")
            .unbounded()
            .action((x, c) => c.copy(files = c.files :+ x))
            .text("Several File(s) or a regex.")
        ),
      cmd("status")
        .action((_, c) => c.copy( command = "status"))
        .text("Displays the files status"),
      cmd("diff")
        .action((_, c) => c.copy( command = "diff"))
        .text("Displays the commits differences"),
      cmd("commit")
        .action((_, c) => c.copy(command = "commit"))
        .text("Commit the index")
    )
  }
}
