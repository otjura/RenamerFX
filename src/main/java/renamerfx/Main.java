/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 * 
 */

package renamerfx;

/**
 * Central launch point for the application.
 */
public class Main {
    
    public static void main(String[] args) {
        if (args.length != 0) {
            App.commandLine(args);
        }
        else {
            FXMLGuiStarter.launcher(args);
        }
    }
}
