package Utilities;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

// CODE BORROWED FROM: https://stackoverflow.com/questions/36861056/
// javafx-textfield-auto-suggestions?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

public class AutocompleteTextField extends TextField {
    //Local variables
    //entries to autocomplete
    private final SortedSet<String> entries;
    private boolean validText;
    private boolean triggerPopup;
    //popup GUI
    ContextMenu entriesPopup;
    Robot r;


    public void setAutocompleteWidth(double width) {
        this.width = width;
    }


    double width;


    public AutocompleteTextField() {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        triggerPopup = false;
        validText = false;
        width = 400;
        setListener();
    }

    private void setListener() {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = getText();
            //always hide suggestion if nothing has been entered (only "spacebars" are disallowed in TextFieldWithLengthLimit)
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //filter all possible suggestions depends on "Text", case insensitive
                List<String> filteredEntries = entries.stream()
                        .filter(e -> e.toLowerCase().contains(enteredText.toLowerCase()))
                        .collect(Collectors.toList());
                //validText = filteredEntries.size() == 1 && !entriesPopup.isShowing(); //check if the text field has a full, valid name

                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText);
                    if (!entriesPopup.isShowing() &&
                            AutocompleteTextField.this.getScene() != null) { //check if owner exists yet in a scene
                        entriesPopup.show(AutocompleteTextField.this, Side.BOTTOM, 0, 0); //position of popup
                    }
                } else {
                    //label with graphic (text flow) to highlight founded subtext in suggestions
                    Label entryLabel = new Label(" + Add a new entry to the database");
                    //entryLabel.setGraphic(Styles.buildTextFlow(result, enteredText));
                    entryLabel.setFont(Font.font("Arial", FontWeight.BOLD,20));
                    entryLabel.setPrefHeight(20);  //don't sure why it's changed with "graphic"
                    entryLabel.setPrefWidth(width);
                    entryLabel.setMaxWidth(width*1.5);
                    CustomMenuItem item = new CustomMenuItem(entryLabel, true);
                    entriesPopup.getItems().clear();
                    entriesPopup.getItems().add(item);
                    entriesPopup.show(this, Side.BOTTOM, 0, 0); //position of popup
                    //if any suggestion is select set it into text and close popup
                    item.setOnAction(actionEvent -> {
                        this.triggerPopup = true;
                        this.setText("");
                    });

                }
            }
        });
    }

    void populatePopup(List<String> searchResult, String searchRequest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label();
            entryLabel.setGraphic(Styles.buildTextFlow(result, searchRequest));
            entryLabel.setPrefHeight(20);  //don't sure why it's changed with "graphic"
            entryLabel.setPrefWidth(width);
            entryLabel.setMaxWidth(width*1.5);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
            item.setOnAction(actionEvent -> {
                validText = true;
                entriesPopup.hide();
                positionCaret(result.length());
                setText(result);
                r.keyPress(KeyEvent.VK_ENTER);
                r.keyRelease(KeyEvent.VK_ENTER);
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    public boolean isValidText() {
        return validText;
    }

    public boolean isTriggerPopup() {
        return triggerPopup;
    }

    public void setTriggerPopup(boolean triggerPopup) {
        this.triggerPopup = triggerPopup;
    }

    public void setValidText(boolean validText) {
        this.validText = validText;
    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() { return entries; }
}
