package no.uib.PMCExplorer.Parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class responsible for extracting and storing data from XML-tables.
 * 
 * @author Markus Berggrav
 */
public class TableParser {
    
    List<String[]> tableHeaders = new ArrayList<>();
    List<String[][]> tableData = new ArrayList<>();
    List<String> tableCaptions = new ArrayList<>();
    
    
    /**
     * Constructor. 
     * Iterates through input tables and retrieves data.
     * 
     * @param htmltables - In-text article tables. 
     */
    public TableParser(Elements htmltables){
        for (Element table : htmltables){
            createTable(table.select("table"));
            storeCaption(table.select("label,caption"));
        }
    }
    
    
    /**
     * Retrieves headers and data from input XML-table.
     * 
     * @param table - Table element.
     */
    private void createTable(Elements table){
        String[] headers = table.select("thead tr th").stream().map(Element::text).toArray(String[]::new);
        tableHeaders.add(headers);
        String[][] data = table.select("tbody tr").stream().map(row -> row.select("td").stream().map(Element::text).toArray(String[]::new)).toArray(String[][]::new);
        tableData.add(data);
        
    }
    
    /**
     * Store table label and caption.
     * 
     * @param caption - Element with the label and caption of the table.
     */
    private void storeCaption(Elements caption){
        String tableCaption = caption.get(0).text() + " : " + caption.get(1).text();
        
        tableCaptions.add(tableCaption);
        
    }
    
    /**
     * Get the list of table headers.
     * 
     * @return - List of headers.
     */
    public List<String[]> getTableHeaders(){
        return tableHeaders;
    }
    
    /**
     * Get the list of table data. 
     * 
     * @return - List of table data.
     */
    public List<String[][]> getTableData(){
        return tableData;
    }
    
    
    /**
     * Get the list of table captions.
     * 
     * @return - List of table captions
     */
    public List<String> getTableCaptions(){
        return tableCaptions;
    }
}
