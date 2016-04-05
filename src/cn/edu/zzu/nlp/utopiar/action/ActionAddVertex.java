package cn.edu.zzu.nlp.utopiar.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.Util;

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
        try {
            StringBuilder strStyle = new StringBuilder();
            Integer boxFontSize = Preferences.getInstance().getBoxFontSize();
            if( boxFontSize != null )
                strStyle.append( "fontSize=" + boxFontSize + ";" );
            Color boxBackgroundColor = Preferences.getInstance().getBoxBackgroundColor();
            if( boxBackgroundColor != null ) 
                strStyle.append( "fillColor=" + Util.colorRGBToHex( boxBackgroundColor ) + ";" );
            Color boxForegroundColor = Preferences.getInstance().getBoxForegroundColor();
            if( boxForegroundColor != null ) 
                strStyle.append( "fontColor=" + Util.colorRGBToHex( boxForegroundColor ) + ";" );
            Color boxBorderColor = Preferences.getInstance().getBoxBorderColor();
            if( boxBorderColor != null ) 
                strStyle.append( "strokeColor=" + Util.colorRGBToHex( boxBorderColor ) + ";" );
            Integer boxWidth = Preferences.getInstance().getBoxWidth();
            Integer boxHeight = Preferences.getInstance().getBoxHeight();
            graph.insertVertex(parent, null, "我来了~", 0, 0, boxWidth == null ? Preferences.DEFAULT_BOX_WIDTH : boxWidth, boxHeight == null ? Preferences.DEFAULT_BOX_HEIGHT : boxHeight, strStyle.toString());
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        
    }

}
