package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

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
        TreeParser parser = editor.getTabbedPane().getCurrentPane();
        boolean isGraphClosed = editor.doCloseGraph();
        if (isGraphClosed) {
            JTextField textFieldGraphNumber = editor.getTextFieldGraphNumber();
            if (textFieldGraphNumber != null) {
                int now = Integer.parseInt(textFieldGraphNumber.getText());
                now--;
                if(now<0||now>parser.getMaxCount()-1){
                    JOptionPane.showMessageDialog(editor, 
                        MessageFormat.format( 
                            Languages.getInstance().getString( "Message.EnterSentenceNumberWithinRange.Body" ), 
                                parser.getMaxCount() ),
                        Languages.getInstance().getString( "Message.Title.Warning" ),
                        JOptionPane.WARNING_MESSAGE);
                    textFieldGraphNumber.setText("");
                    return;
                }
                editor.refreshTree(now);
                editor.updateBottomTextArea();
            }
        }
    }

}
