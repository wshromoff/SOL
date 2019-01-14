package com.jostens.dam.brand.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.dam.brand.common.BrandAutomationManager;
import com.jostens.dam.shared.common.MediaBinConnectionPool;

/**
 * This object is the servlet that handles /StatusAutomations requests
 */
@WebServlet("/StatusAutomations")
public class StatusAutomationsServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	/**
	 * Return status automation's page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		System.out.println("   ***** STATUS AUTOMATIONS SERVLET *****");
		PrintWriter out = response.getWriter();
		List<String> statusMessages = new ArrayList<String>();
		
		MediaBinConnectionPool connectionPool = new MediaBinConnectionPool();
		statusMessages.addAll(connectionPool.getStatusMessages());
		BrandAutomationManager automationMgr = new BrandAutomationManager();
		statusMessages.addAll(automationMgr.getStatusMessages());

		// Build for proper HTML formatting
		out.println("<HTML><BODY><PRE>");
		for (String msg : statusMessages)
		{
			out.println(msg);
		}
		out.println("</PRE></BODY></HTML>");
		out.flush();
		out.close();

	}

}
