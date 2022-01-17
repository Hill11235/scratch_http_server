import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class which given a connection to the ServerSocket handles the connection and request.
 * Implements runnable so can be threaded.
 */
public class ServerHandler implements Runnable {

    private Socket clientConn;
    private HTTPServerHub hub;
    private RegexReader rr;
    private HTTPLogger lgr;
    private InputStreamReader isr;
    private BufferedReader br;

    /**
     * Constructor for class. Links logger, regex class and HTTPServerHub class.
     * @param client client socket making a request.
     * @param hub associated HTTPServerHub class where connection originally made.
     */
    public ServerHandler(Socket client, HTTPServerHub hub) {
        this.clientConn = client;
        this.hub = hub;
        this.rr = new RegexReader();
        this.lgr = hub.getLgr();
    }

    /**
     * Getter method for client Socket.
     * @return client socket connected to Server.
     */
    public Socket getClientConn() {
        return this.clientConn;
    }

    /**
     * Getter method for HTTPServerHub which houses ServerSocket.
     * @return HTTPServerHub where connection made.
     */
    public HTTPServerHub hubGetter() {
        return this.hub;
    }

    /**
     * Getter method for RegexReader class.
     * @return RegexReader class.
     */
    public RegexReader getRr() {
        return this.rr;
    }

    /**
     * Getter method for log associated with ServerSocket.
     * @return log associated with Server.
     */
    public HTTPLogger getLgr() {
        return this.lgr;
    }

    /**
     * Method which overrides run in Runnable interface.
     * Reads request from client and responds with appropriate header and body.
     * Consists of constituent methods from throughout class.
     * Closes client connection in event of exception.
     */
    @Override
    public void run() {
        Socket conn = this.getClientConn();

        try (PrintWriter pw = new PrintWriter(conn.getOutputStream())) {
            String request = this.requestReader();
            String header = this.generateResponseHeader(request);
            RegexReader reader = this.getRr();
            HTTPLogger lg = this.getLgr();

            String body = "";
            if (reader.getRequestType(request).equals("GET")) {
                body = this.generateResponseBody(request);
            }

            String response = header + body;
            lg.logRequest(request + header);

            pw.print(response);
            pw.flush();

        } catch (Exception e) {
            System.err.println("ServerHandler run method error: " + e.getMessage());
            this.cleaner();
        }
    }

    /**
     * Method which can be used to close client connection.
     * Used to close connection in run method in the event of an exception.
     */
    private void cleaner() {
        Socket clientConn = this.getClientConn();
        try {
            clientConn.close();
            br.close();
            isr.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    /**
     * Method which reads HTTP request from client and returns it as a String.
     * @return HTTP request as a String.
     */
    private String requestReader() {
        Socket client = this.getClientConn();

        //uses StringBuilder so unnecessary objects not created in loop concatenation.
        StringBuilder fullRequest = new StringBuilder();

        try {
            isr = new InputStreamReader(client.getInputStream());
            br = new BufferedReader(isr);
            String line = br.readLine();

            while (!line.isEmpty()) {
                fullRequest.append(line);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            System.err.println("Request reader error: " + ioe.getMessage());
            this.cleaner();
        }

        return fullRequest.toString();
    }

    /**
     * Method which given a requested file name, checks its existence and returns an appropriate response code.
     * @param fileName requested file to be checked if present.
     * @return String containing appropriate response code
     */
    private String getResponseCode(String fileName) {
        String filePath = this.getRequestedFilePath(fileName);
        Path p = Paths.get(filePath);

        if (Files.exists(p)) {
            return " 200 OK";
        }

        return " 404 Not Found";
    }

    /**
     * Given requested file, constructs full file path using directory path associated with server.
     * @param fileName requested file.
     * @return full file path as String.
     */
    private String getRequestedFilePath(String fileName) {
        HTTPServerHub linkedHub = this.hubGetter();
        String dirPath = linkedHub.getDirectoryPath();
        String filePath = dirPath + "/" + fileName;

        return filePath;
    }

    /**
     * Return size of requested file in bytes.
     * @param fileName requested file.
     * @return long which is the size of the requested file in bytes.
     */
    private long getFileSize(String fileName) {
        String filePath = this.getRequestedFilePath(fileName);
        long fileSize = 0;

        try {
            Path p = Paths.get(filePath);
            fileSize = Files.size(p);
            return fileSize;

        } catch (IOException | InvalidPathException ioe) {
            System.err.println(ioe.getMessage());
        }

        return 0;
    }

    /**
     * Given client HTTP request generates the response header as a String.
     * Also adds carriage returns and line feeds.
     * @param request client HTTP request as a String.
     * @return HTTP appropriate header as a String.
     */
    private String generateResponseHeader(String request) {

        //uses regex class and its methods to determine nature of request
        RegexReader reader = this.getRr();
        String requestedFile = reader.getRequestedFileName(request);
        String requestType = reader.getRequestType(request);
        String requestProtocol = reader.getRequestedHTTPProtocol(request);
        String requestedContent = reader.getRequestedContentType(request);

        //checks and deals with unrecognised request types
        if ((!requestType.equals("HEAD")) && (!requestType.equals("GET"))) {
            return "HTTP/1.1 501 Not Implemented\r\n\r\n";
        }

        //generates correct response code and content-length if file available
        String responseCode = this.getResponseCode(requestedFile);
        long bodyLength = this.getFileSize(requestedFile);

        //adjust content-type for requests that specify */* format
        String body = generateResponseBody(request);

        if (body.contains("<html>")) {
            requestedContent = "text/html";
        }

        //full header constructed
        String header = requestProtocol + responseCode + "\n";
        header += "Server: HTTPServer" + "\n";
        header += "Content-Type: " + requestedContent + "\n";
        header += "Content-Length: " + bodyLength  + "\n";
        header += "\r\n\r\n";

        return header;
    }

    /**
     * Given client HTTP request, creates appropriate response content.
     * @param request client HTTP request as a String.
     * @return requested content as a String.
     */
    private String generateResponseBody(String request) {

        //uses StringBuilder so unnecessary objects not created in loop concatenation.
        StringBuilder body = new StringBuilder();
        RegexReader reader = this.getRr();

        String requestedFile = reader.getRequestedFileName(request);
        String filePath = this.getRequestedFilePath(requestedFile);

        File toBeRead = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(toBeRead));) {
            String line = br.readLine();
            body.append("<html>\n");
            while ((line = br.readLine()) != null) {
                body.append(line);
                if (!line.equals("</html>")) {
                    body.append("\n");
                }
            }

            return body.toString();

        } catch (IOException ioe) {
            System.err.println("generateBodyResponse: " + ioe.getMessage());
        }

        return body.toString();
    }
}

