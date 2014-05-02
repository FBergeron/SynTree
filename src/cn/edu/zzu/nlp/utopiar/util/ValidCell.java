package cn.edu.zzu.nlp.utopiar.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;
import com.mxgraph.view.mxGraph.mxICellVisitor;

public class ValidCell {

	public static void valid(GraphEditor editor){
		final mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
		final mxGraph graph = graphComponent.getGraph();
		Object root = null;
		List<Object> rootList = graph.findTreeRoots(graph.getDefaultParent(),false,false);
		if (rootList.size() > 1) {
			JOptionPane.showMessageDialog(editor, "请确保有且仅有一个根节点！");
			return;
		} else {
			for (Object object : rootList) {
				root = object;
			}
		}
		final List<mxMultiplicity> mxMultiplicities = new ArrayList<mxMultiplicity>();
		final List<String> havelist = new ArrayList<String>();
		graph.traverse(root, true, new mxICellVisitor() {
			public boolean visit(Object vertex, Object edge) {
				if(havelist.contains(graph.convertValueToString(vertex))){
					return true;
				}
				havelist.add(graph.convertValueToString(vertex));
				mxMultiplicities.add(new mxMultiplicity(false, graph.convertValueToString(vertex), null, null, 1, "1",
						null, "请确保每个子节点有且仅有一个父节点！", null, false));
				return true;
			}
		});
		mxMultiplicity[] multiplicities = new mxMultiplicity[mxMultiplicities.size()];
		for (int i=0;i<mxMultiplicities.size();i++) {
			multiplicities[i] = mxMultiplicities.get(i);
		}
		graph.setMultiplicities(multiplicities);
		
		graph.getModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				graphComponent.validateGraph();
			}
		});
	}
	
	/**
	 * 判断是否全由数字组成
	 * @param str
	 */
	public static boolean isNum(String str){
		String num = "1234567890";
		for(int i=0;i<str.length();i++){
			String single = str.substring(i, i+1);
			if(num.indexOf(single)<0)
				return false;
		}
		return true;
	}
}
