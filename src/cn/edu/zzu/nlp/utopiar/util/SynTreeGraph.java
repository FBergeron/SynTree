package cn.edu.zzu.nlp.utopiar.util;

import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public class SynTreeGraph extends mxGraph {

    public SynTreeGraph(GraphEditor editor) {
        this.editor = editor;
    }

    public String getToolTipForCell(Object cell)
    {
        TreeParser parser = editor.getTabbedPane().getCurrentPane();
        if(parser.vertex.containsKey(cell))
            return parser.vertex.get(cell);
        else
            return convertValueToString(cell);
    }

    private GraphEditor editor;

}

