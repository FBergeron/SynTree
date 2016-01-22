package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.mxgraph.swing.util.mxGraphActions;

import cn.edu.zzu.nlp.utopiar.action.ActionAddEdge;
import cn.edu.zzu.nlp.utopiar.action.ActionAddVertex;
import cn.edu.zzu.nlp.utopiar.action.ActionSetting;
import cn.edu.zzu.nlp.utopiar.action.ActionSave;

public class EditorMenuBar extends JMenuBar{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private GraphEditor editor;

    public EditorMenuBar(final GraphEditor editor){
        JMenu menu = null;
        this.editor = editor;
        //创建文件菜单
        menu = add(new JMenu("文件"));
        menu.add(editor.bind("设置", new ActionSetting(), "img/gear.png"));
        menu.add(editor.bind("保存", new ActionSave(), "img/save.gif"));

        menu = add(new JMenu("Edition"));
        menu.add(editor.bind("Select All", mxGraphActions.getSelectAllAction()));
        menu.add(editor.bind("Select None", mxGraphActions.getSelectNoneAction()));
        
        //创建编辑菜单
        menu = add(new JMenu("编辑"));
        menu.add(editor.bind("添加结点", new ActionAddVertex(), "img/vertex.gif"));
        menu.add(editor.bind("添加连接", new ActionAddEdge(), "img/edge.gif"));
        
        // 创建帮助菜单
        menu = add(new JMenu("帮助"));

        JMenuItem item = menu.add(new JMenuItem("关于"));
        item.setIcon(new ImageIcon("img/about.gif"));
        item.addActionListener(new ActionListener()
        {
            
            public void actionPerformed(ActionEvent e)
            {
                editor.about();
            }
        });
    }

    public GraphEditor getEditor() {
        return editor;
    }
    
    
}
