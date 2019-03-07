package com.waspr.web;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.im4java.core.IM4JavaException;

import com.jostens.jemm2.image.CDRFile;
import com.jostens.jemm2.image.PNGFile;
import com.jostens.jemm2.image.PNGFileCompare;
import com.jostens.jemm2.web.HTMLHelper;

import magick.MagickException;

@WebServlet("/cdr")
public class CDRServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside CDR Servlet");
		
		
//		LogfileHelper helper = LogfileHelper.getActiveLogHelper();
//		String logfileHTML = getBuiltOutTemplate(helper);

		String cdrFileHTML = HTMLHelper.getTemplateHTML("/CDR.html");

		response.getWriter().append(cdrFileHTML);

	}

	// 
	// doPost is what performs the query
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside CDR Post Servlet");

		// Get form parameters
		String file1 = request.getParameter("f1");
		int i = file1.lastIndexOf("\\");
		file1 = file1.substring(i+1);
		System.out.println("File1 = " + file1);

		CDRFile cdr = new CDRFile(file1);
		try
		{
			cdr.convertAllImagesToJPG();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		String cdrFileHTML = HTMLHelper.getTemplateHTML("/CDR.html");
		String cdrImagesHTML = HTMLHelper.getTemplateHTML("/CDRImages.html");

		// Build the pages display
		String template = "<img src=\"image?id=XXXXX&type=jpg\" width=\"120px\" height=\"120px\" />";
		StringBuffer sb = new StringBuffer();
		for (int k = 1; k <= cdr.getPages().size(); k++)
		{
			String aPage = template;
//			int j = path.lastIndexOf("/");
//			String name = path.substring(j+1);
			aPage = aPage.replace("XXXXX", "page" + k);
//			aPage = aPage.replace(".bmp", "");
//			System.out.println("APAGE=" + aPage);
			sb.append(aPage);
		}
		cdrImagesHTML = cdrImagesHTML.replace("[CDR_PAGES]", sb.toString());

		String emptyImage = "\"data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==\"";

		// Build list 1 for book
		sb.setLength(0);
		for (int k = 1; k <= cdr.getPages().size(); k++)
		{
			String aPage = "\"image?id=page" + k + "&type=jpg\"";
			sb.append(aPage + ", ");
		}
		sb.append(emptyImage);
//		System.out.println("-->" + sb.toString());
		cdrImagesHTML = cdrImagesHTML.replace("[IMAGES1]", sb.toString());
		System.out.println("-->" + cdrImagesHTML);

		// Build list 2 for book
		sb.setLength(0);
		for (int k = 2; k <= cdr.getPages().size(); k++)
		{
			String aPage = "\"image?id=page" + k + "&type=jpg\"";
			sb.append(aPage + ", ");
		}
		sb.append(emptyImage + ", ");
		sb.append("\"image?id=page1&type=jpg\"");
//		System.out.println("-->" + sb.toString());
		cdrImagesHTML = cdrImagesHTML.replace("[IMAGES2]", sb.toString());
		System.out.println("-->" + cdrImagesHTML);

		
		response.getWriter().append(cdrFileHTML + cdrImagesHTML);
		
//		// Open the file objects
//		PNGFile png1 = new PNGFile(file1);
//		PNGFile png2 = new PNGFile(file2);
//		
//		PNGFileCompare compare = new PNGFileCompare();
//		compare.setFile1(png1);
//		compare.setFile2(png2);
//		compare.compare();
//
//		String comparefileHTML = HTMLHelper.getTemplateHTML("/CompareFile.html");
//		String comparefileResultsHTML = HTMLHelper.getTemplateHTML("/CompareFileResults.html");
//
//		comparefileResultsHTML = comparefileResultsHTML.replace("[File1]", file1);
//		comparefileResultsHTML = comparefileResultsHTML.replace("[File2]", file2);
//		comparefileResultsHTML = comparefileResultsHTML.replace("[EQUAL]", compare.isFilesMatch() + "");
//		comparefileResultsHTML = comparefileResultsHTML.replace("[REASON]", compare.getFinalVerdict());
//
//		StringBuffer sb = new StringBuffer();
//		String compareResults = compare.getCompareResults();
//		StringTokenizer st = new StringTokenizer(compareResults, "<BR>");
//		while (st.hasMoreTokens())
//		{
//			String result = st.nextToken();
////			System.out.println("-->" + result);
//			
//			StringTokenizer st2 = new StringTokenizer(result, "|");
//			String testToken = st2.nextToken();
//			String file1Token = st2.nextToken();
//			String file2Token = st2.nextToken();
//			String successToken = st2.nextToken();
//
//			String compareResultsRowHTML = HTMLHelper.getTemplateHTML("/CompareResultsRow.html");
//			compareResultsRowHTML = compareResultsRowHTML.replace("[TEST]", testToken);
//			compareResultsRowHTML = compareResultsRowHTML.replace("[FILE1]", file1Token);
//			compareResultsRowHTML = compareResultsRowHTML.replace("[FILE2]", file2Token);
//			compareResultsRowHTML = compareResultsRowHTML.replace("[SUCCESS]", successToken);
//			
//			sb.append(compareResultsRowHTML);
//
//		}
//
//		comparefileResultsHTML = comparefileResultsHTML.replace("[RESULTS_ROWS]", sb.toString());
//
//
//		response.getWriter().append(comparefileHTML + comparefileResultsHTML);
//
//		System.out.println("Files Match: " + compare.isFilesMatch());
//		System.out.println("Reason: " + compare.getFinalVerdict());

	}

}
