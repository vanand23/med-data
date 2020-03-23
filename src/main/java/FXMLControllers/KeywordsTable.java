package FXMLControllers;

import Singletons.Database;
import Singletons.KeywordManager;
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

    //The following are the parameters that display in the table view of keywords in the database
    @FXML
    private TableView<Keyword> keywordsDBTable; //the overall keyword database table

    @FXML
    private TableColumn<Keyword, String> keywordName; //name of keyword

    @FXML
    private TableColumn<Keyword, String> keywordAbbrev; //abbreviation of keyword

    @FXML
    private TableColumn<Keyword, String> keywordAffix; //affix (prefix or suffix) of data type

    @FXML
    private TableColumn<Keyword, String> keywordDataType; //type of data (numeric, alphanumeric, none)

    @FXML
    public ChoiceBox<String> selectKeywordFile; //choose what database file to use to store the above parameters

    @FXML
    private JFXButton addKeywordsButton; //add new keywords to database button

    //list of new keywords the user wants to input into the database
    private final static ObservableList<Keyword> listOfKeywordsFromDatabase = FXCollections.observableArrayList();

    //getter for the list above
    static ObservableList<Keyword> getListOfKeywordsFromDatabase() {
        return listOfKeywordsFromDatabase;
    }

    //get the value the user selected in the table
    private static String selectedValue = "";

    //setter for the string above
    static void setSelectedValue(String selectedValue) {
        KeywordsTable.selectedValue = selectedValue;
    }

    @FXML
    private JFXButton cancelButton; //basic functionality button

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set the column name to populate the corresponding column with the input data the user provided
        keywordName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        keywordAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        keywordAffix.setCellValueFactory(new PropertyValueFactory<>("affix"));
        keywordDataType.setCellValueFactory(new PropertyValueFactory<>("dataType"));

        //update the database dropdown list
        KeywordManager.getInstance().subscribe(this);
        onTypeUpdate();
        //selecting which keyword database file to use to load the newly created experiments
        selectKeywordFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            if (newSelectedFile != null) { //if the user chooses to add a new file to the list of csv databases
                switch (newSelectedFile) {
                    case "All Databases": //show all the available databases
                        refreshTableOfKeywords();
                        break;
                    case "+ Create new keyword database file": //show the option to create a new database file
                        try {
                            //if this option is selected, show a popup screen to type in the name of the new database file and then close the window
                            Stage stage = popupScreen("FXML/createNewKeywordDatabase.fxml", addKeywordsButton.getScene().getWindow());
                            stage.setOnHidden(windowEvent -> onTypeUpdate());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectKeywordFile.setValue(selectedValue);
                        break;
                    default: //set the default to showing the list of databases for keywords
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
        keywordsDBTable.setEditable(true); //make keywords database table editable
        keywordsDBTable.setItems(listOfKeywordsFromDatabase); //set the user inputted items in the table

        listOfKeywordsFromDatabase.clear();
        refreshTableOfKeywords();
    }

    @FXML
    //make sure to update the keyword table
    private void refreshTableOfKeywords()
    {
        listOfKeywordsFromDatabase.clear();
        HashMap<String, Keyword> listOfKeywords = KeywordManager.getInstance().getKeywords();
        for (Map.Entry<String, Keyword> entry : listOfKeywords.entrySet()) {
            listOfKeywordsFromDatabase.add(entry.getValue()); //add the user inputted value into the table
        }
    }

    @FXML
    //Add entries to the Keywords Database table view
    public void handleAddKeywordsButton (ActionEvent e) throws IOException {
        popupScreen("FXML/addKeywordToDatabase.fxml", addKeywordsButton.getScene().getWindow());
    }

    @FXML
    //Delete selected row in the table view for keywords database window and also in the csv database file
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
    //closes the window and does not save any user inputted values
    public void handleCancelButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

    @Override
    //update the dropdown menu for selecting which database file to load and use to store keywords
    public void onTypeUpdate() {
        ArrayList<String> keywordFiles = KeywordManager.getInstance().getKeywordFiles();
        selectKeywordFile.getItems().clear();
        selectKeywordFile.getItems().add("All Databases");
        selectKeywordFile.getItems().addAll(keywordFiles);
        selectKeywordFile.getItems().add("+ Create new keyword database file");
        selectKeywordFile.getSelectionModel().selectFirst();
    }
}
