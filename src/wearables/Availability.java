//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.26 at 12:10:59 PM GMT 
//


package wearables;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Availability complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Availability">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="product_id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Availability", propOrder = {
    "productId",
    "storeId",
    "quantity"
})
public class Availability {

    @XmlElement(name = "product_id", required = true)
    protected BigInteger productId;
    @XmlElement(name = "store_id", required = true)
    protected BigInteger storeId;
    @XmlElement(required = true)
    protected BigInteger quantity;

    public BigInteger getProductId() {
        return productId;
    }

    public void setProductId(BigInteger value) {
        this.productId = value;
    }
 
    public BigInteger getStoreId() {
        return storeId;
    }

    public void setStoreId(BigInteger value) {
        this.storeId = value;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger value) {
        this.quantity = value;
    }
    
    public String toString() {
    	return "Prod_ID: " + productId +
    			"\tStore_ID: " + storeId + 
    			"\tQty: " + quantity ;
    }

}
