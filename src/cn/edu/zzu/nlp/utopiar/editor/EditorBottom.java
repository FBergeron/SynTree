package cn.edu.zzu.nlp.utopiar.editor;

import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class EditorBottom extends JScrollPane{

    /**
     * 
     */
    private static final long serialVersionUID = 6856024347910766867L;
    
    public EditorBottom(GraphEditor editor){
        JCheckBox fencibBox = new JCheckBox("分词");
        //textArea.setLineWrap(true);
        //textArea.setWrapStyleWord(true);
//      textArea.setEditable(false);
        
        setViewportView(textArea);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, fencibBox);
    }

    public JEditorPane getTextArea() {
        return textArea;
    }   
    
    private JEditorPane textArea = new JEditorPane("text/html","<html>This is <u>a test</u>.</html>");
    
}
