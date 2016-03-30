package cn.edu.zzu.nlp.utopiar.util;

import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Languages implements ItemSelectable {

    public static Locale[] supported = {
        Locale.ENGLISH,
        Locale.FRENCH,
        Locale.JAPANESE,
        Locale.CHINESE
    };

    public static Languages getInstance() {
        return( instance );
    }

    public Locale getCurrent() {
        return( current );
    }

    public void setCurrent( Locale locale ) {
        current = locale;
        bundle = ResourceBundle.getBundle( "Strings", current );
        ItemEvent evt = new ItemEvent( this, ItemEvent.ITEM_STATE_CHANGED, locale, ItemEvent.SELECTED );
        for( Iterator it = itemListeners.iterator(); it.hasNext(); ) {
            ItemListener listener = (ItemListener)it.next();
            listener.itemStateChanged( evt );
        }
    }

    public String getString( String key ) {
        return( bundle.getString( key ) );
    }

    public synchronized void addItemListener( ItemListener listener ) {
        itemListeners.add( listener );
    }

    public synchronized void removeItemListener( ItemListener listener ) {
        itemListeners.remove( listener );
    }

    public Object[] getSelectedObjects() {
        return( new Object[] { current } );
    }

    private List<ItemListener> itemListeners = new ArrayList<ItemListener>();

    private Locale current;
    private ResourceBundle bundle;

    private static Languages instance = new Languages();

}
