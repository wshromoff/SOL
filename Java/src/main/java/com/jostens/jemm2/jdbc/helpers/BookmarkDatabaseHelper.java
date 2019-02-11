package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;

public class BookmarkDatabaseHelper
{

	/**
	 * Get the current count of bookmarks
	 * @throws SQLException 
	 */
	public int getBookmarkCount(Connection c) throws SQLException
	{
		int bookmarkCount = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_BOOKMARK_COUNT);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		while (rs.next())
		{
			bookmarkCount = rs.getInt(1);
		}

		rs.close();
		statement.close();
		
		return bookmarkCount;
	}

	/**
	 * Add a bookmark
	 * @throws SQLException 
	 */
	public void addBookmark(Connection c, String documentID) throws SQLException
	{
		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_BOOKMARK);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setString(1, documentID);
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		c.commit();
		
	}

	/**
	 * Is a document bookmarked
	 * @throws SQLException 
	 */
	public boolean isDocumentBookmarked(Connection c, String documentID) throws SQLException
	{
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_BOOKMARK_DOCUMENTID);
		selectStmt = selectStmt.replace("[DOCUMENTID]", documentID);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean isBookmarked = false;
		if (rs.next())
		{
			isBookmarked = true;
		}

		rs.close();
		statement.close();

		return isBookmarked;
	}

	/**
	 * Delete a bookmark
	 * @throws SQLException 
	 */
	public void deleteBookmark(Connection c, String documentID) throws SQLException
	{
		// Try to delete the ID from the customer table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_BOOKMARK);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setString(1, documentID);
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		c.commit();

	}

}
