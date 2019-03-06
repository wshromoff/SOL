package com.waspr.web;

import java.io.IOException;

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

		String logfileHTML = HTMLHelper.getTemplateHTML("/CompareFile.html");

		response.getWriter().append(logfileHTML);

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
