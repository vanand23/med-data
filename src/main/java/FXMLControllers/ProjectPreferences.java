package FXMLControllers;

import Singletons.Config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//This window is to allow users to have a project hierarchy to store all their data
//Can also enter in and save parameters that will populate in other fields that show up more than once such as researcher name
public class ProjectPreferences extends ScreenController implements Initializable {

    //The following are different fields the user can enter information for in the project preferences page
    @FXML
    private TextField researcherName; //name of researcher

    @FXML
    private TextField projectName; //name of project

    @FXML
    private JFXTextArea projectDescription; //description of project

    private String delimiter; //set the prefered separation character to use in the file name output

    //the choices are:
    @FXML
    private RadioButton asteriskButton;

    @FXML
    private RadioButton hyphenButton;

    @FXML
    private RadioButton underscoreButton;

    //preview box to see what the sample file name would look like with the choosen delimiter
    @FXML
    private TextField previewBox;

    //keep data persistent across different screens such as when switching to full namer or compact namer
    @FXML
    private JFXCheckBox rememberData;

    private static boolean isRememberData;

    //basic functionality buttons
    @FXML
    public JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;



    @Override
    //initialize the fields in project preferences
    public void initialize(URL location, ResourceBundle resources){
        updateDelimiter();
        generatePreview();

        //the config file is used to store the values the user enters on this project preferences screen to keep data persistent in other windows
        String configResearcherName = Config.getInstance().getProperty("researcherName");
        if(configResearcherName != null && !configResearcherName.trim().isEmpty())
        {
            researcherName.setText(configResearcherName);
        }
        String configProjectName = Config.getInstance().getProperty("projectName");
        if(configProjectName != null && !configProjectName.trim().isEmpty())
        {
            projectName.setText(configProjectName);
        }
        String configProjectDescription = Config.getInstance().getProperty("projectDescription");
        if(configProjectDescription != null && !configProjectDescription.trim().isEmpty())
        {
            projectDescription.setText(configProjectDescription);
        }
        String configRememberData = Config.getInstance().getProperty("rememberData");
        if(configRememberData != null && !configRememberData.trim().isEmpty())
        {
            rememberData.setSelected(Boolean.valueOf(configRememberData));
        }
    }

    //set the delimiter to what the user chooses
    @FXML
    public void setDelimiterToasterisk(ActionEvent e) throws IOException{
        delimiter = "*";
        generatePreview();
    }

    @FXML
    public void setDelimiterToHyphen(ActionEvent e) throws IOException{
        delimiter = "-";
        generatePreview();
    }

    @FXML
    public void setDelimiterToUnderscore(ActionEvent e) throws IOException{
        delimiter = "_";
        generatePreview();
    }


    //closes the project preferences screen and does not save any parameters that may have been entered
    @FXML
    public void closeProjectPreferences(ActionEvent e) throws IOException{
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

    //create a preview of the delimiter in use in the sample file name
    private void generatePreview(){
        previewBox.setText("Example" + delimiter + "File" + delimiter + "Name");
    }

    //save the data the user enters to be carried over to other windows such as researcher name that will stay the same in full namer
    public void saveProjectPreferences(ActionEvent e) throws IOException{
        Config.getInstance().setProperty("delimiter",delimiter);
        if(isRememberData){
        Config.getInstance().setProperty("researcherName",researcherName.getText());
        }
        else{
            Config.getInstance().setProperty("researcherName", "");
        }
        Config.getInstance().setProperty("projectName",projectName.getText());
        Config.getInstance().setProperty("projectDescription",projectDescription.getText());

        closeProjectPreferences(e);
    }

    //updates the delimiter based on the user's choice
    private void updateDelimiter(){

        delimiter = Config.getInstance().getProperty("delimiter");
        if(delimiter == null || delimiter.trim().isEmpty()){
            delimiter = "_";
        }
        switch(delimiter)
        {
            case "_":
                underscoreButton.setSelected(true); //underscore used
                hyphenButton.setSelected(false);
                asteriskButton.setSelected(false);
                break;
            case "-":
                underscoreButton.setSelected(false);
                hyphenButton.setSelected(true); //hyphen used
                asteriskButton.setSelected(false);
                break;
            case "*":
                underscoreButton.setSelected(false);
                hyphenButton.setSelected(false);
                asteriskButton.setSelected(true); //asterisk used
                break;
            default: //the default delimiter is set to the underscore
                underscoreButton.setSelected(true);
                hyphenButton.setSelected(false);
                asteriskButton.setSelected(false);
        }
    }

    @FXML
    //check if data is stored and made persistent correctly
    public void checkRememberData(ActionEvent e){
        if (rememberData.isSelected()){
            isRememberData = true;
            Config.getInstance().setProperty("rememberData", "true");
        }
        else{
           isRememberData = false;
           Config.getInstance().setProperty("rememberData", "false");
        }
    }
}
