package service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DictionaryServiceImpl implements DictionaryService {

    //Path to the xml file
    private final String PATH = "your/path/to/dictionary.xml";

    /*
    **Method returns the  Xml document
    */
    private Document readData() {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.isIgnoringElementContentWhitespace();
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(PATH);
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*
    **Method returns the id of the searched word, if the searched word does not exist it return empty String
    */
    private String idWord(NodeList res, String wordTr) {

        for (int i = 0; i < res.getLength(); i++) {

            Node n = res.item(i);

            if (n.getTextContent().equals(wordTr)) {

                return n.getAttributes().item(0).getNodeValue();

            }
        }
        return "";
    }
    
    /*
    ** Method return the translated word if the word we want to translate exists in our dictionary. In the absence of a word or a poorly loaded Xml dictionary, it prints the appropriate message.
    ** The choice of a non-existent language has not been processed, as it is envisaged that the client will not allow free input of the language. See details in the Instructions.
    */

    @Override
    public String translate(String word, String language1, String language2) {

        String translateWorrd = "";

        
            // Taking the word and which is translated and switching to lower case for its further
            String wordTr = word.trim().toLowerCase();

            //Read and parase XML documents
            Document doc = readData();
            if (doc != null) {
                
            try {

            // Creating  XPathFactory object with which we get  XPath object
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();

            // Create XPathExpression object that represents an Xpath expression. The expression returns all words from the language from which we chose to translate the word
            XPathExpression xpe = xp.compile("//language[@lg = '" + language1 + "']/word");

            NodeList res = (NodeList) xpe.evaluate(doc, XPathConstants.NODESET);
            // System.out.println(res.getLength());

            // Id search words
            String id = idWord(res, wordTr);
            if (!(id.equals(""))) {

                // Create  XPathExpression object that represents an Xpath expression. The phrase returns all words in the language we have chosen to translate the word
                XPathExpression xpe1 = xp.compile("//language[@lg = '" + language2 + "']/word");

                NodeList res1 = (NodeList) xpe1.evaluate(doc, XPathConstants.NODESET);
                // System.out.println(res.getLength());

                for (int i = 0; i < res1.getLength(); i++) {

                    Node n = res1.item(i);

                    if (n.getAttributes().item(0).getNodeValue().equals(id)) {
                        
                        translateWorrd = n.getTextContent();
                    }
                }
            } else {

                translateWorrd = "Word " + '"' + word + '"' + " does not exist in our little dictionary";
            }

        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        }}else{
                
            translateWorrd = "Service is currently down, please try again later.";
         }
        return translateWorrd;
    }

}
