package com.jostens.jemm2.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

import com.jostens.jemm2.JEMM2Constants;

public class PNGFile
{
	// Image as an array of bytes
	private byte[] imageAsBytes = null;
	// Flag indicating if png file Signature
	private boolean pngSignature = false;
	// All the chunks for this image
	private List<PNGFileChunk> chunks = new ArrayList<PNGFileChunk>();
	// Flag to indicate if this entire file is valid based on CRC check
	private boolean pngFileValid = true;
	
	public PNGFile()
	{
		
	}

	public PNGFile(String fileName) throws IOException
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

	public PNGFile(InputStream input) throws IOException
	{
		loadImage(input);
	}
	
    public void loadImage(InputStream input) throws IOException
    {
    	if (input == null)
    	{
      		pngFileValid = false;		// File stream not valid so mark this entire file as invalid
    		System.out.println("Invalid PNG File Stream");
    		return;
    	}
    	// Read the file into a buffer
    	imageAsBytes = getImageAsByteArray(input);
//    	System.out.println("LENGTH = " + imageAsBytes.length);
    	pngSignature = isValidSignature();
    	if (!pngSignature)
    	{
      		pngFileValid = false;		// Signature not valid so mark this entire file as invalid
    		System.out.println("Invalid PNG Signature");
    		return;
    	}
    	// Load all the chunks
    	getAllChunks();
    	
    }
    
    public byte[] getImageAsByteArray(InputStream is) throws IOException
    {
    	ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    	int nRead;
    	byte[] data = new byte[16384];

    	while ((nRead = is.read(data, 0, data.length)) != -1) {
    	  buffer.write(data, 0, nRead);
    	}

    	return buffer.toByteArray();    	
    }
    
    public boolean isValidSignature()
    {
    	byte[] SIGNATURE = {(byte)137, 80, 78, 71, 13, 10, 26, 10};
    	
        for(int i = 0; i < SIGNATURE.length; i++)
        {
            if(imageAsBytes[i] != SIGNATURE[i])
            {
                return false;
            }
        }
        return true;
    }
    
    public PNGFileChunk getNextChunk(int start)
    {
    	PNGFileChunk chunk = new PNGFileChunk();
    	// First 4 bytes make up the length
    	chunk.setLength(Arrays.copyOfRange(imageAsBytes, start, start + 4));
//    	displayBytes(chunk.getLength(), 0, 4);
//    	System.out.println("Chunk length is = " + chunk.getIntLength());
    	// Next 4 bytes make up the type
    	chunk.setType(Arrays.copyOfRange(imageAsBytes, start + 4, start + 8));
    	int dataLength = chunk.getIntLength();
//    	System.out.println("DATALENGTH = " + dataLength);
    	// Datalength is the data for this chunk
    	chunk.setData(Arrays.copyOfRange(imageAsBytes, start + 8, start + 8 + dataLength));
    	// Next 4 bytes make up the CRC
//    	System.out.println("SIZE=" + imageAsBytes.length);
//       	System.out.println("1=" + (start + 8 + dataLength));
//      	System.out.println("2=" + (start + 8 + dataLength + 4));
      	        	chunk.setCrc(Arrays.copyOfRange(imageAsBytes, start + 8 + dataLength, start + 8 + dataLength + 4));
//    	displayBytes(chunk.getCrc(), 0, 4);
      	
      	// Validate CRC
      	boolean validCRC = validateCRC(chunk);
      	if (!validCRC)
      	{
      		pngFileValid = false;		// A CRC check failed so mark this entire file as invalid
      	}
      	chunk.setValidCRC(validCRC);
//    	System.out.println("Sizes = " + start + " : " + chunk.getChunkSize() + " : " + imageAsBytes.length + " : " + validCRC);
    	
    	return chunk;
    }
    
    public boolean validateCRC(PNGFileChunk aChunk)
    {
    	// Take CRC value from the chunk and convert to int
    	int crc = ByteBuffer.wrap(aChunk.getCrc(), 0, 4).getInt();
        long chunkCRC = crc & 0x00000000ffffffffL; // Make it unsigned.
//        System.out.println("Chunk CRC = " + chunkCRC);

    	
        CRC32 crc32 = new CRC32();
        crc32.update(aChunk.getType());
        crc32.update(aChunk.getData());
        long calculatedCRC = crc32.getValue();
        
//        System.out.println("Calculated CRC = " + calculatedCRC);
        return chunkCRC == calculatedCRC;
    }
    
    public void getAllChunks()
    {
//    	int chunkCount = 1;
    	int offset = 8;
    	while (true)
    	{
    		PNGFileChunk aChunk = getNextChunk(offset);
//    		if (aChunk.getChunkSize() == 12)
    		if (aChunk.isIEND())
    		{
    			break;		// IEND Chunk
    		}
    		if (chunks.isEmpty() && !aChunk.isIHDR())
    		{
          		pngFileValid = false;		// First chunk not header so mark this entire file as invalid
    			System.out.println("First CHUNK is not a Header");
    			return;
    		}
    		chunks.add(aChunk);
//    		chunkCount++;
    		offset += aChunk.getChunkSize();
    		if (offset >= imageAsBytes.length)
    		{
    			break;
    		}
    	}
    }


	public void setImageAsBytes(byte[] imageAsBytes)
	{
		this.imageAsBytes = imageAsBytes;
	}
	public byte[] getImageAsBytes()
	{
		return imageAsBytes;
	}

	public void displayBytes(int start, int count)
	{
		for (int i = 0; i < count; i++)
		{
			System.out.println(i+1 + " : " + imageAsBytes[start + i]);
		}
	}
	public void displayBytes(byte[] array, int start, int count)
	{
		for (int i = 0; i < count; i++)
		{
			System.out.println(i+1 + " : " + array[start + i]);
		}
	}

	public boolean isPngFileValid()
	{
		return pngFileValid;
	}

	/**
	 * Get String of all Chunk CRC's concatenated together.  If 2 png's have this match they would be identical
	 * @throws UnsupportedEncodingException 
	 */
	public String getALLCRCBytes() throws UnsupportedEncodingException
	{
		StringBuffer sb = new StringBuffer();
		for (PNGFileChunk chunk : chunks)
		{
			String s = new String(chunk.getCrc(), 0, 4, "UTF-8");
			sb.append(s);
//			sb.append(chunk.getCrc());
		}
		return sb.toString();
	}
	
	public int getImageSize()
	{
		return imageAsBytes.length;
	}

	public List<PNGFileChunk> getChunks()
	{
		return chunks;
	}
	
	
}
