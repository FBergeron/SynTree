package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public class ActionSetting extends ActionGraph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphEditor editor = getEditor(e);
		editor.setting();
	}

}
