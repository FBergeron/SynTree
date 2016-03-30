package cn.edu.zzu.nlp.utopiar.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Preferences {

    public static Preferences getInstance() {
        if( instance == null )
            instance = new Preferences();
        return( instance );
    }

    public static String getDataFolder() {
        String dataFolder = null;
        String osName = (System.getProperty("os.name")).toLowerCase();
        if( osName.startsWith( "windows" )  ) {
            String appDataFolder = System.getenv().get( "LOCALAPPDATA" );
            if( appDataFolder == null || "".equals( appDataFolder ) )
                appDataFolder = System.getenv().get( "APPDATA" );
            dataFolder = appDataFolder + "/syntree";
        }
        else if( osName.startsWith( "mac" ) || osName.startsWith( "linux" ) )
            dataFolder = System.getProperty( "user.home" ) + "/.syntree";
        if( dataFolder != null )
            new File( dataFolder ).mkdirs();
        return( dataFolder );
    }

    public void load() throws Exception {
        File prefsFile = new File( getDataFolder(), "prefs.xml" );
        if( prefsFile.exists() )
            fromXML( new BufferedInputStream( new FileInputStream( prefsFile ) ) );
    }

    public void save() throws Exception {
        File prefsFile = new File( getDataFolder(), "prefs.xml" );
        FileOutputStream fos = new FileOutputStream( prefsFile );
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( fos, "UTF-8" ) );
        writer.write( toXML() );
        writer.flush();
        writer.close();
    }

    public Color getGraphBackgroundColor() {
        return( graphBackgroundColor );
    }

    public void setGraphBackgroundColor( Color color ) throws Exception {
        graphBackgroundColor = color;
        save();
    }

    public Color getBoxBackgroundColor() {
        return( boxBackgroundColor );
    }

    public void setBoxBackgroundColor( Color color ) throws Exception {
        boxBackgroundColor = color;
        save();
    }

    public Color getBoxForegroundColor() {
        return( boxForegroundColor );
    }

    public void setBoxForegroundColor( Color color ) throws Exception {
        boxForegroundColor = color;
        save();
    }

    public Color getBoxBorderColor() {
        return( boxBorderColor );
    }

    public void setBoxBorderColor( Color color ) throws Exception {
        boxBorderColor = color;
        save();
    }

    public Color getEdgeColor() {
        return( edgeColor );
    }

    public void setEdgeColor( Color color ) throws Exception {
        edgeColor = color;
        save();
    }

    public String getLanguage() {
        return( language == null ? "en" : language );
    }

    public void setLanguage( String language ) throws Exception {
        this.language = language;
        save();
    }

    public String getLookAndFeel() {
        return( lookAndFeel );
    }

    public void setLookAndFeel( String lookAndFeel ) throws Exception {
        this.lookAndFeel = lookAndFeel;
        save();
    }

    public Integer getBoxWidth() {
        return( boxWidth );
    }

    public void setBoxWidth( Integer width ) throws Exception {
        this.boxWidth = width;
        save();
    }

    public Integer getBoxHeight() {
        return( boxHeight );
    }

    public void setBoxHeight( Integer height ) throws Exception {
        this.boxHeight = height;
        save();
    }

    public Integer getHorizontalInterBoxGap() {
        return( horizontalInterBoxGap );
    }

    public void setHorizontalInterBoxGap( Integer gap ) throws Exception {
        this.horizontalInterBoxGap = gap;
        save();
    }

    public Integer getVerticalInterBoxGap() {
        return( verticalInterBoxGap );
    }

    public void setVerticalInterBoxGap( Integer gap ) throws Exception {
        this.verticalInterBoxGap = gap;
        save();
    }

    public Integer getBoxFontSize() {
        return( boxFontSize );
    }

    public void setBoxFontSize( Integer size ) throws Exception {
        this.boxFontSize = size;
        save();
    }

    public String toXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
        xml += "<prefs>\n";
        if( graphBackgroundColor != null ) {
            String graphBackgroundColorStr = graphBackgroundColor.getRed() + "," + 
                graphBackgroundColor.getGreen() + "," + graphBackgroundColor.getBlue();
            xml += "  <pref " + getKeyValueAsXmlAttributes( "graphBackgroundColor", graphBackgroundColorStr ) + "/>\n";
        }
        if( boxBackgroundColor != null ) {
            String boxBackgroundColorStr = boxBackgroundColor.getRed() + "," + 
                boxBackgroundColor.getGreen() + "," + boxBackgroundColor.getBlue();
            xml += "  <pref " + getKeyValueAsXmlAttributes( "boxBackgroundColor", boxBackgroundColorStr ) + "/>\n";
        }
        if( boxForegroundColor != null ) {
            String boxForegroundColorStr = boxForegroundColor.getRed() + "," + 
                boxForegroundColor.getGreen() + "," + boxForegroundColor.getBlue();
            xml += "  <pref " + getKeyValueAsXmlAttributes( "boxForegroundColor", boxForegroundColorStr ) + "/>\n";
        }
        if( boxBorderColor != null ) {
            String boxBorderColorStr = boxBorderColor.getRed() + "," + 
                boxBorderColor.getGreen() + "," + boxBorderColor.getBlue();
            xml += "  <pref " + getKeyValueAsXmlAttributes( "boxBorderColor", boxBorderColorStr ) + "/>\n";
        }
        if( edgeColor != null ) {
            String edgeColorStr = edgeColor.getRed() + "," + 
                edgeColor.getGreen() + "," + edgeColor.getBlue();
            xml += "  <pref " + getKeyValueAsXmlAttributes( "edgeColor", edgeColorStr ) + "/>\n";
        }
        if( language != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "language", language + "" ) + "/>\n";
        if( lookAndFeel != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "lookAndFeel", lookAndFeel + "" ) + "/>\n";
        if( boxWidth != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "boxWidth", boxWidth + "" ) + "/>\n";
        if( boxHeight != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "boxHeight", boxHeight + "" ) + "/>\n";
        if( horizontalInterBoxGap != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "horizontalInterBoxGap", horizontalInterBoxGap + "" ) + "/>\n";
        if( verticalInterBoxGap != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "verticalInterBoxGap", verticalInterBoxGap + "" ) + "/>\n";
        if( boxFontSize != null )
            xml += "  <pref " + getKeyValueAsXmlAttributes( "boxFontSize", boxFontSize + "" ) + "/>\n";

        xml += "</prefs>\n";
        return xml;
    }

    public void fromXML(InputStream is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true); //convert CDATA node to Text node
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);

        NodeList list = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                String tagName = e.getTagName().toLowerCase();
                if( "pref".equals( tagName ) ) {
                    NamedNodeMap attr = e.getAttributes();
                    if( attr != null ) {
                        Node keyNode = attr.getNamedItem( "key" );
                        String key = ( keyNode == null ? null : keyNode.getNodeValue() );
                        Node valueNode = attr.getNamedItem( "value" );
                        String value = ( valueNode == null ? null : valueNode.getNodeValue() );
                        if( "graphBackgroundColor".equals( key ) ) {
                            String[] rgbVals = value.split( "," );
                            if( rgbVals.length == 3 ) {
                                int red = Integer.parseInt( rgbVals[ 0 ] );
                                int green = Integer.parseInt( rgbVals[ 1 ] );
                                int blue = Integer.parseInt( rgbVals[ 2 ] );
                                graphBackgroundColor = new Color( red, green, blue );
                            }
                        }
                        else if( "boxBackgroundColor".equals( key ) ) {
                            String[] rgbVals = value.split( "," );
                            if( rgbVals.length == 3 ) {
                                int red = Integer.parseInt( rgbVals[ 0 ] );
                                int green = Integer.parseInt( rgbVals[ 1 ] );
                                int blue = Integer.parseInt( rgbVals[ 2 ] );
                                boxBackgroundColor = new Color( red, green, blue );
                            }
                        }
                        else if( "boxForegroundColor".equals( key ) ) {
                            String[] rgbVals = value.split( "," );
                            if( rgbVals.length == 3 ) {
                                int red = Integer.parseInt( rgbVals[ 0 ] );
                                int green = Integer.parseInt( rgbVals[ 1 ] );
                                int blue = Integer.parseInt( rgbVals[ 2 ] );
                                boxForegroundColor = new Color( red, green, blue );
                            }
                        }
                        else if( "boxBorderColor".equals( key ) ) {
                            String[] rgbVals = value.split( "," );
                            if( rgbVals.length == 3 ) {
                                int red = Integer.parseInt( rgbVals[ 0 ] );
                                int green = Integer.parseInt( rgbVals[ 1 ] );
                                int blue = Integer.parseInt( rgbVals[ 2 ] );
                                boxBorderColor = new Color( red, green, blue );
                            }
                        }
                        else if( "edgeColor".equals( key ) ) {
                            String[] rgbVals = value.split( "," );
                            if( rgbVals.length == 3 ) {
                                int red = Integer.parseInt( rgbVals[ 0 ] );
                                int green = Integer.parseInt( rgbVals[ 1 ] );
                                int blue = Integer.parseInt( rgbVals[ 2 ] );
                                edgeColor = new Color( red, green, blue );
                            }
                        }
                        else if( "language".equals( key ) )
                            language = value;
                        else if( "lookAndFeel".equals( key ) )
                            lookAndFeel = value;
                        else if( "boxWidth".equals( key ) )
                            boxWidth = Integer.parseInt( value );
                        else if( "boxHeight".equals( key ) )
                            boxHeight = Integer.parseInt( value );
                        else if( "horizontalInterBoxGap".equals( key ) )
                            horizontalInterBoxGap = Integer.parseInt( value );
                        else if( "verticalInterBoxGap".equals( key ) )
                            verticalInterBoxGap = Integer.parseInt( value );
                        else if( "boxFontSize".equals( key ) )
                            boxFontSize = Integer.parseInt( value );
                    }
                }
            }
        }
    }

    private Preferences() {
    }

    private String getKeyValueAsXmlAttributes( String key, String value ) {
        return( "key=\"" + key + "\" value=\"" + value + "\"" );
    }

    private Color   graphBackgroundColor;
    private Color   boxBackgroundColor;
    private Color   boxForegroundColor;
    private Color   boxBorderColor;
    private Color   edgeColor;
    private String  language;
    private String  lookAndFeel;
    private Integer boxWidth;
    private Integer boxHeight;
    private Integer horizontalInterBoxGap;
    private Integer verticalInterBoxGap;
    private Integer boxFontSize;

    private static Preferences instance;

}
