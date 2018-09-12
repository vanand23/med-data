package FXMLControllers;

import Singletons.FXMLManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends ScreenController{

    @FXML
    private JFXButton simpleNamerButton;

    @FXML
    private JFXButton fullNamerButton;

    /**
     * creates a popup window with the admin user manager screen
     *
     * @param e Action event given
     * @throws IOException exception
     */

    @FXML
    private JFXButton preferencesButton;

    @FXML
    public JFXButton closeButton;

    @FXML
    public void handleCloseButton(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
   public void handleSimpleNamer(ActionEvent e) throws IOException{
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/simpleNamer.fxml", simpleNamerButton.getScene().getWindow(),"Simple Namer");
        System.out.println("Runs");
    }

    @FXML
    public void handleFullNamer(ActionEvent e) throws IOException {
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/fullNamer.fxml", fullNamerButton.getScene().getWindow(),"Full Namer");
    }

    @FXML
    public void handlePreferences(ActionEvent e) throws IOException {
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/myProjectPreferences.fxml", preferencesButton.getScene().getWindow(),
                        "Project Preferences");
    }

    @FXML
    private void goToAdminOptionsScreen(ActionEvent e) throws IOException {
        //switchScreen(, "FXML/AdminOptions.fxml");

    }

}
