package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import cn.edu.zzu.nlp.readTree.Constraint;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGraph;
import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.Util;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraph.mxICellVisitor;
import com.mxgraph.view.mxMultiplicity;

public class GraphEditor extends JPanel{    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int VIEW_MODE_SENTENCE_ONLY = 0;
    public static final int VIEW_MODE_WORDS = 1;
    public static final int VIEW_MODE_WORDS_AND_POS = 2;
    public static final int VIEW_MODE_WORDS_AND_CONSTRAINTS = 3;

    /**
     * 
     */
    protected mxUndoManager undoManager;
    
    
    protected mxRubberband rubberband;


    /**
     * 
     */
    protected mxIEventListener undoHandler = new mxIEventListener()
    {
        public void invoke(Object source, mxEventObject evt)
        {
            undoManager.undoableEditHappened((mxUndoableEdit) evt
                    .getProperty("edit"));
        }
    };
    
    protected mxIEventListener connectHandler = new mxIEventListener()
    {
        public void invoke(Object source, mxEventObject evt) {
            mxCell edge = (mxCell)evt.getProperty("cell");
            StringBuilder strStyle = new StringBuilder("");
            java.awt.Color edgeColor = Preferences.getInstance().getEdgeColor();
            if( edgeColor != null ) 
                strStyle.append( "strokeColor=" + Util.colorRGBToHex( edgeColor ) + ";" );
            edge.setStyle( strStyle.toString() );
        }
    };
    
    public GraphEditor() throws IOException{
        setLayout(new BorderLayout());
        installTabbedPane();

        // Stores a reference to the graph and creates the command history
        mxGraphComponent graphComponent = getGraphComponent();//TabbedPane().chineseGraphComponent;
        final mxGraph graph = graphComponent.getGraph();

        graph.setCellsSelectable(true);
        undoManager = createUndoManager();
        // Do not change the scale and translation after files have been loaded
        graph.setResetViewOnRootChange(false);

        changeUndo(graphComponent);      
        
        installToolBar();
        installBottom();
        
        rubberband = new mxRubberband(graphComponent);
    }
   
    public void update() {
        if( toolbar != null )
            toolbar.update();
    }

    public void validCells(mxGraphComponent graphComponent){
        final mxGraph graph = graphComponent.getGraph();
        Object root = null;
        List<Object> rootList = graph.findTreeRoots(graph.getDefaultParent(),false,false);
        if (rootList.size() > 1) {
            JOptionPane.showMessageDialog(this, "请确保有且仅有一个根节点！");
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
     * 
     */
    public void changeUndo(mxGraphComponent graphComponent){
        mxGraph graph = graphComponent.getGraph();
        // Adds the command history to the model and view
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);
        getGraphComponent().getConnectionHandler().addListener(mxEvent.CONNECT, connectHandler); 
    }
    
    /**
     * 
     */
    protected mxUndoManager createUndoManager()
    {
        return new mxUndoManager();
    }

    /**
     * 
     * @param name
     * @param action
     * @return a new Action bound to the specified string name
     */
    public Action bind(String name, final Action action)
    {
        return bind(name, action, null);
    }

    /**
     * 
     * @param name
     * @param action
     * @return a new Action bound to the specified string name and icon
     */
    @SuppressWarnings("serial")
    public Action bind(String name, final Action action, String iconUrl)
    {
        ImageIcon img = ( iconUrl == null ? null : new ImageIcon( Util.getImageResourceFile( iconUrl, Util.class ) ) );
        AbstractAction newAction = new AbstractAction(name, img)
        {
            public void actionPerformed(ActionEvent e)
            {
                action.actionPerformed(new ActionEvent(getGraphComponent(), e
                        .getID(), e.getActionCommand()));
            }
        };
        
        newAction.putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));
        
