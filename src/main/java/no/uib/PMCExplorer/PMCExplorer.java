

package no.uib.PMCExplorer;
import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import no.uib.PMCExplorer.GUI.MultipleArticles;

/**
 *
 * @author maber
 */
public class PMCExplorer {
    
    public static String Downloads_Folder_Url;

    public static void main(String[] args) { 
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
        
        
        
        try {
            Downloads_Folder_Url = new File(PMCExplorer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/PMC_Explorer_downloads";
        } catch (URISyntaxException ex) {
            Logger.getLogger(PMCExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File downloads = new File(Downloads_Folder_Url);
            if (!downloads.exists()) {
                downloads.mkdir();
            }
            
        new MultipleArticles().setVisible(true);
    }
}
