// -------------------------------------------------------------------------------------------------------------------- //
// import libraries: 

package no.uib.PMCExplorer.ArticleBrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import no.uib.PMCExplorer.PMCExplorer;
import static no.uib.PMCExplorer.Parser.ArticleParser.parseXmlUrl;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

// -------------------------------------------------------------------------------------------------------------------- //


/**
 * Class responsible for connecting to the PubMed database through their API. 
 * 
 * 
 * @author Markus Berggrav
 */
public class PubMedAPI {
    
    Elements pubMedIds;
    List<String> pmcIdList = new ArrayList<>();
    List<String> doi = new ArrayList<>();
    
    public PubMedAPI(String keyWords, int start, int end){
        performPubMedSearch(keyWords, start, end);
        
    }
    
    
    /**
     * Method responsible for performing a PubMed search and retrieving the PubMed IDs that match the input keywords.API used: E-utilities: ESearch
     * 
     * @param keyWords - Keywords provided by the user.
     * @param start - starting index of search.
     * @param end - ending index of search.
     */
    public final void performPubMedSearch(String keyWords, int start, int end){
        
        String eUtilSearch = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?api_key=abfeb3d9f3852ec3c58e18c8d08293747608&"
                + "tool=DataExtractorUIB&email=maberggrav@gmail.com&" + "term=pubmed pmc open access[filter]," + keyWords + "&retstart=" 
                + String.valueOf(start) + "&retmax=" + String.valueOf(end) ;
        
        pubMedIds = parseXmlUrl(eUtilSearch).getElementsByTag("Id");
        
        
        convertToPmcId();
        
        downloadArticles();
        
    }
    
    /**
     * Method responsible for converting the PubMed IDs to PubMedCentral IDs.
     * API used: PubMed ID Converter
     * 
     */
    private void convertToPmcId(){
        
        StringBuilder pubMedIdStringBuilder = new StringBuilder();
        int counter = 0;
        OUTER:
        for (Element id : pubMedIds) {
            switch (counter) {
                case 0 -> {
                    pubMedIdStringBuilder.append(id.text());
                    counter++;
                }
                default -> {
                    pubMedIdStringBuilder.append(",").append(id.text());
                    counter++;
                }
            }
        } 
        
        String idConverter = "https://www.ncbi.nlm.nih.gov/pmc/utils/idconv/v1.0/?"
                + "tool=DataExtractorUIB&email=maberggrav@gmail.com&versions=no&ids=" + pubMedIdStringBuilder.toString();
        
        Elements pmcIds = parseXmlUrl(idConverter).select("record");
        
        pmcIdList = new ArrayList<>();
        
        for (Element id : pmcIds){
            pmcIdList.add(id.attr("pmcid"));
            doi.add(id.attr("doi"));
        } 
        
    }
    
    /**
     * Method responsible for iterating through the PubMed Central ID's and downloading the associated article file.
     * 
     */
    private void downloadArticles(){
        for (String id : pmcIdList){
            File directory = new File(PMCExplorer.Downloads_Folder_Url + "/" + id);
            try{
                System.out.println(id);
                
                downloadArticle(directory,id);
            }
            
            catch (IOException e){
                new File(PMCExplorer.Downloads_Folder_Url + "/" + id + "/" + "/Article_Notes.txt").delete();
                directory.delete();
                System.out.println("Troubles connecting to the PubMed Central API. "
                        + "Reason may be system overload. Wait and try again. ");
                return;
                
            }
        }
    }
    
    /**
     * Method responsible for downloading the tar,gzipped file of the article.The file is located in the PubMed Central online ftp database.
     * API used: The PMC FTP Service
     * 
     * @param directory - directory where the article-files will be installed.
     * @param pubMedCentralid - PubMedCentral ID of the article that will be downloaded.
     * @throws IOException 
     */
    public void downloadArticle(File directory,String pubMedCentralid) throws IOException{
        
        
        
        
        if (!directory.exists()){
            directory.mkdir(); 
            
            new File(PMCExplorer.Downloads_Folder_Url + "/" + pubMedCentralid + "/Article_Notes.txt").createNewFile();
            
            
            
            
            String search = "https://www.ncbi.nlm.nih.gov/pmc/utils/oa/oa.fcgi?"
                    + "tool=DataExtractorUIB&email=maberggrav@gmail.com&id=" + pubMedCentralid;
            
            
            Document doc = Jsoup.connect(search).parser(Parser.xmlParser()).get();
            
            String href = doc.select("link[href]").first().attr("href").replace("ftp://", "https://");
            URL url = new URL(href);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            
            FileOutputStream fileOutputStream = new FileOutputStream(PMCExplorer.Downloads_Folder_Url + "/" + pubMedCentralid + "/" + pubMedCentralid + ".tar.gz");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            
            File tarGzFile = new File(PMCExplorer.Downloads_Folder_Url + "/" + pubMedCentralid + "/" + pubMedCentralid + ".tar.gz"); 
            
            try (TarArchiveInputStream inputStream = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarGzFile)))) {
                TarArchiveEntry entry = null;
                while ((entry = inputStream.getNextTarEntry()) != null){
                    File destPath = new File(PMCExplorer.Downloads_Folder_Url + "/"+pubMedCentralid,entry.getName());
                    
                    if (entry.isDirectory()){
                        destPath.mkdir();
                    }
                    else{
                        fileOutputStream = new FileOutputStream(destPath);
                        IOUtils.copy(inputStream, fileOutputStream);
                        fileOutputStream.close();
                    }
                }
                System.out.println("Sucessfull download");
            }
        
            
            
        } 
    }
    
    /**
     * Get list downloaded list of PubMed Central ID's.
     * 
     * @return List of retrieved PubMed Central ID's
     */
    public List<String> getPmcIdList(){
       return pmcIdList;
    }
    
}
