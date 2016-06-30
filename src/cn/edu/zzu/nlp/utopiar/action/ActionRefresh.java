package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;

public class ActionRefresh extends ActionGraph {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        int revertConfirmation = JOptionPane.showConfirmDialog(null, 
            Languages.getInstance().getString( "ActionRefresh.ConfirmRevert.Body" ),
            Languages.getInstance().getString( "Message.Title.Question" ), 
                JOptionPane.YES_NO_OPTION);
        if(revertConfirmation == JOptionPane.YES_OPTION){ 
            GraphEditor editor = getEditor(e);
            editor.getTabbedPane().getCurrentPane().readData(); 
            editor.refreshTree(editor.getNow());
            editor.updateBottomTextArea();
        }
    }

}
