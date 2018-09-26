package FXMLControllers;

import Singletons.Database;
import Types.KeywordManager;
import Types.KeywordType;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class KeywordsDBTable extends ScreenController implements Initializable {

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addKeywordsButton;

    @FXML
    private TableView<KeywordType> keywordsDBTable;

    @FXML
    private TableColumn keywordName;

    @FXML
    private TableColumn keywordAbbrev;

    @FXML
    private TableColumn keywordAffix;

    @FXML
    private TableColumn keywordDataType;

    private final static ObservableList<KeywordType> DBdata = FXCollections.observableArrayList();

    static ObservableList<KeywordType> getDBdata() {
        return DBdata;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        keywordName.setMinWidth(100);
        keywordAbbrev.setMinWidth(100);
        keywordAffix.setMinWidth(100);
        keywordDataType.setMinWidth(100);

        keywordName.setCellValueFactory(new PropertyValueFactory<KeywordType, String>("longName"));
        keywordAbbrev.setCellValueFactory(new PropertyValueFactory<KeywordType, String>("shortName"));
        keywordAffix.setCellValueFactory(new PropertyValueFactory<KeywordType, String>("affix"));
        keywordDataType.setCellValueFactory(new PropertyValueFactory<KeywordType, String>("dataType"));

        keywordsDBTable.setEditable(true);
        HashMap<String, KeywordType> listOfKeywords = KeywordManager.getInstance().getKeywords();
        for(Map.Entry<String, KeywordType> entry : listOfKeywords.entrySet()) {
            KeywordType value = entry.getValue();
            DBdata.add(new KeywordType("",
                    value.getLongName(),
                    value.getShortName(),
                    value.getAffix(),
                    value.getDataType(),
                    ""));
        }
        keywordsDBTable.setItems(DBdata);


    }

    @FXML
    public void handleAddKeywordsButton (ActionEvent e) throws IOException {

        popupScreen("FXML/addKeywordToDatabase.fxml", addKeywordsButton.getScene().getWindow(),"Add Keyword DB Menu");

    }

    @FXML
    public void handleDeleteButton (ActionEvent e) throws IOException {

        KeywordType selectedItem = keywordsDBTable.getSelectionModel().getSelectedItem();
        keywordsDBTable.getItems().remove(selectedItem);
        try {
            System.out.println(selectedItem.getLongName());
            Database.removeKeyword(selectedItem.getLongName());
            Database.writeKeywordsToCSV("Libraries/defaultKeywords.csv");
        }catch (SQLException e1){
            e1.printStackTrace();
        }

    }

    @FXML
    public void handleCancelButton (ActionEvent e) throws IOException {

        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();

    }

}
