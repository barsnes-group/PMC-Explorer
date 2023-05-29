
package no.uib.PMCExplorer.ArticleBrowser;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static no.uib.PMCExplorer.Parser.ArticleParser.parsePubMedArticle;
import no.uib.PMCExplorer.Parser.ParsedElements;
import no.uib.PMCExplorer.TextNode.TextNode;
import no.uib.jsparklines.data.JSparklinesDataSeries;

/**
 * This class will collect the meta-data of articles either through 
 * PubMed or local files. 
 * 
 * @author Markus Berggrav
 */
public class ArticleBrowser {
    String keyWordString;
    String[] keyWords;
    List<String> pmcIdList = new ArrayList<>();
    List<ParsedElements> articles = new ArrayList<>();
    
    /**
     * Constructor.
     * 
     * @param keyWordString 
     */
    public ArticleBrowser(String keyWordString) {
        this.keyWordString = keyWordString;
        this.keyWords = Arrays.stream(keyWordString.replaceAll("'", "’").split(",")).map(String::trim).toArray(String[] :: new);
        
    }
    
    /**
     * Method responsible for gathering meta-data from local article files.
     * 
     * @return - 2-dimensjonal matrix consisting of articles and their meta-data.
     */
    public Object[][] browseLocalArticles(){
        File downloads = new File("src/main/resources/downloads");
        File[] listOfFiles = downloads.listFiles();
        
        for (File file : listOfFiles){
            pmcIdList.add(file.getName());
        }
        
        return createMetaDataMatrix(pmcIdList);
    }
    
    /**
     * Method responsible for gathering meta-data from articles retrieved through PubMed.
     * 
     * @return - 2-dimensjonal matrix consisting of articles and their meta-data
     */
    public Object[][] browsePubmedArticles(int start, int end){
        
        PubMedAPI pubMedAPI = new PubMedAPI(keyWordString,start, end);
        this.pmcIdList = pubMedAPI.getPmcIdList();
        
        return createMetaDataMatrix(pmcIdList);
    }
    
    /**
     * Creating the meta-data matrix of an input list of pubmed IDs.
     * 
     * @param pmcIds
     * @return 
     */
    public Object[][] createMetaDataMatrix(List<String> pmcIds){
        Object[][] urlMatrix = new Object[pmcIds.size()][9];

        for (int x = 0; x < pmcIds.size(); x++){
            ParsedElements article = parsePubMedArticle(pmcIds.get(x), keyWords);
            TextNode body = article.getBody();
            articles.add(article);
            
            urlMatrix[x][0] = x+1;
            urlMatrix[x][1] = "Notes";
            urlMatrix[x][2] = "<html><b>"+ article.getAuthor() + "</b></html>";
            urlMatrix[x][3] = "<html><b>"+ article.getTitle() + "</b></html>";
            urlMatrix[x][4] = "<html><b>"+ article.getPublicationYear()+"</b></html>";
            urlMatrix[x][5] = pmcIds.get(x);
            urlMatrix[x][6] = body.checkRelevancy();
            urlMatrix[x][7] = createSparklineDataSeries(body.getKeyWordFrequencies().values());
            urlMatrix[x][8] = "Inspect Article";
        }
        return urlMatrix;
    }
    
    /**
     * Method responsible for creating a JSparklineDataSeries.
     * This is necessary for visualizing the keyword frequencies in the GUI
     * 
     * @param frequencies - List of frequencies. 
     * @return - JSparklineDataseries.
     */
    private JSparklinesDataSeries createSparklineDataSeries(Collection<Integer> frequencies){
        ArrayList<Double> doubleValues = new ArrayList<>();
        for (Integer f : frequencies){
            doubleValues.add(Double.valueOf(f)); 
        }
        return new JSparklinesDataSeries(doubleValues, Color.BLACK,null);
         
    } 
    
    
    /**
     * Get parsed document of an article in the matrix. 
     * 
     * @param i - index of the parsed article that should be retrieved.
     * @return - ParsedElements object.
     */
    public ParsedElements getArticle(int i){
        return articles.get(i);
    }
}
