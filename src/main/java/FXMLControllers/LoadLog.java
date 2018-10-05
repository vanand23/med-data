package FXMLControllers;

import Types.LogEntry;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static FXMLControllers.FullNamer.addAllToLogEntryList;
import static FXMLControllers.FullNamer.getLogEntryList;

public class LoadLog implements Initializable {
    @FXML
    private TextField loadedLogPath;

    @FXML
    private JFXButton priorLogHelpButton;

    @FXML
    private JFXButton openLogButton;

    @FXML
    private JFXButton logFileBrowser;

    @FXML
    private JFXButton closeLoggerButton;

    private Desktop desktop = Desktop.getDesktop();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final JFXButton priorLogHelp = priorLogHelpButton;
        final Tooltip priorLogTooltip = new Tooltip();
        priorLogTooltip.setText("Click on the browse button to find a previous log file.");
        priorLogHelp.setTooltip(priorLogTooltip);

        loadedLogPath.textProperty().addListener((obs,oldLogFilePath,newLogFilePath) -> {
            if(loadedLogPath!= null && !loadedLogPath.getText().trim().isEmpty()){
                openLogButton.setDisable(false);
            }
            else{
                openLogButton.setDisable(true);
            }
        });
    }

    private void parseFile(File file) throws IOException
    {
        try{
            Workbook workbook = WorkbookFactory.create(new File(loadedLogPath.getText()));
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            Iterator<Row> rowIterator = sheet.rowIterator();
            for(int j = 0; j < 3; j++)
            {
                if(rowIterator.hasNext())
                {
                    rowIterator.next();
                }
            }//skip to the third row

            ObservableList<LogEntry> tmpList = FXCollections.observableArrayList();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                LogEntry logEntry = new LogEntry();
                Iterator<Cell> cellIterator = row.cellIterator();
                for(int i = 0; i < 8; i++){
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                    switch (i){
                        case 0:
                            logEntry.setExperimentTime(cellValue);
                            break;
                        case 1:
                            logEntry.setExperimentDate(cellValue);
                            break;
                        case 2:
                            logEntry.setResearcherName(cellValue);
                            break;
                        case 3:
                            logEntry.setExperimentName(cellValue);
                            break;
                        case 4:
                            logEntry.setTrialNumber(cellValue);
                            break;
                        case 5:
                            logEntry.setSampleNumber(cellValue);
                            break;
                        case 6:
                            logEntry.setFilename(cellValue);
                            break;
                        case 7:
                            logEntry.setComment(cellValue);
                            break;
                    }
                }
                tmpList.add(logEntry);
            }
            addAllToLogEntryList(tmpList);
        }catch (InvalidFormatException e1){
            e1.printStackTrace();
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
    public void openLog(ActionEvent e) throws IOException{
        File file = new File(loadedLogPath.getText());
        parseFile(file);
        handleClose(e);
    }

    @FXML
    private void browseLogs(ActionEvent e){
        final FileChooser fileChooser = new FileChooser();
        File currentDirectory = new File(".");
        fileChooser.setInitialDirectory(currentDirectory);
        File file = fileChooser.showOpenDialog(logFileBrowser.getScene().getWindow());
        if (file != null) {
            loadedLogPath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleClose(ActionEvent e) {
        Stage primaryStage = (Stage) closeLoggerButton.getScene().getWindow();
        primaryStage.close();
    }
}
