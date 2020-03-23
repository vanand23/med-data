# File Namer Pro Application
File Namer Pro is a Java application that generates standardized file names. This application was designed with consideration of researchers and experimenters at the Univerity Hospital of Zurich in Switzerland in order to better manage and find their data. 

Descriptive file naming is highly important when conducting research, and clear and detailed file names allow for the quick and easy identification of data files. Furthermore, file names that are standard across projects and research groups can facilitate greater clarity in research.

# Download the app
Download by clicking on this link: <a href="https://github.com/jyxiao1/med-data/releases/download/1.0.0-alpha/FileNamerPro.zip"> Version 1.0.0-alpha </a>

# Manual
The purpose of the manual is to provide step-by-step instructions on how to download, launch, and use File Namer Pro.
The full manual can be found here: [User Manual](User_Manual.pdf)

# Tutorial on Modifying the Code
If you would like to make updates to the code or refine the application to fit your institution's needs, please refer to the following guide on how to download the repository and launch it locally:

Make sure to have the following softwares installed on your device - 
* [IntelliJ](https://www.jetbrains.com/idea/ )
* [Java SE Development Kit 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Git](https://github.com/jyxiao1/med-data)
* [Gradle](https://gradle.org/)
* [JFoenix](https://github.com/jfoenixadmin/JFoenix)
* [Scenebuilder](https://gluonhq.com/products/scene-builder/)

1. Clone or download the project from this repository
2. Open the project in the IntelliJ IDE
3. If there is an error that says “Invalid VCS root mapping: The directory <Project> is registered as a Git root, but not Git repositories we’re found there.” :
    * Click on “configure…” within this error and then click on the <Project> Git row in the version control table. 
    * Go to the pencil located towards the bottom of the table and in the drop down next to VCS and change Git to <none>. 
    * Then click “OK” on the dialog box and then “OK” again on the preferences window. 
4. If there is an error for Gradle that reads “Unlinked Gradle project?: Import Gradle project, this will also enable the Gradle Tool window.” : 
    * Go to the Gradle configuration error (it may be located in a dialog box on the screen or at the bottom of the IntelliJ window. The Event log will show the errors) and click on it. This will open the event log. 
    * Then, click on “Import Gradle Project”. 
      * Make sure that next to “Gradle home”, the location of where Gradle is downloaded is displayed.
      * Go to the drop down box next to “Gradle JVM” and select the 1.8 Java JDK (please do not select “use project JDK”). 
  
    * After this, Gradle should run and build the project.
    
5. Go to the project directory (located on the side panel named “1:Project”) and navigate through the folder structure src/main/java/Launcher. 

6. Right click on the Launcher file located in the Launcher folder and then click on “Run Launcher.main()”. 

7. After this, File Namer Pro will open!

# Project Directory
