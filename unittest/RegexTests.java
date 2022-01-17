import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegexTests {

    private RegexReader rr;

    @Before
    public void setup() {
        this.rr = new RegexReader();
    }

    @Test
    public void testGetRequestType() {
        String input1 = "GET /jonl/index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Safari …\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: en-gb,en;q=0.5\n" +
                "Accept-Encoding: gzip,deflate\n" +
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\n" +
                "Keep-Alive: 300\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0";
        String input2 = "1232144";

        String output1 = rr.getRequestType(input1);
        String output2 = rr.getRequestType(input2);

        assertEquals("GET", output1);
        assertEquals("", output2);
    }

    @Test
    public void testGetRequestedFileName() {
        String input1 = "GET /jonl/index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Safari …\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: en-gb,en;q=0.5\n" +
                "Accept-Encoding: gzip,deflate\n" +
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\n" +
                "Keep-Alive: 300\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0";
        String input2 = "1232144";

        String output1 = rr.getRequestedFileName(input1);
        String output2 = rr.getRequestedFileName(input2);

        assertEquals("/jonl/index.html", output1);
        assertEquals("", output2);
    }

    @Test
    public void testGetRequestedContentType() {
        String input1 = "GET /jonl/index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Safari …\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: en-gb,en;q=0.5\n" +
                "Accept-Encoding: gzip,deflate\n" +
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\n" +
                "Keep-Alive: 300\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0";
        String input2 = "1232144";

        String output1 = rr.getRequestedContentType(input1);
        String output2 = rr.getRequestedContentType(input2);

        assertEquals("text/html", output1);
        assertEquals("", output2);
    }

    @Test
    public void testGetRequestedHTTPProtocol() {
        String input1 = "GET /jonl/index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Safari …\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Language: en-gb,en;q=0.5\n" +
                "Accept-Encoding: gzip,deflate\n" +
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\n" +
                "Keep-Alive: 300\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0";
        String input2 = "1232144";

        String output1 = rr.getRequestedHTTPProtocol(input1);
        String output2 = rr.getRequestedHTTPProtocol(input2);

        assertEquals("HTTP/1.1", output1);
        assertEquals("", output2);
    }
}

