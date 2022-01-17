import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which contains all the individual regex methods for the server.
 * Each method used to determine a certain aspect of a request.
 */
public class RegexReader {

    /**
     * Determines request type eg. GET or HEAD from start of request.
     * @param request from client.
     * @return request type from start of request as String.
     */
    public String getRequestType(String request) {
        String returnType = "";

        try {
            Pattern p = Pattern.compile("^[A-Z]+");
            Matcher m = p.matcher(request);
            m.lookingAt();
            returnType = m.group(0);
            return returnType;
        } catch (IllegalStateException ise) {
            System.err.println("getRequestType error: " + ise.getMessage());
        }

        return returnType;
    }

    /**
     * Determines the requested file name and format from client.
     * @param request from client.
     * @return requested file name as String.
     */
    public String getRequestedFileName(String request) {
        String fileName = "";

        try {
            Pattern p = Pattern.compile("[/A-za-z\\.1-9]+ HTTP");
            Matcher m = p.matcher(request);
            m.find();
            fileName = m.group(0);
            int endChar = fileName.indexOf(" HTTP");
            fileName = fileName.substring(0, endChar);

            return fileName;
        } catch (IllegalStateException ise) {
            System.err.println("getRequestFileName error: " + ise.getMessage());
        }

        return fileName;
    }

    /**
     * Determines requested content-type given request String.
     * @param request from client.
     * @return requested content type as String.
     */
    public String getRequestedContentType(String request) {
        String contentType = "";

        try {
            Pattern p = Pattern.compile("(Accept: )[a-zA-z*]+/[a-zA-z*]+");
            Matcher m = p.matcher(request);
            m.find();
            contentType = m.group(0);
            int spaceChar = contentType.indexOf(" ");
            contentType = contentType.substring(spaceChar + 1);
            return contentType;

        } catch (IllegalStateException ise) {
            System.err.println("getRequestedContentType error: " + ise.getMessage());
        }

        return contentType;
    }

    /**
     * Determines requested HTTP protocol given request String.
     * @param request from client.
     * @return request HTTP protocol.
     */
    public String getRequestedHTTPProtocol(String request) {
        String protocol = "";

        try {
            Pattern p = Pattern.compile("(HTTP)/[0-9]{1}[\\.]{1}[0-9]{1}");
            Matcher m = p.matcher(request);
            m.find();
            protocol = m.group(0);
            return protocol;

        } catch (IllegalStateException ise) {
            System.err.println("getRequestedHTTPProtocol error: " + ise.getMessage());
        }

        return protocol;
    }
}

