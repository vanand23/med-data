package Types;

import Singletons.Database;
import Utilities.ITypeObserver;

import javax.naming.NameNotFoundException;
import java.sql.SQLException;
import java.util.*;

public class ExperimentManager {


    // Map of all node objects
    private HashMap<String, ExperimentType> experiments;
    private List<ITypeObserver> observers = new ArrayList<>();

    private ExperimentManager(){
        experiments = Database.loadExperimentsToClasses();
    }

    /**
     * Singleton helper class, MapManager should always be accessed through MapManager.getInstance();
     */
    private static class SingletonHelper{
        private static final ExperimentManager INSTANCE = new ExperimentManager();
    }

    /**
     * Gets the singleton instance of map editor
     * @return the proper single instance of map editor
     */
    public static ExperimentManager getInstance(){
        return SingletonHelper.INSTANCE;
    }

    /**
     * Subscribes an observer to watch for changes in the map editor
     * @param o the observer to watch
     */
    public void subscribe(ITypeObserver o){
        observers.add(o);
        notifyObservers();
    }

    /**
     * Updates all observers, should be called whenever the database updates, creates, or deletes a ndoe
     */
    private void notifyObservers() {
        for(ITypeObserver mo: observers){
            mo.onTypeUpdate();
        }
    }

    // Adds a node to the graph and  Please never directly make a new node, instead just call this function
    // Inputs correspond to the excel columns for Nodes (minus AssignedTeam)
    // RETURN the MapNode object that was created
    public void addExperiment(ExperimentType experimentType){
        try {
            Database.insertExperiment(
                    experimentType.getID(),
                    experimentType.getLongName(),
                    experimentType.getShortName(),
                    experimentType.getDescription());
            Database.writeExperimentsToCSV("Libraries/defaultExperiments.csv");
            experiments.put(experimentType.getID(),experimentType);
            notifyObservers();
        }catch (SQLException e1){
            e1.printStackTrace();
            System.err.println("Could not insert experiment");
        }
    }
/*
    public MapNode addNode(MapNode mapNode){
        try{
            // Add the mapNode to the database
            Database.insertNode(mapNode.getNodeID(), mapNode.getXcoord(), mapNode.getYcoord(), mapNode.getXcoord3d(), mapNode.getYcoord3d(), mapNode.getFloor(), mapNode.getBuilding(), mapNode.getNodeType(), mapNode.getLongName(), mapNode.getShortName());
            // Add the mapNode to the HashMap
            this.nodes.put(mapNode.getID(), mapNode);
            // return mapNode for use
            notifyObservers();
            return mapNode;
        }
        catch(SQLException e){
            System.out.println("MapNode not added.");
        }
        return null;
    }


    // Delete a node from the graph and database
    // INPUT - nodeID - the ID of the node to delete
    public  void deleteNode(String nodeID){
        // retrieve the node from the hashmap
        MapNode n = nodes.get(nodeID);
        if(n == null){
            System.out.println("MapNode " + nodeID + " does note exist.");
            return;
        }

        // delete every edge the node is a part of
        for(String nID: n.getEdges()){
            // remove the node from the connected node's edge list
            nodes.get(nID).getEdges().remove(n.getID());
            // delete every edge in the database consisting of these two nodes
            Database.simpleQuery(edgeDeleteStr, new String[]{n.getID(), nID, nID, n.getID()});
        }

        // Now that all edges have been removed from the graph and the database, delete the node itself
        // Delete the node from the edu.wpi.cs3733d18.teamD.Singletons.Database
        Database.simpleQuery(nodeDeleteStr, new String[]{n.getID()});
        // Delete the node from the HashMap
        nodes.remove(nodeID);
        notifyObservers();
    }

    public void updateNode(MapNode n){
        Database.updateNode(n.getID(), n.getX2D(), n.getY2D(), n.getX3D(), n.getY3D(),
                n.getFloor(), n.getBuilding(), n.getNodeType(), n.getLongName(), n.getShortName());

        nodes.put(n.getID(), Database.getNode(n.getID())); //fixme probably doesn't need to reference database
        notifyObservers();
    }

    public void deleteNode(MapNode n){
        deleteNode(n.getID());
        notifyObservers();
    }*/

    /**
     * function that gets all of the long names filtering stuff
     * @return list of long names
     */
    public List<String> getAllExperimentLongNames(){
        ArrayList<String> ret = new ArrayList<>();
        String[] filterList = {};
        for (HashMap.Entry<String, ExperimentType> entry : experiments.entrySet())
        {
            String value = entry.getValue().getLongName();
            if(!stringContainsItemFromList(value,filterList)){
                ret.add(value);
            }
        }
        return ret;
    }

    /**
     * function which returns the number of keywords
     * @return number of keywords
     */
    public int getNumberOfExperiments(){
        return experiments.size();
    }


    /**
     * Helper function for getLongNames
     * @param inputStr
     * @param items
     * @return true if item is found
     */
    private static boolean stringContainsItemFromList(String inputStr, String[] items)
    {
        for (String item : items) {
            if (inputStr.contains(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Looks up an experiment by either its long or short name
     * @param type this can either be "short" or "long", for choosing short or long name
     * @param name the name of the node to find
     * @return a MapNode object with the given long or short name
     */
    public ExperimentType getExperimentByName(String type, String name) throws NameNotFoundException{
        for (Object o : experiments.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            ExperimentType n = (ExperimentType) pair.getValue();
            if (type.equals("short") && n.getShortName().equals(name)) {
                return n;
            } else if (type.equals("long") && n.getLongName().equals(name)) {
                return n;
            }
        }
        throw new NameNotFoundException(name);
    }

    public HashMap<String, ExperimentType> getExperiments() {
        return experiments;
    }
}