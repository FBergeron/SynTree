package cn.edu.zzu.nlp.utopiar.util;

import javax.swing.UIManager;

public class LookAndFeelNiceInfo extends UIManager.LookAndFeelInfo {

    public LookAndFeelNiceInfo( String name, String className ) {
        super( name, className );
    }

    public String toString() {
        return( getName() );
    }

    public boolean equals( Object obj ) {
        if( obj == this )
            return( true );
        if( !( obj instanceof LookAndFeelNiceInfo ) )
            return( false );
        LookAndFeelNiceInfo lnf = (LookAndFeelNiceInfo)obj;
        return( getClassName().equals( lnf.getClassName() ) );
    }

    public int hashCode() {
        return( getClassName().hashCode() );
    }

}

