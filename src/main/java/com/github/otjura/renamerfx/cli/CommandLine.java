/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.cli;

import com.github.otjura.renamerfx.core.StringTuple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.github.otjura.renamerfx.core.Logic.collectFilesRecursively;
import static com.github.otjura.renamerfx.core.Logic.renameFiles;

/**
 * Exclusively command-line portion of the application.
 */
public final class CommandLine
{
	private static final String HELP_OPTION = "--help";
	private static final String INTERACTIVE_OPTION = "--interactive";

	/**
	 * Yes/no method.
	 *
	 * @return boolean, TRUE on "y" or "yes", FALSE otherwise
	 */
	private static boolean yesNoPrompt()
	{
		Scanner sc = new Scanner(System.in);
		String ans = sc.nextLine().toLowerCase();
		sc.close();
		return ans.equals("y") || ans.equals("yes");
	}

	/**
	 * Interactive guided renaming on command-line.
	 */
	private static void interactiveRenaming()
	{
		System.out.println("Hello! Welcome to RenamerFX interactive renaming!");
		Scanner sc = new Scanner(System.in);
		String cwd = new File(".").getAbsolutePath();
		System.out.println("Current directory: " + cwd);
		String folderPath;

		// Ask for root directory from which to get files for renaming and verify its validity for next
		// operations.
		while (true)
		{
			System.out.print("Rename items in what directory?: ");
			folderPath = sc.nextLine();

			if (folderPath.isEmpty())
			{
				System.out.println("Can't operate on empty path.");
				continue;
			}

			File folder = new File(folderPath);

			if (folder.isFile())
			{
				System.out.println("Need a directory.");
				continue;
			}

			if (!folder.exists())
			{
				System.out.println("Folder doesn't exist. Check path.");
				continue;
			}

			int numberOfItems = Objects
				.requireNonNull(folder.listFiles()).length; // NOTE: lists both files and directories

			if (numberOfItems == 0)
			{
				System.out.println("Folder is empty.");
				continue;
			}

			break;
		}

		// Collect files recursively into array
		List<File> files = new ArrayList<>();
		try
		{
			files = collectFilesRecursively(Paths.get(folderPath));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Renaming sequence
		String replaceWhat;
		String replaceTo;
		while (true)
		{
			System.out.print("Replace what in filenames?: ");
			replaceWhat = sc.nextLine();
			if (replaceWhat.isEmpty())
			{
				System.out.println("Can't replace nothing.");
				continue;
			}

			System.out.print("Replace that with what?: ");
			replaceTo = sc.nextLine();
			if (replaceWhat.equals(replaceTo))
			{
				System.out.println("Same strings, won't do a thing.");
				continue;
			}
			break;
		}

		// Verify acquired files are correct
		fileLister(files);
		System.out.print("Rename in these files? (y/n) ");
		boolean yes = yesNoPrompt();

		// Commence renaming operation
		if (yes)
		{
			System.out.println("Renaming...");
			List<StringTuple> renamed = renameFiles(files, replaceWhat, replaceTo, false);
			for (StringTuple st : renamed)
			{
				System.out.println(st.toString());
			}
			System.out.println("Done!");
		}
		else
		{
			System.out.println("Quitting without renaming...");
		}
		sc.close();
	}

	/**
	 * Prints type and full path filename of File objects in a provided File[] parameter.
	 * Printout is one entry per row.
	 *
	 * @param files
	 * 	list of length > 0
	 */
	private static void fileLister(List<File> files)
	{
		for (File file : files)
		{
			String type = file.isFile() ? "FILE" : "DIRECTORY";
			try
			{
				System.out.println(type + ": " + file.getCanonicalPath());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Prints help message on console.
	 */
	private static void help()
	{
		System.out.printf("""
			USAGE
			renamerfx <folder> <stringToReplace> <replacementString>

			%s for this help
			%s for interactive mode

			""", HELP_OPTION, INTERACTIVE_OPTION
		);
	}

	/**
	 * Command-line version starting point. Provides both interactive and direct args ways of running the renamer.
	 *
	 * @param args
	 * 	command line arguments described in help()
	 */
	public static void commandLine(String[] args)
	{
		// Interactive renaming and help
		if (args.length == 1)
		{
			if (args[0].equals(HELP_OPTION))
			{
				help();
			}
			else if (args[0].equals(INTERACTIVE_OPTION))
			{
				interactiveRenaming();
			}
			else
			{
				help();
			}
		}
		// Rename as purely args based program without interactivity, where
		// args[directory, replaceWhat, replaceTo]
		else if (args.length == 3)
		{
			try
			{
				File dir = new File(args[0]);
				if (dir.exists() && dir.isDirectory())
				{
					List<File> files = collectFilesRecursively(Paths.get(args[0]));
					List<StringTuple> renamedFiles = renameFiles(files, args[1], args[2], false);
					for (StringTuple st : renamedFiles)
					{
						System.out.println(st.toString());
					}
				}
				else
				{
					System.out.println("Error: directory not valid.\n");
				}
			}
			catch (IOException e)
			{
				System.out.println("Something went wrong...");
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Supply all arguments for non-interactive batch renaming:");
			help();
		}
	}
}
