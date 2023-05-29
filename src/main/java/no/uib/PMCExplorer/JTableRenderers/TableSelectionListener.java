
package no.uib.PMCExplorer.JTableRenderers;

import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author maber
 */
public class TableSelectionListener implements ListSelectionListener {
    JTable table;
    JTextArea selectedColumns;
    public TableSelectionListener(JTable table, JTextArea selectedColumns){
        this.selectedColumns = selectedColumns;
        this.table = table;
        
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        
        selectedColumns.setText(Arrays.toString(table.getSelectedColumns()).replaceAll("[\\[\\]]",""));
        
    }
    
}
