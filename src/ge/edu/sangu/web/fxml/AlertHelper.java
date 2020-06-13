package ge.edu.sangu.web.fxml;

import javafx.scene.control.Alert;

public class AlertHelper {

    public static void showError(String message, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message + ", Reason: " + e.getMessage());
        alert.showAndWait();
    }
}
