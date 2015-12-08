//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.09 at 12:05:08 PM GMT 
//


package wearables;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Database complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Database">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Product" type="{}Product" maxOccurs="unbounded"/>
 *         &lt;element name="Store" type="{}Store" maxOccurs="unbounded"/>
 *         &lt;element name="Availability" type="{}Availability" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * This database holds all elements stored in the Wearables
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Database", propOrder = {
    "product",
    "store",
    "availability"
})
public class Database {

    @XmlElement(name = "Product", required = true)
    protected List<Product> product;
    @XmlElement(name = "Store", required = true)
    protected List<Store> store;
    @XmlElement(name = "Availability", required = true)
    protected List<Availability> availability;
    
    //====================== Get entire lists ==========================//

    // Return all Product objects in the database
    public List<Product> getProducts() {
        if (product == null) {
            product = new ArrayList<Product>();
        }
        return this.product;
    }

    // Return all Store objects in the database
    public List<Store> getStores() {
        if (store == null) {
            store = new ArrayList<Store>();
        }
        return this.store;
    }
    // Return all Availability objects in the database
    public List<Availability> getAvailability() {
        if (availability == null) {
            availability = new ArrayList<Availability>();
        }
        return this.availability;
    }
    
    public List<String> getProdCategories() {
    	List<String> cat = new ArrayList<String>();
    	cat.add("Watch");
    	cat.add("Glass");
    	cat.add("Headphone");
    	cat.add("Jacket");
    	return cat;
    }
    
  //====================== Get content from an ID ==========================//
    
    public Product getProdById(BigInteger id) {
    	Product prodFound = null;
    	for	(int i = 0; i < product.size(); i++){
    		if(product.get(i).getId().compareTo(id) == 0){
    			prodFound = product.get(i);
    			break;
    		}
    	}
    	return prodFound;
    }
    
    public Store getStoreById(BigInteger id) {
    	Store storeFound = null;
    	for	(int i = 0; i < store.size(); i++){
    		if(store.get(i).getId().compareTo(id) == 0){
    			storeFound = store.get(i);
    			break;
    		}
    	}
    	return storeFound;
    }
    
  //====================== Get content from keywords ==========================//
    //helper function
    boolean twoWayContain(String s1, String s2) {
    	boolean val = false;
    	if (s1.contains(s2) || s2.contains(s1)) val = true;
    	return val;
    }
    
    // ------ Products by keyword----------
    
    // Returns how much a product matches a keyword. From 0 to 14.
    public int prodKeywordMatch(Product prod, String keyword) {
    	keyword = keyword.toLowerCase();
    	int match = 0;
    	String category = prod.getCategory().substring(5).toLowerCase(); //Category without the "Smart"
    	// Check if category matches
    	if (keyword.contains(category)) match += 1;
    	// Check if keyword mentions the brand or vice versa
    	if (twoWayContain(keyword, prod.getBrand().toLowerCase())) match += 5;
    	// Check if keyword mentions the name and vice versa.
    	if (twoWayContain(keyword,prod.getName().toLowerCase())) match += 5;
    	// Check if the name was mentioned
    	if (prod.getDescription().toLowerCase().contains(keyword)) match += 3;
    	return match;
    }
    
    // Returns a list where the first items match the keyword the best.
    public List<Product> getProductsByKeyword(String keyword) {
    	List<Product> prods = new ArrayList<Product>();
    	Product prod;
    	int matchNo;
    	// This will hold a reference to each product and how much it matches the keyword
    	final HashMap<BigInteger, Integer> hm = new HashMap<BigInteger, Integer>();
    	
    	// Get all products that match the keyword in some way
    	for	(int i = 0; i < product.size(); i++){
    		prod = product.get(i);
    		matchNo = prodKeywordMatch(prod, keyword);
    		if(matchNo > 0){
    			hm.put(prod.getId(), matchNo);
    			prods.add(product.get(i));
    		}
    	}
    	
    	//Sort the list by match number using the hash map hm.
    	Collections.sort(prods, new Comparator<Object>() {
			@Override
			public int compare(final Object prod1, final Object prod2) {
				final int match1 = (int) hm.get(((Product) prod1).getId());
				final int match2 = (int) hm.get(((Product) prod2).getId());
				return (match1 < match2) ? 1 : -1;
			}
    	});
    	
    	//Return sorted list
    	return prods;
    }
    
