package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;

public class ActionRefresh extends ActionGraph {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        int revertConfirmation = JOptionPane.showConfirmDialog(null, "Do you really want to revert all your modifications?","Question", JOptionPane.YES_NO_OPTION);
        if(revertConfirmation == JOptionPane.YES_OPTION){ 
            GraphEditor editor = getEditor(e);
            TreeParser.readData(EditorTabbedPane.getPATH());
            ActionGraph.refreshTree(editor, TreeParser.getNow());
            EditorBottom.getTextArea().setText(SetLabel.setLabel());
        }
    }

}
