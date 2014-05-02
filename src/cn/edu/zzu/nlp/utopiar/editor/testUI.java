package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

public class testUI extends JFrame{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public testUI() {
		JTabbedPane tabbedPane = new JTabbedPane();
		JScrollPane scrollPane1 = new JScrollPane();
		JScrollPane scrollPane2 = new JScrollPane();
		JPanel panel = new JPanel();
		JCheckBox displayFenci = new JCheckBox("显示分词");
		JCheckBox displayCixing = new JCheckBox("显示词性");
		JLabel label = new JLabel();
		JToolBar toolBar = new JToolBar();
        JButton button = new JButton(new ImageIcon("img/redo.gif"));
		
		label.setText("测试文本！");
		toolBar.setFloatable(false);           
		
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(displayFenci);
		panel.add(displayCixing);
		panel.add(label);
		
		toolBar.add(button);
		
		tabbedPane.add("中文",scrollPane1);
		tabbedPane.add("英语",scrollPane2);
		
		JMenuBar menubar = new JMenuBar();//创建菜单工具条
		JMenu fileMenu = new JMenu("文件");//创建菜单栏根目录标签
		JMenu editMenu = new JMenu("编辑");
		JMenu aboutMenu = new JMenu("关于");
		
		JMenuItem saveItem = new JMenuItem("保存");
		JMenuItem addVertexItem = new JMenuItem("添加结点");
		JMenuItem addEdgeItem = new JMenuItem("添加连接");
		JMenuItem aboutItem = new JMenuItem("关于");
		
		menubar.add(fileMenu);//把菜单根目录标签放到菜单工具条上
		menubar.add(editMenu);
		menubar.add(aboutMenu);
		
		fileMenu.add(saveItem); //把菜单项放到指定菜单目录标签中
		editMenu.add(addVertexItem);
		editMenu.add(addEdgeItem);
		aboutMenu.add(aboutItem);
		setJMenuBar(menubar);
		
		getContentPane().add(toolBar,BorderLayout.NORTH);
		
		getContentPane().add(tabbedPane,BorderLayout.CENTER);
		getContentPane().add(panel,BorderLayout.SOUTH);
	}
	
	public static void main(String[] args){
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		JFrame frame = new testUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,200);
		frame.setVisible(true);
	}
}
