package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGraph;
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;
import cn.edu.zzu.nlp.utopiar.util.ValidCell;


public class EditorTabbedPane extends JTabbedPane{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String CHINESE_PATH = "data/train.ch.parse";
    public static String ENGLISH_PATH = "data/train.en.parse";
    
    public static final mxGraph ZH_GRAPH = new mxGraph(){
        public String getToolTipForCell(Object cell)
        {
            if(TreeParser.vertex.containsKey(cell))
                return TreeParser.vertex.get(cell);
            else {
                return convertValueToString(cell);
            }           
        }
    };
    public static final mxGraphComponent ZH_GRAPH_COMPONENT = new mxGraphComponent(ZH_GRAPH);
    public static final mxGraph ENG_GRAPH = new mxGraph(){
        public String getToolTipForCell(Object cell)
        {
            if(TreeParser.vertex.containsKey(cell))
                return TreeParser.vertex.get(cell);
            else {
                return convertValueToString(cell);
            }           
        }
    };
    public static final mxGraphComponent ENG_GRAPH_COMPONENT = new mxGraphComponent(ENG_GRAPH);
    public static String PATH = "data/train.ch.parse";
    public static mxGraph OR_GRAPH = ENG_GRAPH;
    public static String OR_PATH = "data/train.en.parse";
    public static boolean iszH = true; 
    public static mxGraphComponent OR_GraphComponent = ENG_GRAPH_COMPONENT;
    
    public EditorTabbedPane(final GraphEditor editor) throws IOException{
       
        this.editor = editor;

        this.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
        
        JPanel chinesePane = new TreeParser(editor,ZH_GRAPH_COMPONENT,CHINESE_PATH,true);

//      GraphEditor.setGraphComponent(ENG_GRAPH_COMPONENT);
        setOR_GRAPH(ENG_GRAPH);
        setOR_PATH(ENGLISH_PATH);
        setOR_GraphComponent(ENG_GRAPH_COMPONENT);
        JPanel englishPane = new TreeParser(editor,ENG_GRAPH_COMPONENT,ENGLISH_PATH,false);
        addTab("中文",null,chinesePane);
        addTab("英文",null,englishPane);
        ((JTabbedPane)ZH_GRAPH_COMPONENT.getParent().getParent()).setToolTipTextAt(0, CHINESE_PATH);
        ((JTabbedPane)ENG_GRAPH_COMPONENT.getParent().getParent()).setToolTipTextAt(1, ENGLISH_PATH);
        drag(this,editor);
    }

