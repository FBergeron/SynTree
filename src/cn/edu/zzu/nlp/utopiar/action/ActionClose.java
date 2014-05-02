package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorSettingFrame;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
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
		GraphEditor editor = getEditor(e);
		refreshTree(editor, TreeParser.getNow());
		EditorBottom.getTextArea().setText(SetLabel.setLabel());
	}

}
