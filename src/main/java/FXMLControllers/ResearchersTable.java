package FXMLControllers;

import Singletons.Database;
import Types.ResearcherManager;
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

    @FXML
    public ChoiceBox<String> selectResearcherFile;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addResearchersButton;

    @FXML
    private TableView<Researcher> researchersDBTable;

    @FXML
    private TableColumn<Researcher, String> researcherName;

    @FXML
    private TableColumn<Researcher, String> researcherAbbrev;

    private final static ObservableList<Researcher> listOfResearchersFromDatabase = FXCollections.observableArrayList();

    private static String selectedValue = "";

    static void setSelectedValue(String selectedValue) {
        ResearchersTable.selectedValue = selectedValue;
    }

    static ObservableList<Researcher> getListOfResearchersFromDatabase() {
        return listOfResearchersFromDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        researcherName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        researcherAbbrev.setCellValueFactory(new PropertyValueFactory<>("shortName"));

        ResearcherManager.getInstance().subscribe(this);
        onTypeUpdate();
        selectResearcherFile.valueProperty().addListener((obs, oldSelectedFile, newSelectedFile) -> {
            if (newSelectedFile != null) {
                switch (newSelectedFile) {
                    case "All Databases":
                        refreshTableOfResearchers();
                        break;
                    case "+ Create new researcher database file":
                        try {
                            Stage stage = popupScreen("FXML/createNewResearcherDatabase.fxml", addResearchersButton.getScene().getWindow(), "Create new researcher database");
                            stage.setOnHidden(windowEvent -> onTypeUpdate());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectResearcherFile.setValue(selectedValue);
                        break;
                    default:
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
        researchersDBTable.setEditable(true);
        researchersDBTable.setItems(listOfResearchersFromDatabase);

        listOfResearchersFromDatabase.clear();
        refreshTableOfResearchers();
    }

    @FXML
    private void refreshTableOfResearchers()
    {
        listOfResearchersFromDatabase.clear();
        HashMap<String, Researcher> listOfResearchers = ResearcherManager.getInstance().getResearchers();
        for (Map.Entry<String, Researcher> entry : listOfResearchers.entrySet()) {
            listOfResearchersFromDatabase.add(entry.getValue());
        }
    }

    @FXML
    public void handleAddResearchersButton (ActionEvent e) throws IOException {
        popupScreen("FXML/addResearcherToDatabase.fxml", addResearchersButton.getScene().getWindow(),"Add Researcher DB Menu");
    }

    @FXML
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
    public void handleCancelButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
        primaryStage.close();
    }

    @Override
    public void onTypeUpdate() {
        ArrayList<String> researcherFiles = ResearcherManager.getInstance().getResearcherFiles();
        selectResearcherFile.getItems().clear();
        selectResearcherFile.getItems().add("All Databases");
        selectResearcherFile.getItems().addAll(researcherFiles);
        selectResearcherFile.getItems().add("+ Create new researcher database file");
        selectResearcherFile.getSelectionModel().selectFirst();
    }
}
