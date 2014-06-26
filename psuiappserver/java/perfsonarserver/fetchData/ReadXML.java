package perfsonarserver.fetchData;

import java.io.File; 
import java.io.IOException; 
import java.util.Iterator;
import java.util.List; 

import org.jdom2.Document; 
import org.jdom2.Element; 
import org.jdom2.JDOMException; 
import org.jdom2.input.SAXBuilder; 
import org.jdom2.output.XMLOutputter; 

import perfsonarserver.fetchData.consts.MessageAttConsts;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;

public class ReadXML {
	
	 public static void main(String[] args) { 

	        Document doc = null; 

	        File f = new File("responseDJLTest.xml"); 

	        try { 
	            // Das Dokument erstellen 
	            SAXBuilder builder = new SAXBuilder(); 
	            doc = builder.build(f); 
	            XMLOutputter fmt = new XMLOutputter(); 

	            // komplettes Dokument ausgeben 
	            fmt.output(doc, System.out); 

	            // Wurzelelement ausgeben 
	            Element element = doc.getRootElement(); 
	            System.out.println("\nWurzelelement: " + element); 

	            // Wurzelelementnamen ausgeben 
	            System.out.println("Wurzelelementname: " + element.getName()); 

	            // Eine Liste aller direkten Kindelemente eines Elementes erstellen 
	            List alleKinder = (List) element.getChildren(); 
	            System.out.println("Erstes Kindelement: " 
	                    + ((Element) alleKinder.get(1))); 

//	            // Eine Liste aller direkten Kindelemente eines benannten 
//	            // Elementes erstellen 
//	            List benannteKinder = element.getChildren("parameter"); 
//
//	            // Das erste Kindelement ausgeben 
//	            System.out.println("benanntes Kindelement: " 
//	                    + ((Element) benannteKinder.get(0)).getName()); 
//
//	            // Wert eines bestimmten Elementes ausgeben 
//	            Element kind = element.getChild("Nachname"); 
//	            System.out.println("Nachname: " + kind.getValue()); 
//
	            // Attribut ausgeben 
	           
	          Element kind2 = element;
	          System.out.println("scheiss pisse: "+kind2.getChild("Body", kind2.getNamespace()).getName());
	          
	  
	          
	        } catch (JDOMException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 
	    } 

}
