package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
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
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.edu.zzu.nlp.readTree.TreeParser;
import cn.edu.zzu.nlp.utopiar.util.ColorField;
import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.LookAndFeelNiceInfo;
import cn.edu.zzu.nlp.utopiar.util.Preferences;
import cn.edu.zzu.nlp.utopiar.util.SpinnerField;

public class EditorSettingFrame extends JDialog {

    private static final long serialVersionUID = -5938480800710447552L;
    
    public EditorSettingFrame(JFrame owner, GraphEditor editor) {
        super(owner);
        this.editor = editor;
        setLayout(new BorderLayout());
    
        tabPanel = new JTabbedPane();

        JPanel visualPrefsPanel = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 0 ) );
        lookAndFeelLabel = new JLabel();
        lookAndFeelComboBox = new JComboBox<LookAndFeelNiceInfo>();
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
                    if( !hasShownPrefChangeWarning ) {
                        JOptionPane.showOptionDialog(EditorSettingFrame.this, 
                            "<html>You might have to close the Settings Dialog<br>or restart the application to view the changes.</html>", 
                                "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { "OK" }, "OK" );
                        hasShownPrefChangeWarning = true;
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

                    update();
                }
            }
        );

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {
                update();
            }
        };

        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged( ChangeEvent evt ) {
                update();
            }
        };

        graphBackgroundColorLabel = new JLabel();
        graphBackgroundColorField = new ColorField( Preferences.getInstance().getGraphBackgroundColor(), (Color)UIManager.get( "ScrollPane.background" ) );
        graphBackgroundColorField.setPreferredSize( new Dimension( 240, graphBackgroundColorField.getPreferredSize().height ) );
        graphBackgroundColorField.addActionListener( actionListener );

        boxBackgroundColorLabel = new JLabel( "XXXXXXXXXXXXXXXXXXXXXXXXXXX" );
        boxBackgroundColorField = new ColorField( Preferences.getInstance().getBoxBackgroundColor(), new Color( 195, 217, 255 ) );
        boxBackgroundColorField.setPreferredSize( new Dimension( 240, boxBackgroundColorField.getPreferredSize().height ) );
        boxBackgroundColorField.addActionListener( actionListener );

        boxForegroundColorLabel = new JLabel();
        boxForegroundColorField = new ColorField( Preferences.getInstance().getBoxForegroundColor(), new Color( 119, 68, 0 ) );
        boxForegroundColorField.setPreferredSize( new Dimension( 240, boxForegroundColorField.getPreferredSize().height ) );
        boxForegroundColorField.addActionListener( actionListener );

        boxBorderColorLabel = new JLabel();
        boxBorderColorField = new ColorField( Preferences.getInstance().getBoxBorderColor(), new Color( 100, 130, 185 ) );
        boxBorderColorField.setPreferredSize( new Dimension( 240, boxBorderColorField.getPreferredSize().height ) );
        boxBorderColorField.addActionListener( actionListener );

        edgeColorLabel = new JLabel();
        edgeColorField = new ColorField( Preferences.getInstance().getEdgeColor(), new Color( 100, 130, 185 ) );
        edgeColorField.setPreferredSize( new Dimension( 240, edgeColorField.getPreferredSize().height ) );
        edgeColorField.addActionListener( actionListener );

        boxWidthLabel = new JLabel();
        boxWidthSpinner = new SpinnerField( Preferences.getInstance().getBoxWidth(), 50 );
        boxWidthSpinner.addChangeListener( changeListener );

        boxHeightLabel = new JLabel();
        boxHeightSpinner = new SpinnerField( Preferences.getInstance().getBoxHeight(), 30 );
        boxHeightSpinner.addChangeListener( changeListener );

        horizontalInterBoxGapLabel = new JLabel();
        horizontalInterBoxGapSpinner = new SpinnerField( Preferences.getInstance().getHorizontalInterBoxGap(), 10 );
        horizontalInterBoxGapSpinner.addChangeListener( changeListener );

        verticalInterBoxGapLabel = new JLabel();
        verticalInterBoxGapSpinner = new SpinnerField( Preferences.getInstance().getVerticalInterBoxGap(), 40 );
        verticalInterBoxGapSpinner.addChangeListener( changeListener );

        boxFontSizeLabel = new JLabel();
        boxFontSizeSpinner = new SpinnerField( Preferences.getInstance().getBoxFontSize(), 18 );
        boxFontSizeSpinner.addChangeListener( changeListener );

        lookAndFeelLabel.setPreferredSize( new Dimension( lookAndFeelLabel.getPreferredSize().width, lookAndFeelComboBox.getPreferredSize().height ) );
        graphBackgroundColorLabel.setPreferredSize( new Dimension( graphBackgroundColorLabel.getPreferredSize().width, graphBackgroundColorField.getPreferredSize().height ) ); 
        boxBackgroundColorLabel.setPreferredSize( new Dimension( boxBackgroundColorLabel.getPreferredSize().width, boxBackgroundColorField.getPreferredSize().height ) );
        boxForegroundColorLabel.setPreferredSize( new Dimension( boxForegroundColorLabel.getPreferredSize().width, boxForegroundColorField.getPreferredSize().height ) );
        boxBorderColorLabel.setPreferredSize( new Dimension( boxBorderColorLabel.getPreferredSize().width, boxBorderColorField.getPreferredSize().height ) );
        edgeColorLabel.setPreferredSize( new Dimension( edgeColorLabel.getPreferredSize().width, edgeColorField.getPreferredSize().height ) );
        boxWidthLabel.setPreferredSize( new Dimension( boxWidthLabel.getPreferredSize().width, boxWidthSpinner.getPreferredSize().height ) );
        boxHeightLabel.setPreferredSize( new Dimension( boxHeightLabel.getPreferredSize().width, boxHeightSpinner.getPreferredSize().height ) );
        horizontalInterBoxGapLabel.setPreferredSize( new Dimension( horizontalInterBoxGapLabel.getPreferredSize().width, horizontalInterBoxGapSpinner.getPreferredSize().height ) );
        verticalInterBoxGapLabel.setPreferredSize( new Dimension( verticalInterBoxGapLabel.getPreferredSize().width, verticalInterBoxGapSpinner.getPreferredSize().height ) );
        boxFontSizeLabel.setPreferredSize( new Dimension( boxFontSizeLabel.getPreferredSize().width, boxHeightSpinner.getPreferredSize().height ) );

        Box leftPanel = Box.createVerticalBox();
        Box rightPanel = Box.createVerticalBox();

        leftPanel.add(lookAndFeelLabel);
        rightPanel.add(lookAndFeelComboBox);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 15 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 15 )));
        leftPanel.add(graphBackgroundColorLabel);
        rightPanel.add(graphBackgroundColorField);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(boxBackgroundColorLabel);
        rightPanel.add(boxBackgroundColorField);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(boxForegroundColorLabel);
        rightPanel.add(boxForegroundColorField);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(boxBorderColorLabel);
        rightPanel.add(boxBorderColorField);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(edgeColorLabel);
        rightPanel.add(edgeColorField);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 15 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 15 )));
        leftPanel.add(boxWidthLabel);
        rightPanel.add(boxWidthSpinner);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(boxHeightLabel);
        rightPanel.add(boxHeightSpinner);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(horizontalInterBoxGapLabel);
        rightPanel.add(horizontalInterBoxGapSpinner);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 5 )));
        leftPanel.add(verticalInterBoxGapLabel);
        rightPanel.add(verticalInterBoxGapSpinner);
        leftPanel.add(Box.createRigidArea(new Dimension( 5, 15 )));
        rightPanel.add(Box.createRigidArea(new Dimension( 5, 15 )));
        leftPanel.add(boxFontSizeLabel);
        rightPanel.add(boxFontSizeSpinner);
        leftPanel.add(Box.createVerticalGlue());
        rightPanel.add(Box.createVerticalGlue());

        visualPrefsPanel.add(leftPanel);
        visualPrefsPanel.add(rightPanel);

        JPanel pathSelectPanel = new JPanel(new BorderLayout());
        PathSelectionSettingPanel pathSelectionPanel = new PathSelectionSettingPanel();
        pathSelectPanel.add(pathSelectionPanel,BorderLayout.CENTER);
        
        tabPanel.addTab(null, visualPrefsPanel);
        tabPanel.addTab(null, pathSelectPanel);

        getContentPane().add(tabPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(16, 8, 8, 8)));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        closeButton = new JButton(
            new AbstractAction() {
                public void actionPerformed( ActionEvent evt ) {
                    EditorSettingFrame.this.setVisible( false );
                }
            }
        );

        buttonPanel.add(closeButton);

        // Sets default button for enter key
        getRootPane().setDefaultButton(closeButton);

        setResizable(false);
        setSize(750, 550);

        setLocale( Languages.getInstance().getCurrent() );
    }

    public void setLocale( Locale locale ) {
        super.setLocale( locale );
         
        setTitle( Languages.getInstance().getString( "Frame.Settings.Title" ) );
        if( tabPanel != null ) {
            tabPanel.setTitleAt( 0, Languages.getInstance().getString( "Frame.Settings.TabPanel.Visual" ) );
            tabPanel.setTitleAt( 1, Languages.getInstance().getString( "Frame.Settings.TabPanel.PathSelection" ) );
        }
        if( lookAndFeelLabel != null )
            lookAndFeelLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.LookAndFeel" ) );
        if( graphBackgroundColorLabel != null )
            graphBackgroundColorLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.GraphBackgroundColor" ) );
        if( boxBackgroundColorLabel != null )
            boxBackgroundColorLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.BoxBackgroundColor" ) );
        if( boxForegroundColorLabel != null )
            boxForegroundColorLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.BoxForegroundColor" ) );
        if( boxBorderColorLabel != null )
            boxBorderColorLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.BoxBorderColor" ) );
        if( edgeColorLabel != null )
            edgeColorLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.EdgeColor" ) );
        if( boxWidthLabel != null )
            boxWidthLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.BoxWidth" ) );
        if( boxHeightLabel != null )
            boxHeightLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.BoxHeight" ) );
        if( horizontalInterBoxGapLabel != null )
            horizontalInterBoxGapLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.InterBoxGapWidth" ) );
        if( verticalInterBoxGapLabel != null )
            verticalInterBoxGapLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.InterBoxGapHeight" ) );
        if( boxFontSizeLabel != null )
            boxFontSizeLabel.setText( Languages.getInstance().getString( "Frame.Settings.Visual.BoxFontSize" ) );
        if( closeButton != null )
            closeButton.setText( Languages.getInstance().getString( "Frame.Settings.Button.Ok" ) );
    }

    public void update() {
        try {
            String lnf = ((UIManager.LookAndFeelInfo)lookAndFeelComboBox.getSelectedItem()).getClassName();
            Preferences.getInstance().setLookAndFeel( lnf );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = graphBackgroundColorField.getColor();
            Preferences.getInstance().setGraphBackgroundColor( UIManager.get( "ScrollPane.background").equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = boxBackgroundColorField.getColor();
            Preferences.getInstance().setBoxBackgroundColor( new Color( 195, 217, 255 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = boxForegroundColorField.getColor();
            Preferences.getInstance().setBoxForegroundColor( new Color( 119, 68, 0 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = boxBorderColorField.getColor();
            Preferences.getInstance().setBoxBorderColor( new Color( 100, 130, 185 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }

        try {
            Color color = edgeColorField.getColor();
            Preferences.getInstance().setEdgeColor( new Color( 100, 130, 185 ).equals(color) ? null : color );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        try {
            Integer width = (Integer)boxWidthSpinner.getValue();
            Preferences.getInstance().setBoxWidth( Integer.valueOf( 50 ).equals(width) ? null : width );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        try {
            Integer height = (Integer)boxHeightSpinner.getValue();
            Preferences.getInstance().setBoxHeight( Integer.valueOf( 30 ).equals(height) ? null : height );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        try {
            Integer gap = (Integer)horizontalInterBoxGapSpinner.getValue();
            Preferences.getInstance().setHorizontalInterBoxGap( Integer.valueOf( 60 ).equals(gap) ? null : gap );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        try {
            Integer gap = (Integer)verticalInterBoxGapSpinner.getValue();
            Preferences.getInstance().setVerticalInterBoxGap( Integer.valueOf( 40 ).equals(gap) ? null : gap );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        try {
            Integer fontSize = (Integer)boxFontSizeSpinner.getValue();
            Preferences.getInstance().setBoxFontSize( Integer.valueOf( 18 ).equals(fontSize) ? null : fontSize );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
        
        editor.getTabbedPane().update();
    }

    public GraphEditor getEditor() {
        return( editor );
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
            Box zhPanel = Box.createVerticalBox();
            zhPanel.add( Box.createVerticalGlue() );
            JLabel zhImg = new JLabel( new ImageIcon( "img/zh.png" ) );
            zhImg.setAlignmentX( JComponent.CENTER_ALIGNMENT );
            zhPanel.add( zhImg );
            zhPanel.add( Box.createRigidArea( new Dimension( 10, 10 ) ) );
            Box engPanel = Box.createVerticalBox();
            engPanel.add( Box.createVerticalGlue() );
            JLabel engImg = new JLabel( new ImageIcon( "img/eng.png" ) );
            engImg.setAlignmentX( JComponent.CENTER_ALIGNMENT );
            engPanel.add( engImg );
            engPanel.add( Box.createRigidArea( new Dimension( 10, 10 ) ) );
            JButton zhButton = new JButton("选择");
            zhButton.setAlignmentX( JComponent.CENTER_ALIGNMENT );
            JButton engButton = new JButton("选择");
            engButton.setAlignmentX( JComponent.CENTER_ALIGNMENT );
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
            zhPanel.add(zhButton);
            zhPanel.add( Box.createVerticalGlue() );
            engPanel.add(engButton);
            engPanel.add( Box.createVerticalGlue() );
            this.add("中文路径选择",zhPanel);
            this.add("英文路径选择",engPanel);
        }
        
    }

    private JLabel                          lookAndFeelLabel;
    private JComboBox<LookAndFeelNiceInfo>  lookAndFeelComboBox;

    private JTabbedPane     tabPanel;
    private JLabel          graphBackgroundColorLabel;
    private ColorField      graphBackgroundColorField;
    private JLabel          boxBackgroundColorLabel;
    private ColorField      boxBackgroundColorField;
    private JLabel          boxForegroundColorLabel;
    private ColorField      boxForegroundColorField;
    private JLabel          boxBorderColorLabel;
    private ColorField      boxBorderColorField;
    private JLabel          edgeColorLabel;
    private ColorField      edgeColorField;
    private JLabel          boxWidthLabel;
    private SpinnerField    boxWidthSpinner;
    private JLabel          boxHeightLabel;
    private SpinnerField    boxHeightSpinner;
    private JLabel          horizontalInterBoxGapLabel;
    private SpinnerField    horizontalInterBoxGapSpinner;
    private JLabel          verticalInterBoxGapLabel;
    private SpinnerField    verticalInterBoxGapSpinner;
    private JLabel          boxFontSizeLabel;
    private SpinnerField    boxFontSizeSpinner;

    private JButton         closeButton;

    private boolean hasShownPrefChangeWarning = false;

    private final GraphEditor editor;

}
