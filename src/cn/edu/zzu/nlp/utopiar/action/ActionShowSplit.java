package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGraph;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;

public class ActionShowSplit extends ActionGraph implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		List<String> temp = TreeParser.getLeaf();
		String label = "";
		for (String string : temp) {
			label += string;
			label += " ";
		}
		EditorBottom.getTextArea().setText(label);
	}

}
