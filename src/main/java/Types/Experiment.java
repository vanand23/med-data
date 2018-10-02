package Types;

import Singletons.Database;

import java.util.LinkedList;

public class Experiment {
    private String longName;
    private String shortName;
    private String description;
    private String filename;

    public String getLongName() {
        return longName;
    }

    public String getShortName() { return shortName; }

    public String getDescription() { return description;}

    public String getFilename() {
        return filename;
    }

    /**
     * Constructor of Experiment
     * @param longName  String of this MapNode's long name
     * @param shortName String of this MapNode's short name
     * @param description  String of this MapNode's long name
     */
    public Experiment(String longName, String shortName, String description, String filename){
        this.longName = longName;
        this.shortName = shortName;
        this.description = description;
        this.filename = filename;
    }

    /**
     * Updates the contents of the database to match the current state of this node
     */
    private void updateDB() {
        Database.updateExperiment(this.longName, this.shortName, this.description, this.filename);
    }



    /*
    public void copy(MapNode n ){
        this.nodeID = n.nodeID;
        this.xcoord = n.xcoord;
        this.ycoord = n.ycoord;
        this.xcoord3d = n.xcoord3d;
        this.ycoord3d = n.ycoord3d;
        this.floor = n.floor;
        this.building = n.building;
        this.nodeType = n.nodeType;
        this.longName = n.longName;
        this.shortName = n.shortName;
        updateDB();
    }
    */
}
