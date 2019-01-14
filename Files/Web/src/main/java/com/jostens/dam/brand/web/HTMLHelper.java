package com.jostens.dam.brand.web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.jostens.dam.shared.jdbc.Statements;

/**
 * Helper object to work with HTML.  There can be help with getting a template file
 * or caching completed HTML for display back to the requester.  
 * 
 * This object should be comprised of static methods.
 */
public class HTMLHelper
{
	// Static cache of HTML that is updated as threads complete building HTML from a template
	private static Map<String, String> htmlCache = new HashMap<String, String>();

	public static String getTemplateHTML(String templateFilePath)
	{
		StringBuffer sb = new StringBuffer();

		try
		{
			InputStream istream = Statements.class.getResourceAsStream(templateFilePath);
			DataInputStream in = new DataInputStream(istream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{
				if (strLine == null || strLine.length() == 0 || strLine.startsWith("-"))
				{	// Ignore empty lines (length 0) and comment lines (start with dash)
					continue;
				}
//				System.out.println("> " + strLine);
				
				sb.append(strLine);
			}
			
			br.close();
			in.close();
			istream.close();
		} catch (IOException e)
		{
			System.out.println("IOException while retrieving base display: " + templateFilePath);
		}

		return sb.toString();
	}

	/**
	 * Cache the provided HTML using the name provided
	 */
	public static void cacheHTML(String cacheName, String html)
	{
		synchronized (HTMLHelper.htmlCache)
		{
			HTMLHelper.htmlCache.put(cacheName, html);
		}
	}

	/**
	 * Return the Cache'd HTML for the provided name
	 */
	public static String getCachedHTML(String cacheName)
	{
		synchronized (HTMLHelper.htmlCache)
		{
			String cachedHtml = HTMLHelper.htmlCache.get(cacheName);
			if (cachedHtml == null)
			{
				cachedHtml = "";
			}
			return cachedHtml;
		}
	}

}
