package com.jostens.jemm2.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

public class PNGFile
{
	// Image as an array of bytes
	private byte[] imageAsBytes = null;
	// Flag indicating if png file Signature
	private boolean pngSignature = false;
	
	public PNGFile()
	{
		
	}

    public PNGFile(InputStream input) throws IOException
    {
    	if (input == null)
    	{
    		System.out.println("Invalid PNG File Stream");
    		return;
    	}
    	// Read the file into a buffer
    	imageAsBytes = getImageAsByteArray(input);
//    	System.out.println("LENGTH = " + imageAsBytes.length);
    	pngSignature = isValidSignature();
    	if (!pngSignature)
    	{
    		System.out.println("Invalid PNG Signature");
    		return;
    	}
    	
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
      	chunk.setValidCRC(validCRC);
    	System.out.println("Sizes = " + start + " : " + chunk.getChunkSize() + " : " + imageAsBytes.length + " : " + validCRC);
    	
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
    	int chunkCount = 1;
    	int offset = 8;
    	while (true)
    	{
    		PNGFileChunk aChunk = getNextChunk(offset);
//    		if (aChunk.getChunkSize() == 12)
    		if (aChunk.isIEND())
    		{
    			break;		// IEND Chunk
    		}
    		if (chunkCount == 1 && !aChunk.isIHDR())
    		{
    			System.out.println("First CHUNK is not a Header");
    			return;
    		}
    		chunkCount++;
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
    
}
