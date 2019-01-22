package com.jostens.jemm2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.jostens.jemm2.pojo.Design;

public class DesignHelper
{
	public Design getDesign(String definition)
	{
		Design design = new Design();
		
		StringTokenizer st = new StringTokenizer(definition, "|");
		design.setName(st.nextToken());
		design.setKeywords(st.nextToken());
		
		System.out.println("name=" + design.getName());
		
		return design;
	}

	/**
	 * Find all the design load files and call method to persist Design objects
	 * @throws IOException 
	 */
	public List<Design> getDesignObjects(String fileName) throws IOException
	{
		List<Design> designs = new ArrayList<Design>();

        InputStream stream = DesignHelper.class.getResourceAsStream("/com/jostens/jemm2/jdbc/resources/" + fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            System.out.println("Line entered : " + line);
            
            designs.add(new Design(line));
        }
		
		return designs;
	}
}
