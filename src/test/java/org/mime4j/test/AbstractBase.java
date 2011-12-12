package org.mime4j.test;

import java.io.*;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * @author Lukas Vlcek (lukas.vlcek@gmail.com)
 */
public abstract class AbstractBase {

    protected String slurp(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    protected InputStream getInputStream(String path) throws FileNotFoundException {
        InputStream is = null;
        is = getSystemClassLoader().getResourceAsStream(path);
//        if (is == null) {
//            is = ClassLoader.getSystemResourceAsStream(path);
//        }
        if (is == null) {
            is = new FileInputStream(new File(path));
        }
        return is;
    }

    protected String readerToString(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            sb.append((char) c);
        }
        reader.close();
        return sb.toString();
    }
}