    public void update() {
        EditorToolBar.getCombobox().setSelectedIndex(0);
        TreeParser.getSplitList().clear();
        mxGraph saveGraph = null;
        String savePath = null;
        if(PATH.equalsIgnoreCase(CHINESE_PATH)){
            saveGraph = ZH_GRAPH;
            savePath = CHINESE_PATH;
        }else {
            saveGraph = ENG_GRAPH;
            savePath = ENGLISH_PATH;
        }
        String str = ActionGraph.getSaveStr(editor, saveGraph);
        if(str==null){
            return;
        }
        try {
            SaveTree.save(TreeParser.getNow(), str ,savePath);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if(getSelectedIndex()==0){
            PATH = CHINESE_PATH;
            OR_GRAPH = ENG_GRAPH;
            OR_PATH = ENGLISH_PATH;
            OR_GraphComponent = ENG_GRAPH_COMPONENT;
            iszH = true;
            Color prefColor = Preferences.getInstance().getGraphBackgroundColor();
            if( prefColor != null )
                ZH_GRAPH_COMPONENT.getViewport().setBackground( prefColor );
            GraphEditor.changeUndo(ZH_GRAPH_COMPONENT);
            GraphEditor.setGraphComponent(ZH_GRAPH_COMPONENT);
            TreeParser.setCountleaf(1);
            ZH_GRAPH.selectAll();
            ZH_GRAPH.removeCells();
            List<String> list1 = TreeParser.getWord(TreeParser.getNow(),TreeParser.selectData(PATH));
            TreeParser.getLeaf().clear();
            TreeParser.getSplitList().clear();
            TreeParser.vertex.clear();
            TreeParser.creatTree(editor,ZH_GRAPH_COMPONENT,list1,0);
            ValidCell.valid(editor);
            EditorBottom.getTextArea().setText(SetLabel.setLabel());
            EditorToolBar.getDescription().setText("   当前第"+(TreeParser.getNow()+1)+"条,共"+TreeParser.ZHCOUNT+"条    ");
        }else{
            PATH = ENGLISH_PATH;
            OR_GRAPH = ZH_GRAPH;
            OR_PATH = CHINESE_PATH;
            OR_GraphComponent = ZH_GRAPH_COMPONENT;
            iszH = false;
            GraphEditor.changeUndo(ENG_GRAPH_COMPONENT);
            Color prefColor = Preferences.getInstance().getGraphBackgroundColor();
            if( prefColor != null )
                ENG_GRAPH_COMPONENT.getViewport().setBackground( prefColor );
            GraphEditor.setGraphComponent(ENG_GRAPH_COMPONENT);
            TreeParser.setCountleaf(1);
            ENG_GRAPH.selectAll();
            ENG_GRAPH.removeCells();
            List<String> list1 = TreeParser.getWord(TreeParser.getNow(),TreeParser.selectData(PATH));
            TreeParser.getLeaf().clear();
            TreeParser.getSplitList().clear();
            TreeParser.vertex.clear();
            TreeParser.creatTree(editor,ENG_GRAPH_COMPONENT,list1,0);
            ValidCell.valid(editor);
            EditorBottom.getTextArea().setText(SetLabel.setLabel());
            EditorToolBar.getDescription().setText("   当前第"+(TreeParser.getNow()+1)+"条,共"+TreeParser.ENGCOUNT+"条    ");
        }
    }

    public static String getPATH() {
        return PATH;
    }

    public static void setPATH(String pATH) {
        PATH = pATH;
    }

    public static mxGraph getZhGraph() {
        return ZH_GRAPH;
    }

    public static mxGraph getEngGraph() {
        return ENG_GRAPH;
    }

    public static String getChinesePath() {
        return CHINESE_PATH;
    }

    public static String getEnglishPath() {
        return ENGLISH_PATH;
    }

    public static void setCHINESE_PATH(String cHINESEPATH) {
        CHINESE_PATH = cHINESEPATH;
    }

    public static void setENGLISH_PATH(String eNGLISHPATH) {
        ENGLISH_PATH = eNGLISHPATH;
    }

    public static mxGraph getOR_GRAPH() {
        return OR_GRAPH;
    }

    public static void setOR_GraphComponent(mxGraphComponent oRGraphComponent) {
        OR_GraphComponent = oRGraphComponent;
    }

    public static void setOR_GRAPH(mxGraph oRGRAPH) {
        OR_GRAPH = oRGRAPH;
    }

    public static mxGraphComponent getZhGraphComponent() {
        return ZH_GRAPH_COMPONENT;
    }

    public static mxGraphComponent getEngGraphComponent() {
        return ENG_GRAPH_COMPONENT;
    }


    
    public static mxGraphComponent getOR_GraphComponent() {
        return OR_GraphComponent;
    }

    public static boolean iszH() {
        return iszH;
    }

    public static String getOR_PATH() {
        return OR_PATH;
    }

    public static void setOR_PATH(String oRPATH) {
        OR_PATH = oRPATH;
    }

    public void drag(final JTabbedPane comp,final GraphEditor editor)//定义的拖拽方法
    {
        //panel表示要接受拖拽的控件
        new DropTarget(comp, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter()
        {
            @SuppressWarnings("unchecked")
            @Override
            public void drop(DropTargetDropEvent dtde)//重写适配器的drop方法
            {
                try
                {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
                        List<File> list =  (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        String temp="";
                        if(list.size()>1){
                            JOptionPane.showMessageDialog(editor, "请拖入一个文件！","警告",JOptionPane.WARNING_MESSAGE);
                        }else{
                            for(File file:list)
                                temp+=file.getAbsolutePath();
                            if(comp.getSelectedIndex()==1){                             
                                int flag = JOptionPane.showConfirmDialog(null, "确认显示？","确认", JOptionPane.YES_NO_OPTION); 
                                if(flag == 0){
                                    EditorTabbedPane.setENGLISH_PATH(temp);
                                    EditorTabbedPane.setPATH(temp);
                                    TreeParser.readData(EditorTabbedPane.getEnglishPath());
                                    ActionGraph.refreshTree(editor, 0);
                                    EditorBottom.getTextArea().setText(SetLabel.setLabel());
                                    ((JTabbedPane)ENG_GRAPH_COMPONENT.getParent().getParent()).setToolTipTextAt(1, temp);
                                }
                            }
                            else if(comp.getSelectedIndex()==0){                                
                                int flag = JOptionPane.showConfirmDialog(null, "确认显示？","确认", JOptionPane.YES_NO_OPTION); 
                                if(flag == 0){
                                    EditorTabbedPane.setCHINESE_PATH(temp);
                                    EditorTabbedPane.setPATH(temp);
                                    TreeParser.readData(EditorTabbedPane.getChinesePath());
                                    ActionGraph.refreshTree(editor, 0);
                                    EditorBottom.getTextArea().setText(SetLabel.setLabel());
                                    ((JTabbedPane)ZH_GRAPH_COMPONENT.getParent().getParent()).setToolTipTextAt(0, temp);
                                }
                            }
                        }                        
                        dtde.dropComplete(true);//指示拖拽操作已完成
                    }
                    else
                    {
                        dtde.rejectDrop();//否则拒绝拖拽来的数据
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private GraphEditor editor;

}
