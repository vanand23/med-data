package FXMLControllers;

import Singletons.FXMLManager;
import Types.ExperimentManager;
import Types.KeywordManager;
import Types.KeywordType;
import Utilities.AutocompleteTextField;
import Utilities.Config;
import Utilities.ITypeObserver;
import Utilities.KeywordAutocompleteTextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.naming.NameNotFoundException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import sun.rmi.runtime.Log;

import static Utilities.Config.setProperty;
import static javafx.scene.layout.HBox.setHgrow;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class FullNamer extends Namer implements Initializable, ITypeObserver {
    @FXML
    private DatePicker experimentDate;

    @FXML
    private JFXTextField researcherName;

    @FXML
    private JFXTextField trialNumber;


    @FXML
    private JFXTextField sampleNumber;

    @FXML
    private JFXTextField outputText;

    @FXML
    JFXButton updateNameButton;

    @FXML
    private AutocompleteTextField experimentType;

    @FXML
    private JFXButton backButton;

    @FXML
    private VBox vboxOfKeywords;

    @FXML
    private JFXButton addKeywordButton;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton projectPreferencesButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private AnchorPane anchorPaneOfKeywords;

    @FXML
    static TreeTableView tableOfKeywords;

    @FXML
    static TreeTableColumn<KeywordType, String> dataValueColumn;

    @FXML
    static TreeTableColumn<KeywordType, String> nameColumn;

    @FXML
    private JFXButton helpButtonInput;

    @FXML
    private JFXButton helpButtonOutput;

    @FXML
    private TableView<Keywords> keywordsTable;

    @FXML
    private TableColumn columnName;

    @FXML
    private TableColumn columnDataValue;


    private Image removeObjectIcon = new Image("Images/closeIcon.png",30,30,true,true); //pass in the image path

    private static ArrayList<KeywordAutocompleteTextField> sharedListOfKeywords = new ArrayList<>();

    private final static ObservableList<Keywords> data = FXCollections.observableArrayList();

    private static ArrayList<LogEntry> logEntryArrayList = new ArrayList<>();

    public static ObservableList<Keywords> getData() {
        return data;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            final JFXButton inputHelp = helpButtonInput;
            final Tooltip inputTooltip = new Tooltip();
            inputTooltip.setText("Fill in the fields with your desired parameters");
            inputHelp.setTooltip(inputTooltip);

            final JFXButton outputHelp = helpButtonOutput;
            final Tooltip outputTooltip = new Tooltip();
            outputTooltip.setText("The output format is: YYYY_MM_DD_ExperimentAbbreviation_ResearchInitials_TrialNumber_SampleNumber_KeywordAbbreviations");
            outputHelp.setTooltip(outputTooltip);

            ExperimentManager.getInstance().subscribe(this);
            KeywordManager.getInstance().subscribe(this);
            outputText.setEditable(false);
            String pattern = "dd/MM/yyyy";
            experimentDate.setPromptText(pattern.toLowerCase());
            experimentDate.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });
            Config config = new Config();
            String configResearcherName = config.getProperty("researcherName");
            if(configResearcherName != null && !configResearcherName.trim().isEmpty())
            {
                researcherName.setText(configResearcherName);
            }
            String configExperimentType = config.getProperty("experimentType");
            if(configExperimentType != null && !configExperimentType.trim().isEmpty())
            {
                experimentType.setText(configExperimentType);
            }
            String configTrialNumber = config.getProperty("trialNumber");
            if(configTrialNumber != null && !configTrialNumber.trim().isEmpty())
            {
                trialNumber.setText(configTrialNumber);
            }
            String configSampleNumber = config.getProperty("sampleNumber");
            if(configSampleNumber != null && !configSampleNumber.trim().isEmpty())
            {
                sampleNumber.setText(configSampleNumber);
            }

            experimentDate.setValue(LocalDate.now());
            trialNumber.setText("0");
            sampleNumber.setText("0");
            experimentType.setAutocompleteWidth(350);
            columnName.setMinWidth(100);
            columnDataValue.setMinWidth(100);


            columnName.setCellValueFactory(new PropertyValueFactory<Keywords, String>("KeywordName"));
            columnDataValue.setCellValueFactory(new PropertyValueFactory<Keywords, String>("DataValue"));

            keywordsTable.setEditable(true);

            columnName.setCellFactory(TextFieldTableCell.forTableColumn());
            columnName.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent event) {

                            ((Keywords) event.getTableView().getItems().get(
                                    event.getTablePosition().getRow())
                            ).setKeywordName((String) event.getNewValue());

                        }
                    }
            );

            columnDataValue.setCellFactory(TextFieldTableCell.forTableColumn());
            columnDataValue.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent event) {

                            ((Keywords) event.getTableView().getItems().get(
                                    event.getTablePosition().getRow())
                            ).setDataValue((String) event.getNewValue());

                        }
                    }
            );

            keywordsTable.setItems(this.data);

            experimentDate.valueProperty().addListener((obs, oldDate, newDate) -> {
                        outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
            });
            experimentType.textProperty().addListener((obs, oldExperimentType, newExperimentType) -> {
                if(experimentType.isValidText())
                {
                    outputText.setText(updateName(
                            experimentType.getText(),
                            trialNumber.getText(),
                            sampleNumber.getText(),
                            researcherName.getText(),
                            experimentDate.getValue(),
                            sharedListOfKeywords));
                    setProperty("experimentType",newExperimentType);
                    System.out.println("updated!");
                }
            });
            researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                setProperty("researcherName",newResearcherName);
            });
            trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                setProperty("trialNumber",newTrialNumber);
            });
            sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) -> {
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                setProperty("sampleNumber",newSampleNumber);
            });

            outputText.setText(updateName(
                    experimentType.getText(),
                    trialNumber.getText(),
                    sampleNumber.getText(),
                    researcherName.getText(),
                    experimentDate.getValue(),
                    sharedListOfKeywords));
        }
    }
