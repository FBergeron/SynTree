package cn.edu.zzu.nlp.utopiar.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import com.mxgraph.view.mxGraph;

public class EditorAboutFrame extends JDialog
{

    /**
     * 
     */
    private static final long serialVersionUID = -3378029138434324390L;

    /**
     * 
     */
    public EditorAboutFrame(Frame owner)
    {
        super(owner);
        setTitle("关于语法树工具");
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

        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(8, 8, 12, 8)));

        // Adds title
        JLabel titleLabel = new JLabel("关于语法树工具");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleLabel.setOpaque(false);
        panel.add(titleLabel, BorderLayout.NORTH);

        Box subtitlePanel = Box.createVerticalBox();

        // Adds optional subtitle
        JLabel subtitleLabel = new JLabel(
            "<html>SynTree v2.3.0<br>更多信息请访问 <a href=\"https://github.com/FBergeron/SynTree\">https://github.com/FBergeron/SynTree</a></html>");
        subtitleLabel.addMouseListener( new LinkFacilitator( "https://github.com/FBergeron/SynTree" ) );
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
        subtitleLabel.setOpaque(false);

        JLabel subtitleLabel2 = new JLabel( 
            "<html>SynTree v2.2.1 (Original Version)<br>更多信息请访问 <a href=\"http://syntree.github.io/index.html\">http://syntree.github.io/index.html</a></html>" );
        subtitleLabel2.addMouseListener( new LinkFacilitator( "http://syntree.github.io/index.html" ) );
        subtitleLabel2.setBorder(BorderFactory.createEmptyBorder(14, 18, 0, 0));
        subtitleLabel2.setOpaque(false);

        subtitlePanel.add( subtitleLabel );
        subtitlePanel.add( subtitleLabel2 );

        panel.add(subtitlePanel, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.NORTH);


        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        content.add(new JLabel("语法树工具 - 基于JGraphx的语法树修改工具"));
        content.add(new JLabel("作者：郑大自然语言处理实验室  梁军"));
        content.add(new JLabel(" "));

        content.add(new JLabel("mxGraph版本 " + mxGraph.VERSION));
        content.add(new JLabel("Copyright (C) 2013 by ZZU Nlp Ltd."));
        content.add(new JLabel("All rights reserved."));
        content.add(new JLabel(" "));

        try {
            content.add(new JLabel("操作系统: " + System.getProperty("os.name")));
        }
        catch( Exception ignore ) {
        }
        try {
            content.add(new JLabel("操作系统版本: " + System.getProperty("os.version")));
        }
        catch( Exception ignore ) {
        }
        content.add(new JLabel(" "));
        try {
            content.add(new JLabel("Java厂商: " + System.getProperty("java.vendor", "undefined")));
            content.add(new JLabel("Java版本: " + System.getProperty("java.version", "undefined")));
            content.add(new JLabel(" "));
        }
        catch( Exception ignore ) {
        }
        try {
            content.add(new JLabel("安装内存: " + Runtime.getRuntime().totalMemory()));
            content.add(new JLabel("剩余内存: " + Runtime.getRuntime().freeMemory()));
        }
        catch( Exception ignore ) {
        }

        getContentPane().add(content, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(16, 8, 8, 8)));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Adds OK button to close window
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }
        });

        buttonPanel.add(closeButton);

        // Sets default button for enter key
        getRootPane().setDefaultButton(closeButton);

        setResizable(false);
        setSize(460, 500);
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

    public class LinkFacilitator extends MouseAdapter {

        public LinkFacilitator( String url ) {
            this.url = url;
        }

        public void mouseReleased( MouseEvent evt ) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse( new URI( url ) );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private String url;

    }

}

