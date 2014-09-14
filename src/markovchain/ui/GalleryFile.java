/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package markovchain.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 *
 * @author guillaumerebmann
 */
public class GalleryFile {
    
    public List<GalleryComponent> listJlabelGallery =  new ArrayList<>();;
    
    public GalleryFile(){
        
    }
    
    private Document loader(){
        Document doc = null;
         try {
 
            File fXmlFile = new File("gal/galleryFile.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            String path = fXmlFile.getAbsolutePath().replace("galleryFile.xml","");
            
          } catch (Exception e) {
            e.printStackTrace();
        }
         return doc;
    }
    
    public String getAbsolutePath(){
        String result ="";
         try {
 
            File fXmlFile = new File("gal/galleryFile.xml");
            result = fXmlFile.getAbsolutePath().replace("galleryFile.xml","");
            
          } catch (Exception e) {
            e.printStackTrace();
        }
         return result;
    }
    
    public List<GalleryComponent> getArray(){
        
        return listJlabelGallery;
    }
    public void removeXML(String resource) throws TransformerConfigurationException, TransformerException{
         Document doc = loader();
            if(doc != null){
                
                 NodeList nList = doc.getElementsByTagName("file");
                 Node data = doc.getElementsByTagName("data").item(0);
                for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);
                        
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                             
                                Element eElement = (Element) nNode;
                                // Create Gallery Component from the XML data
                                 if (resource.equals(eElement.getElementsByTagName("resource").item(0).getTextContent())) {
                                     System.out.println("remove");
                                    data.removeChild(nNode);
                               }
                        }
                }
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("gal/galleryFile.xml"));
		transformer.transform(source, result);
            }
    }
    
    public void addXML(String title,String picture,String resource) throws TransformerConfigurationException, TransformerException{
         Document doc = loader();
            if(doc != null){
                Node data = doc.getElementsByTagName("data").item(0);
                Element file = doc.createElement("file");
                
                    Element eTitle = doc.createElement("title");
                    Element ePicture = doc.createElement("picture");
                    Element eResource = doc.createElement("resource");
                    
		eTitle.appendChild(doc.createTextNode(title));
                ePicture.appendChild(doc.createTextNode(picture));
                eResource.appendChild(doc.createTextNode(resource));
                
                file.appendChild(eTitle);
                file.appendChild(ePicture);
                file.appendChild(eResource);
                data.appendChild(file);
		
                
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("gal/galleryFile.xml"));
		transformer.transform(source, result);
            }
    }
    
    
    public void loadXML(){
          
            Document doc = loader();
            if(doc != null){
                NodeList nList = doc.getElementsByTagName("file");

                for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        System.out.println("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element eElement = (Element) nNode;
                                // Create Gallery Component from the XML data
                                GalleryComponent g = new GalleryComponent(this.getAbsolutePath()+eElement.getElementsByTagName("picture").item(0).getTextContent(),eElement.getElementsByTagName("resource").item(0).getTextContent(),eElement.getElementsByTagName("title").item(0).getTextContent());
                                listJlabelGallery.add(g); 
                                 System.out.println("\nCurrent pict :"+ this.getAbsolutePath() + eElement.getElementsByTagName("picture").item(0).getTextContent());
                        }
                }
            }
            
        
    }
}
