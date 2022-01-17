import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Class which creates a logger and stores requests in an XML format.
 */
public class HTTPLogger {

    private static Logger lgr = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Constructor for class. Creates logger.
     * Adds a filehandler which handles logging requests and stores log as an XML file.
     * @throws IOException if there are issues creating and using filehandler.
     */
    public HTTPLogger() throws IOException {

        //gets rid of any existing handlers (default console handler)
        LogManager.getLogManager().reset();
        lgr.setLevel(Level.INFO);

        try {
            //default output in XML format
            FileHandler fh = new FileHandler("HTTPLog.log");
            fh.setLevel(Level.INFO);
            lgr.addHandler(fh);
        } catch (IOException ioe) {
            lgr.log(Level.SEVERE, "Logging issue", ioe);
        }

    }

    /**
     * Method which allows server to log messages.
     * Synchronised for thread safety in case of multiple threads trying to write to file at once.
     * @param requestAndHead head of HTTP response to be added to log.
     */
    public synchronized void logRequest(String requestAndHead) {
        //head will contain all necessary request response information.
        lgr.log(Level.INFO, requestAndHead);
    }
}

