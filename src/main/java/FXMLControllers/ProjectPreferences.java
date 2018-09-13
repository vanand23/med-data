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

    @FXML
    private JFXTextField researcherName;

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








}
