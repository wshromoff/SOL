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

	// doGet is what makes the search page look the same as last time user was on the page.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// The SOLR active query has how it appeared when the user was last on it.
//		System.out.println("Inside Search Get Servlet");
		
		SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
		solrQuery.setHitCountOnly(true);
		solrQuery.performQuery();		
		String searchHTML = getBuiltOutTemplate(solrQuery);
		

//		System.out.println("-->" + searchHTML);
		response.getWriter().append(solrQuery.getHitCount() + "," + searchHTML);

	}

	// doPost is what performs the query
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		System.out.println("Inside Search Post Servlet");

		// Get form parameters
		String query = request.getParameter("query");
		String document = request.getParameter("document");
		String newSearch = request.getParameter("newSearch");

		// Display values from form
//		System.out.println("query=" + query);
//		System.out.println("document=" + document);
//		System.out.println("New Search=" + newSearch);
		
		if ("true".equals(newSearch))
		{
			SOLRQuery.newQuery();
			
			SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
			String searchHTML = getBuiltOutTemplate(solrQuery);
			
			response.getWriter().append(0 + "," + searchHTML);
			return;
		}

		SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
		solrQuery.setDocument(document);		// Must set document type first
		solrQuery.setQuery(query);
		solrQuery.setHitCountOnly(true);
		solrQuery.performQuery();		

		response.getWriter().append(solrQuery.getHitCount() + ",");

	}

	/**
	 * Get the current query object and update the template HTML based on that's objects values
	 */
	private String getBuiltOutTemplate(SOLRQuery solrQuery)
	{

		// Get the HTML template and replace marker location with resource HTML based on last query
		String searchHTML = HTMLHelper.getTemplateHTML("/Search.html");		
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
		if (solrQuery.isCustomerPackageSearch())
		{
			searchHTML = searchHTML.replace("[CUSTOMER_PACKAGE_CHECKED]", "checked=\"checked\""); 			
		}
		else
		{
			searchHTML = searchHTML.replace("[CUSTOMER_PACKAGE_CHECKED]", ""); 
		}
		return searchHTML;
	}
}
