package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.widget.N9ComponentFactory;

import com.mxgraph.swing.util.mxGraphActions;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionGo;
import cn.edu.zzu.nlp.utopiar.action.ActionNext;
import cn.edu.zzu.nlp.utopiar.action.ActionPre;
import cn.edu.zzu.nlp.utopiar.action.ActionRebuild;
import cn.edu.zzu.nlp.utopiar.action.ActionRefresh;
import cn.edu.zzu.nlp.utopiar.action.ActionSort;
import cn.edu.zzu.nlp.utopiar.action.ActionUndo;
import cn.edu.zzu.nlp.utopiar.util.SetLabel;
import cn.edu.zzu.nlp.utopiar.util.ValidCell;

public class EditorToolBar extends ToggleButtonToolBar{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @description 用于标记下拉多选框现在所处的状态
     */
    public static int FLAG = 0;
    
    public static final JComboBox comboBox = new JComboBox();   
    
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
        textField.setText("请输入数字~");
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(3, 3, 3, 3), getBorder()));
        setFloatable(false);        
        comboBox.addItem("仅显示句子");
        comboBox.addItem("显示分词");
        comboBox.addItem("显示分词及词性");
        comboBox.addItem("显示分词及约束");
        comboBox.setSelectedIndex(0);
        comboBox.addItemListener(new ItemListener() {
            
            @Override
            public void itemStateChanged(ItemEvent e) {
                setFLAG(comboBox.getSelectedIndex());
                EditorBottom.getTextArea().setText(SetLabel.setLabel());
            }
        });
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        addToggleButton(editor.bind("撤销", new ActionUndo(true),"img/undo.gif"));
        addToggleButton(editor.bind("重做", new ActionUndo(false),"img/redo.gif"));
        addToggleButton(editor.bind("上一个", new ActionPre(),"img/pre.gif"));
        addToggleButton(editor.bind("下一个", new ActionNext(),"img/next.gif"));
        addToggleButton(editor.bind("整理", new ActionSort(),"img/pan.gif"));
        addToggleButton(editor.bind("刷新", new ActionRefresh(),"img/refresh.jpg"));
        addToggleButton(editor.bind("重建", new ActionRebuild()));
        add(comboBox);
        addSeparator();
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
        addToggleButton(editor.bind("GO", new ActionGo()));
        int nowCount = EditorTabbedPane.iszH()?TreeParser.ZHCOUNT:TreeParser.ENGCOUNT;
        description.setText("   当前第"+(TreeParser.getNow()+1)+"条,共"+nowCount+"条    ");
        add(description);
        addToggleButton(editor.bind("+", mxGraphActions.getZoomInAction()));
        addToggleButton(editor.bind("-", mxGraphActions.getZoomOutAction()));
        addToggleButton(editor.bind("=", mxGraphActions.getZoomActualAction()));
    }   
}
