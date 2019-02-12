package com.waspr.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.ActionsDatabaseHelper;
import com.jostens.jemm2.jdbc.helpers.PackageDatabaseHelper;
import com.jostens.jemm2.pojo.Asset;
import com.jostens.jemm2.pojo.AssetPackage;
import com.jostens.jemm2.pojo.CustomerPackage;
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

		String downloadHTML = HTMLHelper.getTemplateHTML("/Downloads.html");

		String documentHTML = getDocumentHTML();
		int i = documentHTML.indexOf(",");
		String documentCount = documentHTML.substring(0, i);
		String documentText = documentHTML.substring(i+1);

//		Jemm2Statements statements = new Jemm2Statements();
//		statements.initializeStatements();
//		Connection c = ConnectionHelper.getJEMM2Connection();
//
////		downloadHTML = downloadHTML.replace("[BUTTON]", getDownloadButton(c, "PR_001438"));
//
//		// Get documentIDs marked for download
//		ActionsDatabaseHelper dbHelper = new ActionsDatabaseHelper();
//		StringBuffer sb = new StringBuffer();
//		int downloadCount = 0;
//		try
//		{
//			List<String> documentIDNamess = dbHelper.getAllActionsWithName(c, ActionsDatabaseHelper.DOWNLOAD);
//			// Now for all these documentIDs, build a StringBuffer of HTML using DownloadHTML for
//			// each document ID.  This StringBuffer will be placed into the Downloads.html
//			for (String documentIDName : documentIDNamess)
//			{
//				int i = documentIDName.indexOf("|");
//				String documentID = documentIDName.substring(0, i);
//				String name = documentIDName.substring(i + 1);
//				
//				String downloadDocumentHTML = HTMLHelper.getTemplateHTML("/DownloadDocument.html");
//				
//				downloadDocumentHTML = downloadDocumentHTML.replace("[TYPE]", getDocumentName(documentID));
//				downloadDocumentHTML = downloadDocumentHTML.replace("[NAME]", name);
//				downloadDocumentHTML = downloadDocumentHTML.replace("[BUTTON]", getDownloadButton(c, documentID));
//				downloadDocumentHTML = downloadDocumentHTML.replace("[DOCUMENT_ID]", documentID);
//
//				sb.append(downloadDocumentHTML.toString());
//			}
//			
//			downloadCount = dbHelper.getActionCount(c, ActionsDatabaseHelper.DOWNLOAD);
//
//		} catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		ConnectionHelper.closeConnection(c);

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
//		
//		if (bookmarkCount == 0)
//		{
//			dashboardHTML = "";
//		}
//
//		System.out.println("DashboardHTML:" + dashboardHTML);
		downloadHTML = downloadHTML.replace("[DOWNLOAD_DOCUMENTS]", documentText);
		

		response.getWriter().append(documentCount + "," + downloadHTML);
	}
	
	private String getDocumentType(String docID)
	{
		if (docID.startsWith("PR"))
		{
			return "1b";
		}
		
		return "ba";
	}

	private String getDocumentName(String docID)
	{
		if (docID.startsWith("PR"))
		{
			return "Part";
		}
		if (docID.startsWith("PK"))
		{
			return "Package";
		}
		
		return "Customer Package";
	}

	private String getDownloadButton(Connection c, String documentID)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<button class=\"remove\" id=\"downloadBTN\" onclick=\"removeDownload('" + documentID + "'); return false;\">");
		// Find button Text
		ActionsDatabaseHelper dbHelper = new ActionsDatabaseHelper();
		String buttonText = "";
		try
		{
			boolean isDocumentBookmarked = dbHelper.isDocumentForAction(c, documentID, ActionsDatabaseHelper.DOWNLOAD);
			if (isDocumentBookmarked)
			{
				buttonText = "Remove Download";
			}
			else
			{
				buttonText = "Download Asset";
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		sb.append(buttonText);
		sb.append("</button>");
//		System.out.println("BUTTON=" + sb.toString());
		return sb.toString();
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Get form parameters
		String documentID = request.getParameter("document");
		String source = request.getParameter("source");
		System.out.println("INSIDE Download SERVLET (POST): " + documentID + " : " + source);
		
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

		if ("remove".equals(source)) 
		{		// From remove download on downloads page
			String downloadHTML = HTMLHelper.getTemplateHTML("/Downloads.html");
	
			String documentHTML = getDocumentHTML();
			int i = documentHTML.indexOf(",");
			String documentCount = documentHTML.substring(0, i);
			String documentText = documentHTML.substring(i+1);
			downloadHTML = downloadHTML.replace("[DOWNLOAD_DOCUMENTS]", documentText);
			response.getWriter().append(documentCount + "," + downloadHTML);
		}
		else
		{
			// Search results page
			response.getWriter().append(downloadCount + "," + buttonText);
		}


	}

	/*
	 * Method to build the document HTML for the download page
	 */
	private String getDocumentHTML()
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		Connection c = ConnectionHelper.getJEMM2Connection();

//		downloadHTML = downloadHTML.replace("[BUTTON]", getDownloadButton(c, "PR_001438"));

		// Get documentIDs marked for download
		ActionsDatabaseHelper dbHelper = new ActionsDatabaseHelper();
		StringBuffer sb = new StringBuffer();
		int downloadCount = 0;
		try
		{
			List<String> documentIDNamess = dbHelper.getAllActionsWithName(c, ActionsDatabaseHelper.DOWNLOAD);
			// Now for all these documentIDs, build a StringBuffer of HTML using DownloadHTML for
			// each document ID.  This StringBuffer will be placed into the Downloads.html
			for (String documentIDName : documentIDNamess)
			{
				int i = documentIDName.indexOf("|");
				String documentID = documentIDName.substring(0, i);
				String name = documentIDName.substring(i + 1);
				
				String downloadDocumentHTML = HTMLHelper.getTemplateHTML("/DownloadDocument.html");
				
				downloadDocumentHTML = downloadDocumentHTML.replace("[TYPE]", getDocumentName(documentID));
				downloadDocumentHTML = downloadDocumentHTML.replace("[NAME]", name);
				downloadDocumentHTML = downloadDocumentHTML.replace("[BUTTON]", getDownloadButton(c, documentID));
				downloadDocumentHTML = downloadDocumentHTML.replace("[DOCUMENT_ID]", documentID);

				// Get the HTML for all the images this part or package contains
				String assetHTML = getAssetHTML(c, documentID);
				downloadDocumentHTML = downloadDocumentHTML.replace("[IMAGES]", assetHTML);
				
				sb.append(downloadDocumentHTML.toString());
			}
			
			downloadCount = dbHelper.getActionCount(c, ActionsDatabaseHelper.DOWNLOAD);

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		ConnectionHelper.closeConnection(c);
		return downloadCount + "," + sb.toString();
	}
	
	private String getAssetHTML(Connection c, String documentID) throws SQLException
	{
		StringBuffer sb = new StringBuffer();
		if (documentID.startsWith("PR"))
		{	// A part, so need a single document for 1b part
			String assetHTML = getAssetHTML(documentID, "1b");
			sb.append(assetHTML);
		}
		else if (documentID.startsWith("PK"))
		{
			// This is a package, so could have multiple assets
			PackageDatabaseHelper dbHelper = new PackageDatabaseHelper();
			AssetPackage aPackage = new AssetPackage();
			aPackage.setID(Integer.parseInt(documentID.substring(3)));
			dbHelper.getPackage(c, aPackage);
			System.out.println("ASSET COUNT = " + aPackage.getAssets().size());
			if (isAssetOfType(aPackage.getAssets(), "ba"))
			{
				sb.append(getAssetHTML(documentID, "ba"));
			}
			if (isAssetOfType(aPackage.getAssets(), "1b"))
			{
				sb.append(getAssetHTML(documentID, "1b"));
			}
			if (isAssetOfType(aPackage.getAssets(), "1g"))
			{
				sb.append(getAssetHTML(documentID, "1g"));
			}
			if (isAssetOfType(aPackage.getAssets(), "1s"))
			{
				sb.append(getAssetHTML(documentID, "1s"));
			}
		}
		else if (documentID.startsWith("CP"))
		{
			// This is a Customer package, so could have multiple assets
			PackageDatabaseHelper dbHelper = new PackageDatabaseHelper();
			CustomerPackage aPackage = new CustomerPackage();
			aPackage.setID(Integer.parseInt(documentID.substring(3)));
			dbHelper.getCustomerPackage(c, aPackage);
			System.out.println("ASSET COUNT = " + aPackage.getaPackage().getAssets().size());
			if (isAssetOfType(aPackage.getaPackage().getAssets(), "ba"))
			{
				sb.append(getAssetHTML(documentID, "ba"));
			}
			if (isAssetOfType(aPackage.getaPackage().getAssets(), "1b"))
			{
				sb.append(getAssetHTML(documentID, "1b"));
			}
			if (isAssetOfType(aPackage.getaPackage().getAssets(), "1g"))
			{
				sb.append(getAssetHTML(documentID, "1g"));
			}
			if (isAssetOfType(aPackage.getaPackage().getAssets(), "1s"))
			{
				sb.append(getAssetHTML(documentID, "1s"));
			}
		}

		return sb.toString();
	}
	
	private boolean isAssetOfType(List<Asset> assets, String type)
	{
		for (Asset asset : assets)
		{
			if (asset.getName().contains("_" + type))
			{
				return true;
			}
		}
		return false;
	}
	
	private String getAssetHTML(String documentID, String type)
	{
//		String documentHTML = "<label><input type=\"radio\" name=\"PR_000009\" onclick=\"imageClicked('PR_000009&type=1b')\">" + 
//				"	<img src=\"image?id=PR_000009&type=1b\" width=\"160px\" height=\"160px\"/></label>";
		String documentHTML = "<label><input type=\"radio\" name=\"[DOCUMENT_ID]\" onclick=\"imageClicked('[DOCUMENT_TYPE]')\">" + 
				"	<img src=\"image?id=[DOCUMENT_TYPE]\" width=\"160px\" height=\"160px\"/></label>";
		
		String documentType = documentID + "&type=" + type;
		
		documentHTML = documentHTML.replace("[DOCUMENT_ID]", documentID);
		documentHTML = documentHTML.replace("[DOCUMENT_TYPE]", documentType);
		
		return documentHTML;
	}
}
