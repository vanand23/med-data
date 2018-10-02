package Types;

import Singletons.Database;
import Utilities.ITypeObserver;

import javax.naming.NameNotFoundException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utilities.DirectorySearcher.filesInDirectory;

public class ResearcherManager {


    // Map of all node objects
    private HashMap<String, Researcher> researchers;
    private List<ITypeObserver> observers = new ArrayList<>();
    private static ArrayList<String> researcherFiles = new ArrayList<>();

    private ResearcherManager(){
        researchers = Database.loadResearchersToClasses();
    }

    /**
     * Singleton helper class, MapManager should always be accessed through MapManager.getInstance();
     */
    private static class SingletonHelper{
        private static final ResearcherManager INSTANCE = new ResearcherManager();
    }

    /**
     * Gets the singleton instance of map editor
     * @return the proper single instance of map editor
     */
    public static ResearcherManager getInstance(){
        return ResearcherManager.SingletonHelper.INSTANCE;
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
    public void addResearcher(Researcher researcher){
        try {
            Database.insertResearcher(
                    researcher.getLongName(),
                    researcher.getShortName(),
                    researcher.getFilename());
            Database.writeResearchersToCSV("Libraries/researchers/defaultResearchers.csv");
            researchers.put(researcher.getLongName(), researcher);
            notifyObservers();
        }catch (SQLException e1){
            e1.printStackTrace();
            System.err.println("Could not insert researcher");
        }
    }

    public void removeResearcher(String researcherName)
    {
        researchers.remove(researcherName);
    }

    private void refreshResearcherFiles()
    {
        researcherFiles.clear();
        File[] newResearcherFiles = filesInDirectory("Libraries/researchers");
        for(File file : newResearcherFiles)
        {
            researcherFiles.add(file.getName());
        }
    }

    /**
     * Returns a list of researcher database files
     * @return returns an ArrayList of all researcher files
     */
    public ArrayList<String> getResearcherFiles() {
        refreshResearcherFiles();
        return researcherFiles;
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
    public List<String> getAllResearcherLongNames(){
        ArrayList<String> ret = new ArrayList<>();
        String[] filterList = {};
        for (HashMap.Entry<String, Researcher> entry : researchers.entrySet())
        {
            String value = entry.getValue().getLongName();
            if(!stringContainsItemFromList(value,filterList)){
                ret.add(value);
            }
        }
        return ret;
    }

    /**
     * function which returns the number of researchers
     * @return number of researchers
     */
    public int getNumberOfResearchers(){
        return researchers.size();
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
     * Looks up an researcher by either its long or short name
     * @param type this can either be "short" or "long", for choosing short or long name
     * @param name the name of the node to find
     * @return a MapNode object with the given long or short name
     */
    public Researcher getResearcherByName(String type, String name) throws NameNotFoundException {
        for (Object o : researchers.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Researcher n = (Researcher) pair.getValue();
            if (type.equals("short") && n.getShortName().equals(name)) {
                return n;
            } else if (type.equals("long") && n.getLongName().equals(name)) {
                return n;
            }
        }
        throw new NameNotFoundException(name);
    }

    public HashMap<String, Researcher> getResearchers() {
        return researchers;
    }
}
