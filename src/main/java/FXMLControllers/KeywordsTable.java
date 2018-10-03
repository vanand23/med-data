package FXMLControllers;

import Singletons.Database;
import Types.KeywordManager;
import Types.Keyword;
import Utilities.ITypeObserver;
import com.jfoenix.controls.JFXButton;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class KeywordsTable extends ScreenController implements Initializable, ITypeObserver {
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

    private static String selectedValue = "";

    static void setSelectedValue(String selectedValue) {
        KeywordsTable.selectedValue = selectedValue;
    }

    static ObservableList<Keyword> getListOfKeywordsFromDatabase() {
        return listOfKeywordsFromDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        keywordName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        keywordAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        keywordAffix.setCellValueFactory(new PropertyValueFactory<>("affix"));
        keywordDataType.setCellValueFactory(new PropertyValueFactory<>("dataType"));


        KeywordManager.getInstance().subscribe(this);
        onTypeUpdate();
        selectKeywordFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            if (newSelectedFile != null) {
                switch (newSelectedFile) {
                    case "All Databases":
                        refreshTableOfKeywords();
                        break;
                    case "+ Create new keyword database file":
                        try {
                            Stage stage = popupScreen("FXML/createNewKeywordDatabase.fxml", addKeywordsButton.getScene().getWindow());
                            stage.setOnHidden(windowEvent -> onTypeUpdate());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectKeywordFile.setValue(selectedValue);
                        break;
                    default:
                        listOfKeywordsFromDatabase.clear();
                        HashMap<String, Keyword> listOfKeywords = KeywordManager.getInstance().getKeywords();
                        for (Map.Entry<String, Keyword> entry : listOfKeywords.entrySet()) {
                            if (entry.getValue().getFilename().equals(newSelectedFile)) {
                                listOfKeywordsFromDatabase.add(entry.getValue());
                            }
                        }
                }
            }
        });

        selectKeywordFile.getSelectionModel().selectFirst();
        keywordsDBTable.setEditable(true);
        keywordsDBTable.setItems(listOfKeywordsFromDatabase);

        listOfKeywordsFromDatabase.clear();
        refreshTableOfKeywords();
    }

    @FXML
    private void refreshTableOfKeywords()
    {
        listOfKeywordsFromDatabase.clear();
        HashMap<String, Keyword> listOfKeywords = KeywordManager.getInstance().getKeywords();
        for (Map.Entry<String, Keyword> entry : listOfKeywords.entrySet()) {
            listOfKeywordsFromDatabase.add(entry.getValue());
        }
    }

    @FXML
    public void handleAddKeywordsButton (ActionEvent e) throws IOException {
        popupScreen("FXML/addKeywordToDatabase.fxml", addKeywordsButton.getScene().getWindow());
    }

    @FXML
    public void handleDeleteButton (ActionEvent e) throws IOException {
        Keyword selectedItem = keywordsDBTable.getSelectionModel().getSelectedItem();
        keywordsDBTable.getItems().remove(selectedItem);
        try {
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

    @Override
    public void onTypeUpdate() {
        ArrayList<String> keywordFiles = KeywordManager.getInstance().getKeywordFiles();
        selectKeywordFile.getItems().clear();
        selectKeywordFile.getItems().add("All Databases");
        selectKeywordFile.getItems().addAll(keywordFiles);
        selectKeywordFile.getItems().add("+ Create new keyword database file");
        selectKeywordFile.getSelectionModel().selectFirst();
    }
}
