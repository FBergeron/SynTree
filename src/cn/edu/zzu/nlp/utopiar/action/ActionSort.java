package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.EditorToolBar;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.Preferences;

public class ActionSort extends ActionGraph{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        mxGraphComponent graphComponent = editor.getGraphComponent();
        final mxGraph graph = graphComponent.getGraph();
        Object cell = null;
        try {
            List<Object> list = graph.findTreeRoots(graph.getDefaultParent());
                    
            if(list.size()>1){
                String str = "";
                for (int i =0;i<list.size();i++) {
                    str=str+graph.getLabel(list.get(i))+"  ";
                }               
                graph.getModel().beginUpdate();
                graph.setCellStyle(":strockeColor=red;fillColor=red", list.toArray());                  
                graph.getModel().endUpdate();                   
                graph.refresh();
                JOptionPane.showMessageDialog(editor, 
                    MessageFormat.format( Languages.getInstance().getString( "Message.TooManyRootVertices.Body" ), list.size() ),
                    Languages.getInstance().getString( "Message.Title.Error" ),
                    JOptionPane.ERROR_MESSAGE );
                return;
            }else {
                for (Object object : list) {
                    cell = object;
                }
            }
            graph.selectEdges();
            Object[] edges = graph.getSelectionCells();
            for (Object object : edges) {
                Object father = graph.getModel().getTerminal(object, true);
                if(father ==null){
                    Object son = graph.getModel().getTerminal(object, false);                   
                    graph.clearSelection();                 
                    Object[] sons = new Object[1];
                    sons[0] = son;
                    graph.getModel().beginUpdate();
                    graph.setCellStyle(":strockeColor=red;fillColor=red", sons);                    
                    graph.getModel().endUpdate();                   
                    graph.refresh();
                    JOptionPane.showMessageDialog(editor, 
                        Languages.getInstance().getString( "Message.VertexWithoutParent.Body" ),
                        Languages.getInstance().getString( "Message.Title.Error" ),
                        JOptionPane.ERROR_MESSAGE );
                    return;
                }
                Object son = graph.getModel().getTerminal(object, false);
                if(son ==null){
                    father = graph.getModel().getTerminal(object, true);                    
                    graph.clearSelection();
                    Object[] fathers = new Object[1];
                    fathers[0] = father;
                    graph.getModel().beginUpdate();
                    graph.setCellStyle(":strockeColor=red;fillColor=red", fathers);                 
                    graph.getModel().endUpdate();                   
                    graph.refresh();
                    JOptionPane.showMessageDialog(editor, 
                        Languages.getInstance().getString( "Message.VertexWithoutChildren.Body" ),
                        Languages.getInstance().getString( "Message.Title.Error" ),
                        JOptionPane.ERROR_MESSAGE );
                    return;
                }
            }
            graph.clearSelection();
            graph.selectCells(true, false);
            Object []objects = graph.getSelectionCells();
            List<Object> singleList = new ArrayList<Object>();
            for (Object object : objects) {
                if(objects.length==1)
                    break;
                int count = graph.getModel().getEdgeCount(object);              
                if(count<1){
                    singleList.add(object);                 
                }
            }
            if (singleList.size()>0) {              
                graph.clearSelection();
                graph.getModel().beginUpdate();
                graph.setCellStyle(":strockeColor=red;fillColor=red", singleList.toArray());                    
                graph.getModel().endUpdate();                   
                graph.refresh();
                JOptionPane.showMessageDialog(editor, 
                    Languages.getInstance().getString( "Message.IsolatedVertexFound.Body" ),
                    Languages.getInstance().getString( "Message.Title.Error" ),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            graph.clearSelection();
            String temp = (String) editor.traverse(cell, graph).get(1);
            String[] strings = temp.split(" ");
            for (String string2 : strings) {
                if(!string2.contains(")(")&&string2.contains("(")&&!string2.startsWith("(")){
                    JOptionPane.showMessageDialog(editor, 
                        Languages.getInstance().getString( "Message.InvalidLeafVertexFound.Body" ),
                        Languages.getInstance().getString( "Message.Title.Error" ),
                        JOptionPane.ERROR_MESSAGE);
                    return ;
                }
            }
            temp = "( " + temp.replaceAll("\\)\\(", "\\) \\(") + " )";
            editor.saveTree(editor.getNow(), temp, editor.getTabbedPane().getPath());
            graph.selectAll();
            graph.removeCells();
            TreeParser parser = editor.getTabbedPane().getCurrentPane();
            parser.setCountleaf(1);
            List<String> list1 = parser.getWord(editor.getNow(),parser.map);
            parser.getLeaf().clear();
            parser.getSplitList().clear();
            parser.vertex.clear();
            JComboBox<String> comboBoxViewMode = editor.getComboBoxViewMode();
            if (comboBoxViewMode != null)
                comboBoxViewMode.setSelectedIndex(0);
            parser.creatTree(editor,graphComponent,list1,  Preferences.DEFAULT_OFFSET_Y);
            editor.updateBottomTextArea();
        } catch (Exception e1) {
            e1.printStackTrace();
        }       
    }

}
