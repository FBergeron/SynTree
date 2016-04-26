package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.Action;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.EditorToolBar;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;

public class ActionGo extends ActionGraph implements Action {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        boolean isGraphClosed = editor.doCloseGraph();
        if (isGraphClosed) {
            int now = Integer.parseInt(EditorToolBar.getTextfield().getText());
            now--;
            if(now<0||now>TreeParser.getMAXCOUNT()-1){
                JOptionPane.showMessageDialog(editor, 
                    MessageFormat.format( 
                        Languages.getInstance().getString( "Message.EnterSentenceNumberWithinRange.Body" ), 
                            TreeParser.getMAXCOUNT() ),
                    Languages.getInstance().getString( "Message.Title.Warning" ),
                    JOptionPane.WARNING_MESSAGE);
                EditorToolBar.getTextfield().setText("");
                return;
            }
            refreshTree(editor, now);
            EditorBottom.getTextArea().setText(editor.getLabelString());
        }
    }

}
