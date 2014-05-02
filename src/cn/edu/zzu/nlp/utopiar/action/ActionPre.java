package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ActionPre extends ActionGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphEditor editor = getEditor(e);
		mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
		mxGraph graph = graphComponent.getGraph();
		String str = null;
		try {
			mxGraph orGraph = EditorTabbedPane.getOR_GRAPH();
			String orPath = EditorTabbedPane.getOR_PATH();
			str = getSaveStr(editor, orGraph);
			if(str==null){
				return;
			}
			SaveTree.save(TreeParser.getNow(), str ,orPath);
			str = getSaveStr(editor, graph);
			if(str==null){
				return;
			}			
			SaveTree.save(TreeParser.getNow(), str, EditorTabbedPane.getPATH());
			int count = TreeParser.getNow();
			count--;
			if(count<0){
				JOptionPane.showMessageDialog(editor, "请从此句开始校对！","警告",JOptionPane.WARNING_MESSAGE);
				return;
			}
			refreshTree(editor, count);
			EditorBottom.getTextArea().setText(SetLabel.setLabel());
		} catch (Exception e1) {
			e1.printStackTrace();
		}				
	}

}
