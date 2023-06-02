// -------------------------------------------------------------------------------------------------------------------- //
// import libraries: 

package no.uib.PMCExplorer.GUI;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import no.uib.PMCExplorer.JTableRenderers.TableSelectionListener;
import no.uib.PMCExplorer.PMCExplorer;
import no.uib.PMCExplorer.Parser.TableParser;
import org.jsoup.select.Elements;

// -------------------------------------------------------------------------------------------------------------------- //

/**
 * Class responsible for presenting the in-text tables. 
 * 
 * @author Markus Berggrav
 */
public class TablePanel {
        TableParser tableParser;
        JPanel tablePanel;
        String pmcId;
    
    
    /**
     * Constructor. 
     * 
     * @param tabbedPane - Input tabbedpane where the table tab will be stored.
     * @param tables - Table elements retrieved from the XML file.
     * @param pmcId  - PubMed Central ID of article. 
     */
    public TablePanel(JTabbedPane tabbedPane, Elements tables, String pmcId){
        
        this.pmcId = pmcId;
        this.tableParser = new TableParser(tables);
        
        
        tabbedPane.addTab("TABLES",initializePanel());
        
        
        
    }
    
        
    /**
     * Initialize JPanel with tables. 
     * 
     * @return - The constructed panel with the tables.
     */    
    private JScrollPane initializePanel(){
        
        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, javax.swing.BoxLayout.PAGE_AXIS));
        tablePanel.setBorder(null);
        
        iterateTables();
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tablePanel);
        scrollPane.getVerticalScrollBar().setValue(0);
        
        return scrollPane;
         
    }
    
    
    /**
     * Retrieve the data from each table contained in the tableParser class.
     * 
     */
    public void iterateTables(){
        List<String[]> tableHeaders = tableParser.getTableHeaders();
        List<String[][]> tableDatas = tableParser.getTableData();
        List<String> tableCaptions = tableParser.getTableCaptions();
        for (int i = 0 ; i < tableHeaders.size();i++){
            createJTable(tableCaptions.get(i),tableHeaders.get(i), tableDatas.get(i));
            tablePanel.add(Box.createRigidArea(new Dimension(tablePanel.getWidth(), 50)));
        }
    }
    
    /**
     * Build and format a JTable.
     * Initialize save button and textarea that displays selected columns.
     * 
     * 
     * @param title - title of the table.
     * @param header - columnd headers.
     * @param data  - 2D-data of the table.
     */
    public void createJTable(String title, String[] header, String[][] data){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
                                                            "<html><h3>"+title+"</h3></html>",
                                                            TitledBorder.CENTER,
                                                            TitledBorder.TOP));
        
        DefaultTableModel tableModel = new DefaultTableModel(data, header){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
            return String.class;
         }
        };
        
        
        
        JTable table = new JTable(tableModel);
        
        table.setRowHeight(75);
        table.setShowGrid(true);
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
       
        panel.add(Box.createRigidArea(new Dimension(tablePanel.getWidth(), 10)));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(900,500));
        scrollPane.setMaximumSize(new Dimension(900,500));
        
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(tablePanel.getWidth(), 10)));
        
        
        Box horiBox = Box.createHorizontalBox();
        horiBox.add(Box.createGlue());
        horiBox.add(new JLabel("Selected columns: "));
        JTextArea selectedColumns = new JTextArea();
        selectedColumns.setEditable(false);
        selectedColumns.setPreferredSize(new Dimension(100,30));
        selectedColumns.setMaximumSize(new Dimension(100,30));
        horiBox.add(selectedColumns);
        JButton button = new JButton("SAVE");
        
        button.addActionListener((ActionEvent e) -> {
            saveTableSubset(table, table.getSelectedColumns());
        });
        
        horiBox.add(button);
        horiBox.add(Box.createGlue());
        
        table.getColumnModel().getSelectionModel().addListSelectionListener(new TableSelectionListener(table,selectedColumns));
        
        
        panel.add(horiBox);
        
        tablePanel.add(panel);
        
        
        
    }
    
    
    /**
     * Retrieve the Article_Notes.txt file, save the table subset and display the file to the user.
     * 
     * @param table - JTable with the selected columns.
     * @param selectedColumns - integer array containing the indices of the selected columns.
     */
    public void saveTableSubset(JTable table, int [] selectedColumns){
        File notesFile = new File(PMCExplorer.Downloads_Folder_Url + "/" + pmcId + "/Article_Notes.txt");
            try {
                writeToFile(notesFile, table, selectedColumns);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            
        if (Desktop.isDesktopSupported()){
            try {
                Desktop.getDesktop().edit(notesFile);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        
    }
      
    
    /** 
     * Method responsible for performing the actual saving of data to the txt file.
     * 
     * @param file - Article_Notes.txt file.
     * @param table - JTable where the data is located.
     * @param selectedColumns - Integer array of selected columns. 
     * @throws IOException 
     */
    public void writeToFile(File file,JTable table, int [] selectedColumns) throws IOException{
        
        BufferedWriter bfw = new BufferedWriter(new FileWriter(file.getAbsolutePath(),true));
        
        
        for (int i: selectedColumns){
            bfw.write(table.getColumnName(i));
            bfw.write("\t");
        }
        
        for (int i = 0; i < table.getRowCount(); i++){
            bfw.newLine();
            for (int j : selectedColumns){
                bfw.write((String) table.getValueAt(i, j));
                bfw.write("\t");
            }
        }
        
        bfw.newLine();
        bfw.newLine();
        bfw.close();
        
        
        
    }
    
}
