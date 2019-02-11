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
import com.jostens.jemm2.jdbc.helpers.BookmarkDatabaseHelper;

@WebServlet("/bookmark")
public class BookmarkServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Get form parameters
		String documentID = request.getParameter("document");
		System.out.println("INSIDE BOOKMARK SERVLET: " + documentID);
		
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		Connection c = ConnectionHelper.getJEMM2Connection();

		int bookmarkCount = 0;
		String buttonText = "";
		
		BookmarkDatabaseHelper dbHelper = new BookmarkDatabaseHelper();
		try
		{
			boolean isDocumentBookmarked = dbHelper.isDocumentBookmarked(c, documentID);
			if (isDocumentBookmarked)
			{
				// Document is bookmarked, so like a toggle remove the bookmark
				dbHelper.deleteBookmark(c, documentID);
				buttonText = "Bookmark Asset";
			}
			else
			{
				dbHelper.addBookmark(c, documentID);
				buttonText = "Remove Bookmark";
			}
			bookmarkCount = dbHelper.getBookmarkCount(c);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		ConnectionHelper.closeConnection(c);

		response.getWriter().append(bookmarkCount + "," + buttonText);


	}

}
