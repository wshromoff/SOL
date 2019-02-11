package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.wassoftware.solr.ConnectToSolr;

public class ActionsDatabaseHelper
{
	// Static constants defining available actions
	public static String BOOKMARK = "Bookmark";
	public static String DOWNLOAD = "Download";

	/**
	 * Get the current count of specified Actions
	 * @throws SQLException 
	 */
	public int getActionCount(Connection c, String action) throws SQLException
	{
		int actionCount = 0;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_ACTION_COUNT);
		selectStmt = selectStmt.replace("[ACTION]", action);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		while (rs.next())
		{
			actionCount = rs.getInt(1);
		}

		rs.close();
		statement.close();
		
		return actionCount;
	}

	/**
	 * Add a Action
	 * @throws SQLException 
	 */
	public void addAction(Connection c, String documentID, String action) throws SQLException
	{
		// Using the documentID, find the name for that document
		String documentName = null;
		ConnectToSolr connect = new ConnectToSolr();
		try
		{
			HttpSolrClient solr = connect.makeConnection();
			SolrQuery query = new SolrQuery();
			query.set("q", "id:" + documentID);
			query.setFields("name");
			QueryResponse response = solr.query(query);
			SolrDocumentList docList = response.getResults();
			if (docList.getNumFound() == 1)
			{
				SolrDocument document = docList.get(0);
				documentName = (String)document.getFieldValue("name");
			}
			
			solr.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// Update the design and keywords for the design
		String insertStmt = Jemm2Statements.getStatement(Jemm2Statements.INSERT_ACTION);

		PreparedStatement preparedInsertStatment = c.prepareStatement(insertStmt.toString());
		// Populate the columns
		preparedInsertStatment.setString(1, documentID);
		preparedInsertStatment.setString(2, action);
		preparedInsertStatment.setString(3, documentName);
		preparedInsertStatment.executeUpdate();
		preparedInsertStatment.close();
		
		c.commit();
		
	}

	/**
	 * Is a document for action found
	 * @throws SQLException 
	 */
	public boolean isDocumentForAction(Connection c, String documentID, String action) throws SQLException
	{
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.IS_DOCUMENT_ACTION);
		selectStmt = selectStmt.replace("[DOCUMENTID]", documentID);
		selectStmt = selectStmt.replace("[ACTION]", action);
		
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
	public void deleteAction(Connection c, String documentID, String action) throws SQLException
	{
		// Try to delete the ID from the customer table
		String deleteStmt = Jemm2Statements.getStatement(Jemm2Statements.DELETE_ACTION);

		PreparedStatement preparedDeleteStatment = c.prepareStatement(deleteStmt);
		// Populate the columns
		preparedDeleteStatment.setString(1, documentID);
		preparedDeleteStatment.setString(2, action);
		preparedDeleteStatment.executeUpdate();
		preparedDeleteStatment.close();
		
		c.commit();

	}

	/**
	 * Return all document IDs for an action
	 * @throws SQLException 
	 */
	public List<String> getAllActions(Connection c, String action) throws SQLException
	{
		List<String> documentIDs = new ArrayList<String>();
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_ALL_ACTIONS);
		selectStmt = selectStmt.replace("[ACTION]", action);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		while (rs.next())
		{
			String documentID = rs.getString(1);
			documentIDs.add(documentID);
		}

		rs.close();
		statement.close();

		return documentIDs;
	}


}
