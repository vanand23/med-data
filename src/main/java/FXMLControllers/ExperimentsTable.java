package FXMLControllers;

import Singletons.Database;
import Types.Experiment;
import Types.ExperimentManager;
import Types.Keyword;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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

import static Singletons.Database.getExperimentFiles;

public class ExperimentsTable extends ScreenController implements Initializable {

    @FXML
    private JFXButton cancelButton;

    @FXML
    private TableView<Experiment> experimentTableView;

    @FXML
    private TableColumn experimentName;

    @FXML
    private TableColumn experimentAbbrev;

    @FXML
    private TableColumn experimentDescription;
    
    @FXML
    private ComboBox<String> selectExperimentFile;

    private final static ObservableList<Experiment> experimentTypeObservableList = FXCollections.observableArrayList();

    static ObservableList<Experiment> getExperimentTypeObservableList() {
        return experimentTypeObservableList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        experimentName.setCellValueFactory(new PropertyValueFactory<Experiment, String>("longName"));
        experimentAbbrev.setCellValueFactory(new PropertyValueFactory<Experiment, String>("shortName"));
        experimentDescription.setCellValueFactory(new PropertyValueFactory<Experiment, String>("description"));

        selectExperimentFile.getItems().add("All Databases");
        selectExperimentFile.getSelectionModel().selectFirst();
        selectExperimentFile.getItems().addAll(getExperimentFiles());
        selectExperimentFile.getItems().add("+ Add new keyword database file");
        selectExperimentFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            HashMap<String, Experiment> listOfExperiments = ExperimentManager.getInstance().getExperiments();
            switch (newSelectedFile)
            {
                case "All Databases":
                    experimentTypeObservableList.clear();
                    for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
                        experimentTypeObservableList.add(entry.getValue());
                    }
                    break;
                case "+ Add new experiment database file":
                    //TODO popup for adding a new database file(include explorer option? create a whole new file?)
                    selectExperimentFile.getSelectionModel().selectFirst();
                    break;
                default:
                    experimentTypeObservableList.clear();
                    for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
                        if(entry.getValue().getFilename().equals(newSelectedFile))
                        {
                            experimentTypeObservableList.add(entry.getValue());
                        }
                    }
            }
        });

        selectExperimentFile.getSelectionModel().selectFirst();


        experimentTableView.setEditable(true);
        HashMap<String, Experiment> listOfExperiments = ExperimentManager.getInstance().getExperiments();
        for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
            experimentTypeObservableList.add(entry.getValue());
        }
        experimentTableView.setItems(experimentTypeObservableList);


    }

    @FXML
    public void handleAddExperiment (ActionEvent e) throws IOException {

        popupScreen("FXML/addExperimentToDatabase.fxml", cancelButton.getScene().getWindow(),"Add Experiment");

    }

    @FXML
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
    public void handleCancelButton (ActionEvent e) throws IOException {

        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();

    }

}
