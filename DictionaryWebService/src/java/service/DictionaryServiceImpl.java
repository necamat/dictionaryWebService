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
    private final String PATH = "C:\\Users\\hp\\Desktop\\GitHub\\dictionaryWebService\\DictionaryWebService\\xml\\dictionary.xml";
    // private static final String PATH = "your/path/to/dictionary.xml";

    //Method returns the  Xml document
    private Document readData() {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.isIgnoringElementContentWhitespace();
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(PATH);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DictionaryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(DictionaryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DictionaryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Method returns the id of the searched word, if the searched word does not exist it return empty String
    private String idWord(NodeList res, String wordTr) {

        for (int i = 0; i < res.getLength(); i++) {

            Node n = res.item(i);

            if (n.getTextContent().equals(wordTr)) {

                return n.getAttributes().item(0).getNodeValue();

            }
        }
        return "";
    }

    @Override
    public String translate(String word, String language1, String language2) {

        String translateWorrd = "";

        
            // Taking the word and which is translated and switching to lower case for its further
            String wordTr = word.trim().toLowerCase();

            //Read and parase XML documents
            Document doc = readData();
            if (doc != null) {
                
            try {

            //Kreiranje objekta XPathFactory uz pomoc kog dobijamo XPath objekat
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();

            //Kreiranje objekta tipa XPathExpression koji prestavlja Xpath izraz. Izraz vraca sve reci sa jezika sa kog smo izabrali da se prevodi rec
            XPathExpression xpe = xp.compile("//language[@lg = '" + language1 + "']/word");

            NodeList res = (NodeList) xpe.evaluate(doc, XPathConstants.NODESET);
            // System.out.println(res.getLength());

            // id trazene reci
            String id = idWord(res, wordTr);
            if (!(id.equals(""))) {

                //Kreiranje objekta tipa XPathExpression koji prestavlja Xpath izraz. Izraz vraca sve reci na jeziku na koji smo izabrali da se prevede rec
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
            Logger.getLogger(DictionaryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }}
        return translateWorrd;
    }

}
