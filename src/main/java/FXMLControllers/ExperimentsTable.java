package FXMLControllers;

import Singletons.Database;
import Types.Experiment;
import Singletons.ExperimentManager;
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

public class ExperimentsTable extends ScreenController implements Initializable, ITypeObserver {

    //The following are for the Experiment Database table:
    @FXML
    private TableView<Experiment> experimentTableView; //the overall table for experiments

    @FXML
    private TableColumn<Experiment, String> experimentName; //column to store the experiment name

    @FXML
    private TableColumn<Experiment, String> experimentAbbrev; //column to store the experiment abbreviation

    @FXML
    private TableColumn<Experiment, String> experimentDescription; //column to store the experiment description
    
    @FXML
    private ChoiceBox<String> selectExperimentFile; //choose what database file to use to store the above parameters

    //a list to store and add all the parameters the user enters in the table
    private final static ObservableList<Experiment> experimentTypeObservableList = FXCollections.observableArrayList();

    //getter for the list above
    static ObservableList<Experiment> getExperimentTypeObservableList() {
        return experimentTypeObservableList;
    }

    //get the value the user selected in the table
    private static String selectedValue = "";

    //setter for the string above
    static void setSelectedValue(String selectedValue) {
        ExperimentsTable.selectedValue = selectedValue;
    }

    @FXML
    private JFXButton cancelButton; //basic functionality button

    @Override
    //initialize the table with the columns and any previously entered data
    public void initialize(URL location, ResourceBundle resources) {
        //set the column name to populate the corresponding column with the input data the user provided
        experimentName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        experimentAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        experimentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        //update the database dropdown list
        ExperimentManager.getInstance().subscribe(this);
        onTypeUpdate();
        //selecting which experiment database file to use to load the newly created experiments
        selectExperimentFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            HashMap<String, Experiment> listOfExperiments = ExperimentManager.getInstance().getExperiments();
            if(newSelectedFile != null) //if the user chooses to add a new file to the list of csv databases
            {
                switch (newSelectedFile)
                {
                    case "All Databases": //show all the available databases
                        experimentTypeObservableList.clear();
                        for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
                            experimentTypeObservableList.add(entry.getValue());
                        }
                        break;
                    case "+ Create new experiment database file": //show the option to create a new database file
                        try{
                            //if this option is selected, show a popup screen to type in the name of the new database file and then close the window
                            Stage stage = popupScreen("FXML/createNewExperimentDatabase.fxml", cancelButton.getScene().getWindow());
                            stage.setOnHidden(windowEvent -> onTypeUpdate());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        selectExperimentFile.setValue(selectedValue);
                        break;
                    default: //set the default to showing the list of databases for experiments
                        experimentTypeObservableList.clear();
                        for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
                            if(entry.getValue().getFilename().equals(newSelectedFile))
                            {
                                experimentTypeObservableList.add(entry.getValue());
                            }
                        }
                }

            }
        });

        selectExperimentFile.getSelectionModel().selectFirst();
        experimentTableView.setEditable(true); //make experiment database table editable
        experimentTableView.setItems(experimentTypeObservableList); //set the user inputted items in the table

        experimentTypeObservableList.clear();
        refreshTableOfExperiments();

    }

    @FXML
    //make sure to update the experiment table
    private void refreshTableOfExperiments()
    {
        experimentTypeObservableList.clear();

        HashMap<String, Experiment> listOfExperiments = ExperimentManager.getInstance().getExperiments();
        for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
            experimentTypeObservableList.add(entry.getValue()); //add the user inputted value into the table
        }
    }

    //Add entries to the Experiments Database table view
    @FXML
    public void handleAddExperiment (ActionEvent e) throws IOException {

        popupScreen("FXML/addExperimentToDatabase.fxml", cancelButton.getScene().getWindow());

    }

    @FXML
    //Delete selected row in the table view for experiments database window and also in the csv database file
    public void handleDeleteButton (ActionEvent e) throws IOException {

        Experiment selectedItem = experimentTableView.getSelectionModel().getSelectedItem();
        experimentTableView.getItems().remove(selectedItem);
        try {
            Database.removeExperiment(selectedItem.getLongName());
            Database.writeExperimentsToCSV("Libraries/experiments/defaultExperiments.csv");
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
    //update the dropdown menu for selecting which database file to load and use to store experiments
    public void onTypeUpdate() {
        ArrayList<String> experimentFiles = ExperimentManager.getInstance().getExperimentFiles();
        selectExperimentFile.getItems().clear();
        selectExperimentFile.getItems().add("All Databases");
        selectExperimentFile.getItems().addAll(experimentFiles);
        selectExperimentFile.getItems().add("+ Create new experiment database file");
        selectExperimentFile.getSelectionModel().selectFirst();
    }

}
