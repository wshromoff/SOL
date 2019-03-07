package com.waspr.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.im4java.core.IM4JavaException;

import com.jostens.jemm2.image.CDRFile;
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
		
		CDRFile cdr = new CDRFile("cdr1.cdr");
		try
		{
			cdr.convertToJPG();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		
//		LogfileHelper helper = LogfileHelper.getActiveLogHelper();
//		String logfileHTML = getBuiltOutTemplate(helper);

		String comparefileHTML = HTMLHelper.getTemplateHTML("/CDR.html");

		response.getWriter().append(comparefileHTML);

	}

}
