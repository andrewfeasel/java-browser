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
            
						Navigator firstTab = myBrowser.newTab();
            firstTab.setPage("http://info.cern.ch");
        });
    }
    
}
