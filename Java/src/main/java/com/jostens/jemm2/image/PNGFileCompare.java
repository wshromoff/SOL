package com.jostens.jemm2.image;

import java.util.List;

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
		
		// Compare for valid signatures
		if (!file1.isValidSignature() || !file2.isValidSignature())
		{
			filesMatch = false;
			finalVerdict = "One or both files is not a PNG File";
			addCompareResult("File is valid PNG format", file1.isValidSignature() + "", file2.isValidSignature() + "", false);
			return;
		}
		addCompareResult("File is valid PNG format", "true", "true", true);
		
		// Compare file sizes and are they within a 10 byte difference
		int file1Size = file1.getImageSize();
		int file2Size = file2.getImageSize();
		boolean sizesMatch = true;
		if ((file1Size >= file2Size) && (file2Size + 10 < file1Size))
		{
			sizesMatch = false;
		}
		if ((file2Size >= file1Size) && (file1Size + 10 < file2Size))
		{
			sizesMatch = false;
		}
		if (sizesMatch)
		{
			addCompareResult("File Sizes", file1Size + "", file2Size + "", true);			
		}
		else
		{
			addCompareResult("File Sizes", file1Size + "", file2Size + "", false);
			finalVerdict = "File size difference exceeds tollerence";
		}

		// Compare chunk counts
		List<PNGFileChunk> file1Chunks = file1.getChunks();
		List<PNGFileChunk> file2Chunks = file2.getChunks();
		boolean chunksMatch = true;
		if ((file1Chunks.size() >= file2Chunks.size()) && (file2Chunks.size() + 2 < file1Chunks.size()))
		{
			chunksMatch = false;
		}
		if ((file2Chunks.size() >= file1Chunks.size()) && (file1Chunks.size() + 2 < file2Chunks.size()))
		{
			chunksMatch = false;
		}
		if (chunksMatch)
		{
			addCompareResult("Chunk Count", file1Chunks.size() + "", file2Chunks.size() + "", true);			
		}
		else
		{
			addCompareResult("Chunk Count", file1Chunks.size() + "", file2Chunks.size() + "", false);
			finalVerdict = "Chunk count difference exceeds tollerence";
		}
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
