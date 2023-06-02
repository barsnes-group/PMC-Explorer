// -------------------------------------------------------------------------------------------------------------------- //
// import libraries: 

package no.uib.PMCExplorer.JTableRenderers;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// -------------------------------------------------------------------------------------------------------------------- //

/**
 * Render buttons inside tablecells.
 * 
 * 
 * @author Markus Berggrav
 */
public class TableButtonRenderer extends JButton implements TableCellRenderer {
    public TableButtonRenderer(){

        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        if (isSelected){
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        }
        else{
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        return this;
    }
}
