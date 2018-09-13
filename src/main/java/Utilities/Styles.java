package Utilities;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


class Styles {

    static TextFlow buildTextFlow(String text, String filter) {
        int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
        Text textBefore = new Text(text.substring(0, filterIndex));
        textBefore.setFont(Font.font("Times New Roman",20));
        Text textAfter = new Text(text.substring(filterIndex + filter.length()));
        textAfter.setFont(Font.font("Times New Roman",20));
        Text textFilter = new Text(text.substring(filterIndex,  filterIndex + filter.length())); //instead of "filter" to keep all "case sensitive"
        textFilter.setFill(Color.ORANGE);
        textFilter.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        return new TextFlow(textBefore, textFilter, textAfter);
    }
}
