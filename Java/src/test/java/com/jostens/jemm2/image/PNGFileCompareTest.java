package com.jostens.jemm2.image;

import java.io.IOException;
import java.util.StringTokenizer;

import org.junit.Test;

public class PNGFileCompareTest
{
//	@Test
	public void test1NullFile() throws IOException
	{
		PNGFile file1 = new PNGFile("png1.png");
		PNGFile file2 = null;
		
		PNGFileCompare pngCompare = new PNGFileCompare();
		pngCompare.setFile1(file1);
		pngCompare.setFile2(file2);
		
		pngCompare.compare();
		
		showResults(pngCompare);
	}


	@Test
	public void test2IdenticalFiles() throws IOException
	{
		PNGFile file1 = new PNGFile("png1.png");
		PNGFile file2 = new PNGFile("png2.png");
		
		PNGFileCompare pngCompare = new PNGFileCompare();
		pngCompare.setFile1(file1);
		pngCompare.setFile2(file2);
		
		pngCompare.compare();
		showResults(pngCompare);

	}

	private void showResults(PNGFileCompare pngCompare)
	{
		System.out.println("\n                     PNG Compare Results\n");
		System.out.printf("Files Compare Equal: " + pngCompare.isFilesMatch() + "\n");
		System.out.printf("Comparison Reason: " + pngCompare.getFinalVerdict() + "\n\n");
		
		System.out.printf("%25sCompare Test%25s | %10sFile 1%10s | %10sFile 2%10s | %5sSuccess%5s |\n", " ", " ", " ", " ", " ", " ", " ", " ");
		String compareResults = pngCompare.getCompareResults();
		StringTokenizer st = new StringTokenizer(compareResults, "<BR>");
		while (st.hasMoreTokens())
		{
			String result = st.nextToken();
			displayResult(result);
//			System.out.println("-->" + result);
		}
	}
	
	private void displayResult(String aResult)
	{
		StringTokenizer st = new StringTokenizer(aResult, "|");
		String test = st.nextToken();
		String file1 = st.nextToken();
		String file2 = st.nextToken();
		String success = st.nextToken();
		
		String testCentered = getCenteredText(test, 63);
		String file1Centered = getCenteredText(file1, 28);
		String file2Centered = getCenteredText(file2, 28);
		String successCentered = getCenteredText(success, 19);
		System.out.printf("%-63s|%-28s|%-28s|%-19s|\n", testCentered, file1Centered, file2Centered, successCentered);
	}
	
	private String getCenteredText(String text, int size)
	{
		int halfSize = text.length() / 2;
		int middle = size / 2;
		int before = middle - halfSize;
		String centered = String.format("%" + before + "s%s", " ", text);
//		System.out.println("CENTERED =" + centered + "|");
		return centered;
	}
}
