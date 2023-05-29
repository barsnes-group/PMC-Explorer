

package no.uib.PMCExplorer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import no.uib.PMCExplorer.GUI.MultipleArticles;

/**
 *
 * @author maber
 */
public class PMCExplorer {

    public static void main(String[] args) { 
        
        setLookAndFeel();
        
        new MultipleArticles().setVisible(true);
    }
    
    
    
    
    
    private static void setLookAndFeel(){
        
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            break;
        }}
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}}
        
    }
}
