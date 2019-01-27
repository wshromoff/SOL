package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.web.HTMLHelper;

@WebServlet("/search")
public class SearchServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Search Get Servlet");
		
		String query = request.getParameter("query");
		System.out.println("query=" + query);
		

		
		// Get the HTML template and replace marker location with resource HTML
		String searchdHTML = HTMLHelper.getTemplateHTML("/Search.html");
		
		response.getWriter().append(0 + "," + searchdHTML);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Search Post Servlet");
		
		String query = request.getParameter("query");
		System.out.println("query=" + query);

		String newSearch = request.getParameter("newSearch");
		System.out.println("New Search=" + newSearch);
		
		if ("true".equals(newSearch))
		{
			// Get the HTML template and replace marker location with resource HTML
			String searchdHTML = HTMLHelper.getTemplateHTML("/Search.html");
			
			response.getWriter().append(0 + "," + searchdHTML);
			return;
		}

		response.getWriter().append(46 + ",");

	}

}
