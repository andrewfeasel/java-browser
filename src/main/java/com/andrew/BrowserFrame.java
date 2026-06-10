
 
/**
 *
 * @author andrewf
 */

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.Element;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;

import java.net.URL;
import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.CookiePolicy;

import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.IOException;

public class BrowserFrame extends JFrame {
    protected static class Tab extends JComponent {
        protected JTextPane textPane;
        protected HTMLEditorKit editorKit;
        protected HTMLDocument document;
        protected NavigatorBar navigatorBar;
        protected Runnable pageChangeListener;
        protected String pageTitle;
        
        protected Tab() {
            super();
            this.setName("New Tab");
            this.setLayout(new BorderLayout());

            this.navigatorBar = new NavigatorBar();
            this.add(this.navigatorBar, BorderLayout.NORTH);
        
            this.textPane = new JTextPane();
            this.textPane.setEditable(false);
        
            this.editorKit = new HTMLEditorKit();
            this.editorKit.setAutoFormSubmission(true);
            this.textPane.setEditorKit(this.editorKit);
        
            this.textPane.addHyperlinkListener(event -> {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    this.navigateTo(event.getURL());
                }
            });
        
            this.document = (HTMLDocument)this.textPane.getDocument();
        
            JButton searchButton = this.navigatorBar.getSearchButton();
            searchButton.addActionListener(event -> {
                try {
                    this.navigateTo(this.navigatorBar.getURL());
                } catch (URISyntaxException | MalformedURLException err) {
                    String pageHtml = String.format("<html><strong>URI/URL Syntax Error</strong><br/><p>%s</p></html>", err.toString());
                    this.textPane.setText(pageHtml);
                }
            });
        
            JScrollPane scrollPane = new JScrollPane(this.textPane);
            this.add(scrollPane, BorderLayout.CENTER);
        }
    
        public void navigateTo(URL url) {
            try {
                this.textPane.setPage(url);
                this.navigatorBar.setURL(url);
                try {
                    HTMLDocument.Iterator it = this.document.getIterator(HTML.Tag.TITLE);
                    int titleStart = it.getStartOffset();
                    this.pageTitle = this.document.getText(titleStart, it.getEndOffset() - titleStart);
                    this.pageChangeListener.run();
                } catch (BadLocationException | NullPointerException error) {
                    this.pageTitle = "Unnamed Tab";
                    this.pageChangeListener.run();
                }
            } catch (IOException err) {
                String pageHtml = String.format("<html><strong>Connection Error</strong><br/><p>%s</p></html>", err.toString());
                this.textPane.setText(pageHtml);
            }
        }
    }
    
    private JToolBar toolBar;
    private JTabbedPane tabbedPane;
    static {
        CookieManager browserCookieManager = new CookieManager();
        browserCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(browserCookieManager);
    }
    
    public BrowserFrame() {
        super();

        this.setTitle("Web Browser");

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
                that.removeTab((Tab)that.getTabbedPane().getSelectedComponent());
            }
        };
        this.toolBar.add(closeTabAction);  
        root.add(this.toolBar, BorderLayout.NORTH);
        
        this.setVisible(true);
        this.setSize(800, 600);
    }
    
    public JTabbedPane getTabbedPane() {
        return this.tabbedPane;
    }
    
    public Tab newTab() {
        Tab tab = new Tab();
        tab.pageChangeListener = () -> {
            int tabIndex = this.tabbedPane.indexOfComponent(tab);
            this.tabbedPane.setTitleAt(tabIndex, tab.pageTitle);
        }; 
        this.tabbedPane.add(tab);
        
        return tab;
    }
    
    public void removeTab(Tab tab) {
        this.tabbedPane.remove(tab);
    }
}
