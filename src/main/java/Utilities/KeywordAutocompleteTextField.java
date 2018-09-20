package Utilities;

import Types.KeywordManager;
import Types.KeywordType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javax.naming.NameNotFoundException;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import static FXMLControllers.FullNamer.getTableOfKeywords;
import static FXMLControllers.FullNamer.sharedListOfKeywordStrings;
import static javafx.scene.layout.HBox.setHgrow;

public class KeywordAutocompleteTextField extends AutocompleteTextField {
    private HBox textFieldContainer;
    private JFXTextField keywordValueField;

    private int state;

    public void setState(int state) {
        this.state = state;
    }

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
                state = 1;
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
                r.keyPress(KeyEvent.VK_ENTER);
                r.keyRelease(KeyEvent.VK_ENTER);
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);
                Label label = new Label(result);
                label.setFont(new Font("Arial Black", 14));
                label.setPrefWidth(USE_COMPUTED_SIZE);
                hBox.getChildren().add(label);
                sharedListOfKeywordStrings.add(result);
                try {
                    if(!KeywordManager.getInstance().getKeywordByName("long",result).getAffix().equals("none")){
                        JFXTextField keyval = new JFXTextField();
                        keyval.setPrefWidth(USE_COMPUTED_SIZE);
                        keyval.setMaxWidth(USE_COMPUTED_SIZE);
                        hBox.getChildren().add(keyval);
                        setHgrow(keyval, Priority.ALWAYS);
                        keywordValueField = keyval;
                    }
                } catch (NameNotFoundException e1) {
                    e1.printStackTrace();
                }
                textFieldContainer.getChildren().add(1,hBox);
                textFieldContainer.getChildren().remove(this);
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

}
