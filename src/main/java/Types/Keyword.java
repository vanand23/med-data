package Types;

import Singletons.Database;

public class Keyword {
    private String longName;
    private String shortName;
    private String dataType;
    private String affix;
    private String dataValue;
    private String filename;

    public String getLongName() {
        return longName;
    }
    public String getShortName() {
        return shortName;
    }
    public String getDataType() {
        return dataType;
    }
    public String getAffix() {
        return affix;
    }
    public String getDataValue() {
        return dataValue;
    }
    public String getFilename() {
        return filename;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public void setAffix(String affix) {
        this.affix = affix;
    }
    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Keyword(String longName, String shortName, String dataType, String affix, String dataValue, String filename) {
        this.longName = longName;
        this.shortName = shortName;
        this.dataType = dataType;
        this.affix = affix;
        this.dataValue = dataValue;
        this.filename = filename;
    }


    /**
     * Updates the contents of the database to match the current state of this node
     */
    private void updateDB() {
        Database.updateKeyword(
                this.longName,
                this.shortName,
                this.dataType,
                this.affix,
                this.dataValue,
                this.filename);
    }

}
