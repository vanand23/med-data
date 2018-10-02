package Utilities;

import java.io.File;

public class DirectorySearcher {
    public static File[] filesInDirectory(String directory)
    {
        File folder = new File(directory);
        return folder.listFiles();
    }

    public static boolean doesFileExist(String fileToSearch, String directory)
    {
        File[] listOfFiles = filesInDirectory(directory);
        if(listOfFiles != null)
        {
            for(File file : listOfFiles)
            {
                if(file.isFile())
                {
                    if(file.getName().equals(fileToSearch))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
