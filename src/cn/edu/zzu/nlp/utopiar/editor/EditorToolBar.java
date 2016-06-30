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
import cn.edu.zzu.nlp.utopiar.util.Util;

public class EditorToolBar extends JToolBar {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public final JComboBox<String> comboBoxViewMode = new JComboBox<String>();   
   
    public final JTextField textFieldGraphNumber = new JTextField(10);
    
    public JComboBox<String> getComboBoxViewMode() {
        return comboBoxViewMode;
    }

    public JTextField getTextFieldGraphNumber() {
        return textFieldGraphNumber;
    }

    public EditorToolBar(final GraphEditor editor,int orientation){     
        super(orientation);
        this.editor = editor;
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
        textFieldGraphNumber.setPreferredSize( new Dimension( 200, buttonSave.getPreferredSize().height ) );
        textFieldGraphNumber.setMaximumSize(  new Dimension( 200, buttonSave.getPreferredSize().height ) );
        add(textFieldGraphNumber);
        textFieldGraphNumber.addFocusListener(new FocusListener() {
            
            @Override
            public void focusLost(FocusEvent e) {
                String temp = textFieldGraphNumber.getText();
                if(!Util.isNum(temp)){
                    JOptionPane.showMessageDialog(editor, 
                        Languages.getInstance().getString( "Message.EnterValidSentenceNumber.Body" ),
                        Languages.getInstance().getString( "Message.Title.Warning" ),
                        JOptionPane.WARNING_MESSAGE);
                    textFieldGraphNumber.setText("");
                }
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                textFieldGraphNumber.setText("");
            }
        });
        actionGo = editor.bind(null, new ActionGo(), "/img/go.png");
        addButton(actionGo);
        addSeparator();

        int nowCount = editor.getTabbedPane().getCurrentPane().count;
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

        comboBoxViewMode.addItem(""); // ShowOnlySentence
        comboBoxViewMode.addItem(""); // DisplayWords
        comboBoxViewMode.addItem(""); // DisplayWordsAndPOS
        comboBoxViewMode.addItem(""); // DisplayWordsAndConstraints
        comboBoxViewMode.setSelectedIndex(0);
        comboBoxViewMode.addItemListener(new ItemListener() {
            
            @Override
            public void itemStateChanged(ItemEvent e) {
                editor.setViewMode(comboBoxViewMode.getSelectedIndex());
                editor.updateBottomTextArea();
            }
        });
        comboBoxViewMode.setPreferredSize( new Dimension( 200, buttonSave.getPreferredSize().height ) );
        comboBoxViewMode.setMaximumSize( new Dimension( 200, buttonSave.getPreferredSize().height ) );
        add(comboBoxViewMode);
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
        
        textFieldGraphNumber.setText( Languages.getInstance().getString( "Toolbar.EnterSentenceNumber" ) );
        actionSave.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Save" ) );
        actionUndo.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Undo" ) );
        actionRedo.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Redo" ) );
        actionSort.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Sort" ) );
        actionPrev.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.PrevGraph" ) );
        actionNext.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.NextGraph" ) );
        actionGo.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Go" ) );
        actionRebuild.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Rebuild" ) );
        actionRefresh.putValue( Action.NAME, Languages.getInstance().getString( "Toolbar.Refresh" ) );
       
        int selectedIndex = comboBoxViewMode.getSelectedIndex();
        comboBoxViewMode.removeAllItems();
        comboBoxViewMode.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.ShowOnlySentence" ) );
        comboBoxViewMode.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.DisplayWords" ) );
        comboBoxViewMode.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.DisplayWordsAndPOS" ) );
        comboBoxViewMode.addItem( Languages.getInstance().getString( "Toolbar.ShowOption.DisplayWordsAndConstraints" ) );
        comboBoxViewMode.setSelectedIndex( selectedIndex == -1 ? 0 : selectedIndex );

        update();
    }

    public void update() {
        int nowCount = editor.getTabbedPane().getCurrentPane().count;
        String strFormat = Languages.getInstance().getString( "Toolbar.Desc" );
        if( strFormat != null ) {
            String strDesc = MessageFormat.format( strFormat, editor.getNow() + 1, nowCount );
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

    private GraphEditor editor;

}
