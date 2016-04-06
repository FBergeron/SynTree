package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;

public class ActionAddEdge extends ActionGraph{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        JOptionPane.showMessageDialog(editor, 
            Languages.getInstance().getString( "Message.NotImplemented.Body" ),
            Languages.getInstance().getString( "Message.Title.Warning" ),
            JOptionPane.WARNING_MESSAGE);
    }

}
