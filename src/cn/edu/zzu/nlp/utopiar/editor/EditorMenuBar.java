package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.TransferHandler;

import com.mxgraph.swing.util.mxGraphActions;

import cn.edu.zzu.nlp.utopiar.action.ActionAddEdge;
import cn.edu.zzu.nlp.utopiar.action.ActionAddVertex;
import cn.edu.zzu.nlp.utopiar.action.ActionDelete;
import cn.edu.zzu.nlp.utopiar.action.ActionSetting;
import cn.edu.zzu.nlp.utopiar.action.ActionSave;
import cn.edu.zzu.nlp.utopiar.action.ActionSetLocale;
import cn.edu.zzu.nlp.utopiar.action.ActionUndo;
import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.Preferences;

public class EditorMenuBar extends JMenuBar{

    private static final long serialVersionUID = 1L;
    
    private GraphEditor editor;

    public EditorMenuBar(final GraphEditor editor) {
        JMenu menu = null;
        this.editor = editor;
        
        menuFile = new JMenu();
        menu = add(menuFile);
        actionSettings = editor.bind("", new ActionSetting(), "img/gear.png");
        menu.add( actionSettings );
        actionSave = editor.bind("", new ActionSave(), "img/save.gif");
        menu.add( actionSave );

        menuEdition = new JMenu();
        menu = add( menuEdition );
        actionUndo = editor.bind("", new ActionUndo(true),"img/undo.gif");
        menu.add( actionUndo );
        actionRedo = editor.bind("", new ActionUndo(false),"img/redo.gif");
        menu.add( actionRedo );
        menu.addSeparator();

        actionCut = editor.bind("", TransferHandler.getCutAction(), "img/cut.gif");
        menu.add( actionCut ); 
        actionCopy = editor.bind("", TransferHandler.getCopyAction(), "img/copy.gif");
        menu.add( actionCopy ); 
        actionPaste = editor.bind("", TransferHandler.getPasteAction(), "img/paste.gif");
        menu.add( actionPaste ); 
        menu.addSeparator();
        
        actionDelete = editor.bind("", new ActionDelete(), "img/delete.gif");
        menu.add( actionDelete );
        menu.addSeparator();

        actionAddVertex = editor.bind("", new ActionAddVertex(), "img/vertex.gif");
        menu.add( actionAddVertex );
        actionAddEdge = editor.bind("", new ActionAddEdge(), "img/edge.gif");
        menu.add( actionAddEdge );
        menu.addSeparator();
        
        actionSelectAll = editor.bind("", mxGraphActions.getSelectAllAction());
        menu.add( actionSelectAll );
        actionSelectNone = editor.bind("Select None", mxGraphActions.getSelectNoneAction());
        menu.add( actionSelectNone );
        
        menu = add(new JMenu("?"));

        ActionSetLocale selectedActionSetLocale = null;

        String prefLang = Preferences.getInstance().getLanguage();
        ResourceBundle bundle = ResourceBundle.getBundle( "Strings" );
        ButtonGroup buttonGroupLoc = new ButtonGroup();
        for( Locale locale : Languages.supported ) {
            String label = bundle.getString("Language." + locale.getLanguage());
            String img = "img/flag_" + locale.getLanguage() + ".png";
            ActionSetLocale actionSetLocale = new ActionSetLocale( locale.getLanguage(), label, new ImageIcon(img) );
            JRadioButtonMenuItem itemLoc = new JRadioButtonMenuItem( actionSetLocale );
            if( prefLang.equals( locale.getLanguage() ) ) {
                itemLoc.setSelected( true );
                selectedActionSetLocale = actionSetLocale;
            }
            buttonGroupLoc.add(itemLoc);
            menu.add(itemLoc);
        }
        menu.addSeparator();

        menuItemAbout = new JMenuItem();
        menuItemAbout.setIcon(new ImageIcon("img/about.gif"));
        menuItemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editor.about();
            }
        });
        menu.add(menuItemAbout);

        Languages.getInstance().addItemListener(
            new ItemListener() {
                public void itemStateChanged( ItemEvent evt ) {
                    Locale locale = (Locale)evt.getItem();
                    setLocale( locale );
                }
            }
        );
        
        selectedActionSetLocale.actionPerformed(null);
    }

    public void setLocale( Locale locale ) {
        super.setLocale( locale );
        
        ResourceBundle bundle = ResourceBundle.getBundle( "Strings", locale );

        menuFile.setText( bundle.getString( "Menu.File" ) );
        actionSettings.putValue( Action.NAME, bundle.getString( "Menu.File.Settings" ) );
        actionSave.putValue( Action.NAME, bundle.getString( "Menu.File.Save" ) );

        menuEdition.setText( bundle.getString( "Menu.Edition" ) );
        actionUndo.putValue( Action.NAME, bundle.getString( "Menu.Edition.Undo" ) );
        actionRedo.putValue( Action.NAME, bundle.getString( "Menu.Edition.Redo" ) );
        actionCut.putValue( Action.NAME, bundle.getString( "Menu.Edition.Cut" ) );
        actionCopy.putValue( Action.NAME, bundle.getString( "Menu.Edition.Copy" ) );
        actionPaste.putValue( Action.NAME, bundle.getString( "Menu.Edition.Paste" ) );
        actionDelete.putValue( Action.NAME, bundle.getString( "Menu.Edition.Delete" ) );
        actionAddVertex.putValue( Action.NAME, bundle.getString( "Menu.Edition.AddVertex" ) );
        actionAddEdge.putValue( Action.NAME, bundle.getString( "Menu.Edition.AddEdge" ) );
        actionSelectAll.putValue( Action.NAME, bundle.getString( "Menu.Edition.SelectAll" ) );
        actionSelectNone.putValue( Action.NAME, bundle.getString( "Menu.Edition.SelectNone" ) );

        menuItemAbout.setText( bundle.getString( "Menu.Help.About" ) );
    }

    public GraphEditor getEditor() {
        return editor;
    }

    private JMenu menuFile;
    private Action actionSettings;
    private Action actionSave;

    private JMenu menuEdition;
    private Action actionUndo;
    private Action actionRedo;
    private Action actionCut;
    private Action actionCopy;
    private Action actionPaste;
    private Action actionDelete;
    private Action actionAddVertex;
    private Action actionAddEdge;
    private Action actionSelectAll;
    private Action actionSelectNone;

    private JMenuItem menuItemAbout;

}
