package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.solr.web.SOLRQuery;
import com.jostens.jemm2.web.HTMLHelper;

@WebServlet("/searchResults")
public class SearchResultsServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String dashboardHTML = HTMLHelper.getTemplateHTML("/SearchResults.html");

		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setHitCountOnly(false);
		query.performQuery();
		
		System.out.println("IDs=" + query.getResultIDs());
		// Build up all the search result document TD cells
		StringBuffer sb = new StringBuffer();
		for (String docID : query.getResultIDs())
		{
			String resultDocument = HTMLHelper.getTemplateHTML("/SearchResultDocument.html");
			resultDocument = resultDocument.replace("[DOCUMENT_ID]", docID);
			sb.append(resultDocument);
			
		}

		dashboardHTML = dashboardHTML.replace("[RESULT_DOCUMENTS]", sb.toString());
		response.getWriter().append(dashboardHTML);

	}

}
