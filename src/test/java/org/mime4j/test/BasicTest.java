package org.mime4j.test;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.TextBody;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Lukas Vlcek (lukas.vlcek@gmail.com)
 */
public class BasicTest extends AbstractBase {

    @Test
    public void fromEncodingGBK() throws IOException, MimeException {

        Message message = ParserUtil.getMessage(getInputStream("mbox/esb-users-01.mbox"));
        assertEquals("钱宇虹", message.getFrom().get(0).getName());

    }

    @Test
    public void fromEncodingUTF8() throws IOException, MimeException {

        Message message = ParserUtil.getMessage(getInputStream("mbox/jbpm-users-01.mbox"));
        assertEquals("jiacc@gillion.com.cn", message.getFrom().get(0).getName());

    }

    /**
     * Hard to say if there is any systematic resolution to this issue.
     * Why is Mailman able to output 'ñ' while mime4j outputs 'Ã±' ?
     *
     * Reference: http://lists.jboss.org/pipermail/jboss-cluster-dev/2008-April/000000.html
     *
     * @throws IOException
     * @throws MimeException
     */
    @Test
    public void bodyEncoding() throws IOException, MimeException {

        Message message = ParserUtil.getMessage(getInputStream("mbox/jboss-cluster-dev-01.mbox"));

        Body body = message.getBody();
        String mimeType = message.getMimeType().toLowerCase();
        String contentTransferEncoding = message.getContentTransferEncoding();
        String charset = message.getCharset();

        assertEquals("ISO-8859-1", charset);
        assertEquals("text/plain", mimeType);
        assertEquals("8bit", contentTransferEncoding);

        assertTrue( body instanceof TextBody );

        String bodyContent = readerToString(((TextBody)body).getReader());

//        System.out.println(bodyContent);

        assertTrue( bodyContent.indexOf("Zamarreño") > -1 );

    }
}
