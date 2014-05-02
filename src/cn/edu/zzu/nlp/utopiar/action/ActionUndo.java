package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public class ActionUndo extends ActionGraph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	protected boolean undo;

	/**
	 * 
	 */
	public ActionUndo(boolean undo)
	{
		this.undo = undo;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e)
	{
		GraphEditor editor = getEditor(e);

		if (editor != null)
		{
			if (undo)
			{
				editor.getUndoManager().undo();
			}
			else
			{
				editor.getUndoManager().redo();
			}
		}
	}

}
