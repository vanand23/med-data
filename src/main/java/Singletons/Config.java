package Singletons;

import FXMLControllers.FullNamer;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static FXMLControllers.Namer.sharedFilename;

public class Config {
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

    public void setProperty(String propertyToSet, String propertyValue){
        try{
            Properties configFile = new Properties();
            configFile.load(new FileInputStream("config.properties"));
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

    public boolean setFieldFromConfig(JFXTextField fieldToSet, String itemToGet){
        String configItemName = getProperty(itemToGet);
        if(configItemName != null && !configItemName.trim().isEmpty())
        {
            fieldToSet.setText(configItemName);
            switch(itemToGet)
            {
                case "experimentName":
                    sharedFilename.setExperiment(configItemName);
                    break;
                case "sampleNumber":
                    sharedFilename.setSampleNumber(Integer.parseInt(configItemName));
                    break;
                case "trialNumber":
                    sharedFilename.setTrialNumber(Integer.parseInt(configItemName));
                    break;
                case "researcherName":
                    sharedFilename.setResearcher(configItemName);
                    break;
            }

            return true;
        }else{
            return false;
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