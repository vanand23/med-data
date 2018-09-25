package FXMLControllers;

import Utilities.Config;
import com.jfoenix.controls.JFXButton;
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

import static Utilities.Config.setProperty;

public class ProjectPreferences extends ScreenController implements Initializable {

    private static ProjectPreferences instance = new ProjectPreferences();

   //@FXML
   //private JFXButton helpButtonFilepath;

    @FXML
    private JFXTextField researcherName;

    @FXML
    private JFXTextField projectName;

    @FXML
    private JFXTextArea projectDescription;

    //@FXML
    //private JFXTextField keywordFilepath;

    //@FXML
    //private JFXButton addGlossaryButton;

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

    private String delimiter;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        updateDelimiter();
        generatePreview();
        Config config = new Config();
        String configResearcherName = config.getProperty("researcherName");
        if(configResearcherName != null && !configResearcherName.trim().isEmpty())
        {
            researcherName.setText(configResearcherName);
        }
        String configProjectName = config.getProperty("projectName");
        if(configProjectName != null && !configProjectName.trim().isEmpty())
        {
            projectName.setText(configProjectName);
        }
        String configProjectDescription = config.getProperty("projectDescription");
        if(configProjectDescription != null && !configProjectDescription.trim().isEmpty())
        {
            projectDescription.setText(configProjectDescription);
        }
        /*
        researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
            setProperty("researcherName",newResearcherName);
        });
        projectName.textProperty().addListener((obs, oldProjectName, newProjectName) -> {
            setProperty("projectName",newProjectName);
        });
        projectDescription.textProperty().addListener((obs, oldProjectDescription, newProjectDescription) -> {
            setProperty("projectDescription",newProjectDescription);
        });*/

        //final JFXButton filepathHelp = helpButtonFilepath;
        //final Tooltip filepathTooltip = new Tooltip();
        //filepathTooltip.setText("This should look like: /csv/Example.csv");
        //filepathHelp.setTooltip(filepathTooltip);


    }

    @FXML
    public void setDelimiterToAsterix(ActionEvent e) throws IOException{
        delimiter = "*";
        generatePreview();
        //setProperty("delimiter",delimiter);
    }

    @FXML
    public void setDelimiterToHyphen(ActionEvent e) throws IOException{
        delimiter = "-";
        generatePreview();
        //setProperty("delimiter",delimiter);
    }

    @FXML
    public void setDelimiterToUnderscore(ActionEvent e) throws IOException{
        delimiter = "_";
        generatePreview();
        //setProperty("delimiter",delimiter);
    }


    @FXML
    public void closeProjectPreferences(ActionEvent e) throws IOException{
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/fullNamer.fxml", cancelButton.getScene().getWindow(),"Compact Namer");

    }

    private void generatePreview(){
        previewBox.setText("Example" + delimiter + "File" + delimiter + "Name");
    }

    public void saveProjectPreferences(ActionEvent e) throws IOException{
        setProperty("delimiter",delimiter);
        setProperty("researcherName",researcherName.getText());
        setProperty("projectName",projectName.getText());
        setProperty("projectDescription",projectDescription.getText());
        closeProjectPreferences(e);
    }

    //updates the delimiter based on the user's choice in the radio buttons
    private void updateDelimiter(){
        Config config = new Config();
        delimiter = config.getProperty("delimiter");
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
    public void openKeywordLibrary (ActionEvent e) throws IOException {
        popupScreen("FXML/KeywordsDBTable.fxml", asterixButton.getScene().getWindow(),"Add Keywords to DB");
    }

    @FXML
    public void openExperimentLibrary (ActionEvent e) throws IOException {
        popupScreen("FXML/experimentsTable.fxml", asterixButton.getScene().getWindow(),"Add Experiments to DB");
    }



    /**
     * Singleton helper class, MapManager should always be accessed through MapManager.getInstance();
     */
    private static class SingletonHelper{
        private static final ProjectPreferences INSTANCE = new ProjectPreferences();
    }

    /**
     * Gets the singleton instance of project preferences
     * @return the proper single instance of map editor
     */
    static ProjectPreferences getInstance(){
        return SingletonHelper.INSTANCE;
    }

}
