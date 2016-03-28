package cn.edu.zzu.nlp.utopiar.editor;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;

import cn.edu.zzu.nlp.utopiar.action.ActionDelete;
import cn.edu.zzu.nlp.utopiar.action.ActionUndo;

public class EditorPopupMenu extends JPopupMenu {

    public EditorPopupMenu(GraphEditor graphEditor, mxGraphComponent graphComponent) {
        boolean selected = !graphComponent.getGraph().isSelectionEmpty();

        add(graphEditor.bind("撤销", new ActionUndo(true),"img/undo.gif"));
        add(graphEditor.bind("重做", new ActionUndo(false),"img/redo.gif"));
        addSeparator();

        add(graphEditor.bind("Cut", TransferHandler.getCutAction(), "img/cut.gif")); 
        add(graphEditor.bind("Copy", TransferHandler.getCopyAction(), "img/copy.gif")); 
        add(graphEditor.bind("Paste", TransferHandler.getPasteAction(), "img/paste.gif")); 
        addSeparator();

        add(graphEditor.bind("Delete", new ActionDelete(), "img/delete.gif"));
        addSeparator();

        add(graphEditor.bind("Select All", mxGraphActions.getSelectAllAction()));
        add(graphEditor.bind("Select None", mxGraphActions.getSelectNoneAction()));
    }

}

