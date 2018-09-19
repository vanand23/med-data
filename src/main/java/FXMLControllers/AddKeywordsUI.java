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

public class AddKeywordsUI extends ScreenController implements Initializable, ITypeObserver {

    @FXML
    private AutocompleteTextField keywordName;

    @FXML
    private JFXTextField keywordDataVal;

    @FXML
    private JFXButton okButton;


    @Override
    public void initialize(URL location, ResourceBundle resources){

        keywordName.setAutocompleteWidth(350);
        //keywordDataVal.setText("0");

    }

    private void getParamNames() {

        String keywordNameText = keywordName.getText();
        String keywordDataValText = keywordDataVal.getText();

        String keywordAbbrev = "";

        if(keywordNameText != null && !(keywordNameText.trim().isEmpty()))
        {

            try {
                keywordAbbrev = KeywordManager.getInstance().getKeywordByName("long", keywordNameText).getShortName();

            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }

    }

    @FXML
    public void updateKeywords(ActionEvent e) throws IOException {
        onTypeUpdate();
    }

    @FXML
    public void handleOKButton(ActionEvent e) throws IOException {
        //data.add(new Keyword(keywordName.getText(), keywordDataVal.getText()));
        //keywordName.clear();
        //keywordDataVal.clear();
    }

    @Override
    public void onTypeUpdate() {
        ArrayList<String> keynames = (ArrayList<String>) KeywordManager.getInstance().getAllKeywordLongNames();
        keywordName.getEntries().addAll(keynames);

    }
}
