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

    @FXML
    private TextField thekeywordName;

    @FXML
    private TextField thekeywordAbbrev;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private RadioButton prefixRadioButton;

    @FXML
    private RadioButton suffixRadioButton;

    @FXML
    private RadioButton numericData;

    @FXML
    private RadioButton alphanumericData;

    @FXML
    private RadioButton noData;

    @FXML
    private TextField keywordPreviewField;

    @FXML
    private ComboBox<String> pickKeywordDatabase;

    @FXML
    private JFXButton keywordPreviewHelp;

    private String keywordAffix;

    private String keywordDataType;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        final JFXButton previewHelp = keywordPreviewHelp;
        final Tooltip previewTooltip = new Tooltip();
        previewTooltip.setText("View a preview of your keyword with example values.");
        previewHelp.setTooltip(previewTooltip);

        pickKeywordDatabase.getItems().addAll(KeywordManager.getInstance().getKeywordFiles());
        pickKeywordDatabase.getSelectionModel().selectFirst();

        final ToggleGroup AffixPrefButtons = new ToggleGroup();
        final ToggleGroup DataTypePrefButtons = new ToggleGroup();
        thekeywordName.setMinWidth(100);
        thekeywordAbbrev.setMinWidth(100);

        thekeywordAbbrev.textProperty().addListener((obs, oldPreview, newPreview) ->{
            if (thekeywordAbbrev!= null && !thekeywordAbbrev.getText().trim().isEmpty()){
                generatePreview();
            }
            else{keywordPreviewField.setText("");}
        } );
    }

    @FXML
    public void handleSubmitButton(ActionEvent e) throws IOException {
        getListOfKeywordsFromDatabase().add(new Keyword(
                thekeywordName.getText(),
                thekeywordAbbrev.getText(),
                keywordAffix,
                keywordDataType,
                "",
                pickKeywordDatabase.getValue()
                ));

        KeywordManager.getInstance().addKeyword(
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
    }

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
    public void handleCancelButton (ActionEvent e) throws IOException {

        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();

    }

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
