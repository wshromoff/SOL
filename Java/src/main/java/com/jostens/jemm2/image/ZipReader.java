package com.jostens.jemm2.image;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipReader
{
	private Map<String, List<String>> fonts = new HashMap<String, List<String>>();
	private int examined = 0;
	private int invalid = 0;
	private int exception = 0;

//	public static void main(String[] args) throws Exception
//	{
//		String startPath = args[0];
//		if (startPath == null || startPath.length() == 0)
//		{
//			System.out.println("Invalid Starting Path.");
//			return;
//		}
//		startPath = startPath.replaceAll("\\\\", "/");
//		ZipReader reader = new ZipReader();
//		reader.testGetFolderGUID(startPath);
//	}
//
//	@Test
//	public void testGetFolderGUID(String startPath) throws Exception
//	{
//		fileNames = new JostensLogFile("./filenames.txt");
//		fontUsage = new JostensLogFile("./fontUsage.txt");
////		String startPath = "K:\\Scholastic\\Minnesota\\Bethel University (Saint Paul)";
////		startPath = startPath.replaceAll("\\\\", "/");
////		walk("K:/Scholastic/Minnesota/Andover HS (Andover)");
//		walk(startPath);
////		FileInputStream fis = new FileInputStream("C:/Temp/Font/NEW bldg MFG.cdr");
////		FileInputStream fis = new FileInputStream("K:/Scholastic/Minnesota/Andover HS (Andover)/2006/diploma mfg (98082) Andover High School.cdr");
//		
////		String fonts = getFonts(fis);
////		System.out.println("-->" + fonts);
//		
////		System.out.println("FONT COUNT = " + fonts.size());
//		
//		Map<String, List<String>> sortedFonts = sortByComparator(fonts);
//
//		int value = -1;
//		fontUsage.info("\n\nFont and Usage Counts ");
//		fontUsage.info("Examined: " + examined);
//		fontUsage.info("Invalid: " + invalid);
//		fontUsage.info("Exception: " + exception);
//		fontUsage.info("\n");
//		for (Map.Entry<String, List<String>> entry : sortedFonts.entrySet()) {
//			if (value != entry.getValue().size())
//			{
//				fontUsage.info("    --- ");
//				value = entry.getValue().size();
//			}
//			fontUsage.info(entry.getKey() + "   [" + entry.getValue().size() + "]");
//		}
//		
//		System.out.println("\n\nComplete.");
//
//	}
//	
//	private Map<String, List<String>> sortByComparator(Map<String, List<String>> unsortMap) {
//
//		// Convert Map to List
//		List<Map.Entry<String, List<String>>> list = 
//			new LinkedList<Map.Entry<String, List<String>>>(unsortMap.entrySet());
//
//		// Sort list with comparator, to compare the Map values
//		Collections.sort(list, new Comparator<Map.Entry<String, List<String>>>() {
//			public int compare(Map.Entry<String, List<String>> o1,
//                                           Map.Entry<String, List<String>> o2) {
//				int size1 = o1.getValue().size();
//				int size2 = o2.getValue().size();
//				if (size1 == size2)
//				{
//					return o1.getKey().compareTo(o2.getKey());
//				}
//				if (size1 < size2)
//				{
//					return 1;
//				}
//				return -1;
////				return (o1.getValue().size()).compareTo(o2.getValue().size());
//			}
//		});
//
//		// Convert sorted map back to a Map
//		Map<String, List<String>> sortedMap = new LinkedHashMap<String, List<String>>();
//		for (Iterator<Map.Entry<String, List<String>>> it = list.iterator(); it.hasNext();) {
//			Map.Entry<String, List<String>> entry = it.next();
//			sortedMap.put(entry.getKey(), entry.getValue());
//		}
//		return sortedMap;
//	}
//
//	
//	   public void walk( String path ) throws IOException 
//	   {
//
//	        File root = new File( path );
//	        File[] list = root.listFiles();
//
//	        if (list == null) return;
//
//	        for ( File f : list ) {
//	            if ( f.isDirectory() ) {
//	                walk( f.getAbsolutePath() );
////	                System.out.println( "Dir:" + f.getAbsoluteFile() );
//	            }
//	            else 
//	            {
//	            	if (!f.getAbsolutePath().endsWith(".cdr"))
//	            	{
//	            		continue;
//	            	}
//	            	String cdrPath = f.getAbsolutePath().replaceAll("\\\\", "/");
////	                System.out.println( cdrPath);
//	            	String fonts = null;
//	            	try
//	            	{
//	            		fonts = getFonts(cdrPath);
//	            	} catch (Exception e)
//	            	{
//	        			System.out.println("(E)  " + cdrPath);
//	        			fileNames.info("(E)  " + cdrPath);
//	        			exception++;
//	        			continue;
//	            	}
//	        		if (fonts.isEmpty())
//	        		{
//	        			System.out.println("(I)  " + cdrPath);
//	        			fileNames.info("(I)  " + cdrPath);
//	        			invalid++;
//	        		}
//	        		else
//	        		{
//	        			System.out.println(cdrPath);	        			
//	        			fileNames.info(cdrPath);
//	        			examined++;
//	        		}
//	        		
//	        		addFonts(fonts, cdrPath);
//	            }
//	        }
//	    }
//
//	   private void addFonts(String cdrFonts, String cdrPath)
//	   {
//		   StringTokenizer st = new StringTokenizer(cdrFonts, "|");
//		   while (st.hasMoreTokens())
//		   {
//			   String font = st.nextToken();
//			   List<String> cdrs = fonts.get(font);
//			   if (cdrs == null)
//			   {
//				   cdrs = new ArrayList<String>();
//				   fonts.put(font, cdrs);
//			   }
//			   cdrs.add(cdrPath);
//		   }
//	   }

	private String getFonts(String path) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		boolean fontsFound = false;
		
		FileInputStream fis = new FileInputStream(path);
        ZipInputStream zipIn = new ZipInputStream(fis);
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) 
        {
//        	System.out.println("NAME=" + entry.getName());
        	if ("metadata/metadata.xml".equals(entry.getName()))
        	{
        		boolean fontSection = false;
//        		System.out.println("FOUND IT!");
        	    Scanner sc = new Scanner(zipIn);
        	    while(sc.hasNextLine())
        	    {
        	    	String line = sc.nextLine();
        	    	if (line.contains("<FontsUsed>"))
        	    	{
        	    		fontSection = true;
        	    		fontsFound = true;
        	    		continue;
        	    	}
        	    	if (line.contains("</FontsUsed>"))
        	    	{
        	    		fontSection = false;
        	    		break;
        	    	}
        	    	int i = line.indexOf("<rdf:li>");
        	    	if (fontSection && i != -1)
        	    	{
        	    		int j = line.indexOf("</rdf:li>");
        	    		sb.append(line.substring(i + 8, j) + "|");
//        	    		System.out.println("FONT = " + line.substring(i + 8, j));
        	    	}
//        	        System.out.println(line);
        	    }
        	    sc.close();

        		//        		String fonts = getFonts(entry.)
        	}
            zipIn.closeEntry();
            if (fontsFound)
            {
            	break;
            }
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
		return sb.toString();
	}
}
