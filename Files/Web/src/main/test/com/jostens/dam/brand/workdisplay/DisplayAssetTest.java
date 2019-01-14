package com.jostens.dam.brand.workdisplay;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;

public class DisplayAssetTest extends BrandBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
		useLiveFolders();
		connectToMB();		// Do a bogus connect to initialize FolderGUIDS correctly for test execution
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

	@Test
	public void testToString()
	{
		DisplayAsset da = new DisplayAsset();
		da.setAssetName("BR000822_2277867_crest_vector_flat_2t_dx_0x_gds_blx_x_x_x_x_x_x_x_x.cdr");
		da.setArtistName("shromow");
		da.setInStageTime(302);
		da.setTotalTime(4000);
		System.out.println(da);
		
		da.setAssetName("BR000999_2277867_crest_vector_flat_2t_dx_0x_gds_blx_x_x_x_x_x_x_x_x.cdr");
		da.setInStageTime(2176);
		da.setTotalTime(4000);
		System.out.println(da);

	}

}
