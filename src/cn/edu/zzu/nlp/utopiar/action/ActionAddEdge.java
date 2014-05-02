package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public class ActionAddEdge extends ActionGraph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphEditor editor = getEditor(e);
		JOptionPane.showMessageDialog(editor, "不好意思，该功能请稍后~");
	}

}
