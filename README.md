# SGIT

## Libraries used and versions

* java version 2.13.1
* scala version 2.13.1
* better-file version 3.8.0
* scopt version 4.0.0-RC2


## Instructions

* **sbt test** : launches the tests
* **sbt run** : launches the application 
* **sbt assembly** : Create a jar to launch the application as a standalone


## How to use the the standalone JAR file ?


By executing the sbt assembly command, a jar is created at `/target/scala-2.13/sgit.jar`. To execute it, you can use command `java -jar sgit.jar` in the directory.

In order to use the commands with the keyword `sgit`, and not `java -jar sgit.jar`, just create an alias. This is possible thanks to the following command that needed to be executed in the directory where the jar is located :

* On Linux/Mac bash : `alias sgit='java -jar sgit.jar'`
* On Windows Powershell : `Set-Alias -Name sgit -Value java -jar sgit.jar`


## Available commands

* **sgit init** : Allows to initialize the repository.sgit 
* **sgit add** : Add file contents to the index
* **sgit commit** : Record changes to the repository
* **sgit status** : Show the working tree status (not finished)
* **sgit diff** : Show changes between commits, commit and working tree, etc (not finished)