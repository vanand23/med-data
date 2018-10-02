package Types;

import Singletons.Database;

public class Researcher {
    private String longName;
    private String shortName;
    private String filename;

    public String getLongName() {
        return longName;
    }

    public String getShortName() { return shortName; }

    public String getFilename() {
        return filename;
    }

    /**
     * Constructor of Experiment
     * @param longName  String of this MapNode's long name
     * @param shortName String of this MapNode's short name
     * @param filename  String of this MapNode's file name
     */
    public Researcher(String longName, String shortName, String filename){
        this.longName = longName;
        this.shortName = shortName;
        this.filename = filename;
    }

    /**
     * Updates the contents of the database to match the current state of this node
     */
    private void updateDB() {
        //Database.updateResearcher(this.longName, this.shortName, this.filename);
    }

}
