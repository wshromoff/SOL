package com.waspr.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jostens.jemm2.JEMM2Constants;
import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.ImagePathHelper;

/**
 * Display the supplied image to a web site
 */
@WebServlet("/image")
public class ImageServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		Connection c = ConnectionHelper.getJEMM2Connection();

		System.out.println("Inside Image Servlet " + request.getRequestURI());
		
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String format = request.getParameter("format");		// Value selected for format dropdown list
		System.out.println("ImageServlet=" + id + ":" + type + ":" + format);

	if ("jpg".equals(type))
	{
		System.out.println(" JPG !!!!");
		// Doing a .cdr .jpg image
		if (c != null)
		{
	       ConnectionHelper.closeConnection(c);
		}

	       String filePath = JEMM2Constants.COMPARE_PATH + id + ".jpg";
	       System.out.println("FP=" + filePath);
	       File file = new File(filePath);
	       FileInputStream fis = new FileInputStream(file);
	       BufferedImage bi = ImageIO.read(fis);
	       OutputStream os = response.getOutputStream();
	       ImageIO.write(bi, "jpeg", os);
	       return;

	}
		String filePath = "";
		
		// Use image path helper to get full path to .png image to use
		ImagePathHelper helper = new ImagePathHelper();
		
		try
		{
			filePath = JEMM2Constants.ROOT_DAM_PATH + helper.getImagePath(c, id, type);
			System.out.println("Image_Path= " + filePath);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}


//	       ServletContext sc = getServletContext();
//	       
//	       InputStream is = sc.getResourceAsStream("/Users/wadeshromoff/assets/BR000860_1062612_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_1b_2550.png");

		// Override the above found value if Constant says to use hard coded value
       if (JEMM2Constants.useHardCoded)
       {
	       // hard coded paths are used during development testing
	       filePath = JEMM2Constants.ROOT_DAM_PATH +   "/DAM/2/61/BR000860_1062612_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_XX_2550.png";
	       filePath = filePath.replace("XX", type);
       }
	       
       ConnectionHelper.closeConnection(c);

       File file = new File(filePath);
       FileInputStream fis = new FileInputStream(file);
       BufferedImage bi = ImageIO.read(fis);
       OutputStream os = response.getOutputStream();
       ImageIO.write(bi, "png", os);

	        // SVG - NOT HANDLED - NEEDS LOTS OF WORK
////	        Blob blob = rs.getBlob("ICON");
//	        String filePath = "/Users/wadeshromoff/assets/BR000860_1062612_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_XX.svg";
//		       filePath = filePath.replace("XX", type);
//		       File file = new File(filePath);
//		       FileInputStream fis = new FileInputStream(file);
//	        byte[] bytes = new byte[5000];
//	        fis.read(bytes);
//	        response.getOutputStream().write(bytes);
//	        response.setContentType("image/svg+xml");
	        
//	       File file = new File(filePath);
//	       System.out.println("FILE=" + file.getParent());
//	       FileInputStream fis = new FileInputStream(file);
//	       
//	        BufferedImage bi = ImageIO.read(fis);
//	        System.out.println("BI SIZE = " + bi);
//	        response.setContentType("image/svg+xml");
//	        OutputStream os = response.getOutputStream();
//	        ImageIO.write(bi, "png", os);

//		       String filePath = "/Users/wadeshromoff/assets/BR000860_1062612_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_XX.svg";


		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}

}
