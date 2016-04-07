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
import java.text.MessageFormat;

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

import cn.edu.zzu.nlp.utopiar.util.Languages;

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
        setTitle( Languages.getInstance().getString( "Frame.About.Title" ) );
        setLayout(new BorderLayout());

        // Creates the gradient panel
        JPanel panel = new JPanel(new BorderLayout())
        {
            private static final long serialVersionUID = -5062895855016210947L;

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
        JLabel titleLabel = new JLabel( Languages.getInstance().getString( "Frame.About.Description" ) );
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleLabel.setOpaque(false);
        panel.add(titleLabel, BorderLayout.NORTH);

        Box subtitlePanel = Box.createVerticalBox();

        // Adds optional subtitle
        JLabel subtitleLabel = new JLabel( Languages.getInstance().getString( "Frame.About.Subtitle.Version-2.3.0" ) );
        subtitleLabel.addMouseListener( new LinkFacilitator( "https://github.com/FBergeron/SynTree" ) );
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
        subtitleLabel.setOpaque(false);

        JLabel subtitleLabel2 = new JLabel( Languages.getInstance().getString( "Frame.About.Subtitle.Version-2.2.1" ) );
        subtitleLabel2.addMouseListener( new LinkFacilitator( "http://syntree.github.io/index.html" ) );
        subtitleLabel2.setBorder(BorderFactory.createEmptyBorder(14, 18, 0, 0));
        subtitleLabel2.setOpaque(false);

        subtitlePanel.add( new JLabel( " " ) );
        subtitlePanel.add( subtitleLabel );
        subtitlePanel.add( subtitleLabel2 );

        panel.add(subtitlePanel, BorderLayout.CENTER);
        getContentPane().add(panel, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        content.add(new JLabel( Languages.getInstance().getString( "Frame.About.Author" ) ) );
        content.add(new JLabel(Languages.getInstance().getString("Frame.About.Copyright")));
        content.add(new JLabel(" "));

        try {
            content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.OS.Name" ), System.getProperty("os.name"))) );
            content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.OS.Version" ), System.getProperty("os.version"))) );
            content.add(new JLabel(" "));
        }
        catch( Exception ignore ) {
        }

        try {
            content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.Java.Vendor" ), System.getProperty("java.vendor", "undefined"))) );
            content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.Java.Version" ), System.getProperty("java.version", "undefined"))) );
            content.add(new JLabel(" "));
        }
        catch( Exception ignore ) {
        }

        content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.mxGraph.Version" ), mxGraph.VERSION)));
        content.add(new JLabel(" "));

        try {
            content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.Memory.Total" ), Runtime.getRuntime().totalMemory())) );
            content.add(new JLabel( MessageFormat.format( Languages.getInstance().getString( "Frame.About.Memory.Free" ), Runtime.getRuntime().freeMemory())) );
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
        JButton closeButton = new JButton("OK");
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
        pack();
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

