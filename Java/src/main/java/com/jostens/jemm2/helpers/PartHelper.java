package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jostens.jemm2.pojo.Part;

public class PartHelper
{

	/**
	 * Find all the part load files and call method to persist Part objects
	 * @throws IOException 
	 */
	public List<Part> getPartObjects(String fileName) throws IOException
	{
		List<Part> parts = new ArrayList<Part>();

        InputStream stream = DesignHelper.class.getResourceAsStream("/com/jostens/jemm2/jdbc/resources/" + fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
//            System.out.println("Line entered : " + line);
            
        	parts.add(new Part(line));
        }
		
		return parts;
	}

}
