package cn.edu.zzu.nlp.utopiar.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import cn.edu.zzu.nlp.utopiar.util.Languages;
import cn.edu.zzu.nlp.utopiar.util.Preferences;

public class ActionSetLocale extends AbstractAction {

    public ActionSetLocale( String id, String name, ImageIcon icon ) {
        super( name, icon );
        this.id = id;
    }

    public void actionPerformed( ActionEvent evt ) {
        try {
            Preferences.getInstance().setLanguage( id );
            Languages.getInstance().setCurrent( new Locale( id ) );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    private String id;

}

