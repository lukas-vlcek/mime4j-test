package org.mime4j.test;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.MessageServiceFactory;
import org.apache.james.mime4j.field.LenientFieldParser;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.MimeConfig;

/**
 * @author Lukas Vlcek (lukas.vlcek@gmail.com)
 */
public class ParserUtil {

    private static MessageBuilder messageBuilder;

    private ParserUtil() {}

    public static MessageBuilder getMessageBuilder() throws MimeException {
        MessageBuilder mb = messageBuilder;
        if (mb == null) {
            synchronized(ParserUtil.class) {
                mb = messageBuilder;
                if (mb == null) {

                    MimeConfig config = new MimeConfig();
                    config.setMaxLineLen(10000);
        //            config.setStrictParsing(false);

                    mb = MessageServiceFactory.newInstance().newMessageBuilder();
                    ((DefaultMessageBuilder)mb).setMimeEntityConfig(config);
                    ((DefaultMessageBuilder)mb).setFieldParser(LenientFieldParser.getParser());

                    messageBuilder = mb;
                }
            }
        }
        return mb;
    }
}
