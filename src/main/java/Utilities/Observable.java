package Utilities;

import Utilities.Observer;

/**
 * Implement this interface for any class you want to be observable by another object
 */
public interface Observable {

	/**
	 * Register an object as observing this class
	 * @param o Object to register
	 */
	void register(Observer o);

	/**
	 * Notify observers of change
	 */
	void notifyObservers();
}
