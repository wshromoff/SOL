package com.waspr.web;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.image.PNGFile;
import com.jostens.jemm2.image.PNGFileCompare;
import com.jostens.jemm2.web.HTMLHelper;

@WebServlet("/compare")
public class CompareServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Compare Servlet");
		
//		LogfileHelper helper = LogfileHelper.getActiveLogHelper();
//		String logfileHTML = getBuiltOutTemplate(helper);

		String comparefileHTML = HTMLHelper.getTemplateHTML("/CompareFile.html");

		response.getWriter().append(comparefileHTML);

	}

	// doPost is what performs the query
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Inside Compare Post Servlet");

		// Get form parameters
		String file1 = request.getParameter("f1");
		String file2 = request.getParameter("f2");
		int i = file1.lastIndexOf("\\");
		file1 = file1.substring(i+1);
		i = file2.lastIndexOf("\\");
		file2 = file2.substring(i+1);
		System.out.println("File1 = " + file1);
		System.out.println("File2 = " + file2);
		
		// Open the file objects
		PNGFile png1 = new PNGFile(file1);
		PNGFile png2 = new PNGFile(file2);
		
		PNGFileCompare compare = new PNGFileCompare();
		compare.setFile1(png1);
		compare.setFile2(png2);
		compare.compare();

		String comparefileHTML = HTMLHelper.getTemplateHTML("/CompareFile.html");
		String comparefileResultsHTML = HTMLHelper.getTemplateHTML("/CompareFileResults.html");

		comparefileResultsHTML = comparefileResultsHTML.replace("[File1]", file1);
		comparefileResultsHTML = comparefileResultsHTML.replace("[File2]", file2);
		comparefileResultsHTML = comparefileResultsHTML.replace("[EQUAL]", compare.isFilesMatch() + "");
		comparefileResultsHTML = comparefileResultsHTML.replace("[REASON]", compare.getFinalVerdict());

		StringBuffer sb = new StringBuffer();
		String compareResults = compare.getCompareResults();
		StringTokenizer st = new StringTokenizer(compareResults, "<BR>");
		while (st.hasMoreTokens())
		{
			String result = st.nextToken();
//			System.out.println("-->" + result);
			
			StringTokenizer st2 = new StringTokenizer(result, "|");
			String testToken = st2.nextToken();
			String file1Token = st2.nextToken();
			String file2Token = st2.nextToken();
			String successToken = st2.nextToken();

			String compareResultsRowHTML = HTMLHelper.getTemplateHTML("/CompareResultsRow.html");
			compareResultsRowHTML = compareResultsRowHTML.replace("[TEST]", testToken);
			compareResultsRowHTML = compareResultsRowHTML.replace("[FILE1]", file1Token);
			compareResultsRowHTML = compareResultsRowHTML.replace("[FILE2]", file2Token);
			compareResultsRowHTML = compareResultsRowHTML.replace("[SUCCESS]", successToken);
			
			sb.append(compareResultsRowHTML);

		}

		comparefileResultsHTML = comparefileResultsHTML.replace("[RESULTS_ROWS]", sb.toString());


		response.getWriter().append(comparefileHTML + comparefileResultsHTML);

		System.out.println("Files Match: " + compare.isFilesMatch());
		System.out.println("Reason: " + compare.getFinalVerdict());
		
//		String newSearch = request.getParameter("newSearch");
//
//		// Display values from form
////		System.out.println("query=" + query);
////		System.out.println("document=" + document);
////		System.out.println("New Search=" + newSearch);
//		
//		if ("true".equals(newSearch))
//		{
//			SOLRQuery.newQuery();
//			
//			SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
//			String searchHTML = getBuiltOutTemplate(solrQuery);
//			
//			response.getWriter().append(0 + "," + searchHTML);
//			return;
//		}
//
//		SOLRQuery solrQuery = SOLRQuery.getActiveQuery();
//		solrQuery.setDocument(document);		// Must set document type first
//		solrQuery.setQuery(query);
//		solrQuery.setHitCountOnly(true);
//		solrQuery.performQuery();		
//
//		response.getWriter().append(solrQuery.getHitCount() + ",");

	}

}
