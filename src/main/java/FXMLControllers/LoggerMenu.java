package FXMLControllers;

import Utilities.Config;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static FXMLControllers.FullNamer.getLogEntryArrayList;

public class LoggerMenu extends ScreenController implements Initializable {

    @FXML
    private JFXTextField priorLogPath;

    @FXML
    private JFXButton logFileBrowser;

    @FXML
    private JFXTextField newLogFileName;

    @FXML
    private JFXButton closeLoggerButton;

    @FXML
    private JFXButton priorLogHelpButton;

    @FXML
    private JFXButton openLogButton;

    private Desktop desktop = Desktop.getDesktop();


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
    public void generateLog(ActionEvent e){
        try {
            XSSFWorkbook workbook;
            String fileLocation;
            if (priorLogPath.getText() != null && !priorLogPath.getText().trim().isEmpty()) {
                FileInputStream fis = new FileInputStream(new File(priorLogPath.getText()));
                workbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = workbook.getSheetAt(0);
                writeToWorkbook(workbook, sheet, false);
                fileLocation = priorLogPath.getText();
            } else {
                workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Sheet1");
                writeToWorkbook(workbook, sheet, true);
                File currDir = new File(".");
                String path = currDir.getAbsolutePath();
                String sheetName = newLogFileName.getText();
                if ((sheetName == null) || (sheetName.trim().isEmpty())) {
                    fileLocation = path.substring(0, path.length() - 1) + "/LogFiles/untitled.xlsx";
                } else {
                    fileLocation = path.substring(0, path.length() - 1) + "/LogFiles/" + sheetName + ".xlsx";
                }

            }
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            System.out.println(fileLocation);
            workbook.close();
            System.out.println("printed log file");
        }catch (IOException e1){
            System.out.println("ERROR MATE");
        }
    }

    private void writeToWorkbook(XSSFWorkbook workbook, Sheet sheet, boolean isNewWorkbook){
        int rowNumber;
        if(isNewWorkbook){
            rowNumber = 3;
        }
        else{
            rowNumber = sheet.getPhysicalNumberOfRows();
            System.out.println(rowNumber);
        }
        Config config = new Config();
        String projectName = config.getProperty("projectName");
        String projectDescription = config.getProperty("projectDescription");
        if (projectName != null && !projectName.trim().isEmpty()) {
            Row projectNameRow = sheet.createRow(0);
            Cell cell = projectNameRow.createCell(0);
            cell.setCellValue("Project Name: " + projectName);
        }
        if (projectDescription != null && !projectDescription.trim().isEmpty()) {
            Row projectNameRow = sheet.createRow( 1);
            Cell cell = projectNameRow.createCell(0);
            cell.setCellValue("Project Description: " + projectDescription);
        }
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BLACK1.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(font);

        Row tableHeaderRow = sheet.createRow( 2);
        Cell dateHeaderCell = tableHeaderRow.createCell(0, CellType.STRING);
        dateHeaderCell.setCellValue("DATE");
        dateHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(0,2000);
        Cell timeHeaderCell = tableHeaderRow.createCell(1, CellType.STRING);
        timeHeaderCell.setCellValue("TIME");
        timeHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(1,2000);
        Cell researcherNameHeaderCell = tableHeaderRow.createCell(2, CellType.STRING);
        researcherNameHeaderCell.setCellValue("RESEARCHER NAME");
        researcherNameHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(2,5500);
        Cell experimentTypeHeaderCell = tableHeaderRow.createCell(3, CellType.STRING);
        experimentTypeHeaderCell.setCellValue("EXPERIMENT TYPE");
        experimentTypeHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(3,5500);
        Cell trialNumberHeaderCell = tableHeaderRow.createCell(4, CellType.STRING);
        trialNumberHeaderCell.setCellValue("TRIAL NUMBER");
        trialNumberHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(4,4000);
        Cell sampleNumberHeaderCell = tableHeaderRow.createCell(5, CellType.STRING);
        sampleNumberHeaderCell.setCellValue("SAMPLE NUMBER");
        sampleNumberHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(5,4500);
        Cell fileNameHeaderCell = tableHeaderRow.createCell(6, CellType.STRING);
        fileNameHeaderCell.setCellValue("FILE NAME");
        fileNameHeaderCell.setCellStyle(headerStyle);
        sheet.setColumnWidth(6,3500);

        ArrayList<LogEntry> logEntryArrayList = getLogEntryArrayList();

        int i = rowNumber;
        for (LogEntry logEntry : logEntryArrayList) {
            Row newRow = sheet.createRow(i);
            Cell experimentDate = newRow.createCell(0);
            Cell experimentTime = newRow.createCell(1);
            Cell researcherName = newRow.createCell(2);
            Cell experimentType = newRow.createCell(3);
            Cell trialNumber = newRow.createCell(4);
            Cell sampleNumber = newRow.createCell(5);
            Cell fileName = newRow.createCell(6);
            Cell comment = newRow.createCell(7);

            experimentDate.setCellValue(logEntry.getExperimentDate());
            experimentTime.setCellValue(logEntry.getExperimentTime());
            researcherName.setCellValue(logEntry.getResearcherName());
            experimentType.setCellValue(logEntry.getExperimentType());
            trialNumber.setCellValue(logEntry.getTrialNumber());
            sampleNumber.setCellValue(logEntry.getSampleNumber());
            fileName.setCellValue(logEntry.getFileName());
            int j = 7;
            /*for(AutocompleteTextField autocompleteTextField : logEntry.getListOfKeywords())
            {
                Cell keyword = newRow.createCell(j);
                keyword.setCellValue(autocompleteTextField.get);
                j++;
            }*/
            comment.setCellValue("");
            i++;
        }
    }



   @FXML
    public void resetLogger(ActionEvent e){
       ArrayList<LogEntry> logEntryArrayList = getLogEntryArrayList();
        logEntryArrayList.clear();
    }

    @FXML
    public void openLog(ActionEvent e){
       File file = new File(priorLogPath.getText());
       openFile(file);

    }





}

