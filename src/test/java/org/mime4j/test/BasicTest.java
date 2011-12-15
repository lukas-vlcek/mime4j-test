package org.mime4j.test;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.messaging.saaj.packaging.mime.internet.MimeUtility;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.message.*;
import org.mime4j.test.util.ReaderInputStream;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Lukas Vlcek (lukas.vlcek@gmail.com)
 */
public class BasicTest extends AbstractBase {

    @Test
    public void fromEncodingGBK() throws IOException {

        Message message = ParserUtil.getMessage(getInputStream("mbox/esb-users-01.mbox"));
        assertEquals("钱宇虹", message.getFrom().get(0).getName());
        assertEquals("钱宇虹 <yhqian99@163.com>", message.getFrom().get(0).getName() + " <" + message.getFrom().get(0).getAddress() + ">");
        assertEquals("=?GBK?B?x67T7rrn?= <yhqian99@163.com>", message.getHeader().getField("from").getBody());

    }

    @Test
    public void fromEncodingUTF8() throws IOException {

        Message message = ParserUtil.getMessage(getInputStream("mbox/jbpm-users-01.mbox"));
        assertEquals("jiacc@gillion.com.cn", message.getFrom().get(0).getName());
        assertEquals("jiacc@gillion.com.cn <jiacc@gillion.com.cn>", message.getFrom().get(0).getName() + " <" + message.getFrom().get(0).getAddress() + ">");
        assertEquals("\"=?utf-8?B?amlhY2NAZ2lsbGlvbi5jb20uY24=?=\" <jiacc@gillion.com.cn>",message.getHeader().getField("from").getBody());

    }

    /**
     * Hard to say if there is any systematic resolution to this issue.
     * Why is Mailman able to output 'ñ' while mime4j outputs 'Ã±' ?
     *
     * Reference: http://lists.jboss.org/pipermail/jboss-cluster-dev/2008-April/000000.html
     *
     * @throws IOException
     */
    @Test
    public void bodyEncoding() throws IOException {

        Message message = ParserUtil.getMessage(getInputStream("mbox/jboss-cluster-dev-01.mbox"));

        Body body = message.getBody();
        String mimeType = message.getMimeType().toLowerCase();
        String contentTransferEncoding = message.getContentTransferEncoding();
        String charset = message.getCharset();

        assertEquals("ISO-8859-1", charset);
        assertEquals("text/plain", mimeType);
        assertEquals("8bit", contentTransferEncoding);

        assertTrue( body instanceof TextBody);

        String bodyContent = readerToString(((TextBody)body).getReader());

//        System.out.println(bodyContent);

        assertFalse(bodyContent.indexOf("Zamarreño") > -1);

    }

    @Test
    public void contentExtraction() throws IOException, MessagingException {
        Message message = ParserUtil.getMessage(getInputStream("mbox/esb-users-01.mbox"));

        assertTrue(message.getBody() instanceof Multipart);
        
        Multipart multipart = (Multipart)message.getBody();
        assertEquals( 2, multipart.getCount() );

        boolean passed = false;
        List<BodyPart> entities =  multipart.getBodyParts();
        for (Entity e : entities) {
            assertTrue(e instanceof BodyPart);

            String mimeType = e.getMimeType();
            if (mimeType.equalsIgnoreCase("text/plain")) {
                passed = true;

                String contentTransferEncoding = e.getContentTransferEncoding();
                String charset = e.getCharset();
                
                assertTrue( e.getBody() instanceof TextBody );
                TextBody body = (TextBody) e.getBody();

                String content = null;
                InputStream output = null;

                ReaderInputStream ris = new ReaderInputStream(body.getReader(),charset);
                output = MimeUtility.decode(ris, contentTransferEncoding.toLowerCase());
                StringWriter writer = new StringWriter();
                IOUtils.copy(output, writer, charset);
                content = writer.toString();

//                String expected = " Jeff,\r\n\r\n\r\n\r\n Since";
                String expected = " Jeff,\n\n\n\n Since you";
                String actual = content.substring(0,20);
                
                assertEquals(actual, expected);
            }
        }
        assertTrue(passed);
    }
}
