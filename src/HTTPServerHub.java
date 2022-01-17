import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class which creates server socket and starts listening.
 * Creates a new ServerHandler thread for each connection.
 */
public class HTTPServerHub {

    private ServerSocket ss;
    private int port;
    private String directoryPath;
    private HTTPLogger lgr;

    /**
     * Constructor for the class. Creates ServerSocket on provided port.
     * Creates ServerHandler for each client and adds this to a thread.
     * @param directoryPath path of directory which contains files that can be shared.
     * @param port port for ServerSocket to listen and be accessed on.
     */
    public HTTPServerHub(String directoryPath, int port) {
        this.directoryPath = directoryPath;
        this.port = port;

        try {
            ss = new ServerSocket(port);
            this.lgr = new HTTPLogger();
            System.out.println("HTTP server started and listening on port: " + this.port);
            while (true) {
                Socket client = ss.accept();
                System.out.println("New connection from " + client.getInetAddress());
                ServerHandler sh = new ServerHandler(client, this);
                Thread t = new Thread(sh);
                t.start();
            }
        } catch (IOException ioe) {
            System.err.println("Server error: " + ioe.getMessage());
            cleanup();
        }
    }

    /**
     * Getter method for directory path.
     * @return directory path as a String.
     */
    public String getDirectoryPath() {
        return this.directoryPath;
    }

    /**
     * Getter method for associated log.
     * @return associated HTTPLogger class.
     */
    public HTTPLogger getLgr() {
        return this.lgr;
    }

    /**
     * Closes ServerSocket in the case of an IOException in the constructor.
     */
    private void cleanup() {
        try {
            ss.close();
        } catch (IOException ioe) {
            System.err.println("clean up method: " + ioe.getMessage());
        }
    }
}

