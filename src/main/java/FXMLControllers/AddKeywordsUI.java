package FXMLControllers;


import Types.KeywordManager;
import Utilities.AutocompleteTextField;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import static FXMLControllers.FullNamer.getData;

public class AddKeywordsUI extends ScreenController implements Initializable, ITypeObserver {

    @FXML
    private AutocompleteTextField keywordName;

    @FXML
    private JFXTextField keywordDataVal;

    @FXML
    private JFXButton okButton;


    @Override
    public void initialize(URL location, ResourceBundle resources){
        onTypeUpdate();
        keywordName.setAutocompleteWidth(350);
        keywordName.textProperty().addListener((obs, oldExperimentType, newExperimentType) -> {
            if(keywordName.isValidText())
            {
                try {
                    if(!KeywordManager.getInstance().getKeywordByName("long",keywordName.getText()).getAffix().equals("none")) {
                        keywordDataVal.setDisable(false);
                        keywordDataVal.setText("");
                    }else{
                        keywordDataVal.setDisable(true);
                        keywordDataVal.setText("N/A");
                    }
                    }catch (NameNotFoundException e){
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
        Keyword keyword = new Keyword(keywordName.getText(), keywordDataVal.getText());
        parameterData.add(keyword);
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
}