        return newAction;
    }

    public mxGraphComponent getGraphComponent() {
        return getTabbedPane().getCurrentGraphComponent();
    }   

    public EditorTabbedPane getTabbedPane() {
        return( tabbedPane );
    }

    public mxUndoManager getUndoManager() {
        return undoManager;
    }

    public JComboBox<String> getComboBoxViewMode() {
        return (toolbar == null ? null : toolbar.getComboBoxViewMode());
    }

    public JTextField getTextFieldGraphNumber() {
        return (toolbar == null ? null : toolbar.getTextFieldGraphNumber());
    }

    public void updateBottomTextArea() {
        JEditorPane bottomTextArea = getBottomTextArea();
        if (bottomTextArea != null)
            bottomTextArea.setText(getLabelString());
    }

    public JEditorPane getBottomTextArea() {
        return (bottom == null ? null : bottom.getTextArea());
    }

    protected void installToolBar()
    {
        toolbar = new EditorToolBar(this,JToolBar.HORIZONTAL);
        add(toolbar, BorderLayout.NORTH);
    }
    
    protected void installTabbedPane() throws IOException
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        tabbedPane = new EditorTabbedPane(this);
        panel.add(tabbedPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }
    
    /**
     * 
     */
    protected void installBottom() {
        bottom = new EditorBottom(this);
        add(bottom, BorderLayout.SOUTH);
    }
   
    /**
     * 
     */
    public JFrame createFrame(final EditorMenuBar menuBar)
    {
        final JFrame frame = new JFrame();
        frame.setContentPane(this);     
        frame.setJMenuBar(menuBar);
        frame.setSize(1200, 750);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                boolean isGraphClosed = doCloseGraph();
                if(isGraphClosed) {
                    try {
                        backupTrainData();
                    }
                    catch( Exception ex ) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog( frame, 
                            Languages.getInstance().getString( "Message.BackupError.Body" ),
                            Languages.getInstance().getString( "Message.Title.Error" ),
                            JOptionPane.ERROR_MESSAGE );
                    }
                    System.exit( 0 );
                }
            }
        });
        Util.setFrameCenter(frame);
        return frame;
    }
    
    /*
     * If the graph's data have changed, display a dialog to confirm that that data must be saved or not.
     * Try to save the graph's data if it's needed.
     * @return <code>true</code> if the data has been saved successfully and the graph can be disposed of, <code>false</code> otherwise.
     */
    public boolean doCloseGraph() {
        if (!isModified())
            return(true);

        int flag = JOptionPane.showConfirmDialog(null, 
            Languages.getInstance().getString( "Message.ConfirmSaveData.Body" ),
            Languages.getInstance().getString( "Message.Title.Question" ),
            JOptionPane.YES_NO_CANCEL_OPTION);
        if (flag == JOptionPane.CANCEL_OPTION) 
            return(false);
        else if(flag == JOptionPane.YES_OPTION){
            boolean isDataSaved = doSave();
            if (!isDataSaved)
                return (false);
        }
        return(true);
    }

    /*
     * Save the data.
     * @return <code>true</code> if the data has been saved successfully, <code>false</code> otherwise.
     */
    public boolean doSave() {
        mxGraph graph = getGraphComponent().getGraph();
        String str = null;
        try {
            str = getSaveStr(getTabbedPane().getOrGraph());
            //str = ActionGraph.getSaveStr(this, EditorTabbedPane.getOR_GRAPH());
            if(str==null)
                return(false);
            saveTree(getNow(), str, getTabbedPane().getOrPath());
            saveTree(getTabbedPane().getOrPath());

            str = getSaveStr(graph);
            //str = ActionGraph.getSaveStr(this, graph);
            if(str==null){
                System.out.print("str="+str);
                return(false);
            }           
            saveTree(getNow(), str, getTabbedPane().getPath());
            saveTree(getTabbedPane().getPath());
            return (true);
        } catch (Exception e1) {
            e1.printStackTrace();
            return (false);
        }
    }
   
    public void saveTree(int now, String str, String path) throws Exception{
        TreeParser parser = getTabbedPane().getPane(path);
        parser.map.put(Integer.valueOf(now), str);
    }
    
    public boolean saveTree(String path){   
        TreeParser parser = getTabbedPane().getPane(path);
        HashMap<Integer, String> saveMap = parser.map;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            String init = "";
            fos.write(init.getBytes());
            fos = new FileOutputStream(path,true);
            for (int i = 0; i < saveMap.size(); i++) {
                String temp = saveMap.get(i);
                temp += System.getProperty("line.separator");
                fos.write(temp.getBytes("utf-8"));
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }       
        return true;
    }

    public List<Object> traverse(Object vertex, mxGraph graph) {
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
    
    public String getSaveStr(mxGraph graph){
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
            JOptionPane.showMessageDialog(this, 
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
                JOptionPane.showMessageDialog(this, 
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
                JOptionPane.showMessageDialog(this, 
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
            JOptionPane.showMessageDialog(this, 
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
                JOptionPane.showMessageDialog(this, 
                    Languages.getInstance().getString( "Message.InvalidLeafVertexFound.Body" ),
                    Languages.getInstance().getString( "Message.Title.Error" ),
                    JOptionPane.ERROR_MESSAGE);
                return null ;
            }
        }           
        str = "( " + str.replaceAll("\\)\\(", "\\) \\(") + " )";
        return str;
    }

    public void refreshTree(int count){
        mxGraphComponent graphComponent = getGraphComponent();
        mxGraph graph = graphComponent.getGraph();
        mxGraph orGraph = getTabbedPane().getOrGraph();
        String orPath = getTabbedPane().getOrPath();
        mxGraphComponent orGraphComponent = getTabbedPane().getOrGraphComponent();
        graph.selectAll();
        graph.removeCells();
        orGraph.selectAll();
        orGraph.removeCells();
        setNow(count);
        
        TreeParser parser = getTabbedPane().getPane(orPath);
        parser.setCountleaf(1);
        List<String> list = parser.getWord(count,parser.map);
        parser.getLeaf().clear();
        parser.getSplitList().clear();
        parser.vertex.clear();
        Object root = parser.creatTree(this,orGraphComponent,list, Preferences.DEFAULT_OFFSET_Y);      
        int scrollValue = (int)(TreeParser.MARGIN * graphComponent.getGraph().getView().getScale());
        orGraphComponent.getHorizontalScrollBar().setValue(scrollValue);

        parser = getTabbedPane().getCurrentPane();
        parser.setCountleaf(1);
        List<String> list1 = parser.getWord(getNow(),parser.map);
        parser.getLeaf().clear();
        parser.getSplitList().clear();
        parser.vertex.clear();
        parser.creatTree(this, graphComponent, list1, Preferences.DEFAULT_OFFSET_Y);     
        graphComponent.getHorizontalScrollBar().setValue(scrollValue);

        validCells(graphComponent);
        JComboBox<String> comboBoxViewMode = getComboBoxViewMode();
        if (comboBoxViewMode != null)
            comboBoxViewMode.setSelectedIndex(0);
        clearHighlight();
        update();
        setModified(false);
    }
    
    /**
     * 设置路径窗口
     */
    public void setting()
    {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null) {
            EditorSettingFrame setting = new EditorSettingFrame(frame, this);
            setting.setModal(true);
            setting.setResizable(true);

            // Centers inside the application frame
            int x = frame.getX() + (frame.getWidth() - setting.getWidth()) / 2;
            int y = frame.getY() + (frame.getHeight() - setting.getHeight()) / 2;
            setting.setLocation(x, y);

            // Shows the modal dialog and waits
            setting.setVisible(true);
        }
    }
    
    /**
     * 设置关于窗口
     */
    public void about()
    {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null)
        {
            EditorAboutFrame about = new EditorAboutFrame(frame);
            about.setModal(true);

            // Centers inside the application frame
            int x = frame.getX() + (frame.getWidth() - about.getWidth()) / 2;
            int y = frame.getY() + (frame.getHeight() - about.getHeight()) / 2;
            about.setLocation(x, y);

            // Shows the modal dialog and waits
            about.setVisible(true);
        }
    }

    public boolean isModified() {
        return( isModified );
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public void setHighlight( int startAndEnd ) {
        this.highlightStartPos = this.highlightEndPos = startAndEnd;
    }

    public void setHighlight( int start, int end ) {
        this.highlightStartPos = start;
        this.highlightEndPos = end;
    }

    public void clearHighlight() {
        this.highlightStartPos = this.highlightEndPos = -1;
    }

    public String getHighlightedLabel( String label ) {
        if( highlightStartPos == -1 || highlightStartPos == Integer.MAX_VALUE || highlightEndPos == Integer.MIN_VALUE )
            return( label );
        String[] words = label.split( " " );
        words[ highlightStartPos ] = "<span style=\"background-color: yellow\">" + words[ highlightStartPos ];
        words[ highlightEndPos ] = words[ highlightEndPos ] + "</span>";
        return( String.join( " ", words ) );
    }

    public String getLabelString() {
        String label = "";
        TreeParser parser = getTabbedPane().getCurrentPane();
        if(getViewMode()==VIEW_MODE_WORDS_AND_POS){
            List<String> splitList = parser.getSplitList();
            for (String string : splitList) {
                label += string;
            }
            return label;
        }
        List<String> temp = parser.getLeaf();
        int index = 0;
        for (String string : temp) {
            if(getViewMode()==VIEW_MODE_SENTENCE_ONLY/*&&!EditorTabbedPane.iszH()*/||getViewMode()==VIEW_MODE_WORDS_AND_CONSTRAINTS){
                label += string;
                label += " ";
//          }else if(getViewMode()==VIEW_MODE_SENTENCE_ONLY&&EditorTabbedPane.iszH()){
//              label += string;
            }
            if(getViewMode()==VIEW_MODE_WORDS){
                label += String.valueOf(index)+string;
                label +="  ";
            }
            index++;
        }
        
        //读取已有约束并显示
        if(getViewMode()==VIEW_MODE_SENTENCE_ONLY){
            if(getTabbedPane().getPath().equalsIgnoreCase(getTabbedPane().getChinesePath())){
                if (parser.constraint.get(getNow())!=null
                        &&parser.constraint.get(getNow()).length()!=0){
                    label = parser.constraint.get(getNow());
                    if(!label.contains("@#@#@#")){
                        label += "@#@#@# ";
                    }
                }else if(!label.contains("@#@#@#")){
                    parser.constraint.put(getNow(), label);
                    label += "@#@#@# ";
                }
            }else if(getTabbedPane().getPath().equalsIgnoreCase(getTabbedPane().getEnglishPath())){
                if (parser.constraint.get(getNow())!=null
                        &&parser.constraint.get(getNow()).length()!=0){
                    label = parser.constraint.get(getNow());
                    if(!label.contains("@#@#@#")){
                        label += "@#@#@# ";
                    }
                }else if(!label.contains("@#@#@#")){
                    parser.constraint.put(getNow(), label);
                    label += "@#@#@# ";
                }
            }
            else if(!label.contains("@#@#@#")){
                label += "@#@#@# ";
            }
            label = getHighlightedLabel(label);
        }
        
        if(getViewMode()==VIEW_MODE_WORDS_AND_CONSTRAINTS){
            Constraint c = new Constraint();
            List<String> list = parser.getWord(getNow(), parser.map);
            c.creatTree(list);
            label += "  @#@#@#  " + c.constraint;
        }
        return "<html>" + label + "</html>";
    }

    /**
     * 窗口居中显示
     * @param jFrame
     */
    /**
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
        UIManager.installLookAndFeel( new UIManager.LookAndFeelInfo( "BeautyEye", "org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelCross" ) );

        try {
            Preferences.getInstance().load();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
       
        try {
            String lnf = Preferences.getInstance().getLookAndFeel();
            if( lnf == null )
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            else if( "org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelCross".equals( lnf ) ) {
                org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
                UIManager.put("RootPane.setupButtonVisible", false);
                Util.initGlobalFontSetting(new Font("微软雅黑",5,14));
                // mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
                // mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";
            }
            else {
                UIManager.setLookAndFeel(lnf);
            }
        }
        catch( Exception e ) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch( Exception e2 ) {
                e2.printStackTrace();
            }
        }

        GraphEditor editor = new GraphEditor();
        editor.createFrame(new EditorMenuBar(editor)).setVisible(true);
    }

    private void backupTrainData() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
        Calendar now = Calendar.getInstance();
        String timestamp = formatter.format( now.getTime() );
        
        ZipOutputStream output = new ZipOutputStream(
            new BufferedOutputStream( new FileOutputStream( "data/backup_" + timestamp + ".zip" ) ) );
        byte data[] = new byte[ BUFFER_SIZE ];
        String[] files = { "data/train.ch.parse", "data/train.en.parse" };
        for( String file : files ) {
            BufferedInputStream input = new BufferedInputStream( new FileInputStream( file ) );
            ZipEntry entry = new ZipEntry( file );
            output.putNextEntry( entry );
            int bytesRead;
            while ( ( bytesRead = input.read( data, 0, BUFFER_SIZE ) ) != -1 )
                output.write( data, 0, bytesRead );
            input.close();
        }
        output.close();
    }

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    /**
     * @description 用于标记下拉多选框现在所处的状态
     */
    private int viewMode = VIEW_MODE_SENTENCE_ONLY;
    
    private EditorTabbedPane tabbedPane;

    private int highlightStartPos = -1;
    private int highlightEndPos = -1;

    private EditorToolBar toolbar;
    private EditorBottom bottom;

    private boolean isModified = false;

    private static final int BUFFER_SIZE = 8192;

    /**
     * 记录当前句法树位置
     */
    public int now = 0;

}
