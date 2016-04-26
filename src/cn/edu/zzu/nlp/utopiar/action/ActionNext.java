package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;

public class ActionNext extends ActionGraph {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        boolean isGraphClosed = editor.doCloseGraph();
        if (isGraphClosed) {
            int count = TreeParser.getNow();
            if(count + 1 >TreeParser.getMAXCOUNT()-1){
                JOptionPane.showMessageDialog(editor, 
                    Languages.getInstance().getString( "Message.NoMoreNextSentence.Body" ),
                    Languages.getInstance().getString( "Message.NoMoreNextSentence.Title" ),
                    JOptionPane.WARNING_MESSAGE);
            }
            else
                count++;
            refreshTree(editor, count);
            EditorBottom.getTextArea().setText(editor.getLabelString());
        }
    }

}
