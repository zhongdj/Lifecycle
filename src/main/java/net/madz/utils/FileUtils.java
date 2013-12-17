package net.madz.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FileUtils {

    private FileUtils() {}

    public static String readFileContent(URL file) {
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(file.openStream()));
            String line = null;
            StringBuilder contentBuilder = new StringBuilder();
            while ( null != ( line = br.readLine() ) ) {
                contentBuilder.append(line).append("\n");
            }
            content = contentBuilder.toString();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not open file " + file + ". Maybe it's not on the classpath?", e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not open file " + file + ". Maybe it's not on the classpath?", e);
        } finally {
            if ( null != br ) {
                try {
                    br.close();
                } catch (IOException e) {}
            }
        }
        return content;
    }
}
