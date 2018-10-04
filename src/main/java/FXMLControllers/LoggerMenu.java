package FXMLControllers;

import Types.Keyword;
import Types.LogEntry;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static FXMLControllers.FullNamer.getLogEntryArrayList;

public class LoggerMenu extends ScreenController implements Initializable {

    @FXML
    private TableView<LogEntry> logTable;

    @FXML
    private TableColumn timeColumn;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn researcherColumn;

    @FXML
    private TableColumn trialNumberColumn;

    @FXML
    private TableColumn sampleNumberColumn;

    @FXML
    private TableColumn filenameColumn;

    @FXML
    private TableColumn commentColumn;

    @FXML
    private TextField priorLogPath;

    @FXML
    private JFXButton logFileBrowser;

    @FXML
    private TextField newLogFileName;

    @FXML
    private JFXButton closeLoggerButton;

    @FXML
    private JFXButton priorLogHelpButton;

    @FXML
    private JFXButton openLogButton;

    private Desktop desktop = Desktop.getDesktop();

    ArrayList<Keyword> allKeywords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final JFXButton priorLogHelp = priorLogHelpButton;
        final Tooltip priorLogTooltip = new Tooltip();
        priorLogTooltip.setText("Click on the browse button to find a previous log file.");
        priorLogHelp.setTooltip(priorLogTooltip);

        priorLogPath.textProperty().addListener((obs,oldLogFilePath,newLogFilePath) -> {
            if(priorLogPath!= null && !priorLogPath.getText().trim().isEmpty()){
                openLogButton.setDisable(false);
            }
            else{
                openLogButton.setDisable(true);
            }
        });
        ObservableList<LogEntry> logEntryArrayList = getLogEntryArrayList();

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("experimentTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("experimentDate"));
        researcherColumn.setCellValueFactory(new PropertyValueFactory<>("researcher"));
        trialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trialNumber"));
        sampleNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sampleNumber"));
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        logTable.setEditable(true);

        timeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        timeColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setExperimentTime((String) event.getNewValue());
                    }
                }
        );

        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setExperimentDate((String) event.getNewValue());
                    }
                }
        );

        researcherColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        researcherColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setResearcherName((String) event.getNewValue());
                    }
                }
        );

        trialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        trialNumberColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setTrialNumber((String) event.getNewValue());
                    }
                }
        );

        sampleNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sampleNumberColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setSampleNumber((String) event.getNewValue());
                    }
                }
        );

        filenameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        filenameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setFilename((String) event.getNewValue());
                    }
                }
        );

        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setComment((String) event.getNewValue());
                    }
                }
        );

        logTable.setItems(logEntryArrayList);
    }

    @FXML
    private void browseLogs(ActionEvent e){
        final FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog((Stage)logFileBrowser.getScene().getWindow());
        if (file != null) {
            //  openFile(file);
            priorLogPath.setText(file.getAbsolutePath());
        }
    }


    private void openFile (File file){
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    LoggerMenu.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    @FXML
    public void closeLogger(ActionEvent e) {
        Stage primaryStage = (Stage) closeLoggerButton.getScene().getWindow();
        primaryStage.close();
    }

   @FXML
    public void resetLogger(ActionEvent e){
       ObservableList<LogEntry> logEntryArrayList = getLogEntryArrayList();
        logEntryArrayList.clear();
    }

    @FXML
    public void openLog(ActionEvent e){
       File file = new File(priorLogPath.getText());
       openFile(file);
    }

    @FXML
    public void handleWriteToNewLogButton(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/logFileWriter.fxml", closeLoggerButton.getScene().getWindow());
    }
}

