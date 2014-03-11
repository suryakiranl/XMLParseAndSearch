package edu.cmu.sv.surya.parsers;

import edu.cmu.sv.surya.dao.TagDAOImpl;
import edu.cmu.sv.surya.dao.TagDAO;
import edu.cmu.sv.surya.dao.util.DBUtils;
import edu.cmu.sv.surya.dto.Tag;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TagXMLParser {
	public static void main(String[] args) {
		TagXMLParser txp = new TagXMLParser();
		long now = new Date().getTime();
		try {
			txp.parseAndSave("/Users/suryakiran/Downloads/SOC-HW/tags.xml");
		} catch (FileNotFoundException | XMLStreamException | SQLException
				| ClassNotFoundException e) {
			System.out.println("An error occurred when parsing tags file.");
			System.out.println("Error details:: " + e.getMessage());
			e.printStackTrace();
		} finally {
			System.out.println("Time taken = " + (new Date().getTime() - now) + "ms");
		}
	}

	public void parseAndSave(String tagFilePath) throws FileNotFoundException,
			XMLStreamException, SQLException, ClassNotFoundException {
		// Corner case validation
		if (tagFilePath == null || "".equals(tagFilePath.trim())) {
			return;
		}

        DBUtils.deleteDBFiles();

		long rowCount = 0;
		TagDAO dao = new TagDAOImpl();

		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory
					.createXMLStreamReader(new FileInputStream(tagFilePath));

			Tag tag = null;
			String key = null;
			String value = null;
			List<Tag> tags = new ArrayList<Tag>();

			dao.createTable();

			while (reader.hasNext()) {
				int event = reader.next();

				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					if ("tag".equalsIgnoreCase(reader.getLocalName())) {
						key = reader.getAttributeValue(0);
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					value = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					if ("tag".equalsIgnoreCase(reader.getLocalName())) {
						tag = new Tag(key, value);
						tags.add(tag);
						rowCount += 1;
						
						if(tags.size() == 10000) {
							dao.saveAll(tags);
							tags.clear();
							dao.commit();
						}
					}
					break;
				default:
					break;
				}
			}
			
			if(tags.size() > 0) {
				dao.saveAll(tags);
				tags.clear();
				dao.commit();
			}
		} finally {
			System.out.println("Total number of rows = " + rowCount);
			long rowCountFromDB = dao.countRowsInTable();
			System.out.println("Rows inserted in DB = " + rowCountFromDB);
		}
	}
}
