package com.jostens.dam.brand.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.common.JostensLog;

/**
 * Methods related to performing an IDOL reindex.  Caller must specify a valid Connection.
 */
public class IDOLReindex
{
	JostensLog log = new JostensLog();

	/**
	 * Examine the ISIDOLINDEXNEEDED column returning the number of rows where this value
	 * is set to 1.  A value of 1 indicates that indexing is needed which should provide a count
	 * of how many rows are yet to be processed.  Like a status value.
	 */
	public int getIndexNeededCount(Connection c)
	{
		int rowCount = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
	
			String sql = "select count(*) from ASSETREFS where ISIDOLREINDEXNEEDED = 1";
			ps = c.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
			{
				rowCount = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("getIndexNeededCount (IDOLReindex)", e);
		}
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (ps != null)
				{
					ps.close();
				}
			} catch (Exception e) {}
		}
		return rowCount;
	}
	
	/**
	 * Call the stored procedure DELETE_IDOL_TASK as step 1 of performing a reindex.  This procedure deletes the 
	 * IDOL Indexing Task (#9) from the mbscheduledtasks table
	 */
	public boolean deleteIDOLTask(Connection c)
	{
		try
		{
			CallableStatement cs = c.prepareCall("{call JOSTENS_DELETE_IDOL_TASK}");
			
			cs.execute();
			
			c.commit();
			
			cs.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("deleteIDOLTask (IDOLReindex)", e);
			//System.out.println("Exception in deleteIDOLTask: " + ExceptionHelper.getStackTraceAsString(e));
		}
		
		return true;
	}
	
	/**
	 * Call the stored procedure INSERT_IDOL_TASK as step 1 of performing a reindex.  This procedure inserts the 
	 * IDOL Indexing Task (#9) to the mbscheduledtasks table
	 */
	public boolean insertIDOLTask(Connection c)
	{
		try
		{
			CallableStatement cs = c.prepareCall("{call JOSTENS_INSERT_IDOL_TASK}");
			
			cs.execute();
			
			c.commit();
			
			cs.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("insertIDOLTask (IDOLReindex)", e);
			//System.out.println("Exception in insertIDOLTask: " + ExceptionHelper.getStackTraceAsString(e));
		}
		
		return true;
	}

	/**
	 * Call the stored procedure INSERT_IDOL_TASK as step 1 of performing a reindex.  This procedure inserts the 
	 * IDOL Indexing Task (#9) to the mbscheduledtasks table
	 */
	public boolean markAssetsToReIndex(Connection c, String parentID)
	{
		try
		{
			CallableStatement cs = c.prepareCall("{call JOSTENS_MARK_FOR_REINDEX(?)}");
			cs.setString(1, parentID);
			
			cs.execute();
			
			c.commit();
			
			cs.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("markAssetsToReIndex (IDOLReindex)", e);
			//System.out.println("Exception in markAssetsToReIndex: " + ExceptionHelper.getStackTraceAsString(e));
		}
		
		return true;
	}

}
