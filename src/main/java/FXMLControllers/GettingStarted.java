package FXMLControllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;

public class GettingStarted {

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton fullNamerButton;

    @FXML
    private JFXButton compactNamerButton;

    @FXML
    private JFXButton projectPreferencesButton;

    @FXML
    private JFXButton loggerButton;

    @FXML
    private JFXButton databasesButton;

    @FXML
    private JFXTextArea instructionsTextArea;

    @FXML
    public void closeGettingStarted(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    private void fullNamerInstructions(ActionEvent e){
        instructionsTextArea.setText("In the ‘EXPERIMENT NAME’ text field, begin to type. Click on the ‘add new entry to the database’ button. Fill in the fields, then click submit. The experiment is now in the database, and will autofill once you begin typing in the box.\n" +
                "In the ‘RESEARCHER NAME’ text field, type in your first and last name. Click on the ‘add new entry to database’ button. Fill in the fields, then click submit. Your name has now been added to the database and will autofill once you begin typing in the box.\n" +
                "In the ‘TRIAL NUMBER’ and ‘SAMPLE NUMBER’ fields, type in your desired trial number and sample numbers. You can use the ‘+’ and ‘-’ buttons to increase and decrease the numbers. \n" +
                "Click the ‘ADD’ button. Begin typing in the ‘NAME’ field, and click on the ‘add new entry to the database’ button. Fill in the fields and click ‘SUBMIT’. The keyword is now in the database. Click ‘ADD’ again, and begin typing in the ‘NAME’ field. The keyword will autofill. Select the keyword, fill in the ‘DATA VALUE’ field if necessary, and click ‘SUBMIT’. Repeat this process until you have the desired number of keywords.\n" +
                "Click the ‘COPY’ button to copy the file name to your clipboard. Repeat this process for each trial and sample.\n" +
                "\n" +
                "If you do not wish to include certain parameters in your file name, simply unselect the checkbox next to undesired parameter.");
    }

    @FXML
    private void compactNamerInstructions(ActionEvent e){
        instructionsTextArea.setText("To navigate to Compact Namer, click on the toggle button at the top of Full Namer. Your experiment, researcher name, and keywords are saved, and you can modify the date, the trial number, and the sample number from the Compact Namer screen. Click the ‘COPY’ button to copy the file name to your clipboard.\n" +
                "To modify the experiment, researcher name, or keywords, toggle back to Full Namer. \n");
    }

    @FXML
    private void projectPreferencesInstructions(ActionEvent e){
        instructionsTextArea.setText("Under construction");
    }

    @FXML
    private void loggerInstructions(ActionEvent e){
        instructionsTextArea.setText("Under construction");
    }

    @FXML
    private void databasesInstructions(ActionEvent e){
        instructionsTextArea.setText("Under construction");
    }

}
