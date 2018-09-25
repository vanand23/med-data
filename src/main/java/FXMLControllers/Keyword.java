package FXMLControllers;

import javafx.beans.property.SimpleStringProperty;

class Keyword {

        private final SimpleStringProperty name;
        private final SimpleStringProperty dataval;

        Keyword(String kname, String dval) {
            this.name = new SimpleStringProperty(kname);
            this.dataval = new SimpleStringProperty(dval);
        }

        String getKeywordName(){
            return name.get();
        }

        void setKeywordName(String kname) {
            name.set(kname);
        }

        String getDataValue(){
            return dataval.get();
        }

        void setDataValue(String dval){
            dataval.set(dval);
        }


    }

