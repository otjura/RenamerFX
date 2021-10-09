/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Common static methods for both GUI and command-line.
 */
public final class Logic
{
	/**
	 * Converts input string to canonical path.
	 *
	 * @param path
	 * 	string representation
	 *
	 * @return canonical path on success, empty string on exception
	 */
	public static String toCanonicalPath(String path)
	{
		try
		{
			return new File(path).getCanonicalPath();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Recurses a given file tree, returning array of File objects upon success.
	 *
	 * @param startDirectory
	 * 	Path, assumes it's an existing valid directory
	 *
	 * @return File[] of indefinite length >= 1
	 *
	 * @throws IOException
	 * 	in case something goes wrong reading files
	 */
	public static List<File> collectFilesRecursively(Path startDirectory) throws IOException
	{
		Stream<Path> fileStream = Files.walk(startDirectory);
		List<File> files = new ArrayList<>();

		// if try-catching below stream operation, the following nag happens:
		// "Local variable files defined in an enclosing scope must be final or effectively final"
		// ^^throwing simplifies code a lot here
		fileStream.forEach(o ->
		{
			File file = o.toFile();
			if (file.isFile())
			{
				files.add(file);
			}
		});
		fileStream.close();

		return files;
	}

	/**
	 * Renames File objects provided in an input array. Renaming is done in place, replacing the original file.
	 * This is default behaviour in common usual tools such as mv and ren.
	 *
	 * @param files
	 * 	list of File objects
	 * @param replaceWhat
	 * 	String to replace in filenames. Assumes no empty String.
	 * @param replaceTo
	 * 	String acting as a replacement. Can be empty for deletion.
	 * @param simulate
	 * 	when true doesn't rename anything, but returns new names (dry run)
	 *
	 * @return List containing string representations of succeeded renames
	 */
	public static List<StringTuple> renameFiles(List<File> files, String replaceWhat, String replaceTo,
		boolean simulate)
	{
		List<StringTuple> renamedFiles = new ArrayList<>();

		for (File file : files)
		{
			if (file.canRead())
			{
				String filename = file.getName();
				String newname = filename.replace(replaceWhat, replaceTo);
				String fullpath = file.getParent() + File.separator;
				String fullnewname = fullpath + newname;
				if (!filename.equals(newname))
				{
					if (!simulate)
					{
						try
						{
							// renames files returning success/failure
							if (!file.renameTo(new File(
								fullnewname)))
							{
								renamedFiles.add(new StringTuple(
									"ERROR " + filename + " couldn't be renamed!",
									""));
							}
						}
						catch (SecurityException e)
						{
							e.printStackTrace();
						}
					}
					renamedFiles.add(new StringTuple(filename, newname));
				}
			}
		}
		return renamedFiles;
	}

	/**
	 * Lineated representation of ArrayList contents.
	 *
	 * @param arr
	 * 	any string array
	 *
	 * @return lineated string representation, newline on empty input
	 */
	public static String pprint(List<String> arr)
	{
		if (arr.isEmpty())
		{
			return "\n";
		}
		StringBuilder sb = new StringBuilder(arr.size());
		arr.forEach(s ->
		{
			if (s == null)
			{
				s = "";
			}
			sb.append(s);
			sb.append("\n");
		});
		return sb.toString();
	}

	/**
	 * Helper method converting file array to string array
	 *
	 * @param files
	 * 	list of valid file objects
	 *
	 * @return names of files
	 */
	public static List<String> fileArrayToStringList(List<File> files)
	{
		List<String> arr = new ArrayList<>(files.size());

		for (File f : files)
		{
			arr.add(f.getName());
		}
		return arr;
	}

	/**
	 * Lists file names of files in a directory dir
	 *
	 * @param dir
	 * 	path, relative or absolute
	 *
	 * @return pretty print of files in directory, empty if no files
	 */
	public static String fileListing(String dir)
	{
		String listing = "";

		if (isValidFolder(dir))
		{
			Path folder = Paths.get(dir);
			try
			{
				List<File> files = collectFilesRecursively(folder);
				List<String> arr = fileArrayToStringList(files);
				listing = pprint(arr);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return listing;
	}

	/**
	 * Recursive renamer. Takes string input on all args. Operates silently.
	 *
	 * @param dir
	 * 	path relative or absolute
	 * @param replaceWhat
	 * 	string to be replaced
	 * @param replaceTo
	 * 	string to replace replaceWhat to
	 * @param simulate
	 * 	true to skip renaming
	 *
	 * @return succeeded renames as a list of string tuples where [oldname, newname]
	 */
	public static List<StringTuple> renameRecursively(String dir, String replaceWhat, String replaceTo,
		boolean simulate)
	{
		List<StringTuple> renamed = new ArrayList<>();

		if (!replaceWhat.isEmpty())
		{
			try
			{
				Path dirPath = Paths.get(dir);
				List<File> files = collectFilesRecursively(dirPath);
				renamed = renameFiles(files, replaceWhat, replaceTo, simulate);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return renamed;
	}

	/**
	 * Checks that target file object is operable directory.
	 *
	 * @param dir
	 * 	a File object
	 *
	 * @return boolean, TRUE on operability FALSE otherwise
	 */
	public static boolean isValidFolder(File dir)
	{
		try
		{
			return dir.exists() && dir.isDirectory();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks that target directory is operable.
	 *
	 * @param dir
	 * 	String
	 *
	 * @return boolean, TRUE on operability
	 */
	public static boolean isValidFolder(String dir)
	{
		return isValidFolder(new File(dir));
	}
}
