package cn.edu.zzu.nlp.utopiar.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import cn.edu.zzu.nlp.utopiar.editor.GraphEditor;

public abstract class ActionGraph extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param e
     * @return Returns the graph for the given action event.
     */
    public final GraphEditor getEditor(ActionEvent e)
    {
        if (e.getSource() instanceof Component)
        {
            Component component = (Component) e.getSource();

            while (component != null
                    && !(component instanceof GraphEditor))
            {
                component = component.getParent();
            }

            return (GraphEditor) component;
        }

        return null;
    }
    
}
