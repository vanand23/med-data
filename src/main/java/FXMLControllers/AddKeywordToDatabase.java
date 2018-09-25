package FXMLControllers;

import Types.KeywordManager;
import Types.KeywordType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static FXMLControllers.KeywordsDBTable.getDBdata;

public class AddKeywordToDatabase extends ScreenController implements Initializable {

    @FXML
    private JFXTextField thekeywordName;

    @FXML
    private JFXTextField thekeywordAbbrev;

    @FXML
    private JFXButton cancelButton;

    private String keywordAffix;

    private String keywordDataType;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        final ToggleGroup AffixPrefButtons = new ToggleGroup();
        final ToggleGroup DataTypePrefButtons = new ToggleGroup();
        thekeywordName.setMinWidth(100);
        thekeywordAbbrev.setMinWidth(100);
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
