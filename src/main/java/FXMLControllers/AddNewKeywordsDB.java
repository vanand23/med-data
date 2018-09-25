package FXMLControllers;

import Singletons.Database;
import Types.KeywordManager;
import Types.KeywordType;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import org.apache.derby.client.am.SqlException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static FXMLControllers.KeywordsDBTable.getDBdata;

public class AddNewKeywordsDB extends ScreenController implements Initializable {

    @FXML
    private JFXTextField thekeywordName;

    @FXML
    private JFXTextField thekeywordAbbrev;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private RadioButton prefixID;

    @FXML
    private RadioButton suffixID;

    @FXML
    private RadioButton numericData;

    @FXML
    private RadioButton alphanumericData;

    @FXML
    private RadioButton noData;

    @FXML
    private JFXTextField keywordPreviewField;

    @FXML
    private JFXButton keywordPreviewHelp;

    String keywordAffix;

    String keywordDataType;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        final JFXButton previewHelp = keywordPreviewHelp;
        final Tooltip previewTooltip = new Tooltip();
        previewTooltip.setText("View a preview of your keyword with example values.");
        previewHelp.setTooltip(previewTooltip);

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
        getDBdata().add(new KeywordDB(thekeywordName.getText(),
                thekeywordAbbrev.getText(),
                keywordAffix,
                keywordDataType,
                ""));
        int keywordListSize = KeywordManager.getInstance().getNumberOfKeywords();
        KeywordType lastKeyword = KeywordManager.getInstance().getKeywords().get(String.valueOf(keywordListSize));
        if(lastKeyword == null)
        {
            lastKeyword = new KeywordType("0","","","","");
        }

        KeywordManager.getInstance().addKeyword(
                new KeywordType((String.valueOf(Integer.valueOf(lastKeyword.getID())+1)),
                thekeywordName.getText(),
                thekeywordAbbrev.getText(),
                keywordDataType,
                keywordAffix));

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
        generatePreview();
    }

    @FXML
    public void handleAlphanumericButton(ActionEvent e) throws IOException {
        keywordDataType = "alphanumeric";
        generatePreview();
    }

    @FXML
    public void handleNoDataButton(ActionEvent e) throws IOException {
        keywordDataType = "no data";
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
           if(prefixID.isSelected()){
               keywordPreviewField.setText("2" + thekeywordAbbrev.getText());
           }
           else{keywordPreviewField.setText(thekeywordAbbrev.getText() + "2");}
       }
       else if (alphanumericData.isSelected()){
           if(prefixID.isSelected()){
               keywordPreviewField.setText("2a" + thekeywordAbbrev.getText());
           }
           else{keywordPreviewField.setText(thekeywordAbbrev.getText() + "2a");}
       }
       else{
           keywordPreviewField.setText(thekeywordAbbrev.getText());
       }



    }

}
