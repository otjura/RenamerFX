/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.core;

/**
 * Central launch point for the application.
 */
public class Main
{
	/**
	 * Main method for the application. If given arguments on launch, will launch the command-line version of the
	 * application.
	 *
	 * @param args
	 * 	List of arguments.
	 */
	public static void main(String[] args)
	{
		if (args.length != 0)
		{
			com.github.otjura.renamerfx.cli.CommandLine.commandLine(args);
		}
		else
		{
			com.github.otjura.renamerfx.gui.FXMLGuiStarter.launcher(args);
		}
	}
}
