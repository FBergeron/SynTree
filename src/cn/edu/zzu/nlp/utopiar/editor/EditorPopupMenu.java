package cn.edu.zzu.nlp.utopiar.editor;

import java.util.Locale;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;

import cn.edu.zzu.nlp.utopiar.action.ActionAddEdge;
import cn.edu.zzu.nlp.utopiar.action.ActionAddVertex;
import cn.edu.zzu.nlp.utopiar.action.ActionDelete;
import cn.edu.zzu.nlp.utopiar.action.ActionUndo;
import cn.edu.zzu.nlp.utopiar.util.Languages;

public class EditorPopupMenu extends JPopupMenu {

    public EditorPopupMenu(GraphEditor graphEditor, mxGraphComponent graphComponent) {
        boolean selected = !graphComponent.getGraph().isSelectionEmpty();

        actionUndo = graphEditor.bind(null, new ActionUndo(true),"img/undo.gif");
        add( actionUndo );
        actionRedo = graphEditor.bind(null, new ActionUndo(false),"img/redo.gif");
        add( actionRedo );
        addSeparator();

        actionCut = graphEditor.bind(null, TransferHandler.getCutAction(), "img/cut.gif");
        add( actionCut ); 
        actionCopy = graphEditor.bind(null, TransferHandler.getCopyAction(), "img/copy.gif");
        add( actionCopy ); 
        actionPaste = graphEditor.bind(null, TransferHandler.getPasteAction(), "img/paste.gif");
        add( actionPaste ); 
        addSeparator();

        actionDelete = graphEditor.bind("Delete", new ActionDelete(), "img/delete.gif");
        add( actionDelete );
        addSeparator();

        actionAddVertex = graphEditor.bind(null, new ActionAddVertex(), "img/vertex.gif");
        add( actionAddVertex );
        actionAddEdge = graphEditor.bind(null, new ActionAddEdge(), "img/edge.gif");
        add( actionAddEdge );
        addSeparator();
        
        actionSelectAll = graphEditor.bind(null, mxGraphActions.getSelectAllAction());
        add( actionSelectAll );
        actionSelectNone = graphEditor.bind(null, mxGraphActions.getSelectNoneAction());
        add( actionSelectNone );
        
        setLocale( Languages.getInstance().getCurrent() );
    }

    public void setLocale( Locale locale ) {
        super.setLocale( locale );
        
        actionUndo.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.Undo" ) );
        actionRedo.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.Redo" ) );
        actionCut.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.Cut" ) );
        actionCopy.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.Copy" ) );
        actionPaste.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.Paste" ) );
        actionDelete.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.Delete" ) );
        actionAddVertex.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.AddVertex" ) );
        actionAddEdge.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.AddEdge" ) );
        actionSelectAll.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.SelectAll" ) );
        actionSelectNone.putValue( Action.NAME, Languages.getInstance().getString( "Menu.Edition.SelectNone" ) );
    }

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

}

