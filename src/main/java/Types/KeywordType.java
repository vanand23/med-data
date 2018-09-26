package Types;

import Singletons.Database;

public class KeywordType {
    private String variableID;
    private String longName;
    private String shortName;
    private String dataType;
    private String affix;
    private String dataValue;

    public String getID() {
        return variableID;
    }

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

    public void setVariableID(String variableID) {
        this.variableID = variableID;
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

    public KeywordType(String variableID, String longName, String shortName, String dataType, String affix, String dataValue) {
        this.variableID = variableID;
        this.longName = longName;
        this.shortName = shortName;
        this.dataType = dataType;
        this.affix = affix;
        this.dataValue = dataValue;
    }


    /**
     * Updates the contents of the database to match the current state of this node
     */
    private void updateDB() {
        Database.updateKeyword(this.variableID, this.longName, this.shortName, this.dataType, this.affix, this.dataValue);
    }

}
