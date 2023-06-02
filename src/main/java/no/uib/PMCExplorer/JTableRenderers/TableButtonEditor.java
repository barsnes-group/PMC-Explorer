// -------------------------------------------------------------------------------------------------------------------- //
// import libraries: 

package no.uib.PMCExplorer.JTableRenderers;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// -------------------------------------------------------------------------------------------------------------------- //

/**
 * Custom editor for buttons inside table cells.
 * 
 * 
 * @author Markus Berggrav
 */

public class TableButtonEditor extends AbstractCellEditor implements TableCellEditor,ActionListener {
    private JButton button;
    private String currentValue;


    public TableButtonEditor() {
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(this);


    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
        button.setText(value.toString());
        currentValue = value.toString();
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();

    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }
}
