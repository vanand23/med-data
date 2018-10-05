package FXMLControllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

public class GettingStarted implements Initializable {

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXTextArea instructionsTextArea;

    @FXML
    public void closeGettingStarted(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instructionsTextArea.setText("Select one of the buttons on the left to view help instructions.");
    }

    @FXML
    private void fullNamerInstructions(ActionEvent e){
        instructionsTextArea.setText("The date will automatically be set to today’s date. To change the date, click on the calendar icon to the right of the date display field. \n" +
                "In the /“EXPERIMENT NAME/” text field, begin to type. Click on the /‘add new entry to the database/’ button. Fill in the fields, then click submit. The experiment is now in the database, and will autofill once you begin typing in the box.\n" +
                "In the /“RESEARCHER NAME/” text field, type in your first and last name. Click on the “/add new entry to database”/ button. Fill in the fields, then click submit. Your name has now been added to the database and will autofill once you begin typing in the box.\n" +
                "In the /“TRIAL NUMBER/” and /“SAMPLE NUMBER/” fields, type in your desired trial number and sample numbers. You can use the /“+/” and /“-/” buttons to increase and decrease the numbers. \n" +
                "Click the /“ADD/” button. Begin typing in the /“NAME/” field, and click on the /“add new entry to the database/” button. Fill in the fields and click /“SUBMIT/”. The keyword is now in the database. Click /“ADD/” again, and begin typing in the /“NAME/” field. The keyword will autofill. Select the keyword, fill in the /“DATA VALUE/” field if necessary, and click /“SUBMIT/”. Repeat this process until you have the desired number of keywords.\n" +
                "Click the /“COPY/” button to copy the file name to your clipboard. Repeat this process for each trial and sample.\n" +
                "\n" +
                "If you do not wish to include certain parameters in your file name, simply unselect the checkbox next to undesired parameter.\n" +
                "For more information on how to add experiments, research names, or keywords to the databases, please consult the database section.");
    }

    @FXML
    private void compactNamerInstructions(ActionEvent e){
        instructionsTextArea.setText("To navigate to Compact Namer, click on the toggle button at the top of Full Namer. Your experiment, researcher name, and keywords are saved, and you can modify the date, the trial number, and the sample number from the Compact Namer screen. Click the “COPY” button to copy the file name to your clipboard.\n" +
                "To modify the experiment, researcher name, or keywords, toggle back to Full Namer. \n");
    }

    @FXML
    private void projectPreferencesInstructions(ActionEvent e){
        instructionsTextArea.setText("Project preferences can be accessed via the “FILE” menu in the top left corner of Full Namer. In project preferences, you can save your name, the project name, the project description, and a separation character (delimiter). If you would like the application to remember the parameters entered in Full Namer after the application has been closed, select the “Remember Full Namer Fields” checkbox.  Click “SAVE” to save your project preferences. If you do not want to save your project preferences, click “CANCEL”. ");
    }

    @FXML
    private void loggerInstructions(ActionEvent e){
        instructionsTextArea.setText("Under construction");
    }

    @FXML
    private void databasesInstructions(ActionEvent e){
        instructionsTextArea.setText("The databases can be accessed via the “FILE” menu in the top left corner of Full Namer. The application includes three databases: researcher names, experiment names, and keywords. Items can be added to the databases from corresponding database menu or directly from Full Namer. \n" +
                "To add an experiment name or a researcher name to the databases via Full Namer, begin typing in the desired field. If the name is not currently in the database, the “Add new entry to the database” button will appear. Click on this button. In this example, we will add a new entry to the experiment database. Enter the full name of the experiment, your desired abbreviation, and a description. Click the submit button. The experiment has now been added to the database. \n" +
                "When you return to the Full Namer screen, the experiment name will appear once you begin typing in the experiment field. \n To add new keywords to the database via Full Namer, click on the “ADD” button next to the keywords table. Begin typing in the “NAME” field. If your keyword is not in the database, the “Add a new entry to the database” button will appear. Click on this button. \n Fill in the fields with your desired information. A preview of your keyword will be displayed in the preview box.  Click the “SUBMIT” button to add the keyword to the database. In Full Namer, click on the “ADD” button. The same popup will appear, and you should be able to search for and enter your keyword.\nClick “SUBMIT”, and the keyword will be added to the keyword display table. \nTo add to the databases via the menu, click on the “FILE” menu in the top left corner of Full Namer, then click on your desired database. \nIn this example, we will look at the keywords database. When you click on “KEYWORDS”, a screen will appear. It includes a table with all the keywords currently in the database. Note, this does not mean that all these keywords are currently loaded into the keyword table in Full Namer. \n To add a new keyword to the database, click the “ADD” button. This will take you to the “ADD NEW KEYWORD TO DATABASE” screen. Follow the same instructions as you did from Full Namer. When you click “SUBMIT”, the keyword will appear in the display table.\n");
    }

}
