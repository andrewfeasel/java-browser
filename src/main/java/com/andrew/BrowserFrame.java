package com.andrew;


 
/**
 *
 * @author andrewf
 */

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;

import java.net.URI;
import java.net.URL;

import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.CookiePolicy;

import java.net.URISyntaxException;
import java.net.MalformedURLException;

public class BrowserFrame extends JFrame {
	static {
		CookieManager browserCookieManager = new CookieManager();
		browserCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(browserCookieManager);
	}

	private JToolBar toolBar;
	private JTabbedPane tabbedPane;
	
	public BrowserFrame() {
		super();
		
		this.setTitle("Web Browser");
		this.setDefaultCloseOperation(super.EXIT_ON_CLOSE);

		JComponent root = this.getRootPane();
		root.setLayout(new BorderLayout());
		
		this.tabbedPane = new JTabbedPane();
		root.add(this.tabbedPane, BorderLayout.CENTER);
		
		this.toolBar = new JToolBar();
		var that = this;
				
		AbstractAction newTabAction = new AbstractAction("New Tab") {
			@Override
			public void actionPerformed(ActionEvent event) {
				that.newTab();
			}
		};
		this.toolBar.add(newTabAction);

		AbstractAction closeTabAction = new AbstractAction("Close Tab") {
			@Override
			public void actionPerformed(ActionEvent event) {
				BrowserTab currentTab = (BrowserTab)that.tabbedPane.getSelectedComponent();
				currentTab.close();
			}
		};
		this.toolBar.add(closeTabAction);
		
		root.add(this.toolBar, BorderLayout.NORTH);
		this.setVisible(true);
		this.setSize(800, 600);
	}
	
	public BrowserTab newTab() {
		BrowserTab tab = new BrowserTab();
		tab.addPropertyChangeListener("title", (PropertyChangeEvent event) -> {
			String newTitle = (String)event.getNewValue();
			if (newTitle == null)
				this.tabbedPane.setTitleAt(this.tabbedPane.indexOfComponent(tab), "Unnamed Tab");
			else
				this.tabbedPane.setTitleAt(this.tabbedPane.indexOfComponent(tab), newTitle);
		});
		tab.addPropertyChangeListener("closed", (PropertyChangeEvent event) -> {
			this.tabbedPane.remove(tab);
		});
		this.tabbedPane.addTab("Unnamed Tab", tab);
		return tab;
	}
}
