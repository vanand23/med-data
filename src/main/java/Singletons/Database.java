package Singletons;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import Types.*;

import static Utilities.DirectorySearcher.filesInDirectory;

/**
 * A large class for running all SQL queries
 * Likely will be broken up or refactored later, we just havent settled on the best pattern yet
 * We've discovered one in the middle of iteration 2 but dont want to do a major refactor in the middle of it
 */
public class Database {

    //private DatabaseManager parent; // the manager managing this database
    static private Connection connection = null; // the database connection
    private static ArrayList<String> experimentFiles = new ArrayList<>();
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
            //dropTables();
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

            loadFilesInDirectory("experiments");
            loadFilesInDirectory("keywords");
            loadFilesInDirectory("researchers");
        }

        // Connection successful!
        System.out.println("Java DB connection established!");
    }

    private static void loadFilesInDirectory(String type) {
        File[] newKeywordFiles = filesInDirectory("Libraries/" + type);
        for(File file : newKeywordFiles)
        {
            loadFromCSV(type, file.getName());
        }


    }

    /** Creates the node and edge tables
     * If tables already exist, do nothing
     */
    private static void createTables(){
        // researchers
        // Create user table if it does not already exist
        createTable("researchers",
                "CREATE TABLE researchers (" +
                        "longName VARCHAR(50) primary key, " +
                        "shortName VARCHAR(50)," +
                        "filename VARCHAR(50))");

        // keywords
        // Create keywords table if it does not already exist
        createTable("keywords",
                "CREATE TABLE keywords(" +
                        "longName VARCHAR(50) primary key," +
                        "shortName VARCHAR(50) not null unique," +
                        "dataType VARCHAR(50)," +
                        "affix VARCHAR(50)," +
                        "filename VARCHAR(50))");

        // experiments
        // Create experiments table if it does not already exist
        createTable("experiments",
                "CREATE TABLE experiments(" +
                        "longName VARCHAR(100) primary key," +
                        "shortName VARCHAR(50)," +
                        "description VARCHAR(100)," +
                        "filename VARCHAR(50))");

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
    private static void dropTables(){
        dropTable("researchers");
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


    // ==========================================================================

    // SPECIAL ACTIONS =======================================================


    /**
     * Retrieves a node from the database by its ID
     * @param longName the experiment's name
     * @return an Experiment object
     */
    public static Experiment getExperiment(String longName){

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM experiments WHERE longName=?");
            ps.setString(1, longName);
            ResultSet rs = ps.executeQuery();

            String shortName, description, filename;
            Experiment n = null;

            // for each node, build an object
            while (rs.next()) {
                shortName = rs.getString("shortName");
                description = rs.getString("description");
                filename = rs.getString("filename");
                // create node instance and put it in the nodes HashMap
                n = new Experiment(longName, shortName, description,filename);
                break;
            }

            rs.close();
            ps.close();


            if(n == null){
                System.out.println("Experiment " + longName + " not found.");
                return null;
            }

            return n;
        }
        catch(SQLException e){
            System.out.println("Could not retrieve experiment " + longName);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a node from the database by its ID
     * @param longName the experiment's name
     * @return an Experiment object
     */
    public static Keyword getKeyword(String longName){

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM keywords WHERE longName=?");
            ps.setString(1, longName);
            ResultSet rs = ps.executeQuery();

            String shortName, dataType, affix, filename;
            Keyword n = null;

            // for each node, build an object
            while (rs.next()) {
                shortName = rs.getString("shortName");
                dataType = rs.getString("dataType");
                affix = rs.getString("affix");
                filename = rs.getString("filename");

                // create node instance and put it in the nodes HashMap
                n = new Keyword(longName, shortName, dataType, affix, "", filename);
                break;
            }

            rs.close();
            ps.close();


            if(n == null){
                System.out.println("Experiment " + longName + " not found.");
                return null;
            }

            return n;
        }
        catch(SQLException e){
            System.out.println("Could not retrieve experiment " + longName);
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
            ResultSet rs = s.executeQuery("SELECT username FROM researchers");

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
    public static void insertKeyword(String longName, String shortName, String dataType, String affix, String filename)  throws SQLException{
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO keywords VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, longName);
            ps.setString(2, shortName);
            ps.setString(3, dataType);
            ps.setString(4, affix);
            ps.setString(5, filename);
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
    public static void insertExperiment(String longName, String shortName, String description, String filename)  throws SQLException{
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO experiments VALUES (?, ?, ?, ?)");
            ps.setString(1, longName);
            ps.setString(2, shortName);
            ps.setString(3, description);
            ps.setString(4, filename);
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
     * Inserts a keyword into the database
     * @param * the inputs correspond to the node class's fields
     * @throws SQLException
     */
    public static void insertResearcher(String longName, String shortName, String filename)  throws SQLException{
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO researchers VALUES (?, ?, ?)");
            ps.setString(1, longName);
            ps.setString(2, shortName);
            ps.setString(3, filename);
            ps.execute();
            ps.close();
            System.out.println("researcher inserted");
        }
        catch(SQLIntegrityConstraintViolationException e) {
            System.out.println("Could not insert researcher because researcher already exists.");
        }
        catch(SQLException e){
            System.out.println("Could not insert the researcher.");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * Updates all the keyword fields to match the given fields (not efficient for changing only a few fields)
     * @param * The inputs correspond to the node's fields
     */
    public static void updateKeyword(String longName, String shortName, String dataType, String affix, String dataValue, String filename){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE keywords SET " +
                    "shortName=?, " +
                    "dataType=?, " +
                    "affix=?" +
                    "filename=?" +
                    "WHERE longName=?");
            ps.setString(1, shortName);
            ps.setString(2, dataType);
            ps.setString(3, affix);
            ps.setString(4, filename);
            ps.setString(5, longName);

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
    public static void updateExperiment(String longName, String shortName, String description, String filename){
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE experiments SET " +
                    "shortName=?, " +
                    "description=?, " +
                    "filename=?" +
                    "WHERE longName=?");
            ps.setString(1, shortName);
            ps.setString(2, description);
            ps.setString(3, filename);
            ps.setString(4, longName);

            ps.execute();
            ps.close();
        }
        catch(SQLException e){
            System.out.println("Could not update experiment " + longName);
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
            BufferedReader reader = new BufferedReader(new FileReader("Libraries/" + type + "/" + filename));

            // Skip the first line (the column titles)
            reader.readLine();

            // Go through each line of the file
            while((line = reader.readLine()) != null){
                // split the line by commas into its items
                String[] items = line.split(splitter);

                // For loading experiments
                switch (type) {
                    case "experiments": {
                        // insert an edge into the database
                        String longName = items[0];
                        String shortName = items[1];
                        String description = items[2];

                        insertExperiment(longName, shortName, description, filename);
                        break;
                    }
                    // For loading keywords
                    case "keywords": {
                        String longName = items[0];
                        String shortName = items[1];
                        String dataType = items[2];
                        String affix = items[3];

                        // insert new node into database
                        insertKeyword(longName, shortName, dataType, affix, filename);
                        break;
                    }
                    case "researchers": {
                        String longName = items[0];
                        String shortName = items[1];

                        insertResearcher(longName, shortName, filename);
                        break;
                    }
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

    public static void removeKeyword(String longName) throws SQLException
    {
        try{
            PreparedStatement ps = connection.prepareStatement("DELETE FROM keywords WHERE longName=?");
            ps.setString(1, longName);
            ps.execute();
        }
        catch(SQLException e){
            System.out.println("Could not remove keyword");
            e.printStackTrace();
        }
        KeywordManager.getInstance().removeKeyword(longName);
    }

    public static void removeExperiment(String longName) throws SQLException
    {
        try{
            PreparedStatement ps = connection.prepareStatement("DELETE FROM experiments WHERE longName=?");
            ps.setString(1, longName);
            ps.execute();
        }
        catch(SQLException e){
            System.out.println("Could not remove experiment");
            e.printStackTrace();
        }
        ExperimentManager.getInstance().removeExperiment(longName);
    }

    public static void removeResearcher(String longName) throws SQLException
    {
        try{
            PreparedStatement ps = connection.prepareStatement("DELETE FROM researchers WHERE longName=?");
            ps.setString(1, longName);
            ps.execute();
        }
        catch(SQLException e){
            System.out.println("Could not remove researcher");
            e.printStackTrace();
        }
        ResearcherManager.getInstance().removeResearcher(longName);
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


            String longName,shortName,dataType,affix;

            // Write each edge to the .csv file
            while(rs.next()){
                // get fields
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                dataType = rs.getString("dataType");
                affix = rs.getString("affix");

                // combine fields into a line and write it
                writer.write(longName + "," + shortName + "," + dataType + "," + affix + "\n");
            }

            s.close();
            rs.close();
            writer.close();
            System.out.println("Successfully saved keywords to " + filename);

        } catch(IOException e){
            System.out.println("Could not save DB to " + filename);
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Could not retrieve from " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Write all edges into a .csv file (puts file in the project directory NOT resources folder)
     * @param filename the name of the file to write to
     */
    public static void writeExperimentsToCSV(String filename){
        // Quit if no database connected
        if(connection == null){
            System.out.println("No connection with ");
            return;
        }
        // Open the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // write column titles
            writer.write("longName,shortName,description\n");

            // retrieve edges from database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM experiments");


            String longName,shortName,description;

            // Write each edge to the .csv file
            while(rs.next()){
                // get fields
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                description = rs.getString("description");

                // combine fields into a line and write it
                writer.write(longName + "," + shortName + "," + description + "\n");
            }

            s.close();
            rs.close();
            writer.close();
            System.out.println("Successfully saved experiments to " + filename);

        } catch(IOException e){
            System.out.println("Could not save DB to " + filename);
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Could not retrieve from " + filename);
            e.printStackTrace();
        }
    }

    /**
     * Write all edges into a .csv file (puts file in the project directory NOT resources folder)
     * @param filename the name of the file to write to
     */
    public static void writeResearchersToCSV(String filename){
        // Quit if no database connected
        if(connection == null){
            System.out.println("No connection with ");
            return;
        }
        // Open the csv file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // write column titles
            writer.write("longName,shortName\n");

            // retrieve edges from database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM researchers");


            String longName,shortName;

            // Write each edge to the .csv file
            while(rs.next()){
                // get fields
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");

                // combine fields into a line and write it
                writer.write(longName + "," + shortName + "\n");
            }//FXMLControllers.ResearchersTable

            s.close();
            rs.close();
            writer.close();
            System.out.println("Successfully saved researchers to " + filename);

        } catch(IOException e){
            System.out.println("Could not save researchers to " + filename);
            e.printStackTrace();
        } catch(SQLException e){
            System.out.println("Could not retrieve from " + filename);
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
     * Build classes from database
     * @return returns a hash map of all MapNodes, with their nodeID as the key
     */
    public static HashMap<String, Researcher> loadResearchersToClasses(){

        HashMap<String, Researcher> researchers = new HashMap<String, Researcher>();

        // Build node objects
        // Nodes MUST be built first to comply with constraints on edge database
        try{
            // Retrieve all experiment types in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM researchers");
            String longName, shortName, filename;

            // for each node, build an object
            while(rs.next()) {
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                filename = rs.getString("filename");

                // create node instance and put it in the nodes HashMap
                Researcher n = new Researcher(longName, shortName, filename);
                researchers.put(longName, n);
            }
        } catch(SQLException e){
            System.out.println("Failed to load researchers to classes");
            e.printStackTrace();
        }
        return researchers;
    }

    /**
     * Build classes from database
     * @return returns a hash map of all MapNodes, with their nodeID as the key
     */
    public static HashMap<String, Experiment> loadExperimentsToClasses(){

        HashMap<String, Experiment> experiments = new HashMap<String, Experiment>();

        // Build node objects
        // Nodes MUST be built first to comply with constraints on edge database
        try{
            // Retrieve all experiment types in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM experiments");
            String longName, shortName, description, filename;

            // for each node, build an object
            while(rs.next()) {
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                description = rs.getString("description");
                filename = rs.getString("filename");
                if(description == null || description.trim().isEmpty())
                {
                    description = "";
                }

                // create node instance and put it in the nodes HashMap
                Experiment n = new Experiment(longName, shortName, description, filename);
                experiments.put(longName, n);
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
    public static ArrayList<String> getExperimentFiles() {
        return experimentFiles;
    }

        /**
         * Build classes from database
         * @return returns a hash map of all MapNodes, with their nodeID as the key
         */
    public static HashMap<String, Keyword> loadKeywordsToClasses(){

        HashMap<String, Keyword> keywords = new HashMap<String, Keyword>();

        // Build node objects
        // Nodes MUST be built first to comply with constraints on edge database
        try{
            // Retrieve all keyword types in database
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM keywords");
            String longName, shortName, dataType, affix, filename;

            // for each node, build an object
            while(rs.next()) {
                longName = rs.getString("longName");
                shortName = rs.getString("shortName");
                dataType = rs.getString("dataType");
                affix = rs.getString("affix");
                filename = rs.getString("filename");


                // create node instance and put it in the nodes HashMap
                Keyword n = new Keyword(longName, shortName, dataType, affix, "", filename);
                keywords.put(longName, n);
            }
        } catch(SQLException e){
            System.out.println("Failed to load keywords to classes");
            e.printStackTrace();
        }
        return keywords;
    }


}
