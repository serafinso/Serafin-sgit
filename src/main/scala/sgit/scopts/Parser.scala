package sgit.scopts

import scopt.{OParser, OParserBuilder}

object Parser {
  val builder: OParserBuilder[Config] = OParser.builder[Config]
  val parser1: OParser[Unit, Config] = {
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
        .children(
          opt[Unit]("message")
            .abbr("m")
            .action((_, c) => c.copy(option = "p"))
            .text("Shows the changes over time."),
          arg[String]("message")
            .action((x, c) => c.copy(message = x))
            .text("Commit message.")
        ),
      cmd("log")
        .action((_,c)=> c.copy(command = "log"))
        .text("Show commit logs.")
        .children(
          opt[Unit]("change between time")
            .abbr("p")
            .action((_, c) => c.copy(option = "p"))
            .text("Shows the changes over time."),
          opt[Unit]("stat")
            .action((_, c) => c.copy(option = "stat"))
            .text("Show diff between commit.")
        )
    )
  }
}
