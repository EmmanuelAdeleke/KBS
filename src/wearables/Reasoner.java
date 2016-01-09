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
import java.util.regex.Pattern;

public class Reasoner {

	public static Database myDatabase; //Database containing all items
	public static File xmlFile;
	private static Scanner reader;
	
	// Lists of synonyms
	public Vector<String> lastSubjectSyn = new Vector<String>(); 
	public Vector<String> productSyn = new Vector<String>(); 
	public Vector<String> storeSyn = new Vector<String>(); 
	
	//A list to hold words and their matching question type.
	public HashMap<String, String> questionMapping = new HashMap<String, String>(); 
	
	public String lastQuestionType = "";
	public String lastSubjectType = "";
	@SuppressWarnings("rawtypes")
	public List lastSubjectList = new ArrayList<String>(); //This will hold content from last question.
	
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
		
		questionMapping.put("where", "where");
		
		questionMapping.put("closest", "where");
		
		questionMapping.put("which way", "where");
		
		questionMapping.put("postcode", "where");
		
		questionMapping.put("what do you have", "describe");
		
		questionMapping.put("what", "describe");
		
		questionMapping.put("have", "describe");
		
		questionMapping.put("any", "describe");
		
		questionMapping.put("how many", "describe");
		
		questionMapping.put("number of", "describe");
		
		questionMapping.put("amount of", "describe");
		
		questionMapping.put("count ", "describe");
		
		questionMapping.put("total ", "describe");
	
		questionMapping.put("there any ", "describe");
	
		questionMapping.put("have any ", "describe");
		
		questionMapping.put("show ", "describe");
		
		questionMapping.put("describe", "describe");
		
		questionMapping.put("store open", "describe");	
		
		questionMapping.put("bye", "farewell");
		
		questionMapping.put("farewell", "farewell");
		
		questionMapping.put("see you", "farewell");
		
		questionMapping.put("hasta la vista", "farewell");
		
		questionMapping.put("cheers", "farewell");
		
		questionMapping.put("thanks", "farewell");
		
