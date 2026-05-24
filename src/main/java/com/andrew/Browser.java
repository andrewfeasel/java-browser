
package com.andrew;

/**
 *
 * @author andrewf
 */

import javax.swing.text.html.*;
import javax.swing.JComponent;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.event.HyperlinkEvent;

import java.awt.BorderLayout;

import java.net.URL;
import java.net.CookieManager;
import java.net.CookieHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

public class Browser extends JFrame {
    public JEditorPane editorPane;
    public HTMLEditorKit editorKit;
    public HTMLDocument document;
    private NavigatorBar navigatorBar;
    
    static {
        CookieHandler.setDefault(new CookieManager());
    }
    
    public Browser() {
        super();

        this.setTitle("Web Browser");

        JComponent root = this.getRootPane();
        root.setLayout(new BorderLayout());
        
        this.navigatorBar = new NavigatorBar();
        root.add(this.navigatorBar, BorderLayout.NORTH);
        
        this.editorPane = new JEditorPane();
        this.editorPane.setContentType("text/html");
        this.editorPane.setEditable(false);
        
        this.editorKit = (HTMLEditorKit)this.editorPane.getEditorKit();
        this.editorKit.setAutoFormSubmission(true);
        
        this.editorPane.addHyperlinkListener(event -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                this.navigateTo(event.getURL());
            }
        });
        
        this.document = (HTMLDocument)this.editorPane.getDocument();
        
        JButton searchButton = this.navigatorBar.getSearchButton();
        searchButton.addActionListener(event -> {
            try {
                this.navigateTo(this.navigatorBar.getURL());
            } catch (URISyntaxException | MalformedURLException err) {
                String pageHtml = String.format("<html><strong>URI/URL Syntax Error</strong><br/><p>%s</p></html>", err.toString());
                this.editorPane.setText(pageHtml);
            }
        });

        JScrollPane scrollPane = new JScrollPane(this.editorPane);
        root.add(scrollPane, BorderLayout.CENTER);
        
        this.setVisible(true);
        this.setSize(800, 600);
    }
    
    public void navigateTo(URL url) {
        try {
            this.editorPane.setPage(url);
            this.navigatorBar.setURL(url);
        } catch (IOException err) {
            String pageHtml = String.format("<html><strong>Connection Error</strong><br/><p>%s</p></html>", err.toString());
            this.editorPane.setText(pageHtml);
        }
    }
}
