package com.waspr.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.brand.helpers.LogfileHelper;
import com.jostens.jemm2.solr.web.SOLRQuery;
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
//		System.out.println("Inside Logs Servlet");
		
		LogfileHelper helper = LogfileHelper.getActiveLogHelper();
		String logfileHTML = getBuiltOutTemplate(helper);

		response.getWriter().append(logfileHTML);

	}
	
	/**
	 * Build out the logFileHTML to be returned to the user
	 * @throws IOException 
	 */
	private String getBuiltOutTemplate(LogfileHelper helper) throws IOException
	{
		String logfileHTML = HTMLHelper.getTemplateHTML("/LogFile.html");

		// Set the selected log radio buttons
		if (helper.isAutomationLog())
		{
			logfileHTML = logfileHTML.replace("[AUTOMATION_CHECKED]", "checked=\"checked\""); 			
			logfileHTML = logfileHTML.replace("[EXCEPTION_CHECKED]", ""); 
		}
		else
		{
			logfileHTML = logfileHTML.replace("[AUTOMATION_CHECKED]", ""); 			
			logfileHTML = logfileHTML.replace("[EXCEPTION_CHECKED]", "checked=\"checked\""); 
		}

		
		StringBuffer sb = new StringBuffer();
		List<String> logLines = helper.tailSelectedLog();
		for (String logLine : logLines)
		{
			sb.append(logLine + "<BR>");
		}
		logfileHTML = logfileHTML.replace("[LOGTEXT]", sb.toString());
		return logfileHTML;
	}
	
	// doPost is what performs the query
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		System.out.println("Inside Logs Post Servlet");

		// Get form parameters
		String log = request.getParameter("log");

		// Display values from form
//		System.out.println("log=" + log);
		
		// Set the log file value into helper
		LogfileHelper helper = LogfileHelper.getActiveLogHelper();
		helper.setLogFile(log);

		String logfileHTML = getBuiltOutTemplate(helper);

		response.getWriter().append(logfileHTML);

	}

}
