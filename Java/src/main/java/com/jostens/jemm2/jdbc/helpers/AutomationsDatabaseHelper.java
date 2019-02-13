package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AutomationsDatabaseHelper
{

	/**
	 * Return the next sequence # from INCOMINGPACKAGE_SEQUENCE
	 */
	public int getNextIncomingPackageSequence(Connection c)
	{
		int value = 0;
		try
		{
			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT INCOMINGPACKAGE_SEQUENCE.NEXTVAL FROM DUAL");
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
