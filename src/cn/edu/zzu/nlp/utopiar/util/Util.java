package cn.edu.zzu.nlp.utopiar.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.UIManager;

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

    /**
     * 判断是否全由数字组成
     * @param str
     */
    public static boolean isNum(String str){
      String num = "1234567890";
      for(int i=0;i<str.length();i++){
          String single = str.substring(i, i+1);
          if(num.indexOf(single)<0)
              return false;
      }
      return true;
    }

    /**
     * 设置全局字体
     * @param fnt
     */
    public static void initGlobalFontSetting(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration<?> keys = UIManager.getDefaults().keys(); keys
                .hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }

    public static void setFrameCenter(JFrame jFrame){
        //居中显示
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        int x = screen.width - jFrame.getWidth()>>1;
        int y = (screen.height - jFrame.getHeight()>>1)-32;
        jFrame.setLocation(x, y);
    }
    
}

