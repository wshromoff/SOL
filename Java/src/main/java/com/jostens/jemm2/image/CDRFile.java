package com.jostens.jemm2.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.jostens.jemm2.JEMM2Constants;

import magick.MagickException;

public class CDRFile
{
	private String thumbnail = null;
	private List<String> pages = new ArrayList<String>();

	public CDRFile()
	{
		
	}

	public CDRFile(String fileName) throws IOException
	{
		String imagePath = JEMM2Constants.COMPARE_PATH + fileName;
		File aFile = new File(imagePath);
		if (!aFile.isFile())
		{
			System.out.println("Cannot find file: " + fileName);
		}

		InputStream iStream = new FileInputStream(imagePath);
		loadImage(iStream);
		
	}

    public void loadImage(InputStream input) throws IOException
    {
        ZipInputStream zipIn = new ZipInputStream(input);
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) 
        {
        	String entryName = entry.getName();
        	if (entryName.contains("thumbnails"))
        	{
        		// Only process thumbnails
            	System.out.println("NAME=" + entryName);
            	int i = entryName.lastIndexOf("/");
            	String fileName = entryName.substring(i+1);
            	
            	String outputFile = JEMM2Constants.COMPARE_PATH + fileName;
            	System.out.println("Output FIle = " + outputFile);
            	
            	if (fileName.startsWith("thumbnail"))
            	{
            		thumbnail = outputFile;
            	}
            	else
            	{
            		pages.add(outputFile);
            	}
            	
            	Files.deleteIfExists(Paths.get(outputFile));
            	Files.copy(zipIn, Paths.get(outputFile));
        	}
 //       	zipIn.
        	
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
    }
    
    public void convertToJPG() throws MagickException, IOException, InterruptedException, IM4JavaException
    {
//    	ImageInfo info = new ImageInfo(thumbnail);
//        //Create MagickImage that converts format
//        MagickImage magick_converter = new MagickImage(info); 
//        //Specify output file name
////        String outputfile = "jmagick_png2jpg.jpg";
//        String outputfile = thumbnail.replace(".bmp", ".jpg");
//        //Set output format
//        magick_converter.setFileName(outputfile);
//        //Write JPG file
//        magick_converter.writeImage(info);
    	ConvertCmd cmd = new ConvertCmd();
    	cmd.setSearchPath("/Users/wadeshromoff/documents/WAS_Software/ImageMagick-7.0.8/bin");

    	String outputfile = thumbnail.replace(".bmp", ".jpg");
    	// create the operation, add images and operators/options
    	IMOperation op = new IMOperation();
//    	op.addImage(thumbnail);
//    	op.resize(1400,900);
//    	op.addImage(outputfile);
    	
    	// Page 3
    	op.addImage("/Users/wadeshromoff/assets/Upload/page2.bmp");
//    	op.resize(1400,900);
    	op.addImage("/Users/wadeshromoff/assets/Upload/page2.jpg");
   	

    	// execute the operation
    	cmd.run(op);
    }
}
