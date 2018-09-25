package Singletons;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class FXMLManager {
	private static FXMLManager instance = new FXMLManager();
	
	private HashMap<String, FXMLLoader> fxmlLoaders;
	private HashMap<String, Node> nodes;

	private String path;

	private FXMLManager() {
		fxmlLoaders = new HashMap<String, FXMLLoader>();
		nodes = new HashMap<String, Node>();
	}

	public void addFXMLNode(String name, Node node) {
		nodes.put(name, node);
	}

	public void setSearchDirectory(String path) {
		this.path = path;
	}

	public void loadFXML(String... fileNames) throws IOException {
		for(int i = 0; i < fileNames.length; i++) {
			loadFXML(fileNames[i]);
		}
	}

	public Node loadFXML(String fileName) throws IOException {
		FXMLLoader loader =  new FXMLLoader(getClass().getClassLoader().getResource(fileName)); //create the fxml loader
		//loader.setLocation(new File(new String(path + "/" + fileName)).toURI().toURL());
		fxmlLoaders.put(fileName, loader);
		Node node = loader.load();
		nodes.put(fileName, node);
		return node;
	}

	private Node loadFXMLWithName(String name, String fileName) throws IOException {
		FXMLLoader loader =  new FXMLLoader(getClass().getClassLoader().getResource(fileName)); //create the fxml loader
		//loader.setLocation(new File(new String(path + "/" + fileName)).toURI().toURL());
		fxmlLoaders.put(name, loader);
		Node node = loader.load();
		nodes.put(name, node);
		return node;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFXMLNode(String name) {
		return (T)nodes.get(name);
	}

	private boolean hasFXMLNode(String name) {
		return nodes.containsKey(name);
	}

	public boolean hasController(String name) {
		return fxmlLoaders.containsKey(name);
	}

	public boolean hasController(Node node) {
		Object controller = getFXMLController(node);
		if(controller != null) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T getOrLoadFXMLNode(String name) throws IOException {
		if(hasFXMLNode(name)) {
			return getFXMLNode(name);
		}

		return (T)this.loadFXML(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getOrLoadFXMLNode(String name, String fileName) throws IOException {
		if(hasFXMLNode(name)) {
			return getFXMLNode(name);
		}

		return (T)this.loadFXMLWithName(name, fileName);
	}

	public FXMLLoader getFXMLLoader(String name) {
		return fxmlLoaders.get(name);
	}

	private <T> T getFXMLController(String name) {
		FXMLLoader loader = fxmlLoaders.get(name);
		return loader.<T>getController();
	}

	private <T> T getFXMLController(Node node) {
		String key = "";
		Iterator<Entry<String, Node>> it = nodes.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Node> pair = (Entry<String, Node>) it.next();
			if (pair.getValue() == node) {
				key = pair.getKey();
				break;
			}
		}
		return getFXMLController(key);
	}

	public void setVisible(String name) {
		Node node = nodes.get(name);
		if(node != null) {
			node.setVisible(true);
		}
	}

	//for scene swapping within a given parent pane
	public void setVisibleExclusive(String name, String parentName) {
		Node parent = nodes.get(parentName);
		if(parent == null)
			return;
		Node node = nodes.get(name);
		if(node == null)
			return;
		Pane p = (Pane)parent;
		for(Node n : p.getChildren()) {
			n.setVisible(false);
		}
		node.setVisible(true);
	}

	public void setInvisible(String name) {
		Node node = nodes.get(name);
		if(node != null) {
			node.setVisible(false);
		}
	}
	
	public static FXMLManager getInstance() {
		return instance;
	}
}