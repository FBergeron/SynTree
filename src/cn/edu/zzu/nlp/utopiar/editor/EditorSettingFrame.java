package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.action.ActionClose;
import cn.edu.zzu.nlp.utopiar.util.ColorField;
import cn.edu.zzu.nlp.utopiar.util.LookAndFeelNiceInfo;
import cn.edu.zzu.nlp.utopiar.util.Preferences;

public class EditorSettingFrame extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -5938480800710447552L;
    
    
    
    public EditorSettingFrame(JFrame owner) {
        super(owner);
        setTitle("设置");
        setLayout(new BorderLayout());
    
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel visualPrefsPanel = new JPanel();
        JLabel lookAndFeelLabel = new JLabel( "Look and Feel" );
        lookAndFeelComboBox = new JComboBox();
        UIManager.LookAndFeelInfo[] lnfInfo = UIManager.getInstalledLookAndFeels();
        LookAndFeelNiceInfo lnfToSelect = null;
        String prefLnf = Preferences.getInstance().getLookAndFeel(); 
        for( UIManager.LookAndFeelInfo lnf : lnfInfo ) {
            LookAndFeelNiceInfo lnfNiceInfo = new LookAndFeelNiceInfo( lnf.getName(), lnf.getClassName() );
            if( prefLnf == null && lnfNiceInfo.getClassName().equals( UIManager.getSystemLookAndFeelClassName() ) )
                lnfToSelect = lnfNiceInfo;
            else if( prefLnf != null && lnfNiceInfo.getClassName().equals( prefLnf ) )
                lnfToSelect = lnfNiceInfo;
            lookAndFeelComboBox.addItem( lnfNiceInfo );
        }
        lookAndFeelComboBox.setSelectedItem( lnfToSelect );
        lookAndFeelComboBox.addActionListener( 
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    lookAndFeelComboBox.hidePopup();
                    if( !hasShownLookAndFeelWarning ) {
                        JOptionPane.showMessageDialog(EditorSettingFrame.this, "You might need to restart the application to activate the selected Look And Feel.");
                        hasShownLookAndFeelWarning = true;
                    }

                    final UIManager.LookAndFeelInfo lnf = (UIManager.LookAndFeelInfo)lookAndFeelComboBox.getSelectedItem();
                    try {
                        UIManager.setLookAndFeel( lnf.getClassName() );
                        SwingUtilities.updateComponentTreeUI(EditorSettingFrame.this);
                        SwingUtilities.updateComponentTreeUI(EditorSettingFrame.this.getOwner());
                    }
                    catch( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        );

        JLabel backgroundColorLabel = new JLabel( "Graph Background Color" );
        graphBackgroundColorField = new ColorField( Preferences.getInstance().getGraphBackgroundColor(), (Color)UIManager.get( "ScrollPane.background" ) );
        graphBackgroundColorField.setPreferredSize( new Dimension( 240, graphBackgroundColorField.getPreferredSize().height ) );
        graphBackgroundColorField.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    EditorTabbedPane.ZH_GRAPH_COMPONENT.getViewport().setBackground(graphBackgroundColorField.getColor());
                    EditorTabbedPane.ENG_GRAPH_COMPONENT.getViewport().setBackground(graphBackgroundColorField.getColor());
                }
            }
        );

        Box leftPanel = Box.createVerticalBox();
        Box rightPanel = Box.createVerticalBox();

        leftPanel.add(lookAndFeelLabel);
        rightPanel.add(lookAndFeelComboBox);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(backgroundColorLabel);
        rightPanel.add(graphBackgroundColorField);
        leftPanel.add(Box.createVerticalGlue());
        rightPanel.add(Box.createVerticalGlue());

        visualPrefsPanel.add(leftPanel);
        visualPrefsPanel.add(rightPanel);

        // Creates the gradient panel
        JPanel gradientPanel = new JPanel(new BorderLayout())
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
        gradientPanel.setLayout(new BorderLayout());
        PathSelectionSettingPanel pathSelectPanel = new PathSelectionSettingPanel();
        gradientPanel.add(pathSelectPanel,BorderLayout.CENTER);
        
        tabPanel.addTab("Visual", visualPrefsPanel);
        tabPanel.addTab("路径选择", gradientPanel);

        getContentPane().add(tabPanel, BorderLayout.CENTER);
        
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

    public String getLookAndFeel() {
        UIManager.LookAndFeelInfo lnf = (UIManager.LookAndFeelInfo)lookAndFeelComboBox.getSelectedItem();
        return( lnf.getClassName() );
    }

    public Color getGraphBackgroundColor() {
        return( graphBackgroundColorField.getColor() );
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

    class PathSelectionSettingPanel extends JTabbedPane{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        final JFileChooser zhFileChooser = new JFileChooser();
        final JFileChooser engFileChooser = new JFileChooser();
        PathSelectionSettingPanel(){
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

    private JComboBox lookAndFeelComboBox;
    private ColorField graphBackgroundColorField;

    private boolean hasShownLookAndFeelWarning = false;

}
