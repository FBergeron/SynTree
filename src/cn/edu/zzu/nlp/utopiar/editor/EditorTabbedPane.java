package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGraph;
import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.SynTreeGraph;


public class EditorTabbedPane extends JTabbedPane{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EditorTabbedPane(final GraphEditor editor) throws IOException{
        this.editor = editor;

        mxRectangle minGraphSize = new mxRectangle(0, 0, 20000, 3000);

        chineseGraph = new SynTreeGraph(editor);
        chineseGraph.setMinimumGraphSize(minGraphSize);

        englishGraph = new SynTreeGraph(editor);
        englishGraph.setMinimumGraphSize(minGraphSize);

        chineseGraphComponent = new mxGraphComponent(chineseGraph);
        chineseGraphComponent.getVerticalScrollBar().setUnitIncrement(10);

        englishGraphComponent = new mxGraphComponent(englishGraph);
        englishGraphComponent.getVerticalScrollBar().setUnitIncrement(10);

        chinesePane = new TreeParser(editor,chineseGraphComponent,chinesePath,true);
        int scrollValue = (int)(TreeParser.MARGIN * chineseGraphComponent.getGraph().getView().getScale());
        chineseGraphComponent.getHorizontalScrollBar().setValue(scrollValue);

        setOrPath(englishPath);
        setOrGraphComponent(englishGraphComponent);
        englishPane = new TreeParser(editor,englishGraphComponent,englishPath,false);
        scrollValue = (int)(TreeParser.MARGIN * englishGraphComponent.getGraph().getView().getScale());
        englishGraphComponent.getHorizontalScrollBar().setValue(scrollValue);
        addTab("",null,chinesePane);
        addTab("",null,englishPane);
        ((JTabbedPane)chineseGraphComponent.getParent().getParent()).setToolTipTextAt(0, chinesePath);
        ((JTabbedPane)englishGraphComponent.getParent().getParent()).setToolTipTextAt(1, englishPath);
        drag(this,editor);

        englishGraphComponent.getGraphControl().addMouseListener( new GraphMouseListener( englishGraphComponent ) );
        chineseGraphComponent.getGraphControl().addMouseListener( new GraphMouseListener( chineseGraphComponent ) );
        
        Languages.getInstance().addItemListener(
            new ItemListener() {
                public void itemStateChanged( ItemEvent evt ) {
                    Locale locale = (Locale)evt.getItem();
                    setLocale( locale );
                }
            }
        );

        mxIEventListener changeListener = new mxIEventListener() {
            public void invoke(Object source, mxEventObject evt) {
                editor.setModified(true);
            }
        };
        englishGraph.getModel().addListener(mxEvent.CHANGE, changeListener);
        chineseGraph.getModel().addListener(mxEvent.CHANGE, changeListener);
        this.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
    }

    public void setLocale( Locale locale ) {
        super.setLocale( locale );
        
        setTitleAt( 0, Languages.getInstance().getString( "EditorTabbedPane.Tab.Chinese.Title" ) );
        setTitleAt( 1, Languages.getInstance().getString( "EditorTabbedPane.Tab.English.Title" ) );
    }

    public class GraphMouseListener extends MouseAdapter {
        public GraphMouseListener( mxGraphComponent gc ) {
            this.graphComponent = gc;
        }

