package com.waspr.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.ActionsDatabaseHelper;
import com.jostens.jemm2.solr.web.DocumentCounts;
import com.jostens.jemm2.solr.web.SOLRQuery;

/**
 * Servlet implementation for tab totals information
 */
@WebServlet("/tabTotals")
public class TabsTotals extends HttpServlet
{
	private static final long serialVersionUID = 1L;
//	private static long searchResultsValue = 0;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		System.out.println("Inside tab totals Servlet");
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		Connection c = ConnectionHelper.getJEMM2Connection();

		
		// Get the current SOLR document counts and take the delta count as documents changed today
		DocumentCounts helper = new DocumentCounts();
		helper.generateCounts();
		long dashboardValue = helper.getDeltaCount();

		// Get the most recent query count
		long searchResultsValue = SOLRQuery.getActiveQuery().getHitCount();

		// Get bookmark count
		ActionsDatabaseHelper bmHelper = new ActionsDatabaseHelper();
		int bookmarkCount = 0;
		int downloadCount = 0;
		try
		{
			bookmarkCount = bmHelper.getActionCount(c, ActionsDatabaseHelper.BOOKMARK);
			downloadCount = bmHelper.getActionCount(c, ActionsDatabaseHelper.DOWNLOAD);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		ConnectionHelper.closeConnection(c);

		response.getWriter().append(dashboardValue + "," + searchResultsValue + "," + bookmarkCount + "," + downloadCount);

	}

}
