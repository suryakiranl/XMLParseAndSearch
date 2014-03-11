package edu.cmu.sv.surya.parsers;

import edu.cmu.sv.surya.dao.DblpDAO;
import edu.cmu.sv.surya.dao.DblpDAOImpl;
import edu.cmu.sv.surya.dao.util.DBUtils;
import edu.cmu.sv.surya.dto.Literature;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by suryakiran on 3/10/14.
 */
public class DblpParser {
    private DblpDAO dao = new DblpDAOImpl();

    public static Set<String> literatureTags;
    static {
        literatureTags = new HashSet<String>();
        literatureTags.add("article");
        literatureTags.add("inproceedings");
        literatureTags.add("proceedings");
        literatureTags.add("book");
        literatureTags.add("incollection");
        literatureTags.add("phdthesis");
        literatureTags.add("mastersthesis");
        literatureTags.add("www");
    }

    public static void main(String[] args) {
        DblpParser dblpParser = new DblpParser();
        long now = new Date().getTime();
        try {
            if(dblpParser.checkDBFileForDeletion()) {
                DBUtils.deleteDBFiles();
                dblpParser.parseAndSave("/Users/suryakiran/Downloads/SOC-HW/dblp.xml");
            }

            dblpParser.findCoAuthors();
        } catch (XMLStreamException | SQLException
                | ClassNotFoundException | IOException e) {
            System.out.println("An error occurred when parsing dblp file.");
            System.out.println("Error details:: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Time taken = " + (new Date().getTime() - now) + "ms");
        }
    }

    private boolean checkDBFileForDeletion() throws IOException {
        if(DBUtils.doesDBExist()) {
            System.out.println("We've noticed a database file exists already.");
            System.out.print("Do you want to reuse it, or parse the XML to create a new one? (Y/N): ");

            BufferedReader reader = getSystemInputReader();
            String str = reader.readLine();
            while(true) {
                switch (str.toLowerCase().trim()) {
                    case "y" : return true;
                    case "n" : return false;
                    default: System.out.print("Invalid input. Please provide Y/N: ");
                        str = reader.readLine();
                        break;
                }
            }
        }

        return true;
    }

    private BufferedReader getSystemInputReader() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        return bufferedReader;
    }

    public void findCoAuthors() throws IOException {
        BufferedReader reader = getSystemInputReader();
        System.out.println();
        System.out.println();
        System.out.print("Please enter the name of author/editor to find his co-author/editor(s): ");
        String authorName = reader.readLine();

        try {
            if(authorName == null) {
                System.out.println("No name entered. Exiting program.");
                return;
            }
            System.out.println("Querying for results .... ");

            List<String[]> litList = dao.findLiteratureByAuthor(authorName);

            System.out.println("Matching results :: ");
            if(litList.size() == 0) {
                System.out.println("NONE");
            } else {
                for(String[] result : litList) {
                    System.out.println(result[0] + " - " +
                            result[1] + " - " +
                            result[2] + " - " +
                            result[3] + " - " +
                            result[4] + " - " +
                            result[5]);
                }
            }
        } finally {

        }

    }

    public void parseAndSave(String filePath) throws FileNotFoundException,
            XMLStreamException, SQLException, ClassNotFoundException {
        // Corner case validation
        if (filePath == null || "".equals(filePath.trim())) {
            return;
        }

        System.out.println("Starting to parse the XML file: " + filePath);

        long rowCount = 0;

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filePath));

            Literature lit = null;
            List<Literature> litList = new ArrayList<Literature>();
            String value = null;

            dao.createTables();

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (literatureTags.contains(reader.getLocalName())) {
                            lit = new Literature();

                            int attrCount = reader.getAttributeCount();
                            for(int loop = 0; loop < attrCount; loop++) {
                                String attrName = reader.getAttributeLocalName(loop);
                                String attrValue = reader.getAttributeValue(loop);

                                switch(attrName) {
                                    case "key" :
                                        lit.setKey(attrValue); break;
                                    case "mDate" :
                                        lit.setmDate(attrValue); break;
                                    default:
                                        break;
                                }
                            }
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        value = reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (literatureTags.contains(reader.getLocalName())) {
                            litList.add(lit);
                            rowCount += 1;

                            if(litList.size() == 4000) {
                                saveLiteratureList(litList);
                            }
                        } else {
                            switch(reader.getLocalName().toLowerCase()) {
                                case "title" :
                                    lit.setTitle(value); break;
                                case "year" :
                                    lit.setYear(value); break;
                                case "url":
                                    lit.setUrl(value); break;
                                case "author" :
                                    lit.getAuthors().add(value); break;
                                case "editor" :
                                    lit.getEditors().add(value); break;
                                default:
                                    break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            saveLiteratureList(litList);
        } finally {
            System.out.println("Parsing and saving the file complete.");
            System.out.println("Total number of rows parsed = " + rowCount);
            long rowCountFromDB = dao.countRowsInTable();
            System.out.println("Rows inserted in DB = " + rowCountFromDB);
        }
    }

    private void saveLiteratureList(List<Literature> litList) throws SQLException {
        if(litList.size() > 0) {
            dao.saveAll(litList);
            litList.clear();
            dao.commit();
        }
    }
}
