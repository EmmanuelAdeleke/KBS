//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.26 at 12:10:59 PM GMT 
//


package wearables;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Store complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Store">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="postcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="region" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phone_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="opening_times" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lat" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="long" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Store", propOrder = {
    "name",
    "address",
    "postcode",
    "city",
    "region",
    "phoneNumber",
    "openingTimes",
    "lat",
    "_long",
    "id"
})
public class Store {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String address;
    @XmlElement(required = true)
    protected String postcode;
    @XmlElement(required = true)
    protected String city;
    @XmlElement(required = true)
    protected String region;
    @XmlElement(name = "phone_number", required = true)
    protected String phoneNumber;
    @XmlElement(name = "opening_times", required = true)
    protected String openingTimes;
    @XmlElement(required = true)
    protected BigDecimal lat;
    @XmlElement(name = "long", required = true)
    protected BigDecimal _long;
    @XmlElement(required = true)
    protected BigInteger id;

    public String getName() {
        return name;
    }
    
    public void setName(String value) {
        this.name = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String value) {
        this.postcode = value;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String value) {
        this.city = value;
    }
 
    public String getRegion() {
        return region;
    }
 
    public void setRegion(String value) {
        this.region = value;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    public String getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(String value) {
        this.openingTimes = value;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal value) {
        this.lat = value;
    }

    public BigDecimal getLong() {
        return _long;
    }

    public void setLong(BigDecimal value) {
        this._long = value;
    }

    public BigInteger getId() {
        return id;
    }
    
    public void setId(BigInteger value) {
        this.id = value;
    }
    
    public String toString() {
    	return "ID: " + id +
    			" Name: " + name + 
    			"\tAddress: " + address +
    			"\tPostcode: " + postcode + 
    			"\tCity: " + city + 
    			"\tRegion: " + region + 
    			"\tOpening Times: " + openingTimes ;
    }

}
