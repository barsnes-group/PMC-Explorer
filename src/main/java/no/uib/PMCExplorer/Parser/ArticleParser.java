
package no.uib.PMCExplorer.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import no.uib.PMCExplorer.PMCExplorer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 * Class consisting of methods for parsing XML file and XML url.
 * 
 * @author Markus Berggrav
 */
public class ArticleParser {
    
    
    /**
     * Method responsible for parsing an articles XML-file.
     * Returning a ParsedElements object with methods for element selection.
     * 
     * @param pmcId - PubMedCentral id of a downloaded article directory.
     * @param keyWords - Input keywords provided by the user.
     * @return  - ParsedElements object with methods for element selection.
     */
    public static ParsedElements parsePubMedArticle(String pmcId,String[] keyWords){
        
        File articleDirectory = new File(PMCExplorer.Downloads_Folder_Url + "/" + pmcId +"/" + pmcId);
        
        File[] articleXml = articleDirectory.listFiles((File dir, String name) -> (name.endsWith(".nxml") || name.endsWith(".xml")));
        
        return new ParsedElements(pmcId,parseXmlFile(articleXml[0]),keyWords);
       
    }
    
    /**
     * Method responsible for parsing an URL (file type - XML).
     * 
     * @param url - Input URL
     * @return - Parsed document.
     */
    public static Document parseXmlUrl(String url){
        
        try {
            
            return Jsoup.connect(url).parser(Parser.xmlParser()).get();
        } 
        catch (IOException e) {
            System.out.println("Parsing exception: Could not connect to API");
            return null;
           
        }  
    }
    
    /**
     * Method responsible for parsing an XML file.
     * 
     * @param file - Input File object that will be parsed.
     * @return - Parsed document.
     */
    public static Document parseXmlFile(File file){
        
        try {
            FileInputStream fis = new FileInputStream(file);
            return Jsoup.parse(fis,null,"",Parser.xmlParser());
        } 
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
           
        }
        
        
    }
    
}
