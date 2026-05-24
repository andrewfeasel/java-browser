package com.andrew;

import javax.swing.SwingUtilities;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import javax.swing.JFrame;

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
            Browser myBrowser = new Browser();
            try {
                myBrowser.navigateTo(new URI("http://wiby.org").toURL());
            } catch (URISyntaxException | MalformedURLException err) {
                err.printStackTrace();
            }
        });
    }
    
}
