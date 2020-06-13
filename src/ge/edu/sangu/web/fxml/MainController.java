package ge.edu.sangu.web.fxml;

import ge.edu.sangu.web.Grabber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainController {

    private static final Logger logger = LogManager.getLogger();

    private Path selectedOutputFolder;

    @FXML
    private TextField outputPathField;

    @FXML
    private TextArea urlsTextArea;

    @FXML
    public void initialize() {
        this.selectedOutputFolder = Paths.get(this.outputPathField.getText());
        logger.debug("Initialized MainController");
    }

    @FXML
    protected void handleChooseOutputFolderButtonAction(ActionEvent event) {
        System.out.println("handleChooseOutputFolderButtonAction");

        // Get stage
        final Node source = (Node) event.getSource();
        final Window mainStage = source.getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select output folder");
        Path selectedOutputFolder = directoryChooser.showDialog(mainStage).toPath();
        if (selectedOutputFolder != null && Files.isDirectory(selectedOutputFolder)) {
            this.selectedOutputFolder = selectedOutputFolder;
            this.outputPathField.setText(selectedOutputFolder.toAbsolutePath().toString());
        } else {
            // TODO This is unnecessary check :(
            Alert alert = new Alert(Alert.AlertType.ERROR, "The output directory should be a folder!");
            alert.showAndWait();
        }
    }

    @FXML
    protected void handleDownloadImagesButtonAction(ActionEvent event) {
        System.out.println("handleDownloadImagesButtonAction");

        String[] urlStrings = urlsTextArea.getText().split("\n");

        logger.info("Grabbing started");

        // (1) Create Grabber class
        Grabber grabber = null;
        try {
            grabber = new Grabber(this.selectedOutputFolder, urlStrings);

            // (2) Execute image grabbing action
            grabber.run();

        } catch (MalformedURLException e) {
            logger.error("Error while creating Grabber class", e);
            AlertHelper.showError("Error while creating Grabber class!", e);
        } catch (IOException e) {
            logger.error("Error while executing run() method of Grabber", e);
            AlertHelper.showError("Error while executing run() method of Grabber!", e);
        }

        logger.info("Grabbing finished");
    }
}
