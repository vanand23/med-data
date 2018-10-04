package FXMLControllers;

import Types.Keyword;
import Types.LogEntry;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<LogEntry, String>  timeColumn;

    @FXML
    private TableColumn<LogEntry, String>  dateColumn;

    @FXML
    private TableColumn<LogEntry, String> researcherColumn;

    @FXML
    private TableColumn<LogEntry, String> trialNumberColumn;

    @FXML
    private TableColumn<LogEntry, String> sampleNumberColumn;

    @FXML
    private TableColumn<LogEntry, String> filenameColumn;

    @FXML
    private TableColumn<LogEntry, String> commentColumn;

    @FXML
    private TextField newLogFileName;

    @FXML
    private JFXButton closeLoggerButton;

    ArrayList<Keyword> allKeywords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<LogEntry> logEntryArrayList = getLogEntryArrayList();

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("experimentTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("experimentDate"));
        researcherColumn.setCellValueFactory(new PropertyValueFactory<>("researcher"));
        trialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trialNumber"));
        sampleNumberColumn.setCellValueFactory(new PropertyValueFactory<>("sampleNumber"));
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        logTable.setEditable(true);
        logTable.setItems(logEntryArrayList);
    }


    @FXML
    public void handleClose(ActionEvent e) {
        Stage primaryStage = (Stage) closeLoggerButton.getScene().getWindow();
        primaryStage.close();
    }

   @FXML
    public void resetLogger(ActionEvent e){
       ObservableList<LogEntry> logEntryArrayList = getLogEntryArrayList();
        logEntryArrayList.clear();
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