/*
    @FXML
    public void addKeyword(ActionEvent e) throws IOException{
        VBox tempList = new VBox();
        tempList.setVisible(false);
        FXMLManager fxmlManager = FXMLManager.getInstance();
        //fxmlManager.setSearchDirectory(System.getProperty("user.dir") + "/src/main/resources/");

        JFXButton removeObjectButton = new JFXButton("", new ImageView(removeObjectIcon));
        removeObjectButton.setPrefSize(15,15);
        removeObjectButton.setPadding(new Insets(0,0,0,0));
        removeObjectButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(39);
        hbox.getChildren().add(removeObjectButton);
        KeywordAutocompleteTextField textField = new KeywordAutocompleteTextField(hbox);
        textField.setPrefWidth(Region.USE_COMPUTED_SIZE);
        textField.setPromptText("Choose keyword");
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.setLabelFloat(false);
        textField.setUnFocusColor(Paint.valueOf("#000000"));
        textField.setFont(new Font("Times New Roman", 20));

        hbox.getChildren().add(textField);
        JFXButton submitKeywordButton = new JFXButton("SUBMIT");
        submitKeywordButton.getStylesheets().add("/CSS/smallButtons.css");
        submitKeywordButton.setFont(new Font("Arial Black", 14));
        submitKeywordButton.setMinWidth(70);
        submitKeywordButton.setPrefWidth(USE_COMPUTED_SIZE);
        submitKeywordButton.setOnAction(actionEvent1 -> {
            outputText.setText(updateName(
                    experimentType.getText(),
                    trialNumber.getText(),
                    sampleNumber.getText(),
                    researcherName.getText(),
                    experimentDate.getValue(),
                    sharedListOfKeywords));
            if(textField.getState() == 1)
            {
                HBox heeb = new HBox();
                heeb.setSpacing(10);
                heeb.setAlignment(Pos.CENTER_LEFT);
                JFXButton removeLabelButton = new JFXButton("", new ImageView(removeObjectIcon));
                removeLabelButton.setPrefSize(15,15);
                removeLabelButton.setPadding(new Insets(0,0,0,0));
                removeLabelButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
                removeLabelButton.setOnAction(e2 -> {
                    sharedListOfKeywords.remove(textField);
                    System.out.println(sharedListOfKeywords);
                    vboxOfKeywords.getChildren().remove(heeb);
                    outputText.setText(updateName(
                            experimentType.getText(),
                            trialNumber.getText(),
                            sampleNumber.getText(),
                            researcherName.getText(),
                            experimentDate.getValue(),
                            sharedListOfKeywords));
                });
                heeb.getChildren().add(removeLabelButton);
                outputText.setText(updateName(
                        experimentType.getText(),
                        trialNumber.getText(),
                        sampleNumber.getText(),
                        researcherName.getText(),
                        experimentDate.getValue(),
                        sharedListOfKeywords));
                tempList.getChildren().add(hbox);
            }
        });
        hbox.getChildren().add(submitKeywordButton);

        setHgrow(textField, Priority.ALWAYS);
        sharedListOfKeywords.add(textField);

        removeObjectButton.setOnAction(e1 -> {
            sharedListOfKeywords.remove(textField);
        });
        onTypeUpdate();
    }
    */

    @FXML
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String stringExperimentType = experimentType.getText();
        String stringTrialNumber = trialNumber.getText();
        String stringSampleNumber = sampleNumber.getText();
        String stringResearcherName = researcherName.getText();
        LocalDate stringExperimentDate = experimentDate.getValue();
        String comment = "";
        String nameToCopy = updateName(
                stringExperimentType,
                stringTrialNumber,
                stringSampleNumber,
                stringResearcherName,
                stringExperimentDate,
                sharedListOfKeywords);

        setProperty("experimentType",experimentType.getText());

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        outputText.setText(nameToCopy);
        logEntryArrayList.add(new LogEntry(
                stringExperimentDate,
                stringResearcherName,
                stringExperimentType,
                stringTrialNumber,
                stringSampleNumber,
                nameToCopy,
                sharedListOfKeywords,
                comment
                ));
    }

    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        popupScreen("FXML/compactNamer.fxml", switchNamers.getScene().getWindow(),"Compact Namer");
    }

    @FXML
    public void handlePreferences(ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        Stage popup = popupScreen("FXML/myProjectPreferences.fxml", projectPreferencesButton.getScene().getWindow(),
                        "Project Preferences");
    }

    @FXML
    public void handleAddButton (ActionEvent e) throws IOException {
        popupScreen("FXML/addKeywordsUI.fxml", addKeywordButton.getScene().getWindow(),"Add Keywords Menu");
    }

    @Override
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentType.getEntries().addAll(experiments);

        ArrayList<String> keywords = (ArrayList<String>) KeywordManager.getInstance().getAllKeywordLongNames();
        for(AutocompleteTextField autocompleteTextField : sharedListOfKeywords){
            autocompleteTextField.getEntries().addAll(keywords);
        }
    }
    static ArrayList<KeywordAutocompleteTextField> getSharedListOfKeywords() {
        return sharedListOfKeywords;
    }

    @FXML
    public void closeFullNamer(ActionEvent e) {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    public void generateLog(ActionEvent e){
        try {
            //new File("testFile.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("testLog");
            Config config = new Config();
            String projectName = config.getProperty("projectName");
            String projectDescription = config.getProperty("projectDescription");
            if (projectName != null && !projectName.trim().isEmpty()) {
                Row projectNameRow = sheet.createRow(0);
                Cell cell = projectNameRow.createCell(0);
                cell.setCellValue("Project Name: " + projectName);
            }
            if (projectDescription != null && !projectDescription.trim().isEmpty()) {
                Row projectNameRow = sheet.createRow(1);
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

            Row tableHeaderRow = sheet.createRow(2);
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


            int i = 3;
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
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("printed log file");
        }catch (IOException e1){
            System.out.println("ERROR MATE");
        }
    }

    public static TreeTableView getTableOfKeywords() {
        return tableOfKeywords;
    }
}