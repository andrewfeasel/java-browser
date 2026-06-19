package com.andrew;

/**
 *
 * @author andrew
 */

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.Dimension;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTML;

import java.util.LinkedList;
import java.util.ListIterator;

import java.net.URL;
import java.net.URI;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class BrowserTab extends JComponent {
	private JTextPane textPane;
	private HTMLEditorKit editorKit;
	private HTMLDocument document;

	private LinkedList<URL> urlList;
	private ListIterator<URL> urlIterator;

	private JButton reloadButton;
	private JButton backButton;
	private JButton nextButton;
	private JButton searchButton;
	private JTextField urlField;

	public BrowserTab() {
		super();
		this.setLayout(new BorderLayout());
		
		this.urlList = new LinkedList<URL>();
		this.urlIterator = this.urlList.listIterator();

		JPanel navigatorBar = new JPanel();
		navigatorBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.backButton = new JButton("Back");
		this.backButton.addActionListener((event) -> {
			this.setPage(this.urlIterator.previous(), false);
		});
		navigatorBar.add(this.backButton);

		this.reloadButton = new JButton("Reload");
		this.reloadButton.addActionListener((event) -> {
			this.setPage(this.textPane.getPage(), false);
		});
		navigatorBar.add(this.reloadButton);

		this.nextButton = new JButton("Next");
		this.nextButton.addActionListener((event) -> {
			this.setPage(this.urlIterator.next(), false);
		});
		navigatorBar.add(this.nextButton);

		this.searchButton = new JButton("Search");
		this.searchButton.addActionListener((event) -> {
			this.setPage(this.urlField.getText(), true);
		});
		navigatorBar.add(this.searchButton);
		
		// this is where the fun begins
		this.urlField = new JTextField(48);

		// set urlField height to reloadButton height
		Dimension newUrlFieldDimension = new Dimension(this.urlField.getPreferredSize().width, this.reloadButton.getPreferredSize().height);
		this.urlField.setPreferredSize(newUrlFieldDimension);

		// fix pixel alignment
		this.urlField.setLocation(this.urlField.getX(), this.urlField.getY() + 2);
		this.urlField.repaint();

		// urlField is only slightly ugly now
		navigatorBar.add(this.urlField);

		this.add(navigatorBar, BorderLayout.NORTH);

		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		
		this.editorKit = new HTMLEditorKit();
		this.editorKit.setAutoFormSubmission(true);
		this.textPane.setEditorKit(this.editorKit);

		this.textPane.addHyperlinkListener(event -> {
			if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				this.setPage(event.getURL(), true);
			}
		});
		
		this.document = (HTMLDocument)this.textPane.getDocument();
		
		this.textPane.addPropertyChangeListener("page", event -> {
			this.document = (HTMLDocument)this.textPane.getDocument();
			String pageTitle = (String)this.document.getProperty(HTMLDocument.TitleProperty);
			this.firePropertyChange("title", null, pageTitle);
		});
		
		JScrollPane scrollPane = new JScrollPane(this.textPane);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public void setUrl(URL newUrl) {
		this.setPage(newUrl, true);
	}
	public void setUrl(String newUrl) {
		this.setPage(newUrl, true);
	}
	public URL getUrl() {
		return this.textPane.getPage();
	}

	private void setPage(URL newPage, boolean shouldUpdate) {
		this.urlField.setText(newPage.toString());
		try {
			this.textPane.setPage(newPage);
		} catch (IOException err) {
			String errorText = String.format("<html><strong>Connection Error</strong><br/><p>%s</p></html>", err.toString());
			this.textPane.setText(errorText);
		}

		if (shouldUpdate) {
			this.urlIterator.forEachRemaining((URL nextUrl) -> {
				this.urlIterator.remove();
			});
			this.urlIterator.add(newPage);
		}

		this.nextButton.setEnabled(this.urlIterator.hasNext());
		this.backButton.setEnabled(this.urlIterator.hasPrevious());
	}
	
	private void setPage(String newPage, boolean shouldUpdate) {
		try {
			URL newUrl = new URI(newPage).toURL();
			this.setPage(newUrl, shouldUpdate);
		} catch (URISyntaxException | MalformedURLException err) {
			String errorText = String.format("<html><strong>URL Format Error</strong><br/><p>%s</p></html>", err.toString());
			this.textPane.setText(errorText);
		}
	}

	public void close() {
		this.firePropertyChange("closed", false, true);
	}
}
