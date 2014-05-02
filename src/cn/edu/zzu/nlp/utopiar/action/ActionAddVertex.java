package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public class ActionAddVertex extends ActionGraph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
		mxGraph graph = graphComponent.getGraph();
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		try
		{
			graph.insertVertex(parent, null, "我来了~", 0, 0, 60,
					30);
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		
	}

}
