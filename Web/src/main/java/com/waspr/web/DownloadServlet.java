package com.waspr.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.ActionsDatabaseHelper;
import com.jostens.jemm2.web.HTMLHelper;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("INSIDE Download SERVLET (GET): ");

//		String dashboardHTML = HTMLHelper.getTemplateHTML("/SearchResults.html");
//
//		Jemm2Statements statements = new Jemm2Statements();
//		statements.initializeStatements();
//		Connection c = ConnectionHelper.getJEMM2Connection();
//
//		ActionsDatabaseHelper dbHelper = new ActionsDatabaseHelper();
//
//		int bookmarkCount = 0;
//		List<String> documentIDs = new ArrayList<String>();
//		try
//		{
//			documentIDs = dbHelper.getAllActions(c, ActionsDatabaseHelper.BOOKMARK);
//			bookmarkCount = dbHelper.getActionCount(c, ActionsDatabaseHelper.BOOKMARK);
//		} catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		
////		System.out.println("IDs=" + documentIDs);
//		// Build up all the search result document TD cells
//		StringBuffer sb = new StringBuffer();
//		for (String docID : documentIDs)
//		{
//			String documentType = getDocumentType(docID);
//			String resultDocument = HTMLHelper.getTemplateHTML("/SearchResultDocument.html");
//			resultDocument = resultDocument.replace("[DOCUMENT_ID]", docID);
//			resultDocument = resultDocument.replace("[DOCUMENT_TYPE]", documentType);
//			sb.append(resultDocument);
//			
//		}
//
//		dashboardHTML = dashboardHTML.replace("[RESULT_DOCUMENTS]", sb.toString());
//
//		ConnectionHelper.closeConnection(c);
//		
//		if (bookmarkCount == 0)
//		{
//			dashboardHTML = "";
//		}
//
//		System.out.println("DashboardHTML:" + dashboardHTML);
		response.getWriter().append(0 + "," + "HELLO");
	}
	
	private String getDocumentType(String docID)
	{
		if (docID.startsWith("PR"))
		{
			return "1b";
		}
		
		return "ba";
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Get form parameters
		String documentID = request.getParameter("document");
		System.out.println("INSIDE Download SERVLET (POST): " + documentID);
		
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		Connection c = ConnectionHelper.getJEMM2Connection();

		int downloadCount = 0;
		String buttonText = "";
		
		ActionsDatabaseHelper dbHelper = new ActionsDatabaseHelper();
		try
		{
			boolean isDocumentDownload = dbHelper.isDocumentForAction(c, documentID, ActionsDatabaseHelper.DOWNLOAD);
			if (isDocumentDownload)
			{
				// Document is set for download, so like a toggle remove the bookmark
				dbHelper.deleteAction(c, documentID, ActionsDatabaseHelper.DOWNLOAD);
				buttonText = "Download Asset";
			}
			else
			{
				dbHelper.addAction(c, documentID, ActionsDatabaseHelper.DOWNLOAD);
				buttonText = "Remove Download";
			}
			downloadCount = dbHelper.getActionCount(c, ActionsDatabaseHelper.DOWNLOAD);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		ConnectionHelper.closeConnection(c);

		response.getWriter().append(downloadCount + "," + buttonText);


	}

}
