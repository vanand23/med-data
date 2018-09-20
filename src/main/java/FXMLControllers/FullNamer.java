package FXMLControllers;

import Singletons.FXMLManager;
import Types.ExperimentManager;
import Types.KeywordManager;
import Utilities.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import javax.naming.NameNotFoundException;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;

public class FullNamer extends ScreenController implements Initializable, ITypeObserver {
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
    private JFXButton addButton;

    @FXML
    private VBox vboxOfKeywords;

    @FXML
    private JFXButton addKeywordButton;

    @FXML
    private JFXToggleButton switchNamers;

    @FXML
    private JFXButton helpButton;

    @FXML
    private TableView<Keywords> keywordsTable;

    @FXML
    private TableColumn columnName;

    @FXML
    private TableColumn columnDataValue;


    private Image removeObjectIcon = new Image("Images/closeIcon.png",30,30,true,true); //pass in the image path
    private int numKeywords;
    private ArrayList<KeywordAutocompleteTextField> listofkeywords = new ArrayList<>();

    private ArrayList<String> keywords;

    private final static ObservableList<Keywords> data = FXCollections.observableArrayList();

    public static ObservableList<Keywords> getData() {
        return data;
    }


    /*public static void setData(ObservableList<Keywords> data) {
        FullNamer.data = data;
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            numKeywords = 0;
            ExperimentManager.getInstance().subscribe(this);
            KeywordManager.getInstance().subscribe(this);
            String pattern = "dd-MM-yyyy";
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
                outputText.setText(updateName());
            });
            experimentType.textProperty().addListener((obs, oldExperimentType, newExperimentType) -> {
                outputText.setText(updateName());
            });

            trialNumber.textProperty().addListener((obs, oldTrialNumber, newTrialNumber) -> {
                outputText.setText(updateName());
            });

            sampleNumber.textProperty().addListener((obs, oldSampleNumber, newSampleNumber) -> {
                outputText.setText(updateName());
            });

            researcherName.textProperty().addListener((obs, oldResearcherName, newResearcherName) -> {
                outputText.setText(updateName());
            });
            updateName();
        }
    }


    /**
     * Show stairs toggled
     */
    private String updateName() {
        String delimiter = ProjectPreferences.getInstance().getDelimiter();
        if(delimiter == null){
            delimiter = "_";
        }

        StringBuilder fname = new StringBuilder();
        String experimentTypeText = experimentType.getText();
        String trialNumberText = trialNumber.getText();
        String researcherNameText = researcherName.getText();
        String sampleNumberText = sampleNumber.getText();

        //int numParams = 3;

        if(experimentDate.getValue() != null)
        {
            fname.append(experimentDate.getValue().toString());
        }

        String experimentShorthand = "";

        if(experimentTypeText != null && !(experimentTypeText.trim().isEmpty()))
        {
            fname.append(delimiter);
            try {
                experimentShorthand = ExperimentManager.getInstance().getExperimentByName("long",experimentType.getText()).getShortName();
                fname.append(experimentShorthand);
            } catch (NameNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        if(trialNumberText != null && !trialNumberText.trim().isEmpty())
        {
            fname.append(delimiter);
            fname.append("T");
            fname.append(trialNumberText);

        }

        if(sampleNumberText != null && !sampleNumberText.trim().isEmpty())
        {
            fname.append(delimiter);
            fname.append("S");
            fname.append(sampleNumberText);
        }

        StringBuilder initial = new StringBuilder();
        if(researcherNameText != null && researcherNameText.length() != 0)
        {
            String name = researcherNameText.toUpperCase();

            String[] parts = name.split(" ");
            StringBuilder finalInitial = new StringBuilder();

            String sepIni;

            for(int i=0; i<parts.length; i++) {

                sepIni = parts[i].substring(0,1);
                finalInitial = initial.append(sepIni);

            }

            fname.append(delimiter);
            fname.append(finalInitial);

        }

        for(KeywordAutocompleteTextField autocompleteTextField : listofkeywords)
        {
            if(autocompleteTextField.getText() != null && !autocompleteTextField.getText().trim().isEmpty())
            {
                String keyword;
                fname.append(delimiter);
                try {
                    JFXTextField keywordValue = autocompleteTextField.getKeywordValueField();
                    keyword = KeywordManager.getInstance().getKeywordByName("long",autocompleteTextField.getText()).getShortName();
                    if(autocompleteTextField.getState() == 1 && keywordValue != null && keywordValue.getText() != null)
                    {
                        String affix = KeywordManager.getInstance().getKeywordByName("long",autocompleteTextField.getText()).getAffix();
                        switch (affix){
                            case "prefix":
                                fname.append(keyword);
                                fname.append(keywordValue.getText());
                                break;
                            case "suffix":
                                fname.append(keywordValue.getText());
                                fname.append(keyword);
                                break;
                            case "no value":
                                fname.append(keyword);
                                break;
                            default:
                                fname.append(keyword);
                                break;
                        }
                    }
                } catch (NameNotFoundException e1) {
                    e1.printStackTrace();
                }
                System.out.println(fname.toString());
            }
        }

        System.out.println(fname.toString());
        return fname.toString();
    }

    @FXML
    public void addKeyword(ActionEvent e) throws IOException{
        FXMLManager fxmlManager = FXMLManager.getInstance();
        //fxmlManager.setSearchDirectory(System.getProperty("user.dir") + "/src/main/resources/");

        JFXButton removeObjectButton = new JFXButton("", new ImageView(removeObjectIcon));
        removeObjectButton.setPrefSize(15,15);
        removeObjectButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().add(removeObjectButton);
        KeywordAutocompleteTextField textField = new KeywordAutocompleteTextField(hbox);
        textField.setMinWidth(350);
        textField.setPromptText("Choose keyword");
        textField.setAlignment(Pos.BASELINE_LEFT);
        textField.setLabelFloat(true);
        textField.setUnFocusColor(Paint.valueOf("#000000"));
        textField.setFont(new Font("Times New Roman", 20));
        hbox.getChildren().add(textField);
        listofkeywords.add(textField);

        removeObjectButton.setOnAction(e1 -> {
            listofkeywords.remove(textField);
            vboxOfKeywords.getChildren().remove(hbox);
        });

        vboxOfKeywords.getChildren().add(hbox);
        numKeywords++;
        onTypeUpdate();
    }

    @FXML
    public void updateExperiment(ActionEvent e) throws IOException{
        onTypeUpdate();
    }

    @FXML
    public void copyFileToClipboard(ActionEvent e) throws IOException{
        String nameToCopy = updateName();

        StringSelection stringSelection = new StringSelection(nameToCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        outputText.setText(nameToCopy);
        int currTrial = Integer.parseInt(trialNumber.getText());
        currTrial++;
        trialNumber.setText(String.valueOf(currTrial));
    }

    @FXML
    public void handleToggleButton (ActionEvent e) throws IOException {
        Stage primaryStage = (Stage) switchNamers.getScene().getWindow();
        primaryStage.close();
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/simpleNamer.fxml", switchNamers.getScene().getWindow(),"Simple Namer");
    }

    @FXML
    public void handleHelpButton (ActionEvent e) throws IOException {
        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/helpMenu.fxml", helpButton.getScene().getWindow(),"Help Menu");
    }

    @FXML
    public void handleAddButton (ActionEvent e) throws IOException {

        FXMLLoader listOfLocationLoader =
                popupScreen("FXML/addKeywordsUI.fxml", addButton.getScene().getWindow(),"Add Keywords Menu");
    }


    @Override
    public void onTypeUpdate() {
        ArrayList<String> experiments = (ArrayList<String>) ExperimentManager.getInstance().getAllExperimentLongNames();
        experimentType.getEntries().addAll(experiments);

        ArrayList<String> keywords = (ArrayList<String>) KeywordManager.getInstance().getAllKeywordLongNames();
        for(AutocompleteTextField autocompleteTextField : listofkeywords){
            autocompleteTextField.getEntries().addAll(keywords);
        }
    }

    private static class SingletonHelper{
        private static final FullNamer INSTANCE = new FullNamer();
    }

    public JFXTextField getResearcherName() {
        return researcherName;
    }

    public static FullNamer getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public ArrayList<KeywordAutocompleteTextField> getListofkeywords() {
        return listofkeywords;
    }

    public AutocompleteTextField getExperimentType() {
        return experimentType;
    }



}


