package com.jostens.jemm2.image;

import java.io.UnsupportedEncodingException;
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
		addCompareResult("File is valid PNG Signature", "true", "true", true);
		
		// Compare for valid format
		if (!file1.isPngFileValid() || !file2.isPngFileValid())
		{
			filesMatch = false;
			finalVerdict = "One or both files does not Validate";
			addCompareResult("File passes file format validations", file1.isPngFileValid() + "", file2.isPngFileValid() + "", false);
			return;
		}
		addCompareResult("File passes file format validations", "true", "true", true);

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
			addCompareResult("File Size", file1Size + "", file2Size + "", true);			
		}
		else
		{
			addCompareResult("File Size", file1Size + "", file2Size + "", false);
			finalVerdict = "File size difference exceeds tollerence";
		}

		// Compare chunk counts
		List<PNGFileChunk> file1Chunks = file1.getChunks();
		List<PNGFileChunk> file2Chunks = file2.getChunks();
//		boolean chunksMatch = true;
//		if ((file1Chunks.size() >= file2Chunks.size()) && (file2Chunks.size() + 2 < file1Chunks.size()))
//		{
//			chunksMatch = false;
//		}
//		if ((file2Chunks.size() >= file1Chunks.size()) && (file1Chunks.size() + 2 < file2Chunks.size()))
//		{
//			chunksMatch = false;
//		}
		if (file1Chunks.size() == file2Chunks.size())
		{
			addCompareResult("Chunk Count", file1Chunks.size() + "", file2Chunks.size() + "", true);			
		}
		else
		{
			addCompareResult("Chunk Count", file1Chunks.size() + "", file2Chunks.size() + "", false);
			finalVerdict = "Chunk count difference exceeds tollerence";
		}
		
		
		addCompareResult(" ", " ", " ", true);
		// Display each chunk size
		int maxChunks = file1Chunks.size();
		if (file2Chunks.size() > maxChunks)
		{
			maxChunks = file2Chunks.size();
		}
		for (int i = 0; i < maxChunks; i++)
		{
			String chunk1Display = "NA";
			String chunk2Display = "NA";
			int chunk1Size = 0;
			int chunk2Size = 0;
			if (i < file1Chunks.size())
			{
				PNGFileChunk chunk1 = file1Chunks.get(i);
				chunk1Size = chunk1.getIntLength();
				chunk1Display = chunk1Size + "";
			}
			if (i < file2Chunks.size())
			{
				PNGFileChunk chunk2 = file2Chunks.get(i);
				chunk2Size = chunk2.getIntLength();
				chunk2Display = chunk2Size + "";
			}
//			PNGFileChunk chunk2 = file2Chunks.get(i);
//			int chunk1Size = chunk1.getIntLength();
//			int chunk2Size = chunk2.getIntLength();
			boolean sizeMatch = chunk1Size == chunk2Size;
			addCompareResult("Chunk " + (i+1) + " Size", chunk1Display, chunk2Display, sizeMatch);

//			if (!sizeMatch)
//			{
//				try
//				{
//					for(byte c : chunk1.getData()) {
//					    System.out.format("%d ", c);
//					}
//					System.out.println();
//					for(byte c : chunk2.getData()) {
//					    System.out.format("%d ", c);
//					}
//					System.out.println();
//					String chunk1Data = new String(chunk1.getData(), 0, chunk1Size);
//					String chunk2Data = new String(chunk2.getData(), 0, chunk2Size, "UTF-8");
//					System.out.println("[" + (i+1) + "]" + chunk1Data);
//					System.out.println("[" + (i+1) + "]" + chunk2Data);
//				} catch (UnsupportedEncodingException e)
//				{
//					e.printStackTrace();
//				}
//
//			}
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