    // ------ Stores by keyword----------
    
    // Returns how much a store matches a keyword. From 0 to 14.
    public int storeKeywordMatch(Store store, String keyword) {
    	keyword = keyword.toLowerCase();
    	int match = 0;
    	// Check Matches
    	if (twoWayContain(keyword, store.getName().toLowerCase())) match += 5;
    	if (twoWayContain(keyword, store.getAddress().toLowerCase())) match += 3;	
    	if (twoWayContain(keyword, store.getPostcode().toLowerCase())) match += 10;
    	if (twoWayContain(keyword, store.getCity().toLowerCase())) match += 5;
    	if (twoWayContain(keyword, store.getRegion().toLowerCase())) match += 3;
    	if (twoWayContain(keyword, store.getOpeningTimes().toLowerCase())) match += 10;
    	return match;
    }
    
    // Returns a list where the first items match the keyword the best.
    public List<Store> getStoresByKeyword(String keyword) {
    	List<Store> stores = new ArrayList<Store>();
    	Store astore;
    	int matchNo;
    	// This will hold a reference to each product and how much it matches the keyword
    	final HashMap<BigInteger, Integer> hm = new HashMap<BigInteger, Integer>();
    	
    	// Get all products that match the keyword in some way
    	for	(int i = 0; i < store.size(); i++){
    		astore = store.get(i);
    		matchNo = storeKeywordMatch(astore, keyword);
    		if(matchNo > 0){
    			hm.put(astore.getId(), matchNo);
    			stores.add(store.get(i));
    		}
    	}
    	
    	//Sort the list by match number using the hash map hm.
    	Collections.sort(stores, new Comparator<Object>() {
			@Override
			public int compare(final Object store1, final Object store2) {
				final int match1 = (int) hm.get(((Store) store1).getId());
				final int match2 = (int) hm.get(((Store) store2).getId());
				return (match1 < match2) ? 1 : -1;
			}
    	});
    	
    	//Return sorted list
    	return stores;
    }
    
    public BigInteger getProdStockInStore(BigInteger prodId, BigInteger storeId) {
    	BigInteger stock = BigInteger.valueOf(0);
    	for	(int i = 0; i < availability.size(); i++){
    		//If same prodId and same store
    		if(availability.get(i).getProductId().compareTo(prodId) == 0 && 
    				availability.get(i).getStoreId().compareTo(storeId) == 0){
    			stock = availability.get(i).getQuantity();
    			break;
    		}
    	}
    	return stock;
    }
    
    public List<Store> getStoresWithProd(BigInteger prodId) {
    	List<Store> stores = new ArrayList<Store>();
    	BigInteger zero = BigInteger.valueOf(0);
    	BigInteger storeId;
    	for	(int i = 0; i < availability.size(); i++){
    		// If same prodId and quantity > 0
    		if(availability.get(i).getProductId().compareTo(prodId) == 0 && 
    				availability.get(i).getQuantity().compareTo(zero) > 0){
    			storeId = availability.get(i).getStoreId();
    			stores.add(getStoreById(storeId));
    		}
    	}
    	return stores;
    }
    
    public List<Product> getStoreProducts(BigInteger storeId) {
    	List<Product> products = new ArrayList<Product>();
    	BigInteger zero = BigInteger.valueOf(0);
    	BigInteger prodId;
    	for	(int i = 0; i < availability.size(); i++){
    		// If same storedId and quantity > 0
    		if(availability.get(i).getStoreId().compareTo(storeId) == 0 && 
    				availability.get(i).getQuantity().compareTo(zero) > 0){
    			prodId = availability.get(i).getProductId();
    			products.add(getProdById(prodId));
    		}
    	}
    	return products;
    }
    
}
