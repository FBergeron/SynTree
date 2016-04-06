package cn.edu.zzu.nlp.utopiar.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
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
import cn.edu.zzu.nlp.utopiar.util.ValidCell;

public abstract class ActionGraph extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param e
     * @return Returns the graph for the given action event.
     */
    public static final GraphEditor getEditor(ActionEvent e)
    {
        if (e.getSource() instanceof Component)
        {
            Component component = (Component) e.getSource();

            while (component != null
                    && !(component instanceof GraphEditor))
            {
                component = component.getParent();
            }

            return (GraphEditor) component;
        }

        return null;
    }
    
    public static List<Object> traverse(Object vertex, mxGraph graph)
    {
        List<Object> roots = graph.findTreeRoots(graph.getDefaultParent());
        List<Object> point = new ArrayList<Object>(2);
        Object root = null;
        if(roots.size()==0){
            point.add(0);
            point.add("");
            return point;
        }
        root = roots.get(0);
        String string = "";
        Integer x = 0 ;
        if (vertex != null)
        {
            int edgeCount = graph.getModel().getEdgeCount(vertex);
            //出度和入度的和为1的结点只有叶子结点也根节点，此处需要判断为非根节点
            if(edgeCount==1&&!vertex.equals(root)){
                string = graph.getLabel(vertex);
                x = Integer.valueOf((int)graph.getCellBounds(vertex).getCenterX());
                point.add(x);
                point.add(string);
                return point;
            }else {
                //非叶子结点需要加括号
                string = "("+graph.getLabel(vertex)+" ";
            }
            List<List<Object>> cells = new ArrayList<List<Object>>();
            if (edgeCount > 0)
            {
                for (int i = 0; i < edgeCount; i++)
                {
                    Object e1 = graph.getModel().getEdgeAt(vertex, i);
                    boolean isSource = graph.getModel().getTerminal(e1, true) == vertex;

                    if (isSource)
                    {                       
                        Object next = graph.getModel().getTerminal(e1, !isSource);
                        if(next!=null){
                            List<Object> temp;
                            temp = traverse(next ,graph) ;
                            x += (Integer)temp.get(0);
                            cells.add(temp);
                        }                                                   
                    }
                }
                //处理根节点只有一个孩子的情况
                if(vertex.equals(root)&&edgeCount==1)
                    edgeCount++;
                x = x/(edgeCount-1);
                point.add(x);
            }
            for (int i = cells.size() - 1; i > 0; --i) {
                boolean isSort=false;
                for (int j = 0; j < i; ++j) {
                    if (((Integer)((List<Object>)cells.get(j+1)).get(0))< ((Integer)((List<Object>)cells.get(j)).get(0))){
                        List<Object> temp = (List<Object>) cells.get(j);
                        cells.set(j, cells.get(j+1));
                        cells.set(j+1, temp);
                        isSort=true;
                    }
                }
                if(!isSort)break;
            }
            for (int i =0 ;i<cells.size();i++) {
                String temp;
                temp = (String) ((List<Object>)cells.get(i)).get(1) ;
                string = string +  temp ;
            }
            if(edgeCount!=1)
                string = string +  ")";
        }
        point.add(string);
        return point;
    }
    
    public static void refreshTree(GraphEditor editor, int count){
        mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
        mxGraph graph = graphComponent.getGraph();
        mxGraph orGraph = EditorTabbedPane.getOR_GRAPH();
        String orPath = EditorTabbedPane.getOR_PATH();
        mxGraphComponent orGraphComponent= EditorTabbedPane.getOR_GraphComponent();
        graph.selectAll();
        graph.removeCells();
        orGraph.selectAll();
        orGraph.removeCells();
        TreeParser.setCountleaf(1);
        TreeParser.setNow(count);
        
        List<String> list = TreeParser.getWord(count,TreeParser.selectData(orPath));
        TreeParser.getLeaf().clear();
        TreeParser.getSplitList().clear();
        TreeParser.vertex.clear();
        TreeParser.creatTree(editor,orGraphComponent,list, Preferences.DEFAULT_OFFSET_Y);      
        TreeParser.setCountleaf(1);
        List<String> list1 = TreeParser.getWord(TreeParser.getNow(),TreeParser.selectData(EditorTabbedPane.getPATH()));
        TreeParser.getLeaf().clear();
        TreeParser.getSplitList().clear();
        TreeParser.vertex.clear();
        TreeParser.creatTree(editor, graphComponent, list1, Preferences.DEFAULT_OFFSET_Y);     
        ValidCell.valid(editor);
        EditorToolBar.getCombobox().setSelectedIndex(0);
        editor.update();
    }
    
    public static String getSaveStr(GraphEditor editor, mxGraph graph){
        String str="";
        Object cell = null;
        List<Object> list = graph.findTreeRoots(graph.getDefaultParent());
        if(list.size()>1){
            String str1 = "";
            for (int i =0;i<list.size();i++) {
                str1=str1+graph.getLabel(list.get(i))+"  ";
            }
            graph.getModel().beginUpdate();
            graph.setCellStyle(":strockeColor=red;fillColor=red", list.toArray());                  
            graph.getModel().endUpdate();                   
            graph.refresh();
            JOptionPane.showMessageDialog(editor, 
                MessageFormat.format( Languages.getInstance().getString( "Message.TooManyRootVertices.Body" ), list.size() ),
                Languages.getInstance().getString( "Message.Title.Error" ),
                JOptionPane.ERROR_MESSAGE );
            return null;
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
                return null;
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
                return null;
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
            return null;
        }
        graph.clearSelection();
        str = (String) traverse(cell, graph).get(1);
        String[] strings = str.split(" ");
        for (String string2 : strings) {
            if(!string2.contains(")(")&&string2.contains("(")&&!string2.startsWith("(")){
                JOptionPane.showMessageDialog(editor, 
                    Languages.getInstance().getString( "Message.InvalidLeafVertexFound.Body" ),
                    Languages.getInstance().getString( "Message.Title.Error" ),
                    JOptionPane.ERROR_MESSAGE);
                return null ;
            }
        }           
        str = "( " + str.replaceAll("\\)\\(", "\\) \\(") + " )";
        return str;
    }
}
