package FXMLControllers;

import javafx.beans.property.SimpleStringProperty;

public class Keywords {

        public final SimpleStringProperty name;
        public final SimpleStringProperty dataval;

        public Keywords(String kname, String dval) {
            this.name = new SimpleStringProperty(kname);
            this.dataval = new SimpleStringProperty(dval);
        }

        public String getKeywordName(){
            return name.get();
        }

        public void setKeywordName(String kname) {
            name.set(kname);
        }

        public String getDataValue(){
            return dataval.get();
        }

        public void setDataValue(String dval){
            dataval.set(dval);
        }


    }