		questionMapping.put("how about", "lastQuestion");
	}
	
	// Return single product at 0
	public Product getSingleProduct(String question) {
		return analyseQuestion(question).productsFound.get(0);
	}
	
	public Store getSingleStore(String question) {
		return analyseQuestion(question).storesFound.get(0);
	}
	
	public String generateAnswer(String question) {
		String answer = "";
		String questionType = "";  					// question type selects method to use in a query
		
		question = question.toLowerCase(); // all in lower case because thats easier to analyse
		
		// Check question type 
		questionType = getQuestionType(question);
//		System.out.println(questionType);
		
		// Check question subject
		AnalysisResult qAn  = analyseQuestion(question);
		// If the question is "how about this.." then repeat the last question type.
		if (questionType.compareTo("lastQuestion") == 0 && lastQuestionType.compareTo("") != 0) {
			questionType = lastQuestionType;
		}
		// Answer accordingly
		switch (questionType) {
	        case "describe":
	        	answer = answerDescribe(qAn);
	            break;
	        case "farewell":
	        	answer = answerFarewell(qAn);
	            break;
	        case "where":
	        	answer = answerWhere(qAn);
	            break;
	        default: 
	        	answer = answerUnknownCase(qAn);
	            break;
	    }
		lastQuestionType = questionType;
		return answer;
	}
	
	
	//=============================================================================================
	//============================ Question analysis functions ====================================
	//=============================================================================================
	
	public String getQuestionType(String question) {
		String questionType = "";
		
		//Check against all keywords to determine the question type.
		
		for (String keyword : questionMapping.keySet()) {
			if (question.contains(keyword)){
				questionType = questionMapping.get(keyword); 
//				question.replaceAll(keyword, "<b>" + keyword + "</b>");
			}
		}
		
		return questionType;
	}
	
	
	@SuppressWarnings("unchecked")
	public AnalysisResult analyseQuestion(String question) {
		Vector<String> synList;
		String synonym;
		String trimmedQuestion = question; // Question without class synonyms found.
		int score; // hold scores temporarily
		
		// This hash map will hold detected class names and their match score.
		HashMap<String, Integer> detectedClasses = new HashMap<String, Integer>();
		
		// List of synonyms lists
		HashMap<String, Vector<String>> subjectsSyn = new HashMap<String, Vector<String>>();
		subjectsSyn.put("Product", productSyn);
		subjectsSyn.put("Store", storeSyn);
		subjectsSyn.put("lastSubject", lastSubjectSyn); //Last subject type will change in every iteration.
		
		// List to hold results found
		List<Product> productsFound = new ArrayList<Product>();
		List<Store> storesFound = new ArrayList<Store>();;
		List<String> prodCategoriesFound = new ArrayList<String>();;
		List<String> storeAreasFound = new ArrayList<String>();;
		
		
		// Check for class synonyms
		for (String className : subjectsSyn.keySet()) {
			
			synList = subjectsSyn.get(className);
			for (int x = 0; x < synList.size(); x++) { 

				synonym = synList.get(x);
				if (question.contains(synonym)) {

					//Add score to hashMap
					score = detectedClasses.getOrDefault(className, 0);
					detectedClasses.put(className, score + 2); 

//					question = question.replace(synonym, "<b>"+synonym+"</b>");
//					System.out.println("Class type " + className + " recognised.");
					
					trimmedQuestion = trimmedQuestion.replace(synonym, " ");
				}
			}
		}
		
		// Pass the score that 'lastSubject' got to the score of the last subject's class.
		// and put the items found in the last question in the relative list.
		if (detectedClasses.getOrDefault("lastSubject", 0) > 0 && lastSubjectType.compareTo("") != 0) {
			int lastSubjScore = detectedClasses.getOrDefault("lastSubject", 0);
			score = detectedClasses.getOrDefault(lastSubjectType, 0);
			detectedClasses.put(lastSubjectType, score + lastSubjScore);
			
			switch (lastSubjectType) {
				case "SpecificProduct":
					productsFound = lastSubjectList;
					break;
				case "ProductCategory":
					prodCategoriesFound = lastSubjectList;
					break;
				case "SpecificStore":
					storesFound = lastSubjectList;
					break;
				case "StoreArea":
					storeAreasFound = lastSubjectList;
					break;
			}
		}
		
		trimmedQuestion.trim(); // Replacing synonyms with white spaces may have left too many spaces.
		
		// Maybe the question is asking for a specific item, so let's search using everything in
		// the question that is not a found synonym as a keyword.
		@SuppressWarnings("rawtypes")
		List stuffFound = new ArrayList<String>();
		
		stuffFound = myDatabase.getProductsByKeyword(trimmedQuestion);
		if (stuffFound.size() > 0 ) {
			productsFound = stuffFound;
			score = detectedClasses.getOrDefault("SpecificProduct", 0) + productsFound.size()*2;
			detectedClasses.put("SpecificProduct", score); 
		}
		
		stuffFound =  myDatabase.getProdCategoriesByKeyword(trimmedQuestion);
		if (stuffFound.size() > 0 ) {
			prodCategoriesFound = stuffFound;
			score = detectedClasses.getOrDefault("ProductCategory", 0) + prodCategoriesFound.size() * 5;
			detectedClasses.put("ProductCategory", score);
		}
		
		stuffFound =  myDatabase.getStoresByKeyword(trimmedQuestion);
		if (stuffFound.size() > 0 ) {
			storesFound = stuffFound;
			score = detectedClasses.getOrDefault("SpecificStore", 0) + storesFound.size()*2;
			detectedClasses.put("SpecificStore", score); 
		}
		
		stuffFound = myDatabase.getStoreAreasByKeyword(trimmedQuestion);
		if (stuffFound.size() > 0 ) {
			storeAreasFound = stuffFound;
			score = detectedClasses.getOrDefault("StoreArea", 0) + storeAreasFound.size()*2;
			detectedClasses.put("StoreArea", score);
		}
		
		/// FOR DEBUGGING =====================================================
//		System.out.println("lastSubjectType = " + lastSubjectType + ".");
//		System.out.println("lastSubjectList = ");
//		printList(lastSubjectList);
//		System.out.println(detectedClasses.toString());
		/// END OF FOR DEBUGGING =====================================================
		
		// Wrapper object for the result
		AnalysisResult result = new AnalysisResult(productsFound, prodCategoriesFound, storesFound, storeAreasFound, detectedClasses, trimmedQuestion);
		return result;
	}
	
	//=============================================================================================
		//============================ Functions to create Answers ====================================
		//=============================================================================================
		
		//============== HOW MANY ================
		public String answerDescribe(AnalysisResult qAn) {
			HashMap<String, Integer> detectedClasses = qAn.detectedClasses; //Classes detected and their match score

			int prodScore = detectedClasses.getOrDefault("Product", 0);
			int prodCategoryScore = detectedClasses.getOrDefault("ProductCategory", 0);
			int storeScore = detectedClasses.getOrDefault("Store", 0);
			int sProdScore = detectedClasses.getOrDefault("SpecificProduct", 0);
			int sStoreScore = detectedClasses.getOrDefault("SpecificStore", 0);
			int storeAreaScore = detectedClasses.getOrDefault("StoreArea", 0);
			int amount = 0;
			String subj1 = "";
			String subj2 = "";
			String answer = "";
			
			//// # of such product...
			if (sProdScore > 0) {
				lastSubjectType = "SpecificProduct";
				Product prod = qAn.productsFound.get(0);
				subj1 = prod.getName();
				// ..in such store
				if (sStoreScore >0) {
					amount = myDatabase.getProdStockInStore(prod.getId(), qAn.storesFound.get(0).getId());
					subj2 = qAn.storesFound.get(0).getName();
					answer = "We have " + amount + " " + subj1 + "s at " + subj2 + " store.";
				} 
				// ..in such areas (Could be more than one)
				else if (storeAreaScore > 0) {
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
					answer = "We have " + amount + " " + getPlural(subj1) + " in " + cityListing + ".";
					answer += "\nYou can find it at these stores: " + storeListing;
				} 
				// (# of stores that have such product)
				else if (storeScore > 0) {
					List<Store> storeList = myDatabase.getStoresWithProd(prod.getId());
					amount = storeList.size();
					answer = "There are " + amount + " stores with " + prod.getName() + " in stock.\nThese are:\n" + listToString(storeList);
				}
				// ..overall
				else {
					amount = myDatabase.getTotalProductStock(prod.getId());
					answer = "We have " + amount + " " + getPlural(subj1) + " distributed across all of our stores.";
				}
			}
			//// # of products of a specific category
			else if (prodCategoryScore > 0) {
				subj1 = qAn.prodCategoriesFound.get(0);
				lastSubjectType = "ProductCategory";
				
				// ..in such store
				if (sStoreScore >0) {
					Store store = qAn.storesFound.get(0);
					List<Product> prodList = myDatabase.getProdInStoreByCategory(store.getId(), subj1);
					amount = prodList.size();
					subj2 = qAn.storesFound.get(0).getName();
					answer = "We have " + amount + " " + getPlural(subj1) + " at " + subj2 + " store.";
				} 
				// ..in such areas (Could be more than one)
				else if (storeAreaScore > 0) {
					amount = 0;
					String cityListing = "";
					String storeListing = "";
					for (int i = 0; i < qAn.storeAreasFound.size(); i++) {
						String cityName = qAn.storeAreasFound.get(i);
						amount += myDatabase.getProdCategoryStockInCity(subj1, cityName);
						storeListing += "\n" + listToString(myDatabase.getStoresByCity(cityName));
						
						//If list has more than one item and it is the last item in the list
						if (qAn.storeAreasFound.size() > 1 && qAn.storeAreasFound.size() - i == 1){
							cityListing += " and";
						}
						cityListing += cityName;
					}
					answer = "We have " + amount + " " + getPlural(subj1) + " in " + cityListing + ".";
					answer += "\nYou can find it at these stores: " + storeListing;
				} 
				// (# of stores that have such product category)
				else if (storeScore > 0) {
					List<Store> storeList = myDatabase.getStoresWithProdCategory(subj1);
					amount = storeList.size();
					answer = "There are " + amount + " stores with " + getPlural(subj1) + " in stock.\nThese are:\n" + listToString(storeList);
				}
				// ..overall
				else {
					amount = myDatabase.getProdCategoryTotalStock(subj1);
					answer = "We have " + amount + " " + getPlural(subj1) + " distributed across our stores.";
				} 
			}
			//// # of products in general in a specific store store
			else if (sStoreScore > 0) {
				lastSubjectType = "SpecificStore";	
				BigInteger storeId = qAn.storesFound.get(0).getId();
				String storeName = qAn.storesFound.get(0).getName();
				amount = myDatabase.getTotalStoreStock(storeId);
				List<Product> prodList = myDatabase.getStoreProducts(storeId);
				
				answer = "There are " + amount + " products at the " + storeName + ", amongst which you can find:\n" + listToString(prodList);		
			}
			//// # of products in general ...
			else if (prodScore > 0) {
				lastSubjectType = "Product";
				// .. in specific area
				if (storeAreaScore > 0) {
					String cityName = qAn.storeAreasFound.get(0);
					List<Store> storeList = myDatabase.getStoresByCity(cityName);
					for (int i = 0; i < storeList.size(); i++) {
						amount += myDatabase.getTotalStoreStock(storeList.get(i).getId());
					}
					answer = "There is a total of " + amount + " products in our stores in " + cityName + ".";						
				}
				// ..overall
				else {
					amount = myDatabase.getTotalStock();		
					answer = "There is a total of " + amount + " products across all of our stores.";
				}			
			}
			//# of stores..
			else if (storeScore > 0) {
				lastSubjectType = "Store";
				
				// .. in specific area
				if (storeAreaScore > 0) {
					String cityName = qAn.storeAreasFound.get(0);
					amount = myDatabase.getStoresByCity(cityName).size();
					answer = "We have " + amount + " stores in " + cityName;
				}
				// .. in general
				else {
					amount = myDatabase.getStores().size();
					answer = "We have " + amount + " stores distributed throughout all of the UK.";
				}		
			}
			// If is none of the above, then I don't know.
			else {
				answer = "I didn't quite get the end there. What exactly would you like to know the amount of?";
			}
			
			setLastSubjectList(lastSubjectType, qAn);
			return answer;
		}
		//============== END OF HOW MANY ================
		
		//============== WHERE ================
		public String answerWhere(AnalysisResult qAn) {
			HashMap<String, Integer> detectedClasses = qAn.detectedClasses; //Classes detected and their match score


			int prodCategoryScore = detectedClasses.getOrDefault("ProductCategory", 0);
			int storeScore = detectedClasses.getOrDefault("Store", 0);
			int sProdScore = detectedClasses.getOrDefault("SpecificProduct", 0);

			int storeAreaScore = detectedClasses.getOrDefault("StoreArea", 0);
			int amount = 0;
			String subj1 = "";
			String answer = "";
			
			//// such product...
			// (# of stores that have such product)
			if (sProdScore > 0) {
				Product prod = qAn.productsFound.get(0);
				subj1 = prod.getName();
				lastSubjectType = "SpecificProduct";
				List<Store> storeList = myDatabase.getStoresWithProd(prod.getId());
				amount = storeList.size();
				answer = "There are " + amount + " stores with " + prod.getName() + " in stock.\nThese are:\n" + listToString(storeList);
			}
			//// a specific category
			else if (prodCategoryScore > 0) {
				subj1 = qAn.prodCategoriesFound.get(0);
				lastSubjectType = "ProductCategory";	
				// ..in such areas (Could be more than one)
				if (storeAreaScore > 0) {
					amount = 0;
					String cityListing = "";
					String storeListing = "";
					for (int i = 0; i < qAn.storeAreasFound.size(); i++) {
						String cityName = qAn.storeAreasFound.get(i);
						amount += myDatabase.getProdCategoryStockInCity(subj1, cityName);
						storeListing += "\n" + listToString(myDatabase.getStoresByCity(cityName));
						
						//If list has more than one item and it is the last item in the list
						if (qAn.storeAreasFound.size() > 1 && qAn.storeAreasFound.size() - i == 1){
							cityListing += " and";
						}
						cityListing += cityName;
					}
					answer = "We have " + amount + " " + getPlural(subj1) + " in " + cityListing + ".";
					answer += "\nYou can find it at these stores: " + storeListing;
				} 
				// (stores that have such product category)
				else {
					List<Store> storeList = myDatabase.getStoresWithProdCategory(subj1);
					amount = storeList.size();
					answer = "There are " + amount + " stores with " + getPlural(subj1) + " in stock.\nThese are:\n" + listToString(storeList);
				}
			}
			// stores..
			else if (storeScore > 0) {
				lastSubjectType = "Store";
				
				// .. in specific area
				if (storeAreaScore > 0) {
					String cityName = qAn.storeAreasFound.get(0);
					amount = myDatabase.getStoresByCity(cityName).size();
					List<Store> storeList = myDatabase.getStoresByCity(cityName);
					answer = "We have " + amount + " stores in " + cityName + ".\nThese are:\n" + listToString(storeList);
				}
				// .. in general
				else {
					amount = myDatabase.getStores().size();
					answer = "We have " + amount + " stores distributed throughout all of the UK.";
				}		
			}
			// If is none of the above, then I don't know.
			else {
				answer = "I didn't quite get the end there. What exactly would you like to know the amount of?";
			}
			
			setLastSubjectList(lastSubjectType, qAn);
			return answer;
		}
		//============== END OF WHERE================
		
		//============== Farewell ================
		public String answerFarewell(AnalysisResult qAn) {
			return "Bye bye. Don't forget to visit our website at www.theWerablesStore.co.uk";
		}
		
		//============== Unknown case ================
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
				}
			} else { //Nothing identified at all
				answer = "Uh, I didn't quite catch that. Can you rephrase it, please.";
			}
			return answer;
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
	
	public static String getPlural(String astring) {
		String plural = astring;
		if (astring.substring(astring.length() - 1, astring.length()).compareTo("s") != 0) {
			plural = astring + "es";
		}
		return plural;
	}
	
	private void setLastSubjectList(String lastSubjectType2, AnalysisResult qAn) {
		switch (lastSubjectType2) {
		case "SpecificProduct":
			lastSubjectList = qAn.productsFound;
			break;
		case "ProductCategory":
			lastSubjectList = qAn.prodCategoriesFound;
			break;
		case "SpecificStore":
			lastSubjectList = qAn.storesFound;
			break;
		case "StoreArea":
			lastSubjectList = qAn.storeAreasFound;
			break;
		}
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
