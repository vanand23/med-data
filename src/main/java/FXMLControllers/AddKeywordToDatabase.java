package FXMLControllers;

import Singletons.KeywordManager;
import Types.Keyword;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static FXMLControllers.KeywordsTable.getListOfKeywordsFromDatabase;

public class AddKeywordToDatabase extends ScreenController implements Initializable {

    //all the fields that can be entered to create a new keyword
    @FXML
    private TextField thekeywordName; //name

    @FXML
    private TextField thekeywordAbbrev; //abbreviation

    private String keywordAffix; //prefix or affix to display keyword abbreviation before or after the data value

    @FXML
    private RadioButton prefixRadioButton;

    @FXML
    private RadioButton suffixRadioButton;

    private String keywordDataType; //numeric, alphanumeric, or no data type for keyword

    @FXML
    private RadioButton numericData;

    @FXML
    private RadioButton alphanumericData;

    @FXML
    private ComboBox<String> pickKeywordDatabase; //pick what database to use to store keywords

    //display a preview of the keyword, affix, and data value
    @FXML
    private TextField keywordPreviewField;


    //basic functionality buttons
    @FXML
    private JFXButton keywordPreviewHelp; //help button for the keyword preview

    @FXML
    private JFXButton cancelButton;

    @FXML
    private RadioButton noData;

    @Override
    //initialize the database with any previously inputted values
    public void initialize(URL location, ResourceBundle resources){
        //help button information for keyword preview
        final JFXButton previewHelp = keywordPreviewHelp;
        final Tooltip previewTooltip = new Tooltip();
        previewTooltip.setText("View a preview of your keyword with example values.");
        previewHelp.setTooltip(previewTooltip);

        pickKeywordDatabase.getItems().addAll(KeywordManager.getInstance().getKeywordFiles());
        pickKeywordDatabase.getSelectionModel().selectFirst();

        //select button options for affix and data type for the user to choose
        final ToggleGroup AffixPrefButtons = new ToggleGroup();
        final ToggleGroup DataTypePrefButtons = new ToggleGroup();

        thekeywordName.setMinWidth(100);
        thekeywordAbbrev.setMinWidth(100);

        //listener to update the preview if different options are selected
        thekeywordAbbrev.textProperty().addListener((obs, oldPreview, newPreview) ->{
            if (thekeywordAbbrev!= null && !thekeywordAbbrev.getText().trim().isEmpty()){
                generatePreview();
            }
            else{keywordPreviewField.setText("");}
        } );
    }

    @FXML
    //add the keyword parameters to the table and Keyword Manager database after submit button is clicked
    //the database chosen to store the keyword parameter information is also stored
    public void handleSubmitButton(ActionEvent e) throws IOException {
        getListOfKeywordsFromDatabase().add(new Keyword( //add parameters to table
                thekeywordName.getText(),
                thekeywordAbbrev.getText(),
                keywordAffix,
                keywordDataType,
                "",
                pickKeywordDatabase.getValue()
                ));

        KeywordManager.getInstance().addKeyword( //add parameters to database
                new Keyword(
                thekeywordName.getText(),
                thekeywordAbbrev.getText(),
                keywordDataType,
                keywordAffix,
                "",
                pickKeywordDatabase.getValue()));
        thekeywordName.clear();
        thekeywordAbbrev.clear();
        Stage primaryStage = (Stage) thekeywordAbbrev.getScene().getWindow();
        primaryStage.close();
        //clear the fields and close the window
    }

    //One of the following affixes can be selected
    @FXML
    public void handlePrefixButton(ActionEvent e) throws IOException {
        keywordAffix = "prefix";
        generatePreview();
    }

    @FXML
    public void handleSuffixButton(ActionEvent e) throws IOException {
        keywordAffix = "suffix";
        generatePreview();
    }

    //One of the following data types can be selected from the user interface
    //Will disbale the prefix/suffix buttons is no data type if chosen
    @FXML
    public void handleNumericButton(ActionEvent e) throws IOException {
        keywordDataType = "numeric";
        prefixRadioButton.setDisable(false);
        suffixRadioButton.setDisable(false);
        generatePreview();
    }

    @FXML
    public void handleAlphanumericButton(ActionEvent e) throws IOException {
        keywordDataType = "alphanumeric";
        prefixRadioButton.setDisable(false);
        suffixRadioButton.setDisable(false);
        generatePreview();
    }

    @FXML
    public void handleNoDataButton(ActionEvent e) throws IOException {
        keywordDataType = "no data";
        prefixRadioButton.setDisable(true);
        suffixRadioButton.setDisable(true);
        generatePreview();
    }

    @FXML
    //closes the window and does not save any inputted values
    public void handleCancelButton (ActionEvent e) throws IOException {

        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();

    }

    //generate a preview of the keyword, affix, and data type chosen by the user
    private void generatePreview(){
       keywordPreviewField.setText(thekeywordAbbrev.getText());
       if(numericData.isSelected()){
           if(prefixRadioButton.isSelected()){
               keywordPreviewField.setText( thekeywordAbbrev.getText() + "2");
           }
           else if (suffixRadioButton.isSelected()){
               keywordPreviewField.setText("2" + thekeywordAbbrev.getText());
           }
           else{keywordPreviewField.setText(thekeywordAbbrev.getText());}
       }
       else if (alphanumericData.isSelected()){
           if(prefixRadioButton.isSelected()){
               keywordPreviewField.setText(thekeywordAbbrev.getText() + "2a");
           }
           else if(suffixRadioButton.isSelected()){
               keywordPreviewField.setText("2a" + thekeywordAbbrev.getText());
           }
           else{keywordPreviewField.setText(thekeywordAbbrev.getText());}
       }
       else{
           keywordPreviewField.setText(thekeywordAbbrev.getText());
       }



    }

}
