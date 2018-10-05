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

    @FXML
    private JFXButton cancelButton;

    @FXML
    private TableView<Experiment> experimentTableView;

    @FXML
    private TableColumn<Experiment, String> experimentName;

    @FXML
    private TableColumn<Experiment, String> experimentAbbrev;

    @FXML
    private TableColumn<Experiment, String> experimentDescription;
    
    @FXML
    private ChoiceBox<String> selectExperimentFile;

    private final static ObservableList<Experiment> experimentTypeObservableList = FXCollections.observableArrayList();

    static ObservableList<Experiment> getExperimentTypeObservableList() {
        return experimentTypeObservableList;
    }

    private static String selectedValue = "";

    static void setSelectedValue(String selectedValue) {
        ExperimentsTable.selectedValue = selectedValue;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        experimentName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        experimentAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        experimentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        ExperimentManager.getInstance().subscribe(this);
        onTypeUpdate();
        selectExperimentFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            HashMap<String, Experiment> listOfExperiments = ExperimentManager.getInstance().getExperiments();
            if(newSelectedFile != null)
            {
                switch (newSelectedFile)
                {
                    case "All Databases":
                        experimentTypeObservableList.clear();
                        for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
                            experimentTypeObservableList.add(entry.getValue());
                        }
                        break;
                    case "+ Create new experiment database file":
                        try{
                            Stage stage = popupScreen("FXML/createNewExperimentDatabase.fxml", cancelButton.getScene().getWindow());
                            stage.setOnHidden(windowEvent -> onTypeUpdate());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        selectExperimentFile.setValue(selectedValue);
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

            }
        });

        selectExperimentFile.getSelectionModel().selectFirst();
        experimentTableView.setEditable(true);
        experimentTypeObservableList.clear();
        HashMap<String, Experiment> listOfExperiments = ExperimentManager.getInstance().getExperiments();
        for(Map.Entry<String, Experiment> entry : listOfExperiments.entrySet()) {
            experimentTypeObservableList.add(entry.getValue());
        }
        experimentTableView.setItems(experimentTypeObservableList);


    }

    @FXML
    public void handleAddExperiment (ActionEvent e) throws IOException {

        popupScreen("FXML/addExperimentToDatabase.fxml", cancelButton.getScene().getWindow());

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
    @Override
    public void onTypeUpdate() {
        ArrayList<String> experimentFiles = ExperimentManager.getInstance().getExperimentFiles();
        selectExperimentFile.getItems().clear();
        selectExperimentFile.getItems().add("All Databases");
        selectExperimentFile.getItems().addAll(experimentFiles);
        selectExperimentFile.getItems().add("+ Create new experiment database file");
        selectExperimentFile.getSelectionModel().selectFirst();
    }

}
