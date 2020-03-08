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

By executing the sbt assembly command in the serafin-sgit directory, a jar is created at `/target/scala-2.13/sgit.jar`. To execute it, you can use the command `java -jar sgit.jar` in the directory.

In order to use the sgit commands with the keyword `sgit`, and not `java -jar sgit.jar`, use the following command :

* On Linux/Mac bash : `alias sgit='java -jar pathToJar/sgit.jar'` or use `source ./sgit.bash` in the serafin-sgit directory 
* On Windows Powershell : `Set-Alias -Name sgit -Value java -jar pathToJar/sgit.jar`

Don't forget to replace `pathToJar` by the real path to the sgit.jar.

Moreover, adding it to the .bashrc file allows the alias to remain functional. 
To do this, just enter `nano .bachrc` and write at the end of the file `alias sgit='java -jar path/to/jar/sgit-assembly-xx.jar'`.
Reload the changes with `source .bashrc`.


## Available commands

- `sgit init` : Allows to initialize the repository.sgit 
- `sgit add` : Add file contents to the index
    - `.` : Add all the untracked and modified files
    - `[files]`:  Add the files if possible
- `sgit commit` : Record changes to the repository
- `sgit status` : Show the working tree status
- `sgit diff` : Show changes between commits, commit and working tree, etc (not finished)
- `sgit log` : Shows the commit logs.
    - ` ` : With no argument the command log print all the different commit starting with the oldest 
    - `-p` : This command show all the different commits and the change among files between them

