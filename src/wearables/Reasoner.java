package wearables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Reasoner {

	public static Database myDatabase; //Database containing all items
	public static File xmlFile;
	private static Scanner reader;
	
	//Terms to refer to the last subject referred to
	public Vector<String> lastSubjectSyn = new Vector<String>(); 
	// Synonyms to store and product
	public Vector<String> productSyn = new Vector<String>(); 
	public Vector<String> storeSyn = new Vector<String>(); 
	
	public String questiontype = "";         // question type selects method to use in a query
	@SuppressWarnings("rawtypes")
	public List classtype = new ArrayList(); // class type selects which class list to query
	public String attributetype = "";        // attribute type selects the attribute to check for in the query

	public Object CurrentSubject; 			 // Last Object dealt with
	public Integer CurrentSubjectIndex;      // Last Index used
	
	public String[][] questionMapping = new String [20][2]; //Holds words and their matching question type.
	
	public Reasoner(String fileName) {
		xmlFile = new File(fileName + ".xml");
	}
	
	public void init() throws FileNotFoundException {
		
		JAXB_XMLParser xmlhandler = new JAXB_XMLParser();
		xmlFile = new File("Wearables.xml");
		FileInputStream readthatfile = new FileInputStream(xmlFile);
		
		myDatabase = xmlhandler.loadXML(readthatfile);
		
		lastSubjectSyn.add(" this");   
		lastSubjectSyn.add(" that");
		lastSubjectSyn.add(" him");
		lastSubjectSyn.add(" her");	
		lastSubjectSyn.add(" it");
		lastSubjectSyn.add(" those");
		lastSubjectSyn.add(" they");
		lastSubjectSyn.add(" them");
		
		productSyn.add(" item");
		productSyn.add(" product");
		productSyn.add(" unit");
		productSyn.add(" stock");	
		
		int x = 0;
		questionMapping[x][0] = "where";
		questionMapping[x][1] = "where";
		
		questionMapping[++x][0] = "closest";
		questionMapping[x][1] = "where";
		
		questionMapping[++x][0] = "which way";
		questionMapping[x][1] = "where";
		
		questionMapping[++x][0] = "postcode";
		questionMapping[x][1] = "where";
		
		questionMapping[++x][0] = "how many";
		questionMapping[x][1] = "howmany";
		
		questionMapping[++x][0] = "number of";
		questionMapping[x][1] = "howmany";
		
		questionMapping[++x][0] = "amount of";
		questionMapping[x][1] = "howmany";
		
		questionMapping[++x][0] = "count ";
		questionMapping[x][1] = "howmany";
		
		questionMapping[++x][0] = "total ";
		questionMapping[x][1] = "howmany";
	
		questionMapping[++x][0] = "there any ";
		questionMapping[x][1] = "howmany";
	
		questionMapping[++x][0] = "have any ";
		questionMapping[x][1] = "howmany";
		
		questionMapping[++x][0] = "how much";
		questionMapping[x][1] = "howmuch";
		
		questionMapping[++x][0] = "s the cost";
		questionMapping[x][1] = "howmuch";
		
		questionMapping[++x][0] = "price";
		questionMapping[x][1] = "howmuch";
		
		questionMapping[++x][0] = "show ";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "do you have";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "describe";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "what are";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "what is";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "store open";
		questionMapping[x][1] = "show";	
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		Reasoner reasoner = new Reasoner("Wearables");
		reasoner.init();

		reader = new Scanner(System.in);
		
		String input = "";
		while (input != "q") {
			System.out.println("Enter a number: ");
			input = reader.nextLine();	
			System.out.println(reasoner.generateAnswer(input));
		}
		
	}
	
	public String generateAnswer(String question) {
		Vector<String> out = new Vector<String>();
		String answer = "";  
		
		question = question.toLowerCase(); // all in lower case because thats easier to analyse
		
		for (int i = 0; i < questionMapping.length; i++) {
			
			if (question.contains(questionMapping[i][0])){
				questiontype = questionMapping[i][1]; 
				question = question.replace("count", "<b>" + questionMapping[i][0] + "</b>");
			}
		}
		
		return question;
	}
	
	public static void test() {
		BigInteger prodId = BigInteger.valueOf(1);
		System.out.println("Product with id: " + prodId);
		System.out.println(myDatabase.getProdById(prodId));
		System.out.println("");
		
		BigInteger storeId = BigInteger.valueOf(111);
		System.out.println("Store with id: " + storeId);
		System.out.println(myDatabase.getStoreById(storeId));
		System.out.println("");
		
		System.out.println("Getting availability of product of id " + prodId + " in store with id " + storeId + ".");
		System.out.println(myDatabase.getProdStockInStore(prodId, storeId));
		System.out.println("");
		
		List<Store> avStores;
		System.out.println("Stores with product of id " + prodId);
		avStores = myDatabase.getStoresWithProd(prodId);
		for(int i = 0; i < avStores.size(); i++) {
			System.out.println(avStores.get(i));
		}
		
		List<Product> avProd;
		System.out.println("Getting products in store " + storeId);
		avProd = myDatabase.getStoreProducts(storeId);
		for(int i = 0; i < avProd.size(); i++) {
			System.out.println(avProd.get(i));
		}
		
		String keyword = "Pebble Watch";
		System.out.println("\nGetting product matches for " + keyword);
		printList(myDatabase.getProductsByKeyword(keyword));
		
		
		keyword = "London";
		System.out.println("\nGetting store matches for " + keyword);
		printList(myDatabase.getStoresByKeyword(keyword));
		
		keyword = "Surrey";
		System.out.println("\nGetting store matches for " + keyword);
		printList(myDatabase.getStoresByKeyword(keyword));
	}
	
	//Helper function
	@SuppressWarnings("rawtypes")
	public static void printList(List alist) {
		for (int i = 0; i< alist.size(); i++) {
			System.out.println(alist.get(i));
		}
	}
	
}
