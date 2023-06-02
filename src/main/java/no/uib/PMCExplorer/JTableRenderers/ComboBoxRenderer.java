// -------------------------------------------------------------------------------------------------------------------- //
// import libraries: 

package no.uib.PMCExplorer.JTableRenderers;
import javax.swing.*;
import java.awt.*;

// -------------------------------------------------------------------------------------------------------------------- //

/**
 * Custom renderer for painting background colors of a JComboBox.
 * 
 * @author Markus Berggrav
 */
public class ComboBoxRenderer extends JButton implements ListCellRenderer  {
    public ComboBoxRenderer(){
        setOpaque(true);

    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        setBackground((Color) value);
        setForeground((Color) value);
        setBorder(null);
        setText(" ");
      
        return this;
    }
}
