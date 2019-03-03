package com.jostens.jemm2.image;

import java.nio.ByteBuffer;

public class PNGFileChunk
{
	private byte[] length;
	private byte[] type;
	private byte[] data;
	private byte[] crc;
	private boolean validCRC = false;
	
	public byte[] getLength()
	{
		return length;
	}
	public void setLength(byte[] length)
	{
		this.length = length;
	}
	public byte[] getType()
	{
		return type;
	}
	public void setType(byte[] type)
	{
		this.type = type;
	}
	public byte[] getData()
	{
		return data;
	}
	public void setData(byte[] data)
	{
		this.data = data;
	}
	public byte[] getCrc()
	{
		return crc;
	}
	public void setCrc(byte[] crc)
	{
		this.crc = crc;
	}

	public int getIntLength()
	{
		return ByteBuffer.wrap(length, 0, 4).getInt();
	}
	public int getIntCrc()
	{
		return ByteBuffer.wrap(crc, 0, 4).getInt();
	}

	public int getChunkSize()
	{
		return getIntLength() + 12;
	}

    private byte[] IHDR = {73, 72, 68, 82};
    private byte[] IEND = {73, 69, 78, 68};

//    private static final int IHDR = 0x49484452;
//    private static final int IEND = 0x49454E44;

	public boolean isIHDR()
	{
        for(int i = 0; i < IHDR.length; i++)
        {
            if(type[i] != IHDR[i])
            {
                return false;
            }
        }
        return true;
		
	}
	public boolean isIEND()
	{
        for(int i = 0; i < IEND.length; i++)
        {
            if(type[i] != IEND[i])
            {
                return false;
            }
        }
        return true;		
	}
	
	public boolean isValidCRC()
	{
		return validCRC;
	}
	public void setValidCRC(boolean validCRC)
	{
		this.validCRC = validCRC;
	}
}
