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

import static FXMLControllers.FullNamer.getLogEntryList;
import static FXMLControllers.FullNamer.setLogEntryList;
import static FXMLControllers.Namer.sharedFilename;

public class LoggerMenu extends ScreenController implements Initializable {

    @FXML
    private TableView<LogEntry> logTable;

    @FXML
    private TableColumn timeColumn;

    @FXML
    private TableColumn dateColumn;

    @FXML
    public TableColumn experimentColumn;

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
    private TextField newLogFileName;

    @FXML
    private JFXButton closeLoggerButton;

    ArrayList<Keyword> allKeywords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<LogEntry> logEntryList = getLogEntryList();

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("experimentTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("experimentDate"));
        experimentColumn.setCellValueFactory(new PropertyValueFactory<>("experimentName"));
        researcherColumn.setCellValueFactory(new PropertyValueFactory<>("researcherName"));
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
                        setLogEntryList(logEntryList);
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
                        setLogEntryList(logEntryList);
                    }
                }
        );

        experimentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        experimentColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        ((LogEntry) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setExperimentName((String) event.getNewValue());
                        setLogEntryList(logEntryList);
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
                        setLogEntryList(logEntryList);
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
                        setLogEntryList(logEntryList);
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
                        setLogEntryList(logEntryList);
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
                        setLogEntryList(logEntryList);
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
                        setLogEntryList(logEntryList);
                    }
                }
        );

        logTable.setItems(logEntryList);
    }


    @FXML
    public void handleClose(ActionEvent e) {
        Stage primaryStage = (Stage) closeLoggerButton.getScene().getWindow();
        primaryStage.close();
    }

   @FXML
    public void resetLogger(ActionEvent e){
       ObservableList<LogEntry> logEntryList = getLogEntryList();
        logEntryList.clear();
    }


    @FXML
    public void handleWriteToNewLogButton(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/logFileWriter.fxml", closeLoggerButton.getScene().getWindow());
    }

    @FXML
    private void handleLoadLog(ActionEvent actionEvent) throws IOException{
        popupScreen("FXML/loadLog.fxml", closeLoggerButton.getScene().getWindow());
    }
}

