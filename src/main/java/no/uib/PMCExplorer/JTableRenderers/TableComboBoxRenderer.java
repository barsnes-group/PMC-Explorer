// -------------------------------------------------------------------------------------------------------------------- //
// import libraries: 

package no.uib.PMCExplorer.JTableRenderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

// -------------------------------------------------------------------------------------------------------------------- //

/**
 * Custom renderer for colored JComboBox inside a JTable.
 * 
 * 
 * @author Markus Berggrav
 */
public class TableComboBoxRenderer extends JComboBox implements TableCellRenderer{
    public TableComboBoxRenderer(){
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground((Color) value);
        setForeground((Color) value);
        return this;
    }
}
