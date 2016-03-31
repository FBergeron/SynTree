package cn.edu.zzu.nlp.utopiar.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public static Image getImageResourceFile( String strResourceFilename, Class srcClass ) {
        Image image = null;
        try {
            BufferedInputStream in = new BufferedInputStream(
                srcClass.getResourceAsStream( strResourceFilename ) );
            if( in == null ) {
                System.err.println( "Image not found:" + strResourceFilename );
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy( in, out );
            image = Toolkit.getDefaultToolkit().createImage( out.toByteArray() );
        }
        catch( java.io.IOException e ) {
            System.err.println( "Unable to read image " + strResourceFilename + "." );
            e.printStackTrace();
        }
        return( image );
    }

    public static void copy( InputStream in, OutputStream out ) throws IOException
    {
        // Do not allow other threads to read from the
        // input or write to the output while copying is
        // taking place
        synchronized( in ) {
            synchronized( out ) {
                byte[] buffer = new byte[ 1024 ];
                while( true ) {
                    int bytesRead = in.read( buffer );
                    if( bytesRead == -1 )
                        break;
                    out.write( buffer, 0, bytesRead );
                }
            }
        }
    }

}

