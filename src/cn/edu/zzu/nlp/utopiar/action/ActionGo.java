package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.Action;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.editor.EditorBottom;
import cn.edu.zzu.nlp.utopiar.editor.EditorTabbedPane;
import cn.edu.zzu.nlp.utopiar.editor.EditorToolBar;
import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;
import cn.edu.zzu.nlp.utopiar.util.Languages;

public class ActionGo extends ActionGraph implements Action {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphEditor editor = getEditor(e);
        mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
        mxGraph graph = graphComponent.getGraph();
        int now = Integer.parseInt(EditorToolBar.getTextfield().getText());
        now--;
        int check = JOptionPane.showConfirmDialog(null, 
            Languages.getInstance().getString( "Message.ConfirmSaveData.Body" ),
            Languages.getInstance().getString( "Message.Title.Question" ),
            JOptionPane.YES_NO_CANCEL_OPTION);
        if(check==0){
            try{
            String str;
            mxGraph orGraph = EditorTabbedPane.getOR_GRAPH();
            String orPath = EditorTabbedPane.getOR_PATH();
            str = getSaveStr(editor, orGraph);
            if(str==null){
                return;
            }
            SaveTree.save(TreeParser.getNow(), str ,orPath);
            str = getSaveStr(editor, graph);
            if(str==null){
                return;
            }
                SaveTree.save(TreeParser.getNow(), str, EditorTabbedPane.getPATH());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else if(check == 2){
            return ;
        }
        if(now<0||now>TreeParser.getMAXCOUNT()-1){
            JOptionPane.showMessageDialog(editor, 
                MessageFormat.format( Languages.getInstance().getString( "Message.EnterSentenceNumberWithinRange.Body" ), TreeParser.getMAXCOUNT() ),
                Languages.getInstance().getString( "Message.Title.Warning" ),
                JOptionPane.WARNING_MESSAGE);
            EditorToolBar.getTextfield().setText("");
            return;
        }
        refreshTree(editor, now);
        EditorBottom.getTextArea().setText(editor.getLabelString());
    }

}
