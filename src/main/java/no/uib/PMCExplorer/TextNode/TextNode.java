package no.uib.PMCExplorer.TextNode;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.jsoup.select.Elements;

/**
 * Class responsbile for iterating the input text segment and keeping track of frequencies
 * 
 * @author Markus Berggrav
 */
public class TextNode {
    String text; 
    Elements textElements;
    String[] keyWords;
    Map<String,Integer> keyWordFrequencies = new HashMap<>();
    Map<String,List<List<Integer>>> keyWordIndices = new HashMap<>();
    Map<String,Integer> allWordFrequencies = new LinkedHashMap<>();
    List<String> stopWords = new ArrayList<>();
    Color relevancy;
    
 
    /**
     * 
     * Constructor. 
     * 
     * @param textElements
     * @param keyWords 
     */
    public TextNode(Elements textElements,String[] keyWords){
       this.text = textElements.outerHtml();
       
       
       this.keyWords = keyWords;
       
       this.textElements = textElements;
       
        importStopWords();
        findKeyWordOccurences(); 
        findAllWordOccurences();
        
        this.relevancy = checkRelevancy();
        
    }
    
    /**
     * Method for text tokenization, storing of tokens/words and keeping track
     * of their frequencies.
     * 
     */
    private void findAllWordOccurences(){
        String[] allWords = textElements.text().split(" |-");
        
        for (String word: allWords){
            word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (!stopWords.contains(word) && !word.matches(".*\\d.*") && !word.equals("")){
                if (allWordFrequencies.containsKey(word)){
                    allWordFrequencies.put(word,allWordFrequencies.get(word)+1);
                }
                else{
                    allWordFrequencies.put(word,1);
                }
            }
        }
        
        getTop20FrequentWords();
    }
    
    
    /**
     * Getting top 20 most frequent words.
     * 
     */
    private void getTop20FrequentWords(){

        allWordFrequencies = allWordFrequencies.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
    
    /**
     * Importing words that should be filtered out when counting word occurences.
     * 
     */
    private void importStopWords(){
        InputStream file = getClass().getClassLoader().getResourceAsStream("stopwords.csv");
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()){
            stopWords.add(myReader.nextLine());
        }
    }
    
    /**
     * Finding the frequencies of the input keywords in the text segment.
     * 
     */
    private void findKeyWordOccurences(){
        for (String keyWord : keyWords){
            if (keyWord.length() > 0 && !keyWord.equals(" ")){
            List<List<Integer>> indices = findInstancesOfKeyWord(textElements.text(), keyWord);
            keyWordFrequencies.put(keyWord, indices.size());
            keyWordIndices.put(keyWord, indices);
            }
        }
        
    }
    
    /**
     * Finding the indexes of where a keyword occurs in a text segment.
     * 
     * @param text - The text that should be processed (String)
     * @param word - The keyword that should be looked for.
     * @return - A list of tuples (start,end) representing the indexes of the keyword occurences.
     */
    public static List<List<Integer>> findInstancesOfKeyWord(String text, String word){
        List<List<Integer>> indexes = new ArrayList<>();
        String lowerCaseText = text.toLowerCase();
        String lowerCaseWord = word.toLowerCase();

        int wordLength = 0;
        int index = 0;

        while(index != -1){
            index = lowerCaseText.indexOf(lowerCaseWord,index + wordLength);
            if (index != -1){
                indexes.add(Arrays.asList(index,(index+word.length())));
            }
            wordLength = word.length();
        }
        return indexes;
    }

    /**
     * Ckeck if the text segment is relevant to the input keywords.
     * If one of the input keywords is in the top 20 most frequent words, the 
     * text is relevant.
     * 
     * @return - Green for relevant, yellow for not relevant.
     */
    public final Color checkRelevancy(){
        Color validityColor = Color.yellow;
        
        for (String keyWord: keyWords){
            if (allWordFrequencies.containsKey(keyWord.toLowerCase())){
                validityColor = Color.GREEN;
            }
        }
        return validityColor;
        
        
    }
    
    /**
     * Get the dictionary consisting of keywords and their frequencies.
     * 
     * @return - Map of (keyword, frequency).
     */
    public Map<String,Integer> getKeyWordFrequencies(){
        return keyWordFrequencies;
    }
    
    /**
     * Get the dictionary consisting of top 20 most frequent words and their frequencies.
     * 
     * @return Map og (word, frequency).
     */
    public Map<String, Integer> getAllWordFrequencies(){
        return allWordFrequencies;
    }
    
    
    /**
     * Get text segment.
     * 
     * @return  - text (String)
     */
    public String getText(){
        return text;
    }
    
    /**
     * Get text elements. An element includes the HTML-tag and the elements are
     * structured in a tree-like matter.
     * 
     * @return - Jsoup Elements.
     */
    public Elements getArticleElements(){
        return textElements;
    }
    
    /**
     * Get input keywords. 
     * 
     * @return - Array of keywords. 
     */
    public String[] getKeyWords() {
        return keyWords;
    } 
    
    /**
     * Get the relevancy color of the text segment.
     * 
     * @return - Green = relevant, Yellow = not relevant.
     */
    public Color getRelevancy(){
        return relevancy;
    }
}
