package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;

public class BookmarkDatabaseHelperTest
{
	private static Connection c = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}

//	@Test
	public void testGetBookmarkCount() throws SQLException
	{
		BookmarkDatabaseHelper helper = new BookmarkDatabaseHelper();
		int count = helper.getBookmarkCount(c);
		System.out.println("Bookmark Count = " + count);
	}

//	@Test
	public void testAddBookmark() throws SQLException
	{
		BookmarkDatabaseHelper helper = new BookmarkDatabaseHelper();
		helper.addBookmark(c, "BR_12345");
	}

//	@Test
	public void testIsBookmarked() throws SQLException
	{
		BookmarkDatabaseHelper helper = new BookmarkDatabaseHelper();
		boolean bookmarked = helper.isDocumentBookmarked(c, "BR_12345");
		System.out.println("Bookmarked = " + bookmarked);
	}

//	@Test
	public void testDeleteBookmark() throws SQLException
	{
		BookmarkDatabaseHelper helper = new BookmarkDatabaseHelper();
		helper.deleteBookmark(c, "BR_12345");
	}

	@Test
	public void testGetAllBookmarks() throws SQLException
	{
		BookmarkDatabaseHelper helper = new BookmarkDatabaseHelper();
		List<String> documentIDs = helper.getAllBookmarks(c);
		System.out.println("Bookmarks = " + documentIDs);
	}

}
