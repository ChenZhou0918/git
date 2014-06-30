package perfsonarserver.fetchDataAndProcess;

/** TEST Class for XML response Files 
 * 
 * @author Mirco Bohlmann
 * 
 * */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossInterfacePair;


public class JdomParser {

	public static void main(String[] args) {
        final String fileName = "responseDJLTest.xml";
        Element parameterElement;
        List<Element> parameterElementeListe = new LinkedList<Element>();
        List<DelayJitterLossInterfacePair> dljInterfaceListe = new ArrayList<>();
        org.jdom2.Document jdomDoc;
        
        try {
            //XML Datei einlesen
            jdomDoc = useDOMParser(fileName);
            //Wurzelelement auslesen und weiter durch den Baum wandern
            Element root = jdomDoc.getRootElement();
            Element bodyElement = root.getChild("Body", Namespace.getNamespace("http://schemas.xmlsoap.org/soap/envelope/"));
            Element message = bodyElement.getChild("message", Namespace.getNamespace("http://ggf.org/ns/nmwg/base/2.0/")); 
            //Die Nachkömmlinge des Metadata Elements auslesen
            Iterator<Element> metadata =  message.getDescendants(new ElementFilter());
            while(metadata.hasNext()) {
               parameterElement =  metadata.next();
               //Nur die Parameterelemente für die weiter Verarbeitung in der Liste speichern
        	   if(parameterElement.getChild("parameter", Namespace.getNamespace("http://ggf.org/ns/nmwg/base/2.0/")) != null) {   	
        		   parameterElementeListe.add(parameterElement);
        	   } 
            }
         
            //Liste der Parameterelemente durchgehen und Attribute rauslesen
           	for(Element pm : parameterElementeListe) {
           		
           		if(pm!=null && pm.getAttributeValue("id").contains("param")){
           			Element parameter = pm.getChild("parameter", Namespace.getNamespace("http://ggf.org/ns/nmwg/base/2.0/"));
           			Iterator<Element> it =  pm.getDescendants(new ElementFilter());
           			DelayJitterLossInterfacePair djli = new DelayJitterLossInterfacePair();
           			
           			//Attributwerte den DelayJitterLossInterfacePair Objekten zuweisen
           			while(it.hasNext()) {
           				parameter = it.next();
           				if(parameter.getAttributeValue("name").equals("receiver")) {
                			djli.setDestination(parameter.getAttributeValue("value"));
                		}
                		if(parameter.getAttributeValue("name").equals("groupsize")) {
                			djli.setGroupsize(Integer.valueOf(parameter.getAttributeValue("value")));
                		}
                		if(parameter.getAttributeValue("name").equals("interval")) {
                			djli.setInterval(Integer.valueOf(parameter.getAttributeValue("value")));
                		}
                		if(parameter.getAttributeValue("name").equals("mid")) {
                			djli.setMid(Integer.valueOf(parameter.getAttributeValue("value"))); 
                		}
                		if(parameter.getAttributeValue("name").equals("precedence")) {
                			djli.setPrecedence(parameter.getAttributeValue("value"));
                		}
                		if(parameter.getAttributeValue("name").equals("receiver_ip")) {
                			djli.setDestinationIP(parameter.getAttributeValue("value"));
                		}
                		if(parameter.getAttributeValue("name").equals("sender")) {
                				djli.setSource(parameter.getAttributeValue("value"));
                		}
                		if(parameter.getAttributeValue("name").equals("sender_ip")) {
                			djli.setSourceIP(parameter.getAttributeValue("value"));
                		}
                		if(parameter.getAttributeValue("name").equals("packetsize")) {
                			djli.setPacketsize(Integer.valueOf(parameter.getAttributeValue("value")));
                		} 
                		  
           			}
           			dljInterfaceListe.add(djli);
                	for(DelayJitterLossInterfacePair data : dljInterfaceListe){
                		System.out.println(data);
                	}	
           		} else {
           			System.out.println(pm);
           		}
           	}  	
           	
        } catch (Exception e) {
        		e.printStackTrace();
        }
    }
    

    //Liefert ein JDOM Dokument zurück
    public static org.jdom2.Document useDOMParser(String fileName)
            throws ParserConfigurationException, SAXException, IOException {
        //Das Dokument erstellen
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(fileName));
        DOMBuilder domBuilder = new DOMBuilder();
        return domBuilder.build(doc);

    }
}