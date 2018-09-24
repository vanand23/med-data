package Singletons;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import Types.*;

/**
 * A large class for running all SQL queries
 * Likely will be broken up or refactored later, we just havent settled on the best pattern yet
 * We've discovered one in the middle of iteration 2 but dont want to do a major refactor in the middle of it
 */
public class Database {

    //private DatabaseManager parent; // the manager managing this database
    static private Connection connection = null; // the database connection
    
    //static private HashMap<String, MapNode> nodes;

    /** Connect and set up the database
     *
     */
    public static void initDatabase(){

        // Make sure the Java DB driver is installed as a dependency (prints instructions to set up if not installed properly)
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Java DB Driver not found. Add the classpath to your module.");
            System.out.println("For IntelliJ do the following:");
            System.out.println("File | Project Structure, Modules, Dependency tab");
            System.out.println("Add by clicking on the green plus icon on the right of the window");
            System.out.println("Select JARs or directories. Go to the folder where the Java JDK is installed");
            System.out.println("Select the folder java/jdk1.8.xxx/Database/lib where xxx is the version.");
            System.out.println("Click OK, compile the code and run it.");
            e.printStackTrace();
            return;
        }

        // look to see if the database already exists (must do this now because creating a connection could open a new,
        // empty database)
        boolean databaseExists = new File("mydb/").exists();

        // Make a connection to the database
        try {
            // substitute your database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:mydb;create=true");
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output fconsole.");
            e.printStackTrace();
            return;
        }

        // Load database from csv if database does not exist
        if(!databaseExists){
            System.out.println("Database not found, loading data from csv");
            // UNCOMMENT THE BELOW LINE TO RESET THE DATABASE
            dropTables();
            createTables();
            // load in .csv files to the database
            // experiments
            //Database.loadFromCSV("experiments", "defaultExperiments.csv");

            // keywords
            //Database.loadFromCSV("keywords", "defaultKeywords.csv");
        }else{
            // UNCOMMENT THE BELOW LINE TO RESET THE DATABASE
            dropTables();
            createTables();
            // load in .csv files to the database
            // experiments
            Database.loadFromCSV("experiments", "defaultExperiments.csv");

            // keywords
            Database.loadFromCSV("keywords", "defaultKeywords.csv");
        }

