package org.mime4j.test;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.field.FieldName;
import org.apache.james.mime4j.dom.field.MailboxListField;
import org.apache.james.mime4j.field.LenientFieldParser;
import org.apache.james.mime4j.field.MailboxListFieldImpl;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.MimeConfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lukas Vlcek (lukas.vlcek@gmail.com)
 */
public class ParserUtil {

    private static MessageBuilder messageBuilder;

    private ParserUtil() {}

    private static MessageBuilder getMessageBuilder() throws MimeException {
        MessageBuilder mb = messageBuilder;
        if (mb == null) {
            synchronized(ParserUtil.class) {
                mb = messageBuilder;
                if (mb == null) {

                    MimeConfig config = new MimeConfig();
                    config.setMaxLineLen(10000);
        //            config.setStrictParsing(false);

                    FieldParser<MailboxListField> mailboxListParser = MailboxListFieldImpl.PARSER;
                    LenientFieldParser fieldParser = new LenientFieldParser();
                    fieldParser.setFieldParser(FieldName.FROM, mailboxListParser);
                    fieldParser.setFieldParser(FieldName.RESENT_FROM, mailboxListParser);

                    DefaultMessageBuilder _mb = new DefaultMessageBuilder();
                    _mb.setMimeEntityConfig(config);
                    _mb.setFieldParser(fieldParser);

                    messageBuilder = _mb;
                    return _mb;
                }
            }
        }
        return mb;
    }

    public static Message getMessage(InputStream is) throws MimeException, IOException {
        Message message = getMessageBuilder().parseMessage(is);
        is.close();
        return message;
    }
}
