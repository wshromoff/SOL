package com.jostens.jemm2.helpers;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.pojo.Design;

public class DesignHelperTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

//	@Test
	public void testGetDesignObjects() throws IOException
	{
		DesignHelper helper = new DesignHelper();
		
		List<Design> designs = helper.getDesignObjects("design_01.txt");
		
		System.out.println("Design count = " + designs.size());
	}

	@Test
	public void testGetDesign() throws IOException
	{
		DesignHelper helper = new DesignHelper();
		
		Design design = helper.getDesign("|20150626-aecac9d49554|Knight,Baron,Crusader,Lancer|");
		
		System.out.println("Keyword count = " + design.getKeywords().size());
	}

}