        // Connection successful!
        System.out.println("Java DB connection established!");
    }

    /** Creates the node and edge tables
     * If tables already exist, do nothing
     */
    private static void createTables(){
        // users
        // Create user table if it does not already exist
        createTable("users",
                "CREATE TABLE users (" +
                        "username VARCHAR(50) primary key, " +
                        "hash VARCHAR(400), " +
                        "role VARCHAR(50))");

        // keywords
        // Create keywords table if it does not already exist
        createTable("keywords",
                "CREATE TABLE keywords(" +
                        "keywordID VARCHAR(50) primary key, " +
                        "longName VARCHAR(50)," +
                        "shortName VARCHAR(50)," +
                        "dataType VARCHAR(50)," +
                        "affix VARCHAR(50))");

        // experiments
        // Create experiments table if it does not already exist
        createTable("experiments",
                "CREATE TABLE experiments(" +
                        "experimentID VARCHAR(50) primary key, " +
                        "longName VARCHAR(100)," +
                        "shortName VARCHAR(50)," +
                        "description VARCHAR(100))");

        System.out.println("Tables are ready.");
    }

    private static void createTable(String tableName, String sqlStatement){
        try{
            Statement stm = connection.createStatement();
            boolean success = stm.execute(sqlStatement);
            stm.close();
            System.out.println("Table " + tableName + " created.");
        } catch( SQLException e ) {
            System.out.println("Table " + tableName + " already exists");
            e.printStackTrace();
        }
    }

    /**
     * destroy all tables
     * Edges must be destroyed first always so as to not create a constraint error
     * Also comments must be destroyed before service requests
     */
    public static void dropTables(){
        dropTable("users");
        dropTable("experiments");
        dropTable("keywords");
    }

    private static void dropTable(String tableName){
        try{
            Statement stm = connection.createStatement();
            stm.execute("DROP TABLE " + tableName);
            stm.close();
            System.out.println("Table " + tableName + " dropped");
        } catch(Exception e){
            System.out.println("Couldn't drop table " + tableName);
            e.printStackTrace();
        }
    }


    // NEW STUFF ================================================================

    /**
     * Simple function for doing most database queries. Does not work for anything that uses data types other than strings
     * @param query the query string to use
     * @param args the arguements to insert into the query string
     */
    public static void simpleQuery(String query, String[] args){
        if(args.length < 1){
            System.out.println("Could not insert, no values given.");
            return;
        }
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            for(int i=0; i<args.length; i++){
                ps.setString(i+1, args[i]);
            }

            ps.execute();
            ps.close();

            /* Use this area instead for a threaded version
            Thread t = new Thread(() -> {
                try {
                    ps.execute();
                    ps.close();
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Could not perform insert because of a key constraint.");
                } catch (SQLException e) {
                    System.out.println("Could not perform insert");
                    e.printStackTrace();
                }
            });

            t.start();
            */

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not perform insert because of a key constraint.");
            //e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("Could not perform query");
            e.printStackTrace();
        }
    }

    // ==========================================================================

    // SPECIAL ACTIONS =======================================================


    /**
     * Retrieves a node from the database by its ID
     * @param experimentID the experiment's name
     * @return an ExperimentType object
     */
    public static ExperimentType getExperiment(String experimentID){

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM experiments WHERE experimentID=?");
            ps.setString(1, experimentID);
            ResultSet rs = ps.executeQuery();

            String longName, shortName, description;
            ExperimentType n = null;

            // for each node, build an object
            while (rs.next()) {
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                description = rs.getString("description");
                // create node instance and put it in the nodes HashMap
                n = new ExperimentType(experimentID, longName, shortName, description);
                break;
            }

            rs.close();
            ps.close();


            if(n == null){
                System.out.println("Experiment " + experimentID + " not found.");
                return null;
            }

            return n;
        }
        catch(SQLException e){
            System.out.println("Could not retrieve experiment " + experimentID);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a node from the database by its ID
     * @param keywordID the experiment's name
     * @return an ExperimentType object
     */
    public static KeywordType getKeyword(String keywordID){

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM experiments WHERE keywordID=?");
            ps.setString(1, keywordID);
            ResultSet rs = ps.executeQuery();

            String longName, shortName, dataType, affix;
            KeywordType n = null;

            // for each node, build an object
            while (rs.next()) {
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                dataType = rs.getString("dataType");
                affix = rs.getString("affix");

                // create node instance and put it in the nodes HashMap
                n = new KeywordType(keywordID, longName, shortName, dataType, affix);
                break;
            }

            rs.close();
            ps.close();


            if(n == null){
                System.out.println("Experiment " + keywordID + " not found.");
                return null;
            }

            return n;
        }
        catch(SQLException e){
            System.out.println("Could not retrieve experiment " + keywordID);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets a list of all existing usernames
     * @return An array list of all usernames
     */
    public static ArrayList<String> getUsernameList(){
        ArrayList<String> nameList = new ArrayList<>();
        try{
            // Retrieve all usernames in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT username FROM users");

            // for each username, add to nameList
            while(rs.next()) {
                nameList.add(rs.getString("username"));
            }
        } catch(SQLException e){
            System.out.println("Failed to retrieve a list of usernames");
            e.printStackTrace();
        }

        return nameList;
    }

    // ==========================================================================

    // LIST LOADERS =============================================================


    // ==========================================================================

    // NODES ----------------------------------------------------------------------------

    /**
     * Inserts a keyword into the database
     * @param * the inputs correspond to the node class's fields
     * @throws SQLException
     */
    public static void insertKeyword(String keywordID, String longName, String shortName, String dataType, String affix)  throws SQLException{
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO keywords VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, keywordID);
            ps.setString(2, longName);
            ps.setString(3, shortName);
            ps.setString(4, dataType);
            ps.setString(5, affix);
            ps.execute();
            ps.close();
            System.out.println("keyword inserted");
        }
        catch(SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not insert keyword because keyword already exists.");
        }
        catch(SQLException e){
            System.out.println("Could not insert the keyword.");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Inserts a keyword into the database
     * @param * the inputs correspond to the node class's fields
     * @throws SQLException
     */
    public static void insertExperiment(String experimentID, String longName, String shortName, String description)  throws SQLException{
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO experiments VALUES (?, ?, ?, ?)");
            ps.setString(1, experimentID);
            ps.setString(2, longName);
            ps.setString(3, shortName);
            ps.setString(4, description);
            ps.execute();
            ps.close();
            System.out.println("experiment inserted");
        }
        catch(SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not insert experiment because experiment already exists.");
        }
        catch(SQLException e){
            System.out.println("Could not insert the experiment.");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * Updates all the keyword fields to match the given fields (not efficient for changing only a few fields)
     * @param * The inputs correspond to the node's fields
     */
    public static void updateKeyword(String keywordID, String longName, String shortName, String dataType, String affix, String dataValue){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE keywords SET " +
                    "dataValue=?, " +
                    "shortName=?, " +
                    "dataType=?, " +
                    "affix=?" +
                    "WHERE keywordID=?");
            ps.setString(1, longName);
            ps.setString(2, shortName);
            ps.setString(3, dataType);
            ps.setString(4, affix);
            ps.setString(5, keywordID);

            ps.execute();
            ps.close();
        }
        catch(SQLException e){
            System.out.println("Could not update keyword " + longName);
            e.printStackTrace();
        }
    }

    /**
     * Updates all the keyword fields to match the given fields (not efficient for changing only a few fields)
     * @param * The inputs correspond to the node's fields
     */
    public static void updateExperiment(String experimentID, String longName, String shortName, String description){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE keywords SET " +
                    "dataValue=?, " +
                    "shortName=?, " +
                    "dataType=?, " +
                    "affix=?" +
                    "dataValue=?" +
                    "WHERE experimentID=?");
            ps.setString(1, longName);
            ps.setString(2, shortName);
            ps.setString(3, description);
            ps.setString(4, experimentID);

            ps.execute();
            ps.close();
        }
        catch(SQLException e){
            System.out.println("Could not update keyword " + longName);
            e.printStackTrace();
        }

    }


    // END OF NODES ---------------------------------------------------------------------------

    /**
     * Load a csv file into the database
     * @param type either "edge" or "node", dictates whether to load edges or nodes from the file
     * @param filename the name of the file to read from (Currently one in the resource folder)
     */
    public static void loadFromCSV(String type, String filename){

        // Quit if no database connected
        if(connection == null){
            System.out.println("No connection with ");
            return;
        }

        String line = ""; // stores each line of the file
        String splitter = ","; // string to use to split each line into columns

        // Open the csv file
        try{
            // open the file from the resources folder
            //BufferedReader reader = new BufferedReader(new InputStreamReader(Database.class.getClassLoader().getResourceAsStream(filename)));
            // use this line instead to load from the project directory instead
            BufferedReader reader = new BufferedReader(new FileReader("Libraries/" + filename));

            // Skip the first line (the column titles)
            reader.readLine();

            // Go through each line of the file
            while((line = reader.readLine()) != null){
                // split the line by commas into its items
                String[] items = line.split(splitter);

                // For loading experiments
                if(type.equals("experiments")) {
                    // insert an edge into the database
                    String experimentID = items[0];
                    String longName = items[1];
                    String shortName = items[2];
                    String description = items[3];

                    insertExperiment(experimentID, longName, shortName, description);
                }
                // For loading keywords
                else if(type.equals("keywords")){
                    String keywordID = items[0];
                    String longName = items[1];
                    String shortName = items[2];
                    String dataType = items[3];
                    String affix = items[4];

                    // insert new node into database
                    insertKeyword(keywordID, longName, shortName, dataType, affix);
                }
            }
        }
        catch(IOException e){
            System.err.println("Error reading " + filename);
            e.printStackTrace();
        }
        catch(NumberFormatException e){
            System.out.println("Error when trying to convert a string into an integer.");
            e.printStackTrace();
        }
        catch(Exception e){
            System.out.println("Unknown error.");
            e.printStackTrace();
        }
    }


    /**
     * Write all edges into a .csv file (puts file in the project directory NOT resources folder)
     * @param filename the name of the file to write to
     */
    public static void writeKeywordsToCSV(String filename){
        // Quit if no database connected
        if(connection == null){
            System.out.println("No connection with ");
            return;
        }
        // Open the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // write column titles
            writer.write("variableID,longName,shortName,dataType,affix\n");

            // retrieve edges from database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM keywords");

            String keywordID,longName,shortName,dataType,affix;

            // Write each edge to the .csv file
            while(rs.next()){
                // get fields
                keywordID = rs.getString("keywordID");
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                dataType = rs.getString("dataType");
                affix = rs.getString("affix");

                // combine fields into a line and write it
                writer.write(keywordID + "," + longName + "," + shortName + "," + dataType + "," + affix + "\n");
            }

            s.close();
            rs.close();
            writer.close();
            System.out.println("Successfully saved keywords to " + filename);

        } catch(IOException e){
            System.out.println("Could not save DB to " + filename);
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Could not retrieve from ");
            e.printStackTrace();
        }
    }

    /**
     * Write all nodes into a .csv file (puts file in the project directory NOT resources folder)
     * @param filename the name of the file to write to
     */
    public static void writeNodesToCSV(String filename){
        // Quit if no database connected
        if(connection == null){
            System.out.println("No connection with ");
            return;
        }

        // Open the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // write the column titles
            writer.write("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,xcoord3d,ycoord3d\n");

            // retrieve all nodes
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM nodes");

            String nodeID, floor, building, nodeType, longName, shortName;
            int xcoord, ycoord, xcoord3d, ycoord3d;

            // for each node, write it to the .csv file
            while(rs.next()){
                // get the node's fields
                nodeID = rs.getString("nodeID");
                xcoord = rs.getInt("xcoord");
                ycoord = rs.getInt("ycoord");
                xcoord3d = rs.getInt("xcoord3d");
                ycoord3d = rs.getInt("ycoord3d");
                floor = rs.getString("floor");
                building = rs.getString("building");
                nodeType = rs.getString("nodeType");
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                // combine fields into a line and write it to the file
                writer.write(nodeID + "," + xcoord + "," + ycoord + "," + floor + "," + building + "," +
                        nodeType + "," + longName + "," + shortName + "," + xcoord3d + "," + ycoord3d + "\n");
            }

            s.close();
            rs.close();
            writer.close();

            System.out.println("Successfully saved nodes to " + filename);

        } catch(IOException e){
            System.out.println("Could not save DB to " + filename);
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Could not retrieve from ");
            e.printStackTrace();
        }
    }

    /**
     * prints out all edges in database, for testing purposes
     */
    public static void dumpEdges(){
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM edges");

            String edgeID, startNodeID, endNodeID;

            while(rs.next()){
                edgeID = rs.getString("edgeID");
                startNodeID = rs.getString("startNodeID");
                endNodeID = rs.getString("endNodeID");
                System.out.format("Edge: ID = %s, Start = %s, End = %s\n", edgeID, startNodeID, endNodeID);
            }

            rs.close();
            s.close();
        }
        catch(SQLException e){
            System.out.println("Could not retrieve edges");
            e.printStackTrace();
        }

    }

    /**
     * prints out all nodes in database, for testing purposes
     */
    public static void dumpNodes(){
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM nodes");

            String nodeID, floor, building, nodeType, longName, shortName;
            int xcoord, ycoord, xcoord3d, ycoord3d;

            while(rs.next()){
                nodeID = rs.getString("nodeID");
                xcoord = rs.getInt("xcoord");
                ycoord = rs.getInt("ycoord");
                xcoord3d = rs.getInt("xcoord3d");
                ycoord3d = rs.getInt("ycoord3d");
                floor = rs.getString("floor");
                building = rs.getString("building");
                nodeType = rs.getString("nodeType");
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                System.out.format("MapNode: ID = %s, X = %d, Y = %d, NewX = %d, Newy = %d," +
                                "Floor = %s, Building = %s, NodeType = %s, LongName = %s, ShortName = %s\n",
                        nodeID, xcoord, ycoord, xcoord3d, ycoord3d, floor, building, nodeType, longName, shortName);
            }

            rs.close();
            s.close();
        }
        catch(SQLException e){
            System.out.println("Could not retrieve edges");
            e.printStackTrace();
        }

    }

    // closes the Database connection
    public static void close() {
        try {
            connection.close();
        }
        catch(SQLException e){
            System.out.println("Failed to close database connection.");
            e.printStackTrace();
        }
    }



    /**
     * Simple function for doing most database queries. Does not work for anything that uses data types other than strings
     * @param query the query string to use
     * @param args the arguements to insert into the query string
     */
    public static void simpleSRQuery(String query, String[] args, Date d){
        if(args.length < 1){
            System.out.println("Could not insert, no values given.");
            return;
        }
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            for(int i=0; i<args.length; i++){
                ps.setString(i+1, args[i]);
            }
            ps.setDate(args.length+1, d);

            ps.execute();
            ps.close();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not perform insert because of a key constraint.");
            //e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("Could not perform query");
            e.printStackTrace();
        }
    }

    /**
     * Simple function for doing most database queries. Does not work for anything that uses data types other than strings
     * @param query the query string to use
     * @param args the arguements to insert into the query string
     */
    public static void updateSRQuery(String query, String[] args, Date d){
        if(args.length < 1){
            System.out.println("Could not insert, no values given.");
            return;
        }
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            for(int i=0; i<args.length-1; i++){
                ps.setString(i+1, args[i]);
            }
            ps.setDate(args.length, d);
            ps.setString(args.length+1, args[args.length-1]);


            ps.execute();
            ps.close();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not perform insert because of a key constraint.");
            //e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("Could not perform query");
            System.out.println(d.toString());
            e.printStackTrace();
        }
    }

    /**
     * Build classes from database
     * @return returns a hash map of all MapNodes, with their nodeID as the key
     */
    public static HashMap<String, ExperimentType> loadExperimentsToClasses(){

        HashMap<String, ExperimentType> experiments = new HashMap<String, ExperimentType>();

        // Build node objects
        // Nodes MUST be built first to comply with constraints on edge database
        try{
            // Retrieve all experiment types in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM experiments");
            String experimentID, longName, shortName, description;

            // for each node, build an object
            while(rs.next()) {
                experimentID = rs.getString("experimentID");
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                description = rs.getString("description");
                if(description == null | description.length() == 0)
                {
                    description = "";
                }


                // create node instance and put it in the nodes HashMap
                ExperimentType n = new ExperimentType(experimentID, longName, shortName, description);
                experiments.put(experimentID, n);
            }
        } catch(SQLException e){
            System.out.println("Failed to load experiments to classes");
            e.printStackTrace();
        }
        return experiments;
    }

    /**
     * Build classes from database
     * @return returns a hash map of all MapNodes, with their nodeID as the key
     */
    public static HashMap<String, KeywordType> loadKeywordsToClasses(){

        HashMap<String, KeywordType> keywords = new HashMap<String, KeywordType>();

        // Build node objects
        // Nodes MUST be built first to comply with constraints on edge database
        try{
            // Retrieve all keyword types in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM keywords");
            String keywordID, longName, shortName, dataType, affix;

            // for each node, build an object
            while(rs.next()) {
                keywordID = rs.getString("keywordID");
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                dataType = rs.getString("dataType");
                affix = rs.getString("affix");


                // create node instance and put it in the nodes HashMap
                KeywordType n = new KeywordType(keywordID, longName, shortName, dataType, affix);
                keywords.put(keywordID, n);
            }
        } catch(SQLException e){
            System.out.println("Failed to load keywords to classes");
            e.printStackTrace();
        }
        return keywords;
    }

}
