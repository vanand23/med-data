package FXMLControllers;

import Singletons.Database;
import Singletons.ResearcherManager;
import Types.Researcher;
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

public class ResearchersTable extends ScreenController implements Initializable, ITypeObserver {

    //The following are the parameters that display in the table view of researchers in the database
    @FXML
    private TableView<Researcher> researchersDBTable; //the overall researcher database table

    @FXML
    private TableColumn<Researcher, String> researcherName; //name of researcher

    @FXML
    private TableColumn<Researcher, String> researcherAbbrev; //initials of researcher

    @FXML
    public ChoiceBox<String> selectResearcherFile; //choose what database file to use to store the above parameters

    @FXML
    private JFXButton addResearchersButton; //add new researchers to database button

    @FXML
    private JFXButton cancelButton; //basic functionality button

    //list of new researcher the user wants to input into the database
    private final static ObservableList<Researcher> listOfResearchersFromDatabase = FXCollections.observableArrayList();

    //getter for the list above
    static ObservableList<Researcher> getListOfResearchersFromDatabase() {
        return listOfResearchersFromDatabase;
    }

    //get the value the user selected in the table
    private static String selectedValue = "";

    //setter for the string above
    static void setSelectedValue(String selectedValue) {
        ResearchersTable.selectedValue = selectedValue;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set the column name to populate the corresponding column with the input data the user provided
        researcherName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        researcherAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));

        //update the database dropdown list
        ResearcherManager.getInstance().subscribe(this);
        onTypeUpdate();
        //selecting which researcher database file to use to load the newly created experiments
        selectResearcherFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            if (newSelectedFile != null) { //if the user chooses to add a new file to the list of csv databases
                switch (newSelectedFile) {
                    case "All Databases": //show all the available databases
                        refreshTableOfResearchers();
                        break;
                    case "+ Create new researcher database file": //show the option to create a new database file
                        try {
                            //if this option is selected, show a popup screen to type in the name of the new database file and then close the window
                            Stage stage = popupScreen("FXML/createNewResearcherDatabase.fxml", addResearchersButton.getScene().getWindow());
                            stage.setOnHidden(windowEvent -> onTypeUpdate());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectResearcherFile.setValue(selectedValue);
                        break;
                    default: //set the default to showing the list of databases for researchers
                        listOfResearchersFromDatabase.clear();
                        HashMap<String, Researcher> listOfResearchers = ResearcherManager.getInstance().getResearchers();
                        for (Map.Entry<String, Researcher> entry : listOfResearchers.entrySet()) {
                            if (entry.getValue().getFilename().equals(newSelectedFile)) {
                                listOfResearchersFromDatabase.add(entry.getValue());
                            }
                        }
                }
            }
        });

        selectResearcherFile.getSelectionModel().selectFirst();
        researchersDBTable.setEditable(true); //make researchers database table editable
        researchersDBTable.setItems(listOfResearchersFromDatabase); //set the user inputted items in the table

        listOfResearchersFromDatabase.clear();
        refreshTableOfResearchers();
    }

    @FXML
    //make sure to update the researchers table
    private void refreshTableOfResearchers()
    {
        listOfResearchersFromDatabase.clear();
        HashMap<String, Researcher> listOfResearchers = ResearcherManager.getInstance().getResearchers();
        for (Map.Entry<String, Researcher> entry : listOfResearchers.entrySet()) {
            listOfResearchersFromDatabase.add(entry.getValue());
        }
    }

    @FXML
    //Add entries to the Researchers Database table view
    public void handleAddResearchersButton (ActionEvent e) throws IOException {
        popupScreen("FXML/addResearcherToDatabase.fxml", addResearchersButton.getScene().getWindow());
    }

    @FXML
    //Delete selected row in the table view for researchers database window and also in the csv database file
    public void handleDeleteButton (ActionEvent e) throws IOException {
        Researcher selectedItem = researchersDBTable.getSelectionModel().getSelectedItem();
        researchersDBTable.getItems().remove(selectedItem);
        try {
            Database.removeResearcher(selectedItem.getLongName());
            Database.writeResearchersToCSV("Libraries/researchers/defaultResearchers.csv");
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
    //update the dropdown menu for selecting which database file to load and use to store researchers
    public void onTypeUpdate() {
        ArrayList<String> researcherFiles = ResearcherManager.getInstance().getResearcherFiles();
        selectResearcherFile.getItems().clear();
        selectResearcherFile.getItems().add("All Databases");
        selectResearcherFile.getItems().addAll(researcherFiles);
        selectResearcherFile.getItems().add("+ Create new researcher database file");
        selectResearcherFile.getSelectionModel().selectFirst();
    }
}
