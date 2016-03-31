package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class ActionSave extends ActionGraph{

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
            str = getSaveStr(editor, EditorTabbedPane.getOR_GRAPH());
            System.out.println(EditorTabbedPane.getOR_GRAPH());
            if(str==null){
                return;
            }
            System.out.println(str);
            SaveTree.save(TreeParser.getNow(), str ,EditorTabbedPane.getOR_PATH());
            SaveTree.save(EditorTabbedPane.getOR_PATH());
            System.out.println(EditorTabbedPane.getOR_PATH());
            str = getSaveStr(editor, graph);
            System.out.println(graph);
            if(str==null){
                System.out.print("str="+str);
                return;
            }
            System.out.println(str);
            System.out.println(EditorTabbedPane.getPATH());
            SaveTree.save(TreeParser.getNow(), str, EditorTabbedPane.getPATH());
            SaveTree.save(EditorTabbedPane.getPATH());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
