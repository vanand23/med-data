package FXMLControllers;

import javafx.beans.property.SimpleStringProperty;

public class KeywordDB {

    private final SimpleStringProperty name;
    private final SimpleStringProperty abbrev;
    private final SimpleStringProperty affix;
    private final SimpleStringProperty datatype;
    private final SimpleStringProperty dataval;

    public KeywordDB(String kname, String kabbrev, String kaffix, String dtype, String dval) {
        this.name = new SimpleStringProperty(kname);
        this.abbrev = new SimpleStringProperty(kabbrev);
        this.affix = new SimpleStringProperty(kaffix);
        this.datatype = new SimpleStringProperty(dtype);
        this.dataval = new SimpleStringProperty(dval);
    }

    public String getKeywordName(){
        return name.get();
    }

    public void setKeywordName(String kname) {
        name.set(kname);
    }

    public String getKeywordAbbreviation(){ return abbrev.get(); }

    public void setKeywordAbbreviation(String kabbrev) { abbrev.set(kabbrev); }

    public String getKeywordAffix(){ return affix.get(); }

    public void setKeywordAffix(String kaffix) { affix.set(kaffix); }

    public String getDataType(){ return datatype.get(); }

    public void setDataType(String dtype) { datatype.set(dtype); }

    public String getDataValue(){
        return dataval.get();
    }

    public void setDataValue(String dval){
        dataval.set(dval);
    }


}
