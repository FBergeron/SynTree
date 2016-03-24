package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

import cn.edu.zzu.nlp.readTree.SaveTree;
import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGraph;
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

public class GraphEditor extends JPanel{    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    public static mxGraphComponent graphComponent;
    
    /**
     * 
     */
    protected static mxUndoManager undoManager;
    
    
    protected mxRubberband rubberband;


    /**
     * 
     */
    protected static mxIEventListener undoHandler = new mxIEventListener()
    {
        public void invoke(Object source, mxEventObject evt)
        {
            undoManager.undoableEditHappened((mxUndoableEdit) evt
                    .getProperty("edit"));
        }
    };
    
    protected static mxIEventListener connectHandler = new mxIEventListener()
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
    
    public GraphEditor() throws IOException
    {
        this( new mxGraphComponent(new mxGraph()));
    }
    
    public GraphEditor( mxGraphComponent component) throws IOException{
        setLayout(new BorderLayout());
        // Stores a reference to the graph and creates the command history
        graphComponent = component;
        final mxGraph graph = graphComponent.getGraph();

        graph.setCellsSelectable(true);
        undoManager = createUndoManager();
        // Do not change the scale and translation after files have been loaded
        graph.setResetViewOnRootChange(false);

        changeUndo(component);      
        
        installToolBar();
        installTabbedPane();
        installBottom();
        
        rubberband = new mxRubberband(graphComponent);
    }
    

    /**
     * 
     */
    public static void changeUndo(mxGraphComponent graphComponent){
        mxGraph graph = graphComponent.getGraph();
        // Adds the command history to the model and view
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);
        getGraphComponent().getConnectionHandler().addListener(mxEvent.CONNECT, connectHandler); 
        //graph.getView().addListener(mxEvent.CONNECT, connectHandler); 
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
        AbstractAction newAction = new AbstractAction(name, (iconUrl != null) ? new ImageIcon(
                iconUrl) : null)
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

    public static mxGraphComponent getGraphComponent() {
        return graphComponent;
    }   

    public static void setGraphComponent(mxGraphComponent graphComponent) {
        GraphEditor.graphComponent = graphComponent;
    }

    public EditorTabbedPane getTabbedPane() {
        return( tabbedPane );
    }

    public mxUndoManager getUndoManager() {
        return undoManager;
    }

    protected void installToolBar()
    {
        add(new EditorToolBar(this,JToolBar.HORIZONTAL), BorderLayout.NORTH);
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
        add(new EditorBottom(this), BorderLayout.SOUTH);
    }
   
    /**
     * 
     */
    public JFrame createFrame(final EditorMenuBar menuBar)
    {
        final JFrame frame = new JFrame();
        frame.setContentPane(this);     
        frame.setJMenuBar(menuBar);
        setFrameCenter(frame);
        frame.setSize(1200, 750);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                int flag = JOptionPane.showConfirmDialog(null, "是否保存？","确认", JOptionPane.YES_NO_CANCEL_OPTION);   
                GraphEditor editor = menuBar.getEditor();
                mxGraphComponent graphComponent = GraphEditor.getGraphComponent();
                mxGraph graph = graphComponent.getGraph();
                if(flag == 0){
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    String str = null;
                    try {
                        str = ActionGraph.getSaveStr(editor, EditorTabbedPane.getOR_GRAPH());
                        if(str==null){
                            return;
                        }
                        SaveTree.save(TreeParser.getNow(), str ,EditorTabbedPane.getOR_PATH());
                        SaveTree.save(EditorTabbedPane.getOR_PATH());
                        str = ActionGraph.getSaveStr(editor, graph);
                        if(str==null){
                            System.out.print("str="+str);
                            return;
                        }           
                        SaveTree.save(TreeParser.getNow(), str, EditorTabbedPane.getPATH());
                        SaveTree.save(EditorTabbedPane.getPATH());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }else if(flag == 1){
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }else if(flag == 2){
                    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);  //阻止关闭窗口
                    return;
                }
            }
        });
        setFrameCenter(frame);
        return frame;
    }
    
    /**
     * 设置路径窗口
     */
    public void setting()
    {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null)
        {
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
    
    /**
     * 设置全局字体
     * @param fnt
     */
    public static void initGlobalFontSetting(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration<?> keys = UIManager.getDefaults().keys(); keys
                .hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }
    
    /**
     * 窗口居中显示
     * @param jFrame
     */
    public static void setFrameCenter(JFrame jFrame){
        //居中显示
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        int x = screen.width - jFrame.getWidth()>>1;
        int y = (screen.height - jFrame.getHeight()>>1)-32;
        jFrame.setLocation(x, y);
    }
    
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
                initGlobalFontSetting(new Font("微软雅黑",5,14));
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

        setGraphComponent(EditorTabbedPane.ZH_GRAPH_COMPONENT);
        GraphEditor editor = new GraphEditor(graphComponent);
        int nowCount = EditorTabbedPane.iszH()?TreeParser.ZHCOUNT:TreeParser.ENGCOUNT;
        EditorToolBar.getDescription().setText("   当前第"+(TreeParser.getNow()+1)+"条,共"+nowCount+"条    ");
        editor.createFrame(new EditorMenuBar(editor)).setVisible(true);
    }

    private EditorTabbedPane tabbedPane;

}
