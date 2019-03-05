package com.jostens.jemm2.image;

/**
 * Compare to PNGFile objects - Return a StringBuffer of status which forms a table for display.
 */
public class PNGFileCompare
{
	private PNGFile file1 = null;
	private PNGFile file2 = null;

	private boolean filesMatch = true;
	private String finalVerdict = "Files were found to be identical";
	private StringBuffer compareResults = new StringBuffer();
	
	public void compare()
	{
		// If either null, no compare can be performed
		if (file1 == null || file2 == null)
		{
			filesMatch = false;
			finalVerdict = "One or both files are null";
			boolean file1Null = (file1 == null);
			boolean file2Null = (file2 == null);
			addCompareResult("File is NULL", file1Null + "", file2Null + "", false);
			return;
		}
		addCompareResult("File is NULL", "false", "false", true);
		
		
	}
	public void setFile1(PNGFile file1)
	{
		this.file1 = file1;
	}
	public void setFile2(PNGFile file2)
	{
		this.file2 = file2;
	}

	private void addCompareResult(String description, String file1Value, String file2Value, boolean success)
	{
		String singleResult = description + "|" + file1Value + "|" + file2Value + "|" + success + "<BR>";
		compareResults.append(singleResult);
	}
	
	public boolean isFilesMatch()
	{
		return filesMatch;
	}
	public String getFinalVerdict()
	{
		return finalVerdict;
	}
	public String getCompareResults()
	{
		return compareResults.toString();
	}	
}
