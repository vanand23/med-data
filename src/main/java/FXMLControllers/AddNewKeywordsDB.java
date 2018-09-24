package FXMLControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
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
    private JFXTextField thedataValue;

    String keywordAffix;

    String keywordDataType;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        final ToggleGroup AffixPrefButtons = new ToggleGroup();
        final ToggleGroup DataTypePrefButtons = new ToggleGroup();
        thekeywordName.setMinWidth(100);
        thekeywordAbbrev.setMinWidth(100);
        thedataValue.setMinWidth(100);
    }

    @FXML
    public void handleSubmitButton(ActionEvent e) throws IOException {
        ObservableList<KeywordDB> theparameterData;
        theparameterData = getDBdata();
        KeywordDB keyword = new KeywordDB(thekeywordName.getText(), thekeywordAbbrev.getText(), keywordAffix, keywordDataType, thedataValue.getText());
        theparameterData.add(keyword);
        thekeywordName.clear();
        thekeywordAbbrev.clear();
        thedataValue.clear();
        Stage primaryStage = (Stage) thedataValue.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    public void handlePrefixButton(ActionEvent e) throws IOException {
        keywordAffix = "prefix";
    }

    @FXML
    public void handleSuffixButton(ActionEvent e) throws IOException {
        keywordAffix = "suffix";
    }

    @FXML
    public void handleNumericButton(ActionEvent e) throws IOException {
        keywordDataType = "numeric";
    }

    @FXML
    public void handleAlphanumericButton(ActionEvent e) throws IOException {
        keywordDataType = "alphanumeric";
    }

    @FXML
    public void handleNoDataButton(ActionEvent e) throws IOException {
        keywordDataType = "no data";
    }

    @FXML
    public void handleCancelButton (ActionEvent e) throws IOException {

        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();

    }

}
