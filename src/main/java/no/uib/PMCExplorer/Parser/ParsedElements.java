
package no.uib.PMCExplorer.Parser;
import java.io.File;
import java.text.BreakIterator;
import no.uib.PMCExplorer.TextNode.TextNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Markus Berggrav
 * 
 * This class is responsible for retrieving the different elements of the parsed XML file. 
 */

public class ParsedElements {
    String pmcId;
    Elements xmlElements;
    String[] keyWords;
    Boolean containsSentences;
    
    
    /**
     * Constructor.
     * 
     * @param pmcId - Pubmed Central ID of the article
     * @param doc - Parsed document
     * @param keyWords - Keywords that are used as input in the GUI. 
     */
    
    public ParsedElements(String pmcId, Document doc, String[] keyWords){
        this.pmcId = pmcId;
        this.xmlElements = doc.select("front,body");
        this.keyWords = keyWords; 
        
    }
    
    public String[] getKeyWords(){
        return keyWords;
    }
    
    /**
     * Get PubMed Central ID.
     * 
     * @return - PubMed Central ID (String)
     */
    public String getPmcId(){
        return pmcId;
    }
    
    /**
     * Get article Doi.
     * 
     * @return - Article doi / url (String)
     */
    public String getDoi(){
        
        String doi = "www.doi.org/" + xmlElements.select("front article-id[pub-id-type=\"doi\"]").first().text();
        return doi;
    }
    /**
     * Get all elements after initial filtering.
     * 
     * @return - Filtered xml elements
     */
    public Elements getDocument(){
        return xmlElements;
    }
    
    
    /**
     * Get author or author + "et al." if there are multiple authors.
     * 
     * @return - Author / authors (String)
     */
    public String getAuthor(){
        String author = xmlElements.select("front contrib-group contrib name surname").first().text() + " et al.";
        return author;
    }
    
    /**
     * Get publication year.
     * 
     * @return - Publication year (String)
     */
    public String getPublicationYear(){
        String year = xmlElements.select("year").first().text();
        return year;
    }
    
    /**
     * Get title of the article.
     * 
     * @return - Article title (String)
     */
    public String getTitle(){
        String title = xmlElements.select("front title-group article-title").text();
        return title;
    }
    
    /**
     * Get article tables. 
     * 
     * @return 
     */
      public Elements getTables(){
        Elements tables = xmlElements.select("table-wrap");
        return tables;  
    }
    
    /**
     * Get the abstract of the article.
     * 
     * @return - Article abstract (TextNode)
     */
    public TextNode getAbstract(){
        TextNode articleAbstract = new TextNode(convertToHtml(xmlElements.select("front title-group article-title, front abstract")), keyWords);
        return articleAbstract;
    }  
    
    /**
     * Get the body of the article.
     * 
     * @return - Article body (TextNode)
     */
    public TextNode getBody(){
        TextNode articleBody = new TextNode(convertToHtml(xmlElements.select("front title-group article-title,body")), keyWords);
        return articleBody;
    }
    
    /**
     * Convert XML-tags to HTML-tags
     * 
     * @param elements - Input XML-tags that should be converted
     * @return - HTML-elements
     */
    public Elements convertToHtml(Elements elements){
        Elements htmlElements = elements.clone();
        for (Element element: htmlElements.select("*")){
            switch (element.tagName()) {
                case "article-title" -> element.tagName("h1");
                case "abstract" -> element.prepend("<h2> Abstract </h2>");
                case "title" -> element.tagName("h2");
                case "label" -> {element.prepend("<br>"); element.tagName("b");}
                case "caption" -> element.tagName("small");
                case "sec" -> element.tagName("section");
                case "bold" -> element.tagName("b");
                case "italic" -> element.tagName("i");
                case "table" -> element.remove();
                case "graphic" -> {element.append(displayImage(element));}
                
                
                default -> {}
            }
        }
        return htmlElements;
        
    }
    
    /**
     * Method thatconverts image tags from XML to HTML. To display the image the filepath has to be provided.
     * 
     * @param element - A figure element (tag = "graphic").
     * @return 
     */
    public String displayImage(Element element){
        String fileName = element.attr("xlink:href");
        File imgFile = new File("src/main/resources/downloads/" + pmcId +"/" + pmcId + "/" + fileName + ".jpg");
        
        if (imgFile.exists()){
            String newElement = "<p> <img src=\"file:" + imgFile.getAbsolutePath() + "\"" + "width = \"500\"" +"/> </p>";
            return newElement;
        }
        else{
            return "Image not found!";
        }
        
    }
    
    /**
     * Get text node consisting of only the sentences where a keyword is present. 
     * These sentences can be regarded at the relevant sentences of the text. 
     * 
     * @return - TextNode instance with a custom HTML as input.
     */
    public TextNode getRelevantSentences(){
        TextNode relevantSentences = new TextNode(buildHTML(getBody().getArticleElements()), keyWords);
        return relevantSentences;
        }
    
    
    /**
     * Iterate through elements and filter out every tag that is not a title, header or a paragraph.
     * Title and headers provide the foundation of the HTML and the paragraphs are passed on for further
     * processing.
     * @param elements - Elements that are part of the article body.
     * @return - Parsed document of the custom made HTML-string
     */
    private Elements buildHTML(Elements elements){
        StringBuilder html = new StringBuilder("<html>");
        for (Element e : elements.select("*")){
            switch(e.tagName()){
                case "h1" -> html.append("<head><h1>").append(e.ownText()).append("</h1></head> <body>");
                case "h2" -> html.append("<h2>").append(e.ownText()).append("</h2>");
                case "p" -> {
                    containsSentences = false;
                    String relevantSentences = retrieveRelevantSentences(e.text(), keyWords);
                   
                    if (containsSentences){
                        html.append(relevantSentences);}
                }
            }
        }
        
        html.append("</body></html>");
        Document doc = Jsoup.parse(html.toString());
        return doc.select("html");
    }
    
    /**
     * Method responsible for extracting the relevant sentences of a paragraph.
     * Relevant means that it contains one of the input keywords.
     * 
     * @param paragraph - Paragraph that will be processed.
     * @param keywords - Keywords provided by the user. 
     * @return - String consisting of only the relevant sentences.
     */
    private String retrieveRelevantSentences(String paragraph, String[] keywords){
        StringBuilder htmlBuilder = new StringBuilder("<div> ");
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(paragraph);
        int start = iterator.first();
        for (int end = iterator.next();end != BreakIterator.DONE; start = end, end = iterator.next() ){
            String sentence = paragraph.substring(start,end);
            for (String word: keywords){
                if (word.contains("+")){
                    String[] combinedWords = word.split("\\+");
                    if (containsAllWords(combinedWords,sentence)){
                        containsSentences = true;
                        htmlBuilder.append("<p> ");
                        htmlBuilder.append("- ").append(sentence);
                        htmlBuilder.append(" </p>");
                        break;
                    }
                }
                if (sentence.contains(word)){
                    containsSentences = true;
                    htmlBuilder.append("<p> ");
                    htmlBuilder.append("- ").append(sentence);
                    htmlBuilder.append(" </p>");
                    break;
                }
            }
        }
        htmlBuilder.append(" </div>");

        return htmlBuilder.toString();
    }
    
    
    /**
     * Checks if a sentence contains all the words of an input list of keywords.
     * 
     * @param keyWords - Input list of keywords.
     * @param sentence - Sentence that should be checked.
     * @return - true or false.
     */
    public static boolean containsAllWords(String[] keyWords, String sentence){
        for (String word: keyWords){
            if (!sentence.contains(word)) return false;
        }
        return true;
    }
    
 
}       
    
