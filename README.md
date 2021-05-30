# RenamerFX

Tool for batch renaming files. Recurses directory tree and does a text replace on filenames.

## Build & Run

Requirements:

* Java 11
* Maven

This is a Maven project not tied to any IDE. Executing `mvn clean javafx:run` in project root should suffice to run it.
avaFX comes in Maven config.

`mvn package` creates a self-contained executable uberjar in ./target.

## Use

Clicking uberjar or running it without arguments from command-line with `java -jar` opens the tool in graphical mode.
Giving it arguments runs it in command-line mode, for example `java -jar RenamerFX-1.2.2-uber.jar --help`

```
USAGE
renamerfx <folder> <stringToReplace> <replacementString>

--help for this help
--interactive for interactive mode
```

## License

This software is licensed under GPLv3. For the full license text, see file LICENSE.

## Contributing

Use the provided IntelliJ_Allman_Style on all code work on this project and make sure your code is readable and if it's
not readable comment it well. You have an idea on how to improve this software? Implement it and send a pull request.

That said, the project goal is to be the best cross-platform graphical renamer application, capable of doing a wide
range of actions, obvious to end-user.
