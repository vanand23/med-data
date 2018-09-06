package Utilities;

/**
 * Observer class for the Map editor. Simply extend this class and call super() in your constructor, and any changes to
 * the database ever made will call your update function
 *
 *  MAKE SURE TO CALL SUPER IN YOUR CONSTRUCTOR THOUGH YOU HAVE BEEN WARNED
 */
public interface ITypeObserver {
    /**
     * Called whenever the map editor changes. Make sure to subscribe your observer to the MapManager!!
     */
     void onTypeUpdate();


}
