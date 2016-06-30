package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ActionPre extends ActionGraph {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        boolean isGraphClosed = editor.doCloseGraph();
        if (isGraphClosed) {
            int count = editor.getNow();
            if(count - 1 < 0){
                JOptionPane.showMessageDialog(editor, 
                    Languages.getInstance().getString( "Message.NoMorePreviousSentence.Body" ),
                    Languages.getInstance().getString( "Message.NoMorePreviousSentence.Title" ),
                    JOptionPane.WARNING_MESSAGE);
            }
            else
                count--;
            editor.refreshTree(count);
            editor.updateBottomTextArea();
        }
    }

}
