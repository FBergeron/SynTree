package cn.edu.zzu.nlp.utopiar.editor;

import javax.swing.Action;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

class ToggleButtonToolBar extends JToolBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new toggle button tool bar.
	 */
	public ToggleButtonToolBar() {
		super();
		
		this.setFloatable(true);
	}

	/**
	 * Adds the toggle button.
	 *
	 * @param a the a
	 * @return the j toggle button
	 */
	JToggleButton addToggleButton(Action a) {
		JToggleButton tb = new JToggleButton(
				(String)a.getValue(Action.NAME),null
//				(Icon)a.getValue(Action.SMALL_ICON)
		);
//		tb.setMargin(zeroInsets);
//		tb.setText(null);
		tb.setEnabled(a.isEnabled());
		tb.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
		tb.setAction(a);
		add(tb);
		return tb;
	}
}