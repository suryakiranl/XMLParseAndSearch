package edu.cmu.sv.surya.parsers.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class XMLParserUtil {
	public static void main(String[] args) {
		XMLParserUtil dxp = new XMLParserUtil();
		long now = new Date().getTime();
		try {
			dxp.parseAndPrintTagsAndAttributes("/Users/suryakiran/Downloads/SOC-HW/dblp.xml");
//			dxp.parseAndPrintTagsAndAttributes("/Users/suryakiran/Downloads/SOC-HW/tags.xml");
		} catch (FileNotFoundException | XMLStreamException | SQLException
				| ClassNotFoundException e) {
			System.out.println("An error occurred when parsing tags file.");
			System.out.println("Error details:: " + e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("Time taken = " + (new Date().getTime() - now) + "ms");
		}
	}
	
	public void parseAndPrintTagsAndAttributes(String tagFilePath) throws FileNotFoundException,
			XMLStreamException, SQLException, ClassNotFoundException {
		// Corner case validation
		if (tagFilePath == null || "".equals(tagFilePath.trim())) {
			return;
		}
		
		Map<String, TreeSet<String>> attrMap = new TreeMap<String, TreeSet<String>>();
		Set<String> xmlTagNames = new TreeSet<String>();
		int level = 0;

		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory
					.createXMLStreamReader(new FileInputStream(tagFilePath));
			
			
			String xmlTagName = null;
			String tagAttrName = null;
			
			while (reader.hasNext()) {
				int event = reader.next();

				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					xmlTagName = reader.getLocalName();
					if(xmlTagNames.add(level + " - " + xmlTagName)) {
						print("<"+ xmlTagName + ">", level);
					}
					for (int loop = 0; loop < reader.getAttributeCount(); loop++ ) {
						tagAttrName = reader.getAttributeLocalName(loop);
						TreeSet<String> tagAttrs = attrMap.get(level + " - " + xmlTagName);
						if(tagAttrs == null) {
							tagAttrs = new TreeSet<String>();
						}
						if(tagAttrs.add(tagAttrName)) {
							print("\tXML Tag = " + xmlTagName + ", Attrib = " + tagAttrName, level);
						}
						attrMap.put(level + " - " + xmlTagName, tagAttrs);
					}
					level++;
					break;
				case XMLStreamConstants.CHARACTERS:
					break;
				case XMLStreamConstants.END_ELEMENT:
					level--;
					break;
				default:
					break;
				}
			}

		} finally {
//			System.out.println("List of all unique tag names ::");
//			for(String xmlTagName : xmlTagNames) {
//				System.out.println("\t" + xmlTagName);
//			}
//			
//			System.out.println("Listing all tag names specific to tags ::");
//			for(String attrMapKey : attrMap.keySet()) {
//				System.out.print("\t XML Tag = " + attrMapKey + "(" + attrMap.get(attrMapKey).size() + ") :: ");
//				for(String attrTag : attrMap.get(attrMapKey) ) {
//					System.out.print(attrTag + ", " );
//				}
//				System.out.println();
//			}
		}
	}
	
	private void print(String value, int level) {
		StringBuffer sb = new StringBuffer();
		for(int loop = 0 ; loop < level ; loop++) {
			sb.append("\t");
		}
		sb.append(value);
		System.out.println(sb.toString());
	}
}
