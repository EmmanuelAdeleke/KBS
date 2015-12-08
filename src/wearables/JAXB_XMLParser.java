package wearables;

import java.io.*;
import javax.xml.bind.*;

public class JAXB_XMLParser {

	private JAXBContext jaxbContext = null;     // generate a context to work in with JAXB											   
	private Unmarshaller unmarshaller = null;   // unmarshall = genrate objects from an xml file												
	
	private Database myDatabase = null;        

	public JAXB_XMLParser() {

		try {
			System.out.println("started jaxb");
			jaxbContext = JAXBContext.newInstance("wearables");  // Package that contains our classes																													
			unmarshaller = jaxbContext.createUnmarshaller();
		}
		catch (JAXBException e) {
			System.out.println("problem jaxb");
		}
	}
	

	public Database loadXML(InputStream fileinputstream) {
		try {
			
			System.out.println("Begin to load XML");
			
			Object xmltoobject = unmarshaller.unmarshal(fileinputstream);

			if (myDatabase == null) {

				myDatabase = (Database) (((JAXBElement<?>) xmltoobject).getValue());
				
				return myDatabase;
			}
		} 
		catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("problem loadXML");
		}
		return null;
	}
}
