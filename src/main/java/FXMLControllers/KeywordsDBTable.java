package FXMLControllers;

import Singletons.Database;
import Types.KeywordManager;
import Types.Keyword;
import Utilities.Config;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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

import static Singletons.Database.getKeywordFiles;

public class KeywordsDBTable extends ScreenController implements Initializable {

    @FXML
    public ChoiceBox<String> selectKeywordFile;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addKeywordsButton;

    @FXML
    private TableView<Keyword> keywordsDBTable;

    @FXML
    private TableColumn<Keyword, String> keywordName;

    @FXML
    private TableColumn<Keyword, String> keywordAbbrev;

    @FXML
    private TableColumn<Keyword, String> keywordAffix;

    @FXML
    private TableColumn<Keyword, String> keywordDataType;

    private final static ObservableList<Keyword> listOfKeywordsFromDatabase = FXCollections.observableArrayList();

    static ObservableList<Keyword> getListOfKeywordsFromDatabase() {
        return listOfKeywordsFromDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectKeywordFile.getItems().add("All Databases");
        selectKeywordFile.getSelectionModel().selectFirst();
        selectKeywordFile.getItems().addAll(getKeywordFiles());
        selectKeywordFile.getItems().add("+ Add new keyword database file");
        selectKeywordFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            HashMap<String, Keyword> listOfKeywords = KeywordManager.getInstance().getKeywords();
            switch (newSelectedFile)
            {
                case "All Databases":
                    listOfKeywordsFromDatabase.clear();
                    for(Map.Entry<String, Keyword> entry : listOfKeywords.entrySet()) {
                        listOfKeywordsFromDatabase.add(entry.getValue());
                    }
                    break;
                case "+ Add new keyword database file":
                    //TODO popup for adding a new database file(include explorer option? create a whole new file?)
                    selectKeywordFile.getSelectionModel().selectFirst();
                break;
                default:
                    listOfKeywordsFromDatabase.clear();
                    for(Map.Entry<String, Keyword> entry : listOfKeywords.entrySet()) {
                        if(entry.getValue().getFilename().equals(newSelectedFile))
                        {
                            listOfKeywordsFromDatabase.add(entry.getValue());
                        }
                    }
            }
        });

        selectKeywordFile.getSelectionModel().selectFirst();

        keywordName.setMinWidth(100);
        keywordAbbrev.setMinWidth(100);
        keywordAffix.setMinWidth(100);
        keywordDataType.setMinWidth(100);

        keywordName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        keywordAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        keywordAffix.setCellValueFactory(new PropertyValueFactory<>("affix"));
        keywordDataType.setCellValueFactory(new PropertyValueFactory<>("dataType"));

        keywordsDBTable.setEditable(true);
        keywordsDBTable.setItems(listOfKeywordsFromDatabase);
    }

    @FXML
    public void handleAddKeywordsButton (ActionEvent e) throws IOException {

        popupScreen("FXML/addKeywordToDatabase.fxml", addKeywordsButton.getScene().getWindow(),"Add Keyword DB Menu");

    }

    @FXML
    public void handleDeleteButton (ActionEvent e) throws IOException {
        Keyword selectedItem = keywordsDBTable.getSelectionModel().getSelectedItem();
        keywordsDBTable.getItems().remove(selectedItem);
        try {
            System.out.println(selectedItem.getLongName());
            Database.removeKeyword(selectedItem.getLongName());
            Database.writeKeywordsToCSV("Libraries/keywords/defaultKeywords.csv");
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
