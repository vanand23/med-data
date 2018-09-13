package Utilities;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class KeywordAutocompleteTextField extends AutocompleteTextField {
    private HBox textFieldContainer;
    private JFXTextField keywordValueField;

    private int state;

    public KeywordAutocompleteTextField(HBox textFieldContainer)
    {
        this.textFieldContainer = textFieldContainer;
        state = 0;
    }

    public int getState() {
        return state;
    }

    public JFXTextField getKeywordValueField() {
        return keywordValueField;
    }

    @Override
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
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
                r.keyPress(KeyEvent.VK_ENTER);
                r.keyRelease(KeyEvent.VK_ENTER);
                VBox vBox = new VBox();
                vBox.getChildren().add(new Label(result));
                JFXTextField keyval = new JFXTextField();
                vBox.getChildren().add(keyval);
                keywordValueField = keyval;
                textFieldContainer.getChildren().add(vBox);
                textFieldContainer.getChildren().remove(this);
                state = 1;
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

}
