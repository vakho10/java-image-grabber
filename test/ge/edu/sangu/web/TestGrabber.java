package ge.edu.sangu.web;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestGrabber {

    static Path TMP_OUTPUT_FOLDER;

    @BeforeAll
    static void init() throws IOException {
        // Create temporary output folder for images
        TMP_OUTPUT_FOLDER = Files.createTempDirectory("Grabber Test");
    }

    @AfterAll
    static void finish() throws IOException {
        Files.walk(TMP_OUTPUT_FOLDER, 1).forEach(file -> {
            try {
                if (Files.isRegularFile(file)) {
                    Files.delete(file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Files.deleteIfExists(TMP_OUTPUT_FOLDER);
    }

    @Test
    @DisplayName("Test parsing image name from URL")
    void testParseFileNameFromURL() throws MalformedURLException {
        final String testURL = "https://sangu.edu.ge/photos/shares/assets/5e55178c44d13.png";
        DummyGrabber dummyGrabber = new DummyGrabber(TMP_OUTPUT_FOLDER, testURL);

        Assertions.assertEquals("5e55178c44d13.png", dummyGrabber.parseFileNameFromURL(testURL));
    }

    @Test
    @DisplayName("Test saving image to output folder method")
    void testSaveImageToOutputFolder() throws IOException {
        final String testURL = "https://sangu.edu.ge/photos/shares/assets/5e55178c44d13.png";
        DummyGrabber dummyGrabber = new DummyGrabber(TMP_OUTPUT_FOLDER, testURL);

        // Test that we have no exception
        Assertions.assertDoesNotThrow(() -> {
            dummyGrabber.saveImageToOutputFolder(testURL);
        });

        // Test that image exists
        String imageName = dummyGrabber.parseFileNameFromURL(testURL);
        Path tmpDownloadedImage = TMP_OUTPUT_FOLDER.resolve(imageName);
        Assertions.assertTrue(Files.exists(tmpDownloadedImage));
    }

    private static class DummyGrabber extends Grabber {

        public DummyGrabber(Path outputFolder, String... urlStrings) throws MalformedURLException {
            super(outputFolder, urlStrings);
        }

        public void saveImageToOutputFolder(String url) throws IOException {
            saveImageToOutputFolder(new URL(url));
        }

        public String parseFileNameFromURL(String url) throws MalformedURLException {
            return super.parseFileNameFromURL(new URL(url));
        }
    }
}
