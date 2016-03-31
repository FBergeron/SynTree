package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.jb2011.lnf.beautyeye.widget.N9ComponentFactory;

import com.mxgraph.swing.util.mxGraphActions;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGo;
import cn.edu.zzu.nlp.utopiar.action.ActionNext;
import cn.edu.zzu.nlp.utopiar.action.ActionPre;
import cn.edu.zzu.nlp.utopiar.action.ActionRebuild;
import cn.edu.zzu.nlp.utopiar.action.ActionRefresh;
import cn.edu.zzu.nlp.utopiar.action.ActionSave;
import cn.edu.zzu.nlp.utopiar.action.ActionSort;
import cn.edu.zzu.nlp.utopiar.action.ActionUndo;
import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.ValidCell;

public class EditorToolBar extends JToolBar {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @description 用于标记下拉多选框现在所处的状态
     */
    public static int FLAG = 0;
    
    public static final JComboBox<String> comboBox = new JComboBox<String>();   
    
    public static final JTextField textField = new JTextField(10);
    
    public static JComboBox getCombobox() {
        return comboBox;
    }

    public static int getFLAG() {
        return FLAG;
    }

    public static void setFLAG(int fLAG) {
        FLAG = fLAG;
    }

    public static JTextField getTextfield() {
        return textField;
    }

    public EditorToolBar(final GraphEditor editor,int orientation){     
        super(orientation);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(3, 3, 3, 3), getBorder()));
        setFloatable(false);        

        actionSave = editor.bind(null, new ActionSave(), "/img/save.gif");
        JButton buttonSave = addButton( actionSave );
        addSeparator();

        actionUndo = editor.bind(null, new ActionUndo(true),"/img/undo.gif");
        addButton( actionUndo );
        actionRedo = editor.bind(null, new ActionUndo(false),"/img/redo.gif");
        addButton( actionRedo );
        addSeparator();

        actionSort = editor.bind(null, new ActionSort(),"/img/pan.gif");
        addButton( actionSort );
        addSeparator();

        actionPrev = editor.bind(null, new ActionPre(),"/img/pre.gif");
        addButton( actionPrev );
        actionNext = editor.bind(null, new ActionNext(),"/img/next.gif");
        addButton( actionNext );
        textField.setPreferredSize( new Dimension( 200, buttonSave.getPreferredSize().height ) );
        textField.setMaximumSize(  new Dimension( 200, buttonSave.getPreferredSize().height ) );
        add(textField);
        textField.addFocusListener(new FocusListener() {
            
            @Override
            public void focusLost(FocusEvent e) {
                String temp = textField.getText();
                if(!ValidCell.isNum(temp)){
                    JOptionPane.showMessageDialog(editor, "请输入数字！","警告",JOptionPane.WARNING_MESSAGE);
                    textField.setText("");
                }
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                textField.setText("");
            }
        });
        actionGo = editor.bind(null, new ActionGo(), "/img/go.png");
        addButton(actionGo);
        addSeparator();

        int nowCount = EditorTabbedPane.iszH()?TreeParser.ZHCOUNT:TreeParser.ENGCOUNT;
        add(description);
        addSeparator();

        JButton zoomInButton = addButton(editor.bind("+", mxGraphActions.getZoomInAction()));
        zoomInButton.setPreferredSize( new Dimension( 40, buttonSave.getPreferredSize().height ) );
        JButton zoomOutButton = addButton(editor.bind("-", mxGraphActions.getZoomOutAction()));
        zoomOutButton.setPreferredSize( new Dimension( 40, buttonSave.getPreferredSize().height ) );
        JButton revertZoomButton = addButton(editor.bind("=", mxGraphActions.getZoomActualAction()));
        revertZoomButton.setPreferredSize( new Dimension( 40, buttonSave.getPreferredSize().height ) );

        add(Box.createHorizontalGlue());

        actionRebuild = editor.bind(null, new ActionRebuild());
        addButton( actionRebuild );

        comboBox.addItem(""); // ShowOnlySentence
        comboBox.addItem(""); // DisplayWords
        comboBox.addItem(""); // DisplayWordsAndPOS
        comboBox.addItem(""); // DisplayWordsAndConstraints
        comboBox.setSelectedIndex(0);
        comboBox.addItemListener(new ItemListener() {
            
            @Override
            public void itemStateChanged(ItemEvent e) {
                setFLAG(comboBox.getSelectedIndex());
                EditorBottom.getTextArea().setText(editor.getLabelString());
            }
        });
        comboBox.setPreferredSize( new Dimension( 200, buttonSave.getPreferredSize().height ) );
        comboBox.setMaximumSize( new Dimension( 200, buttonSave.getPreferredSize().height ) );
        add(comboBox);
        addSeparator();
        actionRefresh = editor.bind(null, new ActionRefresh(),"/img/refresh.jpg");
        addButton( actionRefresh );
        
        Languages.getInstance().addItemListener(
            new ItemListener() {
                public void itemStateChanged( ItemEvent evt ) {
                    Locale locale = (Locale)evt.getItem();
                    setLocale( locale );
                }
            }
        );
    }   

    public void setLocale( Locale locale ) {
        super.setLocale( locale );
        
        textField.setText( Languages.getInstance().getString( "Toolbar.EnterSentenceNumber" ) );
        actionSave.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Save" ) );
        actionUndo.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Undo" ) );
        actionRedo.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Redo" ) );
        actionSort.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Sort" ) );
        actionPrev.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.PrevGraph" ) );
        actionNext.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.NextGraph" ) );
        actionGo.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Go" ) );
        actionRebuild.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Rebuild" ) );
        actionRefresh.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Refresh" ) );
       
        int selectedIndex = comboBox.getSelectedIndex();
        comboBox.removeAllItems();
        comboBox.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.ShowOnlySentence" ) );
        comboBox.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.DisplayWords" ) );
        comboBox.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.DisplayWordsAndPOS" ) );
        comboBox.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.DisplayWordsAndConstraints" ) );
        comboBox.setSelectedIndex( selectedIndex == -1 ? 0 : selectedIndex );

        update();
    }

    public void update() {
        int nowCount = EditorTabbedPane.iszH()?TreeParser.ZHCOUNT:TreeParser.ENGCOUNT;
        String strFormat = Languages.getInstance().getString( "Toolbar.Desc" );
        if( strFormat != null ) {
            String strDesc = MessageFormat.format( strFormat, TreeParser.getNow() + 1, nowCount );
            description.setText( "   " + strDesc + "   ");
        }
    }

    private JToggleButton addToggleButton(Action a) {
        JToggleButton tb = new JToggleButton((String)a.getValue(Action.NAME), null);
        tb.setEnabled(a.isEnabled());
        tb.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
        tb.setAction(a);
        add(tb);
        return tb;
    }

    private JButton addButton(Action a) {
        JButton button = new JButton((String)a.getValue(Action.NAME), null);
        button.setEnabled(a.isEnabled());
        button.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
        button.setAction(a);
        add(button);
        return button;
    }

    private Action actionSave;
    private Action actionUndo;
    private Action actionRedo;
    private Action actionSort;
    private Action actionPrev;
    private Action actionNext;
    private Action actionGo;
    private Action actionRebuild;
    private Action actionRefresh;

    private JLabel description = N9ComponentFactory.createLabel_style2("");
    
}
