/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
	
	public Runnable getCloseListener();
	public void setCloseListener(Runnable listener);
	
	public Runnable getPageChangeListener();
	public void setPageChangeListener(Runnable listener);

	public String getTitle();
	public String getUrlBarText();
}
