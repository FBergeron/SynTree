package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionClose;

public class EditorSettingFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5938480800710447552L;
	
	
	
	public EditorSettingFrame(JFrame owner) {
		super(owner);
		setTitle("路径选择");
		setLayout(new BorderLayout());
		
		// Creates the gradient panel
		JPanel panel = new JPanel(new BorderLayout())
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5062895855016210947L;

			/**
			 * 
			 */
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				// Paint gradient background
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(),
						0, getBackground()));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}

		};
		panel.setLayout(new BorderLayout());
		SettingPanel tabbedPane = new SettingPanel();
		panel.add(tabbedPane,BorderLayout.CENTER);
		
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
				.createEmptyBorder(16, 8, 8, 8)));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// Adds OK button to close window
		JButton closeButton = new JButton(new ActionClose(this));

		closeButton.setText("关闭");
		buttonPanel.add(closeButton);

		// Sets default button for enter key
		getRootPane().setDefaultButton(closeButton);

		setResizable(false);
		setSize(750, 550);
	}



	/**
	 * Overrides {@link JDialog#createRootPane()} to return a root pane that
	 * hides the window when the user presses the ESCAPE key.O
	 */
	protected JRootPane createRootPane()
	{
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

	class SettingPanel extends JTabbedPane{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final JFileChooser zhFileChooser = new JFileChooser();
		final JFileChooser engFileChooser = new JFileChooser();
		SettingPanel(){
			JPanel zhPanel = new JPanel(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 3279239724020827282L;

				public void paint(Graphics g){
					super.paint(g);
					g.drawImage(new ImageIcon("img/zh.png").getImage(), 0, 0, null);
				}
			};
			JPanel engPanel = new JPanel(){
				/**
				 * 
				 */
				private static final long serialVersionUID = -146649402418814022L;

				public void paint(Graphics g){
					super.paint(g);
					g.drawImage(new ImageIcon("img/eng.png").getImage(), 0, 0, null);
				}
			};
			JButton zhButton = new JButton("选择");
			JButton engButton = new JButton("选择");
			engButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					engFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); 
					int intRetVal = engFileChooser.showOpenDialog((Component)e.getSource());
					if (intRetVal == JFileChooser.APPROVE_OPTION) {
						System.out.println(engFileChooser.getSelectedFile().getPath());		
						if(!EditorTabbedPane.iszH()){
							EditorTabbedPane.setPATH(engFileChooser.getSelectedFile().getPath());
						}
						EditorTabbedPane.setENGLISH_PATH(engFileChooser.getSelectedFile().getPath());
						((JTabbedPane)EditorTabbedPane.ENG_GRAPH_COMPONENT.getParent().getParent()).setToolTipTextAt(1, engFileChooser.getSelectedFile().getPath());
						TreeParser.readData(EditorTabbedPane.getEnglishPath());
					}
				}
			});
			zhButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					zhFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); 
					int intRetVal = zhFileChooser.showOpenDialog((Component)e.getSource());
					if (intRetVal == JFileChooser.APPROVE_OPTION) {	
						if(EditorTabbedPane.iszH()){
							EditorTabbedPane.setPATH(zhFileChooser.getSelectedFile().getPath());
						}
						EditorTabbedPane.setCHINESE_PATH(zhFileChooser.getSelectedFile().getPath());
						((JTabbedPane)EditorTabbedPane.ZH_GRAPH_COMPONENT.getParent().getParent()).setToolTipTextAt(0, zhFileChooser.getSelectedFile().getPath());
						TreeParser.readData(EditorTabbedPane.getChinesePath());
					}
				}
			});
			zhButton.setBounds(350, 300, 50, 30);
			zhPanel.setLayout(null);
			zhPanel.add(zhButton);
			engButton.setBounds(350, 300, 50, 30);
			engPanel.setLayout(null);
			engPanel.add(engButton);
			this.add("中文路径选择",zhPanel);
			this.add("英文路径选择",engPanel);
		}
		
	}
}
