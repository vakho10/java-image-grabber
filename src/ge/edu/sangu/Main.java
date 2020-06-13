package ge.edu.sangu;

import ge.edu.sangu.web.Grabber;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        launch(args);
    }

    @Deprecated
    private static void validateArgs(String[] args) {

        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No arguments passed to application");
        }

        if (args.length == 1) {
            throw new IllegalArgumentException("Output folder should be passed");
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        Scene scene = new Scene(root, 500, 400);

        primaryStage.setTitle("Image Grabber");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
