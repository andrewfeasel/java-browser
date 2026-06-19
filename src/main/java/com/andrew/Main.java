package com.andrew;

import javax.swing.SwingUtilities;

/**
 *
 * @author andrewf
*/

public class Main {
	/**
	 * @param args the command line arguments
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			BrowserFrame myBrowser = new BrowserFrame();
			BrowserTab firstTab = myBrowser.newTab();
			firstTab.setUrl("http://info.cern.ch");
		});
	}
}
