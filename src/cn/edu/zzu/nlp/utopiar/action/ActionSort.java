package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.EditorToolBar;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;

public class ActionSort extends ActionGraph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphEditor editor = getEditor(e);
		mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
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
				JOptionPane.showMessageDialog(editor, "现有"+list.size()+"个根节点");
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
					JOptionPane.showMessageDialog(editor, "父节点没有连好！");
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
					JOptionPane.showMessageDialog(editor, "子节点没有连好！");
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
				JOptionPane.showMessageDialog(editor, "请确保不含有孤立结点！");
				return;
			}
			graph.clearSelection();
			String temp = (String) traverse(cell, graph).get(1);
			String[] strings = temp.split(" ");
			for (String string2 : strings) {
				if(!string2.contains(")(")&&string2.contains("(")&&!string2.startsWith("(")){
					JOptionPane.showMessageDialog(editor, "请核对叶子结点是否正确！","警告",JOptionPane.WARNING_MESSAGE);
					return ;
				}
			}
			temp = "( " + temp.replaceAll("\\)\\(", "\\) \\(") + " )";
			SaveTree.save(TreeParser.getNow(), temp, EditorTabbedPane.getPATH());
			graph.selectAll();
			graph.removeCells();
			TreeParser.setCountleaf(1);
			List<String> list1 = TreeParser.getWord(TreeParser.getNow(),TreeParser.selectData(EditorTabbedPane.getPATH()));
			TreeParser.getLeaf().clear();
			TreeParser.getSplitList().clear();
			TreeParser.vertex.clear();
			EditorToolBar.getCombobox().setSelectedIndex(0);
			TreeParser.creatTree(editor,graphComponent,list1,  0);
			EditorBottom.getTextArea().setText(SetLabel.setLabel());
		} catch (Exception e1) {
			e1.printStackTrace();
	    }		
	}

}
