package cn.edu.zzu.nlp.utopiar.util;

import java.util.List;

import cn.edu.zzu.nlp.readTree.Constraint;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.EditorToolBar;

public class SetLabel {

	public static String setLabel(){
		String label = "";
		if(EditorToolBar.FLAG==2){
			List<String> splitList = TreeParser.getSplitList();
			for (String string : splitList) {
				label += string;
			}
			return label;
		}
		List<String> temp = TreeParser.getLeaf();
		int index = 0;
		for (String string : temp) {
			if(EditorToolBar.FLAG==0/*&&!EditorTabbedPane.iszH()*/||EditorToolBar.FLAG==3){
				label += string;
				label += " ";
//			}else if(EditorToolBar.FLAG==0&&EditorTabbedPane.iszH()){
//				label += string;
			}
			if(EditorToolBar.FLAG==1){
				label += String.valueOf(index)+string;
				label +="  ";
			}
			index++;
		}
		
		//读取已有约束并显示
		if(EditorToolBar.FLAG==0){
			if(EditorTabbedPane.getPATH().equalsIgnoreCase(EditorTabbedPane.getChinesePath())){
				if (TreeParser.zhConstraint.get(TreeParser.getNow())!=null
						&&TreeParser.zhConstraint.get(TreeParser.getNow()).length()!=0){
					label = TreeParser.zhConstraint.get(TreeParser.getNow());
					if(!label.contains("@#@#@#")){
						label += "@#@#@# ";
					}
				}else if(!label.contains("@#@#@#")){
					TreeParser.zhConstraint.put(TreeParser.getNow(), label);
					label += "@#@#@# ";
				}
			}else if(EditorTabbedPane.getPATH().equalsIgnoreCase(EditorTabbedPane.getEnglishPath())){
				if (TreeParser.engConstraint.get(TreeParser.getNow())!=null
						&&TreeParser.engConstraint.get(TreeParser.getNow()).length()!=0){
					label = TreeParser.engConstraint.get(TreeParser.getNow());
					if(!label.contains("@#@#@#")){
						label += "@#@#@# ";
					}
				}else if(!label.contains("@#@#@#")){
					TreeParser.engConstraint.put(TreeParser.getNow(), label);
					label += "@#@#@# ";
				}
			}
			else if(!label.contains("@#@#@#")){
				label += "@#@#@# ";
			}
		}
		
		if(EditorToolBar.FLAG==3){
//			TreeParser.readData(EditorTabbedPane.getPATH());
			Constraint.constraint = "";
			List<String> list = TreeParser.getWord(TreeParser.getNow(), TreeParser.selectData(EditorTabbedPane.getPATH()));
			Constraint.leafCount = 0;
			Constraint.creatTree(list);
			label += "  @#@#@#  " + Constraint.constraint;
		}
		return label;
	}
}
