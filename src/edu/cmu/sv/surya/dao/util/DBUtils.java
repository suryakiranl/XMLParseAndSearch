package edu.cmu.sv.surya.dao.util;

import org.h2.tools.DeleteDbFiles;

import java.io.File;

/**
 * Created by suryakiran on 3/10/14.
 */
public class DBUtils {
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String DB_FILE = "soc_xml_parsing";
    public static final String DB_PATH = USER_DIR + "/" + DB_FILE;
    public static final String DB_FILE_EXTENSION = ".h2.db";

    public static void deleteDBFiles() {
        DeleteDbFiles.execute(USER_DIR, DB_FILE, true);
    }

    public static boolean doesDBExist() {
        return new File(DB_PATH + DB_FILE_EXTENSION).exists();
    }

    public static String getDBFilePath() {
        System.out.println("Database located in directory = " + DBUtils.DB_PATH);
        return DB_PATH;
    }
}
