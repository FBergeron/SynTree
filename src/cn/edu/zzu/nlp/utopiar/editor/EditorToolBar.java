package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
    
    public static JLabel description = N9ComponentFactory.createLabel_style2("");
    
    public static JComboBox getCombobox() {
        return comboBox;
    }

    public static int getFLAG() {
        return FLAG;
    }

    public static void setFLAG(int fLAG) {
        FLAG = fLAG;
    }
    

    public static JLabel getDescription() {
        return description;
    }

    public static JTextField getTextfield() {
        return textField;
    }

    public EditorToolBar(final GraphEditor editor,int orientation){     
        super(orientation);
        textField.setText("请输入数字~");
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(3, 3, 3, 3), getBorder()));
        setFloatable(false);        

        JButton buttonSave = addButton(editor.bind("保存", new ActionSave(), "img/save.gif"));
        addSeparator();

        addButton(editor.bind("撤销", new ActionUndo(true),"img/undo.gif"));
        addButton(editor.bind("重做", new ActionUndo(false),"img/redo.gif"));
        addSeparator();

        addButton(editor.bind("整理", new ActionSort(),"img/pan.gif"));
        addSeparator();

        addButton(editor.bind("上一个", new ActionPre(),"img/pre.gif"));
        addButton(editor.bind("下一个", new ActionNext(),"img/next.gif"));
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
        addButton(editor.bind("GO", new ActionGo()));
        addSeparator();

        int nowCount = EditorTabbedPane.iszH()?TreeParser.ZHCOUNT:TreeParser.ENGCOUNT;
        description.setText("   当前第"+(TreeParser.getNow()+1)+"条,共"+nowCount+"条    ");
        add(description);
        addSeparator();

        JButton zoomInButton = addButton(editor.bind("+", mxGraphActions.getZoomInAction()));
        zoomInButton.setPreferredSize( new Dimension( 40, buttonSave.getPreferredSize().height ) );
        JButton zoomOutButton = addButton(editor.bind("-", mxGraphActions.getZoomOutAction()));
        zoomOutButton.setPreferredSize( new Dimension( 40, buttonSave.getPreferredSize().height ) );
        JButton revertZoomButton = addButton(editor.bind("=", mxGraphActions.getZoomActualAction()));
        revertZoomButton.setPreferredSize( new Dimension( 40, buttonSave.getPreferredSize().height ) );

        add(Box.createHorizontalGlue());

        addButton(editor.bind("重建", new ActionRebuild()));

        comboBox.addItem("仅显示句子");
        comboBox.addItem("显示分词");
        comboBox.addItem("显示分词及词性");
        comboBox.addItem("显示分词及约束");
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
        addButton(editor.bind("刷新", new ActionRefresh(),"img/refresh.jpg"));
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

}
