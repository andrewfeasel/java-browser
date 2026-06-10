 

//import com.andrew.BrowserFrame;
import javax.swing.SwingUtilities;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

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
            try {
                BrowserFrame.Tab firstTab = myBrowser.newTab();
                firstTab.navigateTo(new URI("http://info.cern.ch").toURL());
            } catch (URISyntaxException | MalformedURLException err) {
                err.printStackTrace();
            }
        });
    }
    
}
