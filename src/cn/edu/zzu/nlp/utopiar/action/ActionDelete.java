package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public class ActionDelete extends ActionGraph {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        mxGraphComponent graphComponent = editor.getGraphComponent();
        mxGraph graph = graphComponent.getGraph();
        if( graph != null )
            graph.removeCells();
    }

}
