package FXMLControllers;

import Singletons.FXMLManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBase;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectPreferences extends ScreenController implements Initializable {

    private static ProjectPreferences instance = new ProjectPreferences();

    @FXML
    private JFXTextField researcherNamePreference;

    @FXML
    private JFXTextField projectName;

    @FXML
    private JFXTextArea projectDescription;

    @FXML
    private JFXTextField keywordFilepath;

    @FXML
    private JFXButton addGlossaryButton;

    @FXML
    private RadioButton asterixButton;

    @FXML
    private RadioButton hyphenButton;

    @FXML
    private RadioButton underscoreButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextField previewBox;

    private String delimiter;
    private String nameOfResearcher;

   @Override
   public void initialize(URL location, ResourceBundle resources){
       final ToggleGroup prefButtons = new ToggleGroup();
       delimiter = "_";
       generatePreview();

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

    public void generatePreview(){
       previewBox.setText("Example" + delimiter + "File" + delimiter + "Name");
    }

    public static ProjectPreferences getInstance() {return instance;}

   public void saveProjectPreferences(ActionEvent e) throws IOException{
       updateResearcherName();
       updateDelimiter();
   }

   public void updateResearcherName(){
    nameOfResearcher = researcherNamePreference.getText();
    //set researcher name textbook in full namer to nameOfResearcher
   }

    //updates the delimiter based on the user's choice in the radio buttons
   public void updateDelimiter(){


   }








}
