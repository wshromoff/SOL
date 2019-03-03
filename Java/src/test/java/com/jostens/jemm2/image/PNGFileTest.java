package com.jostens.jemm2.image;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class PNGFileTest
{
	private static PNGFile pngFile = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		String imagePath = "/Users/wadeshromoff/assets/Upload/BR000860_2683273_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_1b_2550.png";
//		File aFile = new File(imagePath);
//		System.out.println("FILE = " + aFile.isFile());

		InputStream iStream = new FileInputStream(imagePath);
		pngFile = new PNGFile();
		
		byte[] imageBytes = pngFile.getImageAsByteArray(iStream);
		pngFile.setImageAsBytes(imageBytes);

		
	}

//	@Test
	public void testGetImageAsByteArray() throws IOException
	{
		
//		InputStream iStream = new FileInputStream(imagePath);
//		PNGFile pngFile = new PNGFile();
		
//		byte[] imageBytes = pngFile.getImageAsByteArray(iStream);
		System.out.println("BYTES = " + pngFile.getImageAsBytes().length);
	}

//	@Test
	public void testIsValidSignature() throws IOException
	{
		assertTrue(pngFile.isValidSignature());
	}

//	@Test
	public void testDisplayBytes() throws IOException
	{
		pngFile.displayBytes(0, 8);		// Display signature
		System.out.println("   --- ");
		pngFile.displayBytes(8, 4);		// Display 1st chunk length
		System.out.println("   --- ");
		pngFile.displayBytes(12, 4);		// Display 1st chunk Type
		System.out.println("   --- ");
		pngFile.displayBytes(16, 13);		// Display 1st chunk data
		System.out.println("   --- ");
		pngFile.displayBytes(29, 4);		// Display 1st chunk CRC
		System.out.println("   --- ");
		pngFile.displayBytes(33, 4);		// Display 2nd chunk SIZE
	}

//	@Test
	public void testGetFirstChunk() throws IOException
	{
		PNGFileChunk chunk1 = pngFile.getNextChunk(8);
		PNGFileChunk chunk2 = pngFile.getNextChunk(8 + chunk1.getChunkSize());
	}
	
	@Test
	public void testGetAllChunks() throws IOException
	{
		pngFile.getAllChunks();
	}
	
}
