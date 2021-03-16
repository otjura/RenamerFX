/*
 * Copyright Otso Rajala <ojrajala@gmail.com>, 2020
 *
 */

package renamerfx;

import java.util.ArrayList;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * Common static methods for both GUI and command-line
 */
final class Logic {

    /**
     * Converts input string to canonical path.
     *
     * @param path string representation
     * @return canonical path on success, empty string on exception
     */
    static String toCanonicalPath(String path) {
        String s = "";
        try {
            s = new File(path).getCanonicalPath();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
    * Recurses a given file tree, returning array of File objects upon success.
    *
    * @param startDirectory Path, assumes it's an existing valid directory
    * @return File[] of indefinite length >= 1
    * @throws IOException in case something goes wrong reading files
    */
    static File[] collectFilesRecursively(Path startDirectory) throws IOException {
        Stream<Path> fileStream = Files.walk(startDirectory);
        ArrayList<File> files = new ArrayList<>();
        // if try-catching below stream operation, the following nag happens:
        // "Local variable files defined in an enclosing scope must be final or effectively final"
        // ^^throwing simplifies code a lot here
        fileStream.forEach(o -> {
            File file = o.toFile();
            if (file.isFile()) {
                files.add(file);
            }
        });
        fileStream.close();

        // do this twoliner because ArrayList.toArray() returns Object so can't oneline File[] fileArray = files.toArray();
        File[] fileArray = new File[files.size()];
        fileArray = files.toArray(fileArray); // source.toArray(targetArray)

        return fileArray;
    }

    /**
    * Renames File objects provided in an input array.
    * Renaming is done in place, replacing the original file. This is default behaviour in common usual tools such as mv and ren.
    *
    * @param files array of File objects
    * @param replaceWhat String to replace in filenames. Assumes no empty String.
    * @param replaceTo String acting as an replacement. Can be empty for deletion.
    * @param simulate when true doesn't rename anything, but returns new names (dry run)
    * @return ArrayList containing string representations of succeeded renames
    */
    static ArrayList<String> renameFiles(File[] files, String replaceWhat, String replaceTo, boolean simulate) {
        ArrayList<String> renamedFiles = new ArrayList<>();
        for (File file : files) {
            if (file.canRead()) {
                String filename = file.getName();
                String newname = filename.replace(replaceWhat, replaceTo);
                String fullpath = file.getParent()+File.separator;
                String fullnewname = fullpath+newname;
                if (!filename.equals(newname)) {
                    if (!simulate) {
                        try {
                            boolean succ = file.renameTo(new File(fullnewname)); // renames files in-place
                            if (!succ) {
                                renamedFiles.add("ERROR "+filename+" couldn't be renamed!");
                            }
                        }
                        catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                    renamedFiles.add(filename+" --> "+newname);
                }
            }
        }
        return renamedFiles;
    }

    /**
     * Lineated string representation of ArrayList contents.
     *
     * @param arr any string array
     * @return lineated string representation, newline on empty input
     */
    static String pprint(ArrayList<String> arr) {
        if (arr.isEmpty()) return "\n";
        StringBuilder sb = new StringBuilder(arr.size());
        arr.forEach(s -> {
            if (s == null) s = "";
            sb.append(s);
            sb.append("\n");
        });
        return sb.toString();
    }

    /**
     * Helper method converting file array to string array
     *
     * @param files array of valid file objects
     * @return names of files
     */
    static ArrayList<String> fileArrayToStringArrayList(File[] files) {
        ArrayList<String> arr = new ArrayList<>(files.length);
        for (File f : files ) {
            arr.add(f.getName());
        }
        return arr;
    }

    /**
     * Lists file names of files in a directory dir
     *
     * @param dir path, relative or absolute
     * @return pretty print of files in directory, empty if no files
     */
    static String fileListing(String dir) {
        String listing = "";
        if(isValidFolder(dir)) {
            Path folder = Paths.get(dir);
            try {
                File[] files = collectFilesRecursively(folder);
                ArrayList<String> arr = fileArrayToStringArrayList(files);
                listing = pprint(arr);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listing;
    }

    /**
     * Recursive renamer.
     * Takes string input on all args. Operates silently.
     *
     * @param dir path relative or absolute
     * @param replaceWhat string to be replaced
     * @param replaceTo string to replace replaceWhat to
     * @param simulate true to skip renaming
     * @return succeeded renames as "old --> new"
     */
    static ArrayList<String> renameRecursively(String dir, String replaceWhat, String replaceTo, boolean simulate) {
        ArrayList<String> renamed = new ArrayList<>();
        if (!replaceWhat.isEmpty()) {
            try {
                Path dirPath = Paths.get(dir);
                File[] files = collectFilesRecursively(dirPath);
                renamed = renameFiles(files, replaceWhat, replaceTo, simulate);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return renamed;
    }

    /**
     * Checks that target directory is operable.
     *
     * @param dir a File object
     * @return boolean, true on operability
     */
    static boolean isValidFolder(File dir) {
        try {
            return dir.exists() && dir.isDirectory();
        }
        catch (SecurityException e) {
            return false;
        }
    }

    /**
     * Checks that target directory is operable.
     *
     * @param dir String
     * @return boolean, true on operability
     */
    static boolean isValidFolder(String dir) {
        return isValidFolder(new File(dir));
    }

}
