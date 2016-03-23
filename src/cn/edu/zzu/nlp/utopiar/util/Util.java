package cn.edu.zzu.nlp.utopiar.util;

import java.awt.Color;

public class Util {

    public static String colorRGBToHex( Color c ) {
        StringBuffer str = new StringBuffer( "#" );
        if( c.getRed() < 16 )
            str.append( "0" );
        str.append( Integer.toHexString( c.getRed() ).toUpperCase() );
        if( c.getGreen() < 16 )
            str.append( "0" );
        str.append( Integer.toHexString( c.getGreen() ).toUpperCase() );
        if( c.getBlue() < 16 )
            str.append( "0" );
        str.append( Integer.toHexString( c.getBlue() ).toUpperCase() );
        return( str.toString() );
    }

}

