package FXMLControllers;

import Singletons.KeywordManager;
import Types.Keyword;
import Utilities.AutocompleteTextField;
import Singletons.Config;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import static FXMLControllers.FullNamer.getData;

public class SelectKeywordForFilename extends ScreenController implements Initializable, ITypeObserver {

    @FXML
    private AutocompleteTextField keywordName;

    @FXML
    private TextField keywordDataVal;

    @FXML
    private JFXButton okButton;

    private Pattern numeric = Pattern.compile("-?((\\d*)|(\\d+\\.\\d*))"); //regex for allowing a double
    private Pattern alphanumeric = Pattern.compile("^[a-zA-Z0-9]+$"); //regex for allowing alphanumeric characters

    @Override
    public void initialize(URL location, ResourceBundle resources){
        TextFormatter<Double> numericFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0,
                change -> {
                    String newText = change.getControlNewText();
                    if (numeric.matcher(newText).matches()) {
                        return change;
                    }else{
                        return null;
                    }
                });

        TextFormatter<String> alphanumericFormatter = new TextFormatter<>(change -> {
            if (alphanumeric.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        });


        onTypeUpdate();
        keywordName.setAutocompleteWidth(350);
        keywordName.textProperty().addListener((obs, oldExperimentType, newExperimentType) -> {
            if(keywordName.isValidText())
            {
                try {
                    String dataType = KeywordManager.getInstance().getKeywordByName("long",keywordName.getText()).getDataType();
                    switch(dataType)
                    {
                        case "alphanumeric":
                            keywordDataVal.setTextFormatter(alphanumericFormatter);
                            keywordDataVal.setDisable(false);
                            keywordDataVal.setText("");
                            break;
                        case "numeric":
                            keywordDataVal.setTextFormatter(numericFormatter);
                            keywordDataVal.setDisable(false);
                            keywordDataVal.setText("");
                            break;
                        case "no data":
                            keywordDataVal.setTextFormatter(null);
                            keywordDataVal.setDisable(true);
                            keywordDataVal.setText("N/A");
                            break;
                    }
                 }catch (NameNotFoundException e){
                    e.printStackTrace();
                }
            }else if(keywordName.isTriggerPopup()){
                try {
                    Stage primaryStage = (Stage) keywordName.getScene().getWindow();
                    primaryStage.close();
                    popupScreen("FXML/addKeywordToDatabase.fxml", keywordName.getScene().getWindow());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void updateKeywords(ActionEvent e) throws IOException {
        onTypeUpdate();
    }

    @FXML
    public void handleOKButton(ActionEvent e) throws IOException {
        ObservableList<Keyword> parameterData;
        parameterData = getData();
        Keyword newKeyword = new Keyword(keywordName.getText(), "", "", "", keywordDataVal.getText(), "");
        parameterData.add(newKeyword);

        String configListOfKeywords = Config.getInstance().getProperty("listOfKeywords");
        if(configListOfKeywords == null || configListOfKeywords.trim().isEmpty())
        {
            configListOfKeywords = keywordName.getText() + "," + keywordDataVal.getText();
        }else
        {
            configListOfKeywords = configListOfKeywords + "," + keywordName.getText() + "," + keywordDataVal.getText();
        }
        Config.getInstance().setProperty("listOfKeywords",configListOfKeywords);
        keywordName.setValidText(false);
        keywordName.clear();
        keywordDataVal.clear();
        Stage primaryStage = (Stage) keywordDataVal.getScene().getWindow();
        primaryStage.close();
    }

    @Override
    public void onTypeUpdate() {
        ArrayList<String> keynames = (ArrayList<String>) KeywordManager.getInstance().getAllKeywordLongNames();
        keywordName.getEntries().addAll(keynames);
    }

    @FXML
    public void handleCloseButton(ActionEvent actionEvent) {
        Stage primaryStage = (Stage) okButton.getScene().getWindow();
        primaryStage.close();
    }
}
