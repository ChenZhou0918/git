package perfsonarserver.fetchData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;

/**
 * @author 
 * @since 
 */
public class SaxParserHandler extends DefaultHandler {
  
  /** Actual DelayJitterLossDB object. */
  private DelayJitterLossInterfacePair djli;
  
  /** The list we are going to save the object in. */
  private List<DelayJitterLossInterfacePair> list = new LinkedList<DelayJitterLossInterfacePair>();

  /** This is the builder for the text within the elements. */
  private StringBuilder strBuilder;
  private String temp;

  private String metadataID;
  private Stack elementStack = new Stack();
  private Stack<DelayJitterLossInterfacePair> objectStack = new Stack();
  private Map<String, String> attMap = new HashMap<String, String>();

  
  /**
   * Constructor awaiting a list.
   * @param list The list where the animes are going to be saved in.
   */
  public SaxParserHandler() {
	   
  }

  @Override
  public void startDocument()
  {
    System.out.println( "Document starts." );
  }


  @Override
  public void startElement(String namespaceURI, String localName,
          String qName, Attributes atts) throws SAXException {
	  
	  temp = "";
	  if(qName.equals("nmwg:message")) {
		  System.out.println("test1"); 
	  } else if(qName.equalsIgnoreCase("nmwg:parameter")) {
		  System.out.println("parse parameter attributes...");
	  for (int i = 0; i < atts.getLength(); i++) {
		    String attributeName = atts.getLocalName(i);
		    String attributeValue = atts.getValue(i);
		    
//		    System.out.println("found attribute with localname=" + attributeName 
//		    + " and value=" + attributeValue);
		    attMap.put( attributeName,attributeValue);
		    //System.out.println(attMap.toString());
		}
	  }
	  
	  Iterator it = attMap.entrySet().iterator();
	  String name = "";
	  String value = "";
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found: key: "+pairs.getKey() +" "+ "value: "  +pairs.getValue());
			
		}
  }
	  
//	  temp = "";
//	  if(qName.equalsIgnoreCase("nmwg:message")) {
//		  System.out.println("fu");
//	  } else
//		  if(qName.equalsIgnoreCase("nmwg:parameter")) {
//			  this.djli = new DelayJitterLossInterfacePair();
//			  System.out.println(atts.getLength());
//			 
//			  //Parameterwerte auslesen und Objektwerte setzen
//			  if(atts.getValue("name").equals("receiver") ) {
//				  this.djli.setDestination(atts.getValue("value"));  
//				 System.out.println("value: " +atts.getValue("value"));	
//			  }if(atts.getValue("name").equals("groupsize")) {
//				  this.djli.setGroupsize(Integer.valueOf(atts.getValue("value")));
//				  System.out.println("value: " +atts.getValue("value"));	 
//			  }if(atts.getValue("name").equals("interval")) {
//				  this.djli.setInterval(Integer.valueOf(atts.getValue("value")));
//				  System.out.println("value: " +atts.getValue("value") );	
//			  }if(atts.getValue("name").equals("mid")) {
//				  this.djli.setMid(Integer.valueOf(atts.getValue("value")));
//				  System.out.println("value: " +atts.getValue("value") );	
//			  }if(atts.getValue("name").equals("precedence")) {
//				  this.djli.setPrecedence(atts.getValue("value"));
//				  System.out.println("value: " +atts.getValue("value") );	
//			  }if(atts.getValue("name").equals("receiver_ip")) {
//				  this.djli.setDestinationIP(atts.getValue("value"));
//				  System.out.println("value: " +atts.getValue("value") );	
//			  }if(atts.getValue("name").equals("sender")) {
//				  this.djli.setSource(atts.getValue("value"));
//				  System.out.println("value: " +atts.getValue("value") );
//			  }if(atts.getValue("name").equals("sender_ip")) {
//				  this.djli.setSourceIP(atts.getValue("value"));
//				  System.out.println("value: " +atts.getValue("value"));
//			  }if(atts.getValue("name").equals("packetsize")) {
//				  this.djli.setPacketsize(Integer.valueOf(atts.getValue("value")));
//				  System.out.println("value: " +atts.getValue("value"));
//	    	} this.djli.restmetadataid(); 
//			  this.list.add(djli);
//	    		
//	    	
//			  }
//		  
//			  
//		  
//	  
//	  this.objectStack.push(djli);
	  

		 


  @Override
  public void endElement(String namespaceURI, String localName, String qName) {
	  
   if(qName.equalsIgnoreCase("nmwg:parameter")){
	//   DelayJitterLossInterfacePair object = this.objectStack.pop();
	   //this.list.add(object);
   }
  }
  
  @Override
  public void characters(char ch[], int start, int length) {
	  temp = new String(ch, start, length);
  }
  
  private void printDatas() {  
	  // System.out.println(bookL.size());
	  System.out.println("in der print");
	  for (DelayJitterLossInterfacePair tmpB : this.list) {
	              System.out.println(tmpB.toString());
	  }
  }

  
  @Override
  public void endDocument()
  {
	 // printDatas();
//	  System.out.println("get   Destination: "+djli.getDestination());
//	  System.out.println("get     GroupSize: "+djli.getGroupsize());
//	  System.out.println("get      interval: "+djli.getInterval());
//	  System.out.println("get           Mid: "+djli.getMid());
//	  System.out.println("get    Precedence: "+djli.getPrecedence());
//	  System.out.println("get DestinationIP: "+djli.getDestinationIP());
//	  System.out.println("get        Source: "+djli.getSource());
//	  System.out.println("get      SourceIP: "+djli.getSourceIP());
//	  System.out.println("get    Packetsize: "+djli.getPacketsize());
//	  System.out.println(metadataID);
//	  System.out.println( "Document ends." );
  }

}

