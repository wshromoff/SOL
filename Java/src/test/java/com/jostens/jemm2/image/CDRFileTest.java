package com.jostens.jemm2.image;

import static org.junit.Assert.*;

import java.io.IOException;

import org.im4java.core.IM4JavaException;
import org.junit.Test;

import magick.MagickException;

public class CDRFileTest
{

//	@Test
	public void testNavigateCDR() throws IOException
	{
		CDRFile cdr = new CDRFile("cdr1.cdr");
	}

	@Test
	public void testConvertToJPG() throws Exception
	{
		CDRFile cdr = new CDRFile("cdr2.cdr");
		cdr.convertAllImagesToJPG();
	}

}
