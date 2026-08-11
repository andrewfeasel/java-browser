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
import java.net.URLConnection;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;

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
		this.setLayout(new BorderLayout());
		
		this.tabbedPane = new JTabbedPane();
		this.add(this.tabbedPane, BorderLayout.CENTER);
		
		this.toolBar = new JToolBar();
		var that = this;
		
		this.toolBar.add(new AbstractAction("Close") {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		this.toolBar.add(new AbstractAction("Download Page") {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save As");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(false);


				int fileSelectionStatus = fileChooser.showOpenDialog(that);
				if (fileSelectionStatus != JFileChooser.APPROVE_OPTION) {
					return;
				}

				File fileOutput = fileChooser.getSelectedFile();

				try {
					URLConnection inputConnection = that.getSelectedTab().getUrl().openConnection();
					inputConnection.setDoInput(true);
					inputConnection.setDoOutput(false);
					
					try (
						InputStream remoteInput = inputConnection.getInputStream();
						BufferedInputStream remoteBufferedInput = new BufferedInputStream(remoteInput);
						FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
						BufferedOutputStream bufferedFileOutputStream = new BufferedOutputStream(fileOutputStream);
					) {
						remoteBufferedInput.transferTo(bufferedFileOutputStream);
					}
				} catch (IOException err) {
					err.printStackTrace();
				}
			}
		});

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
		
		this.add(this.toolBar, BorderLayout.NORTH);
		this.setVisible(true);
		this.setSize(900, 600);
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

	public BrowserTab getSelectedTab() {
		return (BrowserTab)this.tabbedPane.getSelectedComponent();
	}
}
