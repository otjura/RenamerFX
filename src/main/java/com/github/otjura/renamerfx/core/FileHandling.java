/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package com.github.otjura.renamerfx.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Common static methods for handling files for both GUI and command-line.
 */
public final class FileHandling {

	private static final String EMPTY_STRING = "";

	/**
	 * Traverses a given file tree, returning array of Path objects upon success. Traversing recursively is an option.
	 *
	 * @param dir     Path, assumes it's an existing valid directory.
	 * @param recurse TRUE to recurse the directory tree down to root, FALSE to do only dir
	 * @return List of Path objects that are files (this method excludes folders)
	 * @throws IOException in case something goes wrong reading files
	 */
	public static List<Path> collectFiles(Path dir, boolean recurse) throws IOException {
		Predicate<Path> isFile = (Files::isRegularFile);
		int depth = recurse ? Integer.MAX_VALUE : 1; // depth 1 means current directory only
		List<Path> files;
		try (Stream<Path> paths = Files.walk(dir, depth)) {
			files = paths.filter(isFile).collect(Collectors.toList());
		}
		return files;
	}

	/**
	 * Renames Paths objects provided in an input array. Renaming is done in place, replacing the original file. This is
	 * default behaviour in common usual tools such as mv and ren.
	 *
	 * @param paths       list of Paths
	 * @param replaceWhat String to replace in filenames. Assumes no empty String.
	 * @param replaceTo   String acting as a replacement. Can be empty for deletion.
	 * @param simulate    when true doesn't rename anything, but returns new names (dry run)
	 * @return List of renames in {oldName,newName} format, or {oldName,ERROR} in case of error.
	 */
	public static List<StringTuple> renamePaths(List<Path> paths, String replaceWhat, String replaceTo, boolean simulate) {
		List<StringTuple> renamed = new LinkedList<>();
		for (Path path : paths) {
			String filename = path.getFileName().toString();
			String newname = filename.replace(replaceWhat, replaceTo);
			// skip renaming and collecting if newName would be the same
			if (!filename.equals(newname)) {
				if (!simulate) {
					try {
						Files.move(path, path.resolveSibling(newname));
					} catch (IOException e) {
						newname = "ERROR: CAN'T RENAME FILE";
					}
				}
				renamed.add(new StringTuple(filename, newname));
			}
		}
		return renamed;
	}

	/**
	 * Read in paths in a directory, then return their file name in StringTuple.
	 *
	 * @param paths List of File objects.
	 * @return List of tuples where each is {fileName, EMPTY_STRING}
	 */
	public static List<StringTuple> pathsAsStringTuples(List<Path> paths) {
		List<StringTuple> stringTuples = new LinkedList<>();
		paths.forEach(file -> stringTuples.add(new StringTuple(file.getFileName().toString(), EMPTY_STRING)));
		return stringTuples;
	}

	/**
	 * Checks that target directory is operable. Won't follow symbolic links.
	 *
	 * @param dir String of full path of a possible directory.
	 * @return boolean, TRUE if dir is directory and can be operated.
	 */
	public static boolean isValidFolder(String dir) {
		try {
			return Files.isDirectory(Paths.get(dir));
		} catch (InvalidPathException | SecurityException e) {
			// get() fails, not valid path | isDirectory() fails, no access rights
			return false;
		}
	}
}
