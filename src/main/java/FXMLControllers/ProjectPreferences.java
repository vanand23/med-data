package FXMLControllers;

import Singletons.FXMLManager;
import Utilities.Config;
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
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import static Utilities.Config.setProperty;

public class ProjectPreferences extends ScreenController implements Initializable {

    private static ProjectPreferences instance = new ProjectPreferences();

   @FXML

   private JFXButton helpButtonFilepath;

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

    public String getDelimiter() {
        return delimiter;
    }

    private String delimiter;
    private String nameOfResearcher;


   @Override
   public void initialize(URL location, ResourceBundle resources){
       final ToggleGroup prefButtons = new ToggleGroup();
       delimiter = "_";
       generatePreview();
       setProperty("delimiter", delimiter);

       final JFXButton filepathHelp = helpButtonFilepath;
       final Tooltip filepathTooltip = new Tooltip();
       filepathTooltip.setText("This should look like: /csv/Example.csv");
       filepathHelp.setTooltip(filepathTooltip);
   }

    @FXML
    public void setDelimiterToAsterix(ActionEvent e) throws IOException{
        delimiter = "*";
        generatePreview();
        setProperty("delimiter", delimiter);
    }

    @FXML
    public void setDelimiterToHyphen(ActionEvent e) throws IOException{
        delimiter = "-";
        generatePreview();
        setProperty("delimiter", delimiter);
    }

    @FXML
    public void setDelimiterToUnderscore(ActionEvent e) throws IOException{
        delimiter = "_";
        generatePreview();
        setProperty("delimiter", delimiter);
    }

    private void generatePreview(){

       previewBox.setText("Example" + delimiter + "File" + delimiter + "Name");
    }

   public void saveProjectPreferences(ActionEvent e) throws IOException{
       updateResearcherName();
       updateDelimiter();
   }

   private void updateResearcherName(){
       String thisResearcherName = researcherName.getText();
       if(thisResearcherName != null && !(thisResearcherName.trim().isEmpty()))
       {
           setProperty("researcherName", thisResearcherName);
       }
       //set researcher name textbook in full namer to nameOfResearcher
   }

    //updates the delimiter based on the user's choice in the radio buttons
    private void updateDelimiter(){
        setProperty("delimiter", delimiter);
    }

    /**
     * Singleton helper class, MapManager should always be accessed through MapManager.getInstance();
     */
    private static class SingletonHelper{
        private static final ProjectPreferences INSTANCE = new ProjectPreferences();
    }

    /**
     * Gets the singleton instance of map editor
     * @return the proper single instance of map editor
     */
    public static ProjectPreferences getInstance(){
        return SingletonHelper.INSTANCE;
    }

}
