package com.wideplay.warp.util.reflect;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 23/02/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ClassNameUtils {


    private ClassNameUtils() {
    }

    private static final String CLASS_EXT = ".class";

    public static final FilenameFilter CLASS_FILE_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return dir.isDirectory() || name.endsWith(".class");
        }
    };

    public static List<String> listRecursive(File dir, FilenameFilter filter) {
        List<String> allClasses = new LinkedList<String>();

        //run internal recursor
        return listRecursiveInteral(dir, dir, filter, allClasses);
    }

    private static List<String> listRecursiveInteral(File rootDir, File dir, FilenameFilter filter, List<String> allClasses) {
        File[] fileList = dir.listFiles(filter);

        //discover classnames from file list
        for (File classFile : fileList) {

            //skip directories
            if (classFile.isDirectory() || !classFile.getName().endsWith(CLASS_EXT))
                continue;

            //strip root dir from class's path
            String className = classFile.getAbsolutePath().substring(rootDir.getAbsolutePath().length());

            //strip .class and leading / (or \)
            className = className.substring(1, className.length() - CLASS_EXT.length());

            //convert backslashes or fwdslashes to dots
            className = className.replaceAll("\\\\|/", "\\.");
            allClasses.add(className);
        }

        //recurse into child directories (packages)
        for (File subdir : fileList)
            if (subdir.isDirectory())
                listRecursiveInteral(rootDir, subdir, filter, allClasses);

        return allClasses;
    }



    public static <T> List<T> addToList(T[] array, List<T> target) {
        for (T t : array)
            target.add(t);

        return target;
    }

    public static String normalize(String className) {
        return className.replaceAll("[\\/\\.@%\\-+=?^&#!*;,~\\(\\)]", "");
    }
}
