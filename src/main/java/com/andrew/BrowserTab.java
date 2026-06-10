/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.andrew;

/**
 *
 * @author andrew
 */

import javax.swing.JComponent;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTML;

import java.net.URL;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserTab extends JComponent implements Navigator {
	private JTextField urlField;
	private JTextPane textPane;
	
	private String pageTitle;
	private Runnable pageChangeListener;
	
	private Runnable closeListener;
	
	private HTMLEditorKit editorKit;
	private HTMLDocument document;
	
	public BrowserTab() {
		super();
		this.setLayout(new BorderLayout());
		
		this.urlField = new JTextField(24);
    this.add(this.urlField, BorderLayout.NORTH);	
		
		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		
		this.editorKit = new HTMLEditorKit();
		this.editorKit.setAutoFormSubmission(true);
		this.textPane.setEditorKit(this.editorKit);

		this.textPane.addHyperlinkListener(event -> {
			if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				this.setPage(event.getURL());
      }
		});
		
		this.document = (HTMLDocument)this.textPane.getDocument();
		
		JScrollPane scrollPane = new JScrollPane(this.textPane);
    this.add(scrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public URL getPage() {
		return this.textPane.getPage();
	}
	
	@Override
	public void setPage(URL newPage) {
		this.urlField.setText(newPage.toString());
		try {
			this.textPane.setPage(newPage);
      try {
				HTMLDocument.Iterator it = this.document.getIterator(HTML.Tag.TITLE);
				int titleStart = it.getStartOffset();
        this.pageTitle = this.document.getText(titleStart, it.getEndOffset() - titleStart);
        this.pageChangeListener.run();
      } catch (BadLocationException error) {
        this.pageTitle = "Unnamed Tab";
        this.pageChangeListener.run();
      }
    } catch (IOException err) {
			String errorText = String.format("<html><strong>Connection Error</strong><br/><p>%s</p></html>", err.toString());
			this.textPane.setText(errorText);
		}
	}
	
	@Override
	public void setPage(String newPage) {
		try {
			URL newUrl = new URI(newPage).toURL();
			this.setPage(newUrl);
		} catch (URISyntaxException | MalformedURLException err) {
			String errorText = String.format("<html><strong>URL Format Error</strong><br/><p>%s</p></html>", err.toString());
			this.textPane.setText(errorText);
		}
	}
	
	@Override
	public String getTitle() {
		return this.pageTitle;
	}
	
	@Override
	public void setPageText(String pageText) {
		this.textPane.setText(pageText);
	}
	
	@Override
	public String getUrlBarText() {
		return this.urlField.getText();
	}
	
	@Override
	public Runnable getPageChangeListener() {
		return this.pageChangeListener;
	}
	
	@Override
	public void setPageChangeListener(Runnable listener) {
		this.pageChangeListener = listener;
	}
	
	@Override
	public Runnable getCloseListener() {
		return this.closeListener;
	}
	
	@Override
	public void setCloseListener(Runnable listener) {
		this.closeListener = listener;
	}
	
	@Override
	public void close() {
		this.closeListener.run();
	}
}
