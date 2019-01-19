package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.ResultSet;

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

}