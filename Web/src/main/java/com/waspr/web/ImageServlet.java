package com.waspr.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println("Inside Image Servlet");
		
		String type = request.getParameter("type");
	       System.out.println("type=" + type);
		

//	       ServletContext sc = getServletContext();
//	       
//	       InputStream is = sc.getResourceAsStream("/Users/wadeshromoff/assets/BR000860_1062612_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_1b_2550.png");

	       // PNG
	       String filePath = "/Users/wadeshromoff/assets/BR000860_1062612_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_XX_2550.png";
	       filePath = filePath.replace("XX", type);
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
