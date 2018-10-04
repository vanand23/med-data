package FXMLControllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

public class About {

    @FXML
    private JFXButton closeButton;

    @FXML
    public void closeAbout(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

}
