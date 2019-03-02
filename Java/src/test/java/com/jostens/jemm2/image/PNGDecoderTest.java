package com.jostens.jemm2.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class PNGDecoderTest
{

	@Test
	public void test() throws IOException
	{
		String imagePath = "/Users/wadeshromoff/assets/Upload/BR000860_2683273_mascot_vector_flat_2t_dx_0x_gds_wts_bks_x_x_x_x_x_x_x_1b_2550.png";
		File aFile = new File(imagePath);
		System.out.println("FILE = " + aFile.isFile());
		
		InputStream iStream = new FileInputStream(imagePath);
		PNGDecoder decoder = new PNGDecoder(iStream);
	}

}
