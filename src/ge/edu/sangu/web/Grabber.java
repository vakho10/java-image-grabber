package ge.edu.sangu.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class grabs HTML document from specified URLs, downloads
 * and saves images to corresponding output folder.
 *
 * @author John Brown
 */
public class Grabber {

    private static final Logger logger = LogManager.getLogger();

    private List<URL> urls = new ArrayList<>();
    private Path outputFolder = Paths.get("images");

    public Grabber(List<URL> urls) {
        this.urls = urls;
    }

    public Grabber(String... urlStrings) throws MalformedURLException {
        for (String urlString : urlStrings) {
            urls.add(new URL(urlString));
        }
    }

    public Grabber(Path outputFolder, List<URL> urls) {
        this(urls);
        this.outputFolder = outputFolder;
    }

    public Grabber(Path outputFolder, String... urlStrings) throws MalformedURLException {
        this(urlStrings);
        this.outputFolder = outputFolder;
    }

    public Grabber(String outputFolderPath, String... urlStrings) throws MalformedURLException {
        this(Paths.get(outputFolderPath), urlStrings);
    }

    /**
     * Starts grabbing process.
     *
     * @throws IOException if there was error while grabbing the image
     */
    public void run() throws IOException {

        logger.debug("Started run() method");

        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }

        for (URL url : urls) {
            // (1) Grab document from this url
            Document doc = null;
            try {
                doc = Jsoup.connect(url.toString()).get();
            } catch (IOException e) {
                logger.error("Couldn't grab HTML document from URL", e);
                continue;
            }

            // (2) Search <img> tags with src attribute present
            Elements imgTags = doc.select("img[src]");
            for (Element imgTag : imgTags) {
                String imgUrlString = imgTag.absUrl("src");
                URL imgUrl = null;
                try {
                    imgUrl = new URL(imgUrlString);
                    logger.debug("Grabbing image from URL: {}", imgUrl);
                } catch (MalformedURLException e) {
                    logger.error("Couldn't grab image from URL", e);
                    continue;
                }

                // (3) Download this image and save it into output folder
                try {
                    saveImageToOutputFolder(imgUrl);
                } catch (IOException e) {
                    logger.error("Couldn't download image from URL", e);
                    continue;
                }
            }


        }
    }

    /**
     * Download image from URL and save to output folder.
     *
     * @param url image url
     * @throws IOException if download error occurred
     */
    protected void saveImageToOutputFolder(URL url) throws IOException {
        String imageName = parseFileNameFromURL(url);
        Path imagePath = outputFolder.resolve(imageName);

        try (InputStream is = url.openStream();
             OutputStream os = Files.newOutputStream(imagePath)) {
            byte[] buffer = new byte[2048];
            int length;
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
        }

        logger.info("Saved image: {}, into folder: {}", imageName, imagePath.toAbsolutePath());
    }

    protected String parseFileNameFromURL(URL url) {
        String fullPath = url.toString();
        return fullPath.substring(
                fullPath.lastIndexOf("/") + 1,
                fullPath.lastIndexOf("?") == -1 ? fullPath.length() : fullPath.lastIndexOf("?"));
    }
}
