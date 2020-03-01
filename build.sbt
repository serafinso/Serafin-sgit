name := "serafin-sgit"

version := "0.1"

scalaVersion := "2.13.1"

mainClass in (Compile, packageBin) := Some("sgit.main.Main")
scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  //SCOPT
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  "com.github.pathikrit" %% "better-files" % "3.8.0",
  //TEST
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
)

parallelExecution in Test := false

//assemblyJarName in assembly := "sgit.jar"