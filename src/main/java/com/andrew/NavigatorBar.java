/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.andrew;

/**
 *
 * @author andrewf
 */

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.FlowLayout;

import java.net.URI;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class NavigatorBar extends JComponent {
    private JButton searchButton;
    private JTextField textField;
    
    public NavigatorBar() {
        super();
        
        this.setLayout(new FlowLayout());        
        this.textField = new JTextField(24);
        this.add(this.textField);
        
        this.searchButton = new JButton("Search");
        this.add(this.searchButton);
    }
    
    public JButton getSearchButton() {
        return this.searchButton;
    }
    
    public URL getURL() throws URISyntaxException, MalformedURLException {
        return new URI(this.textField.getText()).toURL();
    }
    
    public void setURL(URL url) {
        this.textField.setText(url.toString());
    }
}