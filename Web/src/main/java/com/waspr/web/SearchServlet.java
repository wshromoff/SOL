package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.solr.web.SOLRQuery;
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
		String searchHTML = HTMLHelper.getTemplateHTML("/Search.html");
		
		SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
		searchHTML = searchHTML.replace("[QUERY]", solrQuery.getQuery()); 
		if (solrQuery.isPartSearch())
		{
			searchHTML = searchHTML.replace("[PART_CHECKED]", "checked=\"checked\""); 			
		}
		else
		{
			searchHTML = searchHTML.replace("[PART_CHECKED]", ""); 
		}
		if (solrQuery.isCustomerSearch())
		{
			searchHTML = searchHTML.replace("[CUSTOMER_CHECKED]", "checked=\"checked\""); 			
		}
		else
		{
			searchHTML = searchHTML.replace("[CUSTOMER_CHECKED]", ""); 
		}
		if (solrQuery.isPackageSearch())
		{
			searchHTML = searchHTML.replace("[PACKAGE_CHECKED]", "checked=\"checked\""); 			
		}
		else
		{
			searchHTML = searchHTML.replace("[PACKAGE_CHECKED]", ""); 
		}

//		System.out.println("-->" + searchHTML);
		response.getWriter().append(solrQuery.getResults() + "," + searchHTML);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Search Post Servlet");
		
		String query = request.getParameter("query");
		System.out.println("query=" + query);
		String document = request.getParameter("document");
		System.out.println("document=" + document);

		String newSearch = request.getParameter("newSearch");
		System.out.println("New Search=" + newSearch);
		
		if ("true".equals(newSearch))
		{
			SOLRQuery.newQuery();
			
			// Get the HTML template and replace marker location with resource HTML
			String searchHTML = HTMLHelper.getTemplateHTML("/Search.html");
			searchHTML = searchHTML.replace("[QUERY]", ""); 
			searchHTML = searchHTML.replace("[PART_CHECKED]", "checked=\"checked\""); 
			searchHTML = searchHTML.replace("[PACKAGE_CHECKED]", ""); 
			searchHTML = searchHTML.replace("[CUSTOMER_CHECKED]", ""); 
			
			response.getWriter().append(0 + "," + searchHTML);
			return;
		}

		SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
		solrQuery.setQuery(query);
		solrQuery.setDocument(document);
		solrQuery.performQuery(true);		

		response.getWriter().append(solrQuery.getResults() + ",");

	}

}
