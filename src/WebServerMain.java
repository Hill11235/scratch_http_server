import java.io.IOException;

/**
 * Class for the initialisation of web server and handling user inputs.
 */
public class WebServerMain {

    private HTTPServerHub serverHub;
    private String directoryPath;
    private int port;

    /**
     * Constructor for class, saves directory and port and passes these through to the server.
     * @param directoryPath path of directory which contains files that can be shared.
     * @param port port for ServerSocket to listen and be accessed on.
     * @throws IOException when error occurs reading data.
     */
    public WebServerMain(String directoryPath, int port) throws IOException {
        this.directoryPath = directoryPath;
        this.port = port;
        this.serverHub = new HTTPServerHub(directoryPath, port);
    }

    /**
     * Main method for class, takes command line arguments and initialises Server.
     * @param args command line arguments for directory and port.
     */
    public static void main(String[] args) {
        try {
            int inputPort = Integer.parseInt(args[1]);
            WebServerMain wsm = new WebServerMain(args[0], inputPort);
        } catch (Exception e) {
            //catches parsing errors and arg based errors.
            System.err.println("Usage: java WebServerMain <document_root> <port>");
        }
    }
}

