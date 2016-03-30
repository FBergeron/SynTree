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
        
        menu = add(new JMenu("文件"));
        actionSettings = editor.bind("", new ActionSetting(), "img/gear.png");
        menu.add( actionSettings );
        menu.add(editor.bind("保存", new ActionSave(), "img/save.gif"));

        menu = add(new JMenu("Edition"));
        menu.add(editor.bind("撤销", new ActionUndo(true),"img/undo.gif"));
        menu.add(editor.bind("重做", new ActionUndo(false),"img/redo.gif"));
        menu.addSeparator();

        menu.add(editor.bind("Cut", TransferHandler.getCutAction(), "img/cut.gif")); 
        menu.add(editor.bind("Copy", TransferHandler.getCopyAction(), "img/copy.gif")); 
        menu.add(editor.bind("Paste", TransferHandler.getPasteAction(), "img/paste.gif")); 
        menu.addSeparator();
        
        menu.add(editor.bind("Delete", new ActionDelete(), "img/delete.gif"));
        menu.addSeparator();

        menu.add(editor.bind("Select All", mxGraphActions.getSelectAllAction()));
        menu.add(editor.bind("Select None", mxGraphActions.getSelectNoneAction()));
        
        menu = add(new JMenu("编辑"));
        menu.add(editor.bind("添加结点", new ActionAddVertex(), "img/vertex.gif"));
        menu.add(editor.bind("添加连接", new ActionAddEdge(), "img/edge.gif"));
        
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
        if( actionSettings != null )
            actionSettings.putValue( Action.NAME, bundle.getString( "Menu.File.Settings" ) );
        if( menuItemAbout != null )
            menuItemAbout.setText( bundle.getString( "Menu.Help.About" ) );
    }

    public GraphEditor getEditor() {
        return editor;
    }

    private Action actionSettings;
    private JMenuItem menuItemAbout;

}
