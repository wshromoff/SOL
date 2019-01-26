package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.solr.web.DocumentCounts;

/**
 * Servlet implementation for tab totals information
 */
@WebServlet("/tabTotals")
public class TabsTotals extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static long searchResultsValue = 0;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long dashboardValue = 0;
//		long searchResultsValue = 0;
		
		System.out.println("Inside tab totals Servlet");
		DocumentCounts helper = new DocumentCounts();
		helper.generateCounts();
//		response.getWriter().append("SUMMARY " + helper.getDesignCount());
		
		dashboardValue = helper.getDeltaCount();
		response.getWriter().append(dashboardValue + "," + searchResultsValue);
		searchResultsValue++;

	}

}
