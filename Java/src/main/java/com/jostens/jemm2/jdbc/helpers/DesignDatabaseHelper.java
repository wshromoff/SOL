package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.StatementProcessor;

/**
 * Methods for interacting with DESIGN database table so all this complex code is isolated
 */
public class DesignDatabaseHelper
{

	/**
	 * Return the count of design rows
	 */
	public int getDesignCount(Connection c)
	{
		int rowCount = 0;
		String stmt = Jemm2Statements.getStatement(Jemm2Statements.DESIGN_COUNT);

		try
		{
			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				rowCount = rs.getInt(1);
			}
			rs.close();
			processor.closeStatement();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return rowCount;
	}

	/**
	 * Return the next sequence # from DESIGN_SEQUENCE
	 */
	public int getNextDesignSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT DESIGN_SEQUENCE.NEXTVAL FROM DUAL");
			rs.next();
			value = rs.getInt(1);
			rs.close();
			statement.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
//			ExceptionHelper.logExceptionToFile("getNextMBAssetsSequence (CustomerProfileDatabase)", e);
			//System.out.println("Exception in retrieving next MB Assets Sequence value: " + ExceptionHelper.getStackTraceAsString(e));
		}
		return value;
	}

}