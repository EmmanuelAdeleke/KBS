package wearables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Reasoner {

	public List itemList;
	public List storeList;
	public List availabilityList;
	public Database myDatabase;
	public File xmlFile;
	
	public Reasoner(String fileName) {
		itemList = new ArrayList<>();
		storeList = new ArrayList<>();
		availabilityList = new ArrayList<>();
		xmlFile = new File(fileName + ".xml");
	}
	
	public void init() throws FileNotFoundException {
		
		JAXB_XMLParser xmlhandler = new JAXB_XMLParser();
		xmlFile = new File("Wearables.xml");
		FileInputStream readthatfile = new FileInputStream(xmlFile);
		
		myDatabase = xmlhandler.loadXML(readthatfile);
		
		itemList = myDatabase.getProduct();
		storeList = myDatabase.getStore();
		availabilityList = myDatabase.getAvailability();
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		Reasoner reasoner = new Reasoner("Wearables");
		reasoner.init();
		
		for(int i = 0; i < reasoner.itemList.size(); i++) {
			System.out.println(reasoner.itemList.get(i));
		}
		
		for(int i = 0; i < reasoner.storeList.size(); i++) {
			System.out.println(reasoner.storeList.get(i));
		}
		
		for(int i = 0; i < reasoner.availabilityList.size(); i++) {
			System.out.println(reasoner.availabilityList.get(i));
		}
		
	}
	
}
