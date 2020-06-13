package ge.edu.sangu;

import ge.edu.sangu.web.Grabber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        try {
            validateArgs(args); // TODO Add extra validations if necessary
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }

        logger.info("Grabber application started");

        System.out.println("Hello, args were: " + Arrays.toString(args));

        String[] urlStrings = Arrays.copyOfRange(args, 0, args.length - 1);
        String outputFolderPath = args[args.length - 1];

        // (1) Create Grabber class
        Grabber grabber = null;
        try {
            grabber = new Grabber(outputFolderPath, urlStrings);

            // (2) Execute image grabbing action
            grabber.run();

        } catch (MalformedURLException e) {
            logger.error("Error while creating Grabber class", e);
        } catch (IOException e) {
            logger.error("Error while executing run() method of Grabber", e);
        }

        logger.info("Grabber application finished");
    }

    private static void validateArgs(String[] args) {

        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No arguments passed to application");
        }

        if (args.length == 1) {
            throw new IllegalArgumentException("Output folder should be passed");
        }
    }
}
