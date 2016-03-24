package cn.edu.zzu.nlp.utopiar.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ColorField extends Box {

    public ColorField( Color colour, Color defaultColour ) {
        super(BoxLayout.LINE_AXIS);
        defaultColor = defaultColour;
        colorPanel = new JPanel();
        colorPanel.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
        colorPanel.setBackground( colour == null ? defaultColour : colour );
        colorPanel.setPreferredSize( new Dimension( 50, colorPanel.getPreferredSize().height ) );
        pickColorButton = new JButton( "..." );
        pickColorButton.setToolTipText( "Pick a color..." );
        pickColorButton.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    Color col = JColorChooser.showDialog( ColorField.this, "Color Selection", Color.BLACK );
                    colorPanel.setBackground( col );
                    fireActionEvent();
                }
            }
        );
        revertColorButton = new JButton( "X" );
        revertColorButton.setToolTipText( "Revert to default color" );
        revertColorButton.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    colorPanel.setBackground( defaultColor );
                    fireActionEvent();
                }
            }
        );
        this.add( colorPanel );
        this.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
        this.add( pickColorButton );
        this.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
        this.add( revertColorButton );
    }

    public Color getColor() {
        return( colorPanel.getBackground() );
    }

    public synchronized void addActionListener( ActionListener listener ) {
        actionListeners.add( listener );
    }

    public synchronized void removeActionListener( ActionListener listener ) {
        actionListeners.remove( listener );
    }

    protected void fireActionEvent() {
        ActionEvent evt = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, "ColorChanged" );
        for( Iterator it = actionListeners.iterator(); it.hasNext(); ) {
            ActionListener listener = (ActionListener)it.next();
            listener.actionPerformed( evt );
        }
    }

    private Color defaultColor;

    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    private JPanel colorPanel;
    private JButton pickColorButton;
    private JButton revertColorButton;

}