        public void mousePressed(MouseEvent e) {
            if( editor.getViewMode() == 0 ) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && cell instanceof mxCell) {
                    mxCell clickedCell = (mxCell)cell;
                    int[] range = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE };
                    findRange( clickedCell, range );
                    editor.setHighlight( range[ 0 ], range[ 1 ] );
                    editor.updateBottomTextArea();
                }
            }

            if( e.isPopupTrigger() )
                showGraphPopupMenu( e, graphComponent );
        }

        public void mouseReleased(MouseEvent e) {
            if( editor.getViewMode() == 0 ) {
                editor.clearHighlight();
                editor.updateBottomTextArea();
            }

            if( e.isPopupTrigger() )
                showGraphPopupMenu( e, graphComponent );
        }

        private void findRange( mxCell vertex, int[] range ) {
            if( vertex == null )
                return;
            int outgoingEdgeCount = 0;
            for( int i = 0; i < vertex.getEdgeCount(); i++ ) {
                mxCell edge = (mxCell)vertex.getEdgeAt( i );
                if( edge.getSource() == vertex ) {
                    mxCell targetVertex = (mxCell)edge.getTarget();
                    if( targetVertex != null ) {
                        outgoingEdgeCount++;
                        findRange( targetVertex, range );
                    }
                }
            }
            if( outgoingEdgeCount == 0 ) {
                TreeParser parser = editor.getTabbedPane().getCurrentPane();
                String strPos = parser.vertex.get(vertex);
                if( strPos != null ) {
                    try {
                        int pos = Integer.parseInt(strPos);
                        if( pos < range[ 0 ] )
                            range[ 0 ] = pos;
                        if( pos > range[ 1 ] )
                            range[ 1 ] = pos;
                    }
                    catch(NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mxGraphComponent graphComponent;
    }

    protected void showGraphPopupMenu(MouseEvent e, mxGraphComponent graphComponent)
    {
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
        EditorPopupMenu menu = new EditorPopupMenu(editor, graphComponent);
        menu.show(graphComponent, pt.x, pt.y);

        e.consume();
    }

    public void update() {
        JComboBox<String> comboBoxViewMode = editor.getComboBoxViewMode();
        if (comboBoxViewMode != null)
            comboBoxViewMode.setSelectedIndex(0);
        TreeParser parser = getCurrentPane();
        parser.getSplitList().clear();
        mxGraph saveGraph = null;
        String savePath = null;
        if(path.equalsIgnoreCase(chinesePath)){
            saveGraph = chineseGraph;
            savePath = chinesePath;
        }else {
            saveGraph = englishGraph;
            savePath = englishPath;
        }
        String str = editor.getSaveStr(saveGraph);
        if(str==null){
            return;
        }
        try {
            editor.saveTree(editor.getNow(), str ,savePath);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if(getSelectedIndex()==0){
            path = chinesePath;
            parser = getChinesePane();
            OR_GRAPH = englishGraph;
            orPath = englishPath;
            orGraphComponent = englishGraphComponent;
            Color prefColor = Preferences.getInstance().getGraphBackgroundColor();
            if( prefColor != null )
                chineseGraphComponent.getViewport().setBackground( prefColor );
            editor.changeUndo(chineseGraphComponent);
            parser.setCountleaf(1);
            chineseGraph.selectAll();
            chineseGraph.removeCells();
            List<String> list1 = parser.getWord(editor.getNow(),getChinesePane().map);
            parser.getLeaf().clear();
            parser.getSplitList().clear();
            parser.vertex.clear();
            getChinesePane().creatTree(editor,chineseGraphComponent,list1,Preferences.DEFAULT_OFFSET_Y);
            int scrollValue = (int)(TreeParser.MARGIN * chineseGraphComponent.getGraph().getView().getScale());
            chineseGraphComponent.getHorizontalScrollBar().setValue(scrollValue);
        }else{
            path = englishPath;
            parser = getEnglishPane();
            OR_GRAPH = chineseGraph;
            orPath = chinesePath;
            orGraphComponent = chineseGraphComponent;
            editor.changeUndo(englishGraphComponent);
            Color prefColor = Preferences.getInstance().getGraphBackgroundColor();
            if( prefColor != null )
                englishGraphComponent.getViewport().setBackground( prefColor );
            parser.setCountleaf(1);
            englishGraph.selectAll();
            englishGraph.removeCells();
            List<String> list1 = parser.getWord(editor.getNow(),getEnglishPane().map);
            parser.getLeaf().clear();
            parser.getSplitList().clear();
            parser.vertex.clear();
            getEnglishPane().creatTree(editor,englishGraphComponent,list1,Preferences.DEFAULT_OFFSET_Y);
            int scrollValue = (int)(TreeParser.MARGIN * englishGraphComponent.getGraph().getView().getScale());
            englishGraphComponent.getHorizontalScrollBar().setValue(scrollValue);
        }
        editor.validCells(editor.getGraphComponent());
        editor.updateBottomTextArea();
        editor.update();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String pATH) {
        path = pATH;
    }

    public mxGraph getZhGraph() {
        return chineseGraph;
    }

    public mxGraph getEngGraph() {
        return englishGraph;
    }

    public String getChinesePath() {
        return chinesePath;
    }

    public String getEnglishPath() {
        return englishPath;
    }

    public void setChinesePath(String path) {
        chinesePath = path;
    }

    public void setEnglishPath(String path) {
        englishPath = path;
    }

    public mxGraph getOrGraph() {
        return orGraphComponent.getGraph();
    }

    public void setOrGraphComponent(mxGraphComponent oRGraphComponent) {
        orGraphComponent = oRGraphComponent;
    }

    public mxGraphComponent getZhGraphComponent() {
        return chineseGraphComponent;
    }

    public mxGraphComponent getEngGraphComponent() {
        return englishGraphComponent;
    }
    
    public mxGraphComponent getOrGraphComponent() {
        return orGraphComponent;
    }

    public boolean isChinese() {
        return (getSelectedIndex() == 0);
    }

    public String getOrPath() {
        return orPath;
    }

    public void setOrPath(String path) {
        orPath = path;
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
                            JOptionPane.showMessageDialog(editor, 
                                Languages.getInstance().getString( "Message.OnlyDragOneFile.Body" ),
                                Languages.getInstance().getString( "Message.Title.Warning" ),
                                JOptionPane.WARNING_MESSAGE);
                        }else{
                            for(File file:list)
                                temp+=file.getAbsolutePath();
                            if(comp.getSelectedIndex()==1){                             
                                int flag = JOptionPane.showConfirmDialog(null, 
                                    Languages.getInstance().getString( "Message.ConfirmDisplay.Body" ),
                                    Languages.getInstance().getString( "Message.Title.Question" ),
                                    JOptionPane.YES_NO_OPTION); 
                                if(flag == 0){
                                    setEnglishPath(temp);
                                    setPath(temp);
                                    getEnglishPane().readData();
                                    editor.refreshTree(0);
                                    editor.updateBottomTextArea();
                                    ((JTabbedPane)englishGraphComponent.getParent().getParent()).setToolTipTextAt(1, temp);
                                }
                            }
                            else if(comp.getSelectedIndex()==0){                                
                                int flag = JOptionPane.showConfirmDialog(null, 
                                    Languages.getInstance().getString( "Message.ConfirmDisplay.Body" ),
                                    Languages.getInstance().getString( "Message.Title.Question" ),
                                    JOptionPane.YES_NO_OPTION); 
                                if(flag == 0){
                                    setChinesePath(temp);
                                    setPath(temp);
                                    getChinesePane().readData();
                                    editor.refreshTree(0);
                                    editor.updateBottomTextArea();
                                    ((JTabbedPane)chineseGraphComponent.getParent().getParent()).setToolTipTextAt(0, temp);
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

    public TreeParser getChinesePane() {
        return chinesePane;
    }

    public TreeParser getEnglishPane() {
        return englishPane;
    }

    public TreeParser getPane(String path) {
        return (path.equalsIgnoreCase(getChinesePath()) ? getChinesePane() : getEnglishPane());
    }

    public TreeParser getCurrentPane() {
        return getPane(path);
    }

    public mxGraphComponent getCurrentGraphComponent() {
        return (isChinese() ? chineseGraphComponent : englishGraphComponent);
    }

    private TreeParser chinesePane;
    private TreeParser englishPane;

    private String chinesePath = "data/train.ch.parse";
    private String englishPath = "data/train.en.parse";
    
    private mxGraph chineseGraph;
    private mxGraphComponent chineseGraphComponent;
    private mxGraph englishGraph;
    private mxGraphComponent englishGraphComponent;

    private String path = "data/train.ch.parse";
    private mxGraph OR_GRAPH = englishGraph;
    private String orPath = "data/train.en.parse";
    private mxGraphComponent orGraphComponent = englishGraphComponent;
    
}
