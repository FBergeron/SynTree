package cn.edu.zzu.nlp.utopiar.util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.edu.zzu.nlp.utopiar.util.Languages;

public class SpinnerField extends Box {

    public SpinnerField( final Integer value, final Integer defaultValue ) {
        super(BoxLayout.LINE_AXIS);
        this.defaultValue = defaultValue;
        SpinnerNumberModel model = new SpinnerNumberModel( value == null ? defaultValue.intValue() : value.intValue(), 0, 200, 1 );
        spinner = new JSpinner( model );
        spinner.setPreferredSize( new Dimension( 50, spinner.getPreferredSize().height ) );
        revertValueButton = new JButton( "X" );
        revertValueButton.setToolTipText( Languages.getInstance().getString( "SpinnerField.RevertButton.Tooltip" ) );
        revertValueButton.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    spinner.setValue( defaultValue );
                }
            }
        );
        this.add( spinner );
        this.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
        this.add( revertValueButton );
    }

    public Integer getValue() {
        return( (Integer)spinner.getValue() );
    }

    public synchronized void addChangeListener( ChangeListener listener ) {
        spinner.addChangeListener( listener );
    }

    public synchronized void removeChangeListener( ChangeListener listener ) {
        spinner.removeChangeListener( listener );
    }

    private Integer defaultValue;

    private JSpinner spinner;
    private JButton revertValueButton;

}

