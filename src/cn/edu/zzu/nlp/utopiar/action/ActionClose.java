package cn.edu.zzu.nlp.utopiar.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.UIManager;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorSettingFrame;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;

public class ActionClose extends ActionGraph {

    /**
     * 
     */
    private static final long serialVersionUID = -894470505908015159L;
    
    private EditorSettingFrame editorSettingFrame;

    public ActionClose(EditorSettingFrame editorSettingFrame) {
        this.editorSettingFrame = editorSettingFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editorSettingFrame.setVisible(false);

        GraphEditor editor = editorSettingFrame.getEditor();
        refreshTree(editor, TreeParser.getNow());
        EditorBottom.getTextArea().setText(SetLabel.setLabel());

        try {
            Preferences.getInstance().setLookAndFeel( editorSettingFrame.getLookAndFeel() );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = editorSettingFrame.getGraphBackgroundColor();
            Preferences.getInstance().setGraphBackgroundColor( UIManager.get( "ScrollPane.background").equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = editorSettingFrame.getBoxBackgroundColor();
            Preferences.getInstance().setBoxBackgroundColor( new Color( 195, 217, 255 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = editorSettingFrame.getBoxForegroundColor();
            Preferences.getInstance().setBoxForegroundColor( new Color( 119, 68, 0 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = editorSettingFrame.getBoxBorderColor();
            Preferences.getInstance().setBoxBorderColor( new Color( 100, 130, 185 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = editorSettingFrame.getEdgeColor();
            Preferences.getInstance().setEdgeColor( new Color( 100, 130, 185 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        editor.getTabbedPane().update();
    }

}
