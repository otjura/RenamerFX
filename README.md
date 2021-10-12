# RenamerFX

Program for batch renaming files. Recurses directory tree and does a simple text replace on filenames.

## Build & Run

Requirements:

* Java 17
* Maven

This is a Maven project not tied to any IDE. Executing `mvn clean javafx:run` in project root should suffice to run it.
JavaFX comes in Maven config.

`mvn package` creates a self-contained executable uberjar in ./target.

## Use

Clicking uberjar or running it without arguments from command-line with `java -jar` opens the program in graphical mode.
Giving it arguments runs it in command-line mode, for example `java -jar RenamerFX-1.4.0-uber.jar --help` prints the
following help message:

```
USAGE
renamerfx <folder> <stringToReplace> <replacementString>

--help for this help
--interactive for interactive mode
```

## Contributing

The project goal is to be the best cross-platform graphical file renamer application, capable of doing wide range of
actions, while being obvious and beautiful to the end-user. You have an idea on how to improve this software? Implement
it and send a pull request! Please follow the code style guidelines, which you can see from sources.

## License

This software is licensed under GPLv3. For the full license text, see file LICENSE.