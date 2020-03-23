package Singletons;

import Types.Keyword;
import Utilities.ITypeObserver;

import javax.naming.NameNotFoundException;
import java.io.File;
import java.sql.SQLException;
import java.util.*;

import static Utilities.DirectorySearcher.filesInDirectory;

public class KeywordManager {


    // Map of all node objects
    private HashMap<String, Keyword> keywords;
    private List<ITypeObserver> observers = new ArrayList<>();
    private static ArrayList<String> keywordFiles = new ArrayList<>();

    private KeywordManager(){
        keywords = Database.loadKeywordsToClasses();
    }

    /**
     * Singleton helper class, MapManager should always be accessed through MapManager.getInstance();
     */
    private static class SingletonHelper{
        private static final KeywordManager INSTANCE = new KeywordManager();
    }

    /**
     * Gets the singleton instance of map editor
     * @return the proper single instance of map editor
     */
    public static KeywordManager getInstance(){
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
    public void addKeyword(Keyword keyword){
        try {
            Database.insertKeyword(
                    keyword.getLongName(),
                    keyword.getShortName(),
                    keyword.getDataType(),
                    keyword.getAffix(),
                    keyword.getFilename());
            Database.writeKeywordsToCSV("Libraries/keywords/defaultKeywords.csv");
            keywords.put(keyword.getLongName(), keyword);
            notifyObservers();
        }catch (SQLException e1){
            e1.printStackTrace();
            System.err.println("Could not insert keyword");
        }
    }

    public void removeKeyword(String keywordName)
    {
        keywords.remove(keywordName);
    }

    private void refreshKeywordFiles()
    {
        keywordFiles.clear();
        File[] newKeywordFiles = filesInDirectory("Libraries/keywords");
        for(File file : newKeywordFiles)
        {
            keywordFiles.add(file.getName());
        }
    }

    /**
     * Returns a list of keyword database files
     * @return returns an ArrayList of all keyword files
     */
    public ArrayList<String> getKeywordFiles() {
        refreshKeywordFiles();
        return keywordFiles;
    }


/*
    // Adds a node to the graph and  Please never directly make a new node, instead just call this function
    // Inputs correspond to the excel columns for Nodes (minus AssignedTeam)
    // RETURN the MapNode object that was created
    public MapNode addNode(String nodeID, int xcoord, int ycoord, int xcoord3d, int ycoord3d, String floor,
                           String building, String nodeType, String longName, String shortName){
        // Create new node object
        MapNode n = new MapNode(nodeID, xcoord, ycoord, xcoord3d, ycoord3d, floor, building, nodeType, longName, shortName);

        try{
            // Add the node to the database
            Database.insertNode(nodeID, xcoord, ycoord, xcoord3d, ycoord3d, floor, building, nodeType, longName, shortName);
            // Add the node to the HashMap
            this.nodes.put(nodeID, n);
            notifyObservers();
            // return node for use
            return n;
        }
        catch(SQLException e){
            System.out.println("MapNode not added.");
        }
        return null;
    }

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
    public List<String> getAllKeywordLongNames(){
        ArrayList<String> ret = new ArrayList<>();
        String[] filterList = {};
        for (HashMap.Entry<String, Keyword> entry : keywords.entrySet())
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
    public int getNumberOfKeywords(){
        return keywords.size();
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
     * Looks up an keyword by either its long or short name
     * @param type this can either be "short" or "long", for choosing short or long name
     * @param name the name of the node to find
     * @return a MapNode object with the given long or short name
     */
    public Keyword getKeywordByName(String type, String name) throws NameNotFoundException{
        for (Object o : keywords.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Keyword n = (Keyword) pair.getValue();
            if (type.equals("short") && n.getShortName().equals(name)) {
                return n;
            } else if (type.equals("long") && n.getLongName().equals(name)) {
                return n;
            }
        }
        throw new NameNotFoundException(name);
    }

    public HashMap<String, Keyword> getKeywords() {
        return keywords;
    }
}