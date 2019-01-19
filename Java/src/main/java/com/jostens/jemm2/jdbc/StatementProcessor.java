package com.jostens.jemm2.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A wrapper for executing a SQL statement.  This holds all the objects needed
 * to execute a statement, provides access to the result set and a method to close everything.
 * 
 * During the constructor, the statement is executed and all exceptions are thrown.
 */
public class StatementProcessor
{
	private Statement statement = null;
	private ResultSet resultSet = null;

	public StatementProcessor(Connection c, String sqlStatement) throws Exception
	{
		Statement statement = c.createStatement();
		resultSet = statement.executeQuery(sqlStatement);
	}

	public ResultSet getResultSet()
	{
		return resultSet;
	}
	
	public void closeStatement() throws Exception
	{
		if (resultSet != null)
		{
			resultSet.close();
		}
		if (statement != null)
		{
			statement.close();
		}
	}
}
