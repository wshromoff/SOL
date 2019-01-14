package com.jostens.dam.brand.workdisplay;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.brand.web.HTMLHelper;

public class CompletedAssetsTest extends BrandBaseTest
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

//	@Test
	public void testPerformThreadAction()
	{
		CompletedAssets ca = new CompletedAssets();
		ca.performThreadAction();
	}

//	@Test
	public void testGetArtistCompletedAssets()
	{
		CompletedAssets ca = new CompletedAssets();
		Map<String, List<DisplayAsset>> artistAssets = ca.getArtistCompletedAssets();
		System.out.println("Artist count= " + artistAssets.size());
		System.out.println("   Asset count per artist");
        Iterator<Map.Entry<String, List<DisplayAsset>>> iter = artistAssets.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<String, List<DisplayAsset>> entry = iter.next();
        	String artistName = (String)entry.getKey();
        	List<DisplayAsset> assets = entry.getValue();
        	System.out.println(artistName + " : " + assets.size());
        }
	}

//	@Test
	public void testPeformThreadAction()
	{
		CompletedAssets ca = new CompletedAssets();
		ca.performThreadAction();
		
		String cachedHTML = HTMLHelper.getCachedHTML(CompletedAssets.COMPLETED_ASSETS_HTML);
		
		System.out.println("Cache size = " + cachedHTML.length());
		System.out.println("> " + cachedHTML);
	}

	@Test
	public void testSortArtistAssetss()
	{
		CompletedAssets ca = new CompletedAssets();
		ca.performThreadAction();
		
		Map<String, List<DisplayAsset>> artistAssets = ca.getArtistAssets();
		System.out.println("Artist count= " + artistAssets.size());
		System.out.println("   Asset count per artist");
        Iterator<Map.Entry<String, List<DisplayAsset>>> iter = artistAssets.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<String, List<DisplayAsset>> entry = iter.next();
        	String artistName = (String)entry.getKey();
        	List<DisplayAsset> assets = entry.getValue();
        	System.out.println(artistName + " : " + assets.size());
        }
	}

}
