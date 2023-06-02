// -------------------------------------------------------------------------------------------------------------------- //
// import libraries

package no.uib.PMCExplorer.TextNode;
import java.awt.Color;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import static no.uib.PMCExplorer.TextNode.TextNode.findInstancesOfKeyWord;


// -------------------------------------------------------------------------------------------------------------------- //

/**
 * CLass responsible for highlighting text in the GUI.
 * 
 * @author maber
 */
public class CustomHighlighter {
    JEditorPane editorPane;
    
    
    /**
     * Constructor. 
     * 
     * @param editorPane - Text container where the highlighting occurs. 
     */
    public CustomHighlighter(JEditorPane editorPane){
      this.editorPane = editorPane;
        
    }
    
    /**
     * Method responsible for iterating keywords and finding the instances of each keyword.
     * 
     * @param keyWords - List of input keywords.
     * @param color - Chosen highlight color.
     * @throws BadLocationException 
     */
    public void highlightKeywords(String[] keyWords, Color color) throws BadLocationException {
      if (keyWords.length > 0){
        for (String word: keyWords){
            if (word.length() > 1){
                Document doc = editorPane.getDocument();
                List<List<Integer>> indexes = findInstancesOfKeyWord(doc.getText(0, doc.getLength()),word);
                if (indexes != null && !indexes.isEmpty()){
                    highlightText(indexes,color);
                }

                }
            }
        }
    }
    
    
    /**
     * Method that performs the actual highlighting. 
     * 
     * @param indexes - Indexes (start,stop) of the string that should be highlighted.
     * @param color - Highlight color.
     */
    public void highlightText (List<List<Integer>> indexes, Color color) {
        if (!indexes.isEmpty()){
            Highlighter highlighter = editorPane.getHighlighter();

            Highlighter.HighlightPainter painter =
                    new DefaultHighlighter.DefaultHighlightPainter(color);

            for (List<Integer> index : indexes) {
                int p0 = index.get(0);
                int p1 = index.get(1);
                try {
                    removeHighlight(p0);
                    highlighter.addHighlight(p0, p1, painter);

                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    } 
    
    
    /**
     * When changing highlight color, the old color has to be removed before the new
     * color can be introduced.
     * 
     * 
     * @param start - start index of the old highlight (used to locate the text)
     */
    public void removeHighlight(int start){
        Highlighter.Highlight[] highlights = editorPane.getHighlighter().getHighlights();
        for (Highlighter.Highlight highlight : highlights){
            int highlightStart = highlight.getStartOffset();
            if (highlightStart == start){
                editorPane.getHighlighter().removeHighlight(highlight);
            }
        }
    } 
}
