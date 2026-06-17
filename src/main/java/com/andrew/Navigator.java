package com.andrew;

/**
 *
 * @author andrew
 */

import java.net.URL;

public interface Navigator {
	public void close();
	
	public void setPageText(String pageText);
	public URL getPage();
	public void setPage(URL newPage);
	public void setPage(String newPage);
	public String getUrlBarText();
}
