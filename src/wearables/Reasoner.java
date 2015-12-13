package wearables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Reasoner {

	public static Database myDatabase; //Database containing all items
	public static File xmlFile;
	private static Scanner reader;
	
	// Lists of synonyms
	public Vector<String> lastSubjectSyn = new Vector<String>(); 
	public Vector<String> productSyn = new Vector<String>(); 
	public Vector<String> storeSyn = new Vector<String>(); 
	
	//A list to hold words and their matching question type.
	public String[][] questionMapping = new String [25][2]; 
	
	public static void main(String args[]) throws FileNotFoundException {
		Reasoner reasoner = new Reasoner("Wearables");
		reasoner.init();

		reader = new Scanner(System.in);
		
		String input = "";
		while (input != "q") {
			System.out.println("Enter a question: ");
			input = reader.nextLine();	
			System.out.println(reasoner.generateAnswer(input));
		}
		
	}
	
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
		lastSubjectSyn.add(" it?");
		lastSubjectSyn.add(" it.");
		lastSubjectSyn.add(" it!");
		lastSubjectSyn.add(" it ");
		lastSubjectSyn.add(" those");
		lastSubjectSyn.add(" they");
		lastSubjectSyn.add(" them");
		
		productSyn.add(" item");
		productSyn.add(" product");
		productSyn.add(" unit");
		productSyn.add(" stock");	
		
		storeSyn.add("store");
		storeSyn.add("seller");
		
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
		
//		questionMapping[++x][0] = "do you have";
//		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "describe";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "what are";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "what is";
		questionMapping[x][1] = "show";
		
		questionMapping[++x][0] = "store open";
		questionMapping[x][1] = "show";	
		
		questionMapping[++x][0] = "bye";
		questionMapping[x][1] = "farewell";
		
		questionMapping[++x][0] = "farewell";
		questionMapping[x][1] = "farewell";
		
		questionMapping[++x][0] = "see you";
		questionMapping[x][1] = "farewell";
		
		questionMapping[++x][0] = "hasta la vista";
		questionMapping[x][1] = "farewell";
		
		questionMapping[++x][0] = "cheers";
		questionMapping[x][1] = "farewell";
		
		questionMapping[++x][0] = "thanks";
		questionMapping[x][1] = "farewell";
	}
	

	
	
	public String generateAnswer(String question) {
		String answer = "";
		String questionType = "";  					// question type selects method to use in a query
		
		question = question.toLowerCase(); // all in lower case because thats easier to analyse
		
		// Check question type 
		questionType = getQuestionType(question);
		
		// Check question subject
		AnalysisResult qAn  = analyseQuestion(question);
		
		// Answer accordingly
		switch (questionType) {
	        case "howmany":
	        	answer = answerHowMany(qAn);
	            break;
	        default: 
	        	answer = answerUnknownCase(qAn);
	            break;
	    }
		
		return answer;
	}
	
	//=============================================================================================
	//============================ Functions to create Answers ====================================
	//=============================================================================================
	
	public String answerHowMany(AnalysisResult qAn) {
		String trimmedQuestion = qAn.trimmedQuestion; 					// Question without class synonyms found.
		HashMap<String, Integer> detectedClasses = qAn.detectedClasses; //Classes detected and their match score

		int prodScore = detectedClasses.getOrDefault("Product", 0);
		int prodCategoryScore = detectedClasses.getOrDefault("ProductClass", 0);
		int storeScore = detectedClasses.getOrDefault("Store", 0);
		int sProdScore = detectedClasses.getOrDefault("SpecificProduct", 0);
		int sStoreScore = detectedClasses.getOrDefault("SpecificStore", 0);
		int storeAreaScore = detectedClasses.getOrDefault("StoreArea", 0);
		int amount = 0;
		String subj1 = "";
		String subj2 = "";
		String answer = "";
		
		// # of such product...
		if (sProdScore > 0) {
			Product prod = qAn.productsFound.get(0);
			subj1 = prod.getName();
			
			// in such store
			if (sStoreScore >0) {
				amount = myDatabase.getProdStockInStore(prod.getId(), qAn.storesFound.get(0).getId());
				subj2 = qAn.storesFound.get(0).getName();
				answer = "We have " + amount + " " + subj1 + "s at " + subj2 + " store.";
			} 
			// in such areas (Could be more than one)
			else if (storeAreaScore > 0) {
				amount = 0;
				String cityListing = "";
				String storeListing = "";
				for (int i = 0; i < qAn.storeAreasFound.size(); i++) {
					String cityName = qAn.storeAreasFound.get(i);
					amount += myDatabase.getProdStockInCity(prod.getId(), cityName);
					storeListing += "\n" + listToString(myDatabase.getStoresByCity(cityName));
					
					//If list has more than one item and it is the last item in the list
					if (qAn.storeAreasFound.size() > 1 && qAn.storeAreasFound.size() - i == 1){
						cityListing += " and";
					}
					cityListing += cityName;
				}
				answer = "We have " + amount + " " + subj1 + "s in " + cityListing + ".";
				answer += "\nYou can find it at these stores: " + storeListing;
			} 
			// (# of stores that have such product)
			else if (storeScore > 0) {
				List<Store> storeList = myDatabase.getStoresWithProd(prod.getId());
				amount = storeList.size();
				answer = "There are " + amount + " stores with " + prod.getName() + " in stock.\nThese are:\n" + listToString(storeList);
			}
			// overall
			else if (sProdScore > 0) {
				amount = myDatabase.getTotalProductStock(prod.getId());
				answer = "We have " + amount + " " + subj1 + "s distributed across our stores.";
			}
		}
		//# in a specific store store
		else if (sStoreScore > 0) {
			Store store = qAn.storesFound.get(0);
			// # of a prod class in specific store
			if (prodCategoryScore > 0) {
				String categoryFound = qAn.prodCategoriesFound.get(0);
				List<Product> prodList = myDatabase.getProdInStoreByCategory(store.getId(), categoryFound);
				answer = "We have " + amount + " " + categoryFound + "s at the " + store.getName() + " store.";
			}
			// # of products in general in specific store
			else {
				BigInteger storeId = qAn.storesFound.get(0).getId();
				String storeName = qAn.storesFound.get(0).getName();
				amount = myDatabase.getTotalStoreTock(storeId);
				List<Product> prodList = myDatabase.getStoreProducts(storeId);
				
				answer = "There are " + amount + " products at the " + storeName + ", amongst which you can find:\n" + listToString(prodList);
			}
			
		}
		
		return answer;
	}
	
	public String answerUnknownCase(AnalysisResult qAn) {
		String answer = "";
		// Let's give a generic answer with matching items.			
		if ((qAn.productsFound.size() + qAn.storesFound.size()) > 0) {
			answer = "Here are some results that may interest you: \n";
			if (qAn.productsFound.size() > 0) {
				answer += "\nProducts:\n" +	listToString(qAn.productsFound);
			}
			if (qAn.storesFound.size() > 0) {
				answer += "\nStores:\n" + listToString(qAn.storesFound);
				printList(qAn.storesFound);
			}
		} else { //Nothing identified at all
			answer = "Uh, I didn't quite catch that. Can you rephrase it, please.";
		}
		return answer;
	}
	//=============================================================================================
	//============================ Question analysis functions ====================================
	//=============================================================================================
	
	public String getQuestionType(String question) {
		String keyword, type, questionType = "";
		
		//Check against all keywords to determine the question type.
		for (int i = 0; i < questionMapping.length; i++) {	
			keyword = questionMapping[i][0];
			type = questionMapping[i][1];
			if (question.contains(keyword)){
				questionType = type; 
				question = question.replace(keyword, "<b>" + keyword + "</b>");
			}
		}
		return questionType;
	}
	
	
	public AnalysisResult analyseQuestion(String question) {
		Vector<String> synList;
		String synonym;
		String trimmedQuestion = question; // Question without class synonyms found.
		int score; // hold scores temporarily
		
		// This hash map will hold detected class name and their match score.
		HashMap<String, Integer> detectedClasses = new HashMap<String, Integer>();
		
		// List of synonyms lists
		HashMap<String, Vector<String>> subjectsSyn = new HashMap<String, Vector<String>>();
		subjectsSyn.put("Product", productSyn);
		subjectsSyn.put("Store", storeSyn);
		subjectsSyn.put("LastSubject", lastSubjectSyn);
		
		// List to hold results found
		List<Product> productsFound;
		List<Store> storesFound;
		List<String> prodCategoriesFound;
		List<String> storeAreasFound;
		
		
		// Check for class synonyms
		for (String className : subjectsSyn.keySet()) {
			
			synList = subjectsSyn.get(className);
			for (int x = 0; x < synList.size(); x++) { 

				synonym = synList.get(x);
				if (question.contains(synonym)) {

					//Add score to hashMap
					score = detectedClasses.getOrDefault(className, 0);
					detectedClasses.put(className, score + 2); 

					question = question.replace(synonym, "<b>"+synonym+"</b>");
					System.out.println("Class type " + className + " recognised.");

					trimmedQuestion = trimmedQuestion.replace(synonym, " ");
				}
			}

		}
		
		trimmedQuestion.trim(); // Replacing synonyms with white spaces may have left too many spaces.
		
		// Maybe the question is asking for a specific item, so let's search using everything in
		// the question that is not a found synonym as a keyword.
		productsFound = myDatabase.getProductsByKeyword(trimmedQuestion);
		score = productsFound.size()*2;
		if (productsFound.size() > 0 ) detectedClasses.put("SpecificProduct", score); 
		
		prodCategoriesFound = myDatabase.getProdCategoriesByKeyword(trimmedQuestion);
		score = prodCategoriesFound.size() * 5;
		if (prodCategoriesFound.size() > 0 )  detectedClasses.put("ProductClass", score);
		
		storesFound = myDatabase.getStoresByKeyword(trimmedQuestion);
		score = storesFound.size()*2;
		if (storesFound.size() > 0 )  detectedClasses.put("SpecificStore", score); 
		
		storeAreasFound = myDatabase.getStoreAreasByKeyword(trimmedQuestion);
		score = storeAreasFound.size()*2;
		if (storeAreasFound.size() > 0 )  detectedClasses.put("StoreArea", score);
		
		
		// Wrapper object for the result
		AnalysisResult result = new AnalysisResult(productsFound, prodCategoriesFound, storesFound, storeAreasFound, detectedClasses, trimmedQuestion);
		return result;
	}
	
	//=============================================================================================
	//================================ Helper functions ===========================================
	//=============================================================================================
	
	@SuppressWarnings("rawtypes")
	public static void printList(List alist) {
		for (int i = 0; i< alist.size(); i++) {
			System.out.println(alist.get(i));
		}
	}
	
	public static String capitaliseFirstLetter (String input) {
		String output = input.substring(0, 1).toUpperCase() + input.substring(1);
		return output;
	}
	
	@SuppressWarnings("rawtypes")
	public static String listToString(List alist) {
		String listString = "";
		for (int i = 0; i< alist.size(); i++) {
			listString += alist.get(i) + "\n";
		}
		return listString;
	}
	
	
	//=============================================================================================
	//=================================== Helper classes ==========================================
	//=============================================================================================
	
	public static class AnalysisResult {
		public List<Product> productsFound;
		public List<String> prodCategoriesFound;
		public List<Store> storesFound;
		public List<String> storeAreasFound;
		public HashMap<String, Integer> detectedClasses;
		public String trimmedQuestion;
		
		//Constructor setting everything.
		protected AnalysisResult(List<Product> productsFound, List<String> prodCategoriesFound, List<Store> storesFound, 
				List<String> storeAreasFound, HashMap<String, Integer> detectedClasses, String trimmedQuestion) {
			this.productsFound = productsFound;
			this.storesFound = storesFound;
			this.detectedClasses = detectedClasses;
			this.trimmedQuestion = trimmedQuestion;
			this.storeAreasFound = storeAreasFound;
			this.prodCategoriesFound = prodCategoriesFound;
		}		
	}
	
	//=============================================================================================
	//=================================== Test routine ============================================
	//=============================================================================================
	
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
	
}
