package no.uib.PMCExplorer.JTableRenderers;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
/**
 *
 * @author maber
 */

public class TableColorRenderer extends JLabel implements TableCellRenderer {

    public TableColorRenderer(){
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground((Color) value);
        setForeground((Color) value);
        setBorder(null);
        return this;
    }
}
