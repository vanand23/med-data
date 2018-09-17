package Utilities;

import Singletons.Database;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import java.util.*;
import java.util.ResourceBundle;

public class Config {

    private String researcherName;
    private String projectName;
    private String experimentName;
    private String description;
    private ArrayList<String> glossariesOfKeywords = new ArrayList<String>();
    private int trialNumber;
    private int sampleNumber;
    private String delimiter;

    private int intialize;
    private Properties configFile = new Properties();
    public Config(){

        try{
            configFile.load(new FileInputStream("config.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getProperty(String key)
    {
        return this.configFile.getProperty(key);
    }

    static public void setProperty(String propertyToSet, String propertyValue){
        try{
            Properties configFile = new Properties();
            configFile.setProperty(propertyToSet, propertyValue);
            File tempFile = new File("config.properties");
            FileOutputStream fos = new FileOutputStream(tempFile);
            configFile.store(fos, "");
            fos.flush();
            fos.close();
        }catch (IOException e0){
            e0.printStackTrace();
        }
    }

    /**
     * Singleton helper class, Config should always be accessed through MapManager.getInstance();
     */
    private static class SingletonHelper{
        private static final Config INSTANCE = new Config();
    }

    /**
     * Gets the singleton instance of config
     * @return the proper single instance of config
     */
    public static Config getInstance(){
        return SingletonHelper.INSTANCE;
    }



}