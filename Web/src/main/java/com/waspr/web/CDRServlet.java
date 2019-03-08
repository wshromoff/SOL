package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.image.CDRFile;
import com.jostens.jemm2.web.HTMLHelper;

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
		int pageCount = cdr.getPages().size();
		sb.setLength(0);
		int page = 2;
		while (page <= pageCount)
//		for (int k = 2; k <= cdr.getPages().size(); k += 2)
		{
			String aPage = "\"image?id=page" + page + "&type=jpg\"";
			sb.append(aPage + ", ");
			page += 2;
		}
		sb.append(emptyImage);
//		System.out.println("-->" + sb.toString());
		cdrImagesHTML = cdrImagesHTML.replace("[IMAGES1]", sb.toString());
//		System.out.println("-->" + cdrImagesHTML);

		// Build list 2 for book
		sb.setLength(0);
		page = 3;
		while (page <= pageCount)
//		for (int k = 3; k <= cdr.getPages().size(); k += 2)
		{
			String aPage = "\"image?id=page" + page + "&type=jpg\"";
			sb.append(aPage + ", ");
			page += 2;
		}
		if ((pageCount % 2) == 0)
		{
			sb.append(emptyImage + ", ");
		}
		sb.append("\"image?id=page1&type=jpg\"");
//		System.out.println("-->" + sb.toString());
		cdrImagesHTML = cdrImagesHTML.replace("[IMAGES2]", sb.toString());
		System.out.println("-->" + cdrImagesHTML);

		
		response.getWriter().append(cdrFileHTML + cdrImagesHTML);
		
	}

}
