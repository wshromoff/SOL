package com.waspr.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.brand.helpers.LogfileHelper;
import com.jostens.jemm2.web.HTMLHelper;

@WebServlet("/logs")
public class LogsServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Logs Servlet");
		
		String logfileHTML = HTMLHelper.getTemplateHTML("/LogFile.html");

		
		StringBuffer sb = new StringBuffer();
		LogfileHelper helper = LogfileHelper.getActiveLogHelper();
		List<String> logLines = helper.tailSelectedLog();
		for (String logLine : logLines)
		{
			sb.append(logLine + "<BR>");
		}
		logfileHTML = logfileHTML.replace("[LOGTEXT]", sb.toString());

		response.getWriter().append(logfileHTML);

	}
}
