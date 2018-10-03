package FXMLControllers;

import Singletons.Config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectPreferences extends ScreenController implements Initializable {

    @FXML
    public JFXButton saveButton;
    @FXML
    private JFXTextField researcherName;

    @FXML
    private JFXTextField projectName;

    @FXML
    private JFXTextArea projectDescription;

    @FXML
    private RadioButton asterixButton;

    @FXML
    private RadioButton hyphenButton;

    @FXML
    private RadioButton underscoreButton;

    @FXML
    private JFXTextField previewBox;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXCheckBox rememberData;

    private static boolean isRememberData;

    private String delimiter;



    @Override
    public void initialize(URL location, ResourceBundle resources){
        updateDelimiter();
        generatePreview();

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

    @FXML
    public void setDelimiterToAsterix(ActionEvent e) throws IOException{
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


    @FXML
    public void closeProjectPreferences(ActionEvent e) throws IOException{
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/fullNamer.fxml", cancelButton.getScene().getWindow());

    }

    private void generatePreview(){
        previewBox.setText("Example" + delimiter + "File" + delimiter + "Name");
    }

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

    //updates the delimiter based on the user's choice in the radio buttons
    private void updateDelimiter(){

        delimiter = Config.getInstance().getProperty("delimiter");
        if(delimiter == null || delimiter.trim().isEmpty()){
            delimiter = "_";
        }
        switch(delimiter)
        {
            case "_":
                underscoreButton.setSelected(true);
                hyphenButton.setSelected(false);
                asterixButton.setSelected(false);
                break;
            case "-":
                underscoreButton.setSelected(false);
                hyphenButton.setSelected(true);
                asterixButton.setSelected(false);
                break;
            case "*":
                underscoreButton.setSelected(false);
                hyphenButton.setSelected(false);
                asterixButton.setSelected(true);
                break;
            default:
                underscoreButton.setSelected(true);
                hyphenButton.setSelected(false);
                asterixButton.setSelected(false);
        }
    }

    @FXML
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
