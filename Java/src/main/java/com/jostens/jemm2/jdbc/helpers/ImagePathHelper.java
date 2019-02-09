package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jostens.jemm2.jdbc.Jemm2Statements;

/**
 * Be provided a SOLR documentID the type to display and from that be able to find the full image path.
 * This class supports the ImageServlet.
 * 
 * This will involve doing queries to get to the asset table folder_path column.  The database ID has a 2 character
 * prefix which identifies the asset type the image is for and imageType is the type of PNG image to return.
 * 
 * Example identifier would be: PR_003352:1b   <--  Part databaseID=3352  and want black .png
 */
public class ImagePathHelper
{
	
	public String getImagePath(Connection c, String databaseID, String imageType) throws SQLException
	{
		String imagePath = null;
		int id = Integer.parseInt(databaseID.substring(3));
		if (databaseID.startsWith("PR"))
		{
			// Retrieving a part image
			imagePath = getPartImagePath(c, id + "", imageType);
		}
		
		if (imagePath == null)
		{
			return "INVALID";
		}
		return imagePath;
	}
	
	private String getPartImagePath(Connection c, String id, String imageType) throws SQLException
	{
		// Get the Asset table column that represents the supplied imageType
		String imageColumn = getImageColumn(imageType);

		String imagePath = null;
		String selectStmt = Jemm2Statements.getStatement(Jemm2Statements.GET_PART_FOLDER_PATH);
		selectStmt = selectStmt.replace("[PARTID]", id);
		selectStmt = selectStmt.replace("[IMAGECOLOR]", imageColumn);
		
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(selectStmt);
		boolean rowFound = rs.next();
		if (rowFound)
		{
			imagePath = rs.getString(1);
		}

		rs.close();
		statement.close();

		return imagePath;
	}

	/*
	 * for the provided imageType return the equivalent column name from the asset table
	 */
	private String getImageColumn(String imageType)
	{
		if ("1b".equals(imageType))
		{
			return "ISBLACK";
		}
		if ("1g".equals(imageType))
		{
			return "ISGOLD";
		}
		if ("1s".equals(imageType))
		{
			return "ISSILVER";
		}
		if ("ba".equals(imageType))
		{
			return "ISBESTAVAILABLE";
		}
		return "INVALID";
	}
}
