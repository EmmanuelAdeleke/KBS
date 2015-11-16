package wearables;

import java.io.*;
import javax.xml.bind.*;

//This is a candidate for a name change because you wont deal with a library any more in your conversion

public class JAXB_XMLParser {

	private JAXBContext jaxbContext = null;     // generate a context to work in with JAXB											   
	private Unmarshaller unmarshaller = null;   // unmarshall = genrate objects from an xml file												
	
	// This is a candidate for a name change because you wont deal with a library any more in your conversion
	private Database myDatabase = null;            // the main object containing all data

	public JAXB_XMLParser() {

		try {
			System.out.println("started jaxb");
			jaxbContext = JAXBContext.newInstance("wearables");  // Package that contains ouer classes																													
			unmarshaller = jaxbContext.createUnmarshaller();
		}
		catch (JAXBException e) {
			
			System.out.println("problem jaxb");
		}
	}
	
	// Instance objects and return a list with this objects in it
	public Database loadXML(InputStream fileinputstream) {

		
		
		try {
			
			System.out.println("started loadXML");
			
			Object xmltoobject = unmarshaller.unmarshal(fileinputstream);

			if (myDatabase == null) {

				
				System.out.println("if reached");
				// generate the mynewlib object that conatins all info from the xml document
				myDatabase = (Database) (((JAXBElement) xmltoobject).getValue());
				// The above (Library) is a candidate for a name change because you wont deal with 
				// a library any more in your conversion
				
				return myDatabase; // return Library Objekt
			}
		} // try

		catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("problem loadXML");
		}
		return null;
	}
}
