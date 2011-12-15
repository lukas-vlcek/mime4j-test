package org.mime4j.test;

import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.parser.MimeEntityConfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lukas Vlcek (lukas.vlcek@gmail.com)
 */
public class ParserUtil {

    static MimeEntityConfig config;

    static {
        // need to extend default config setting
        config = new MimeEntityConfig();
        config.setMaxLineLen(10000);
    }

    private ParserUtil() {}

    public static Message getMessage(InputStream is) throws IOException {
        Message message = new Message(is,config);
        is.close();
        return message;
    }
}
