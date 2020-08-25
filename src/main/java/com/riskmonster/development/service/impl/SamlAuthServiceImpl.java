package com.riskmonster.development.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.riskmonster.development.conf.CommonValues;
import com.riskmonster.development.service.SamlAuthProviderService;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class SamlAuthServiceImpl implements SamlAuthProviderService {

	@Autowired
	ResourceLoader resourceLoader;
	
	private String csv_db = CommonValues.CSV_DB;

	@Override
	public String getMetadataForConnection(String entityID) {
		
//		Bring XML Data from DB here
		String metadataPath = getMetadataPath(csv_db, entityID);
		return XMLtoString(metadataPath);
	}

	private String XMLtoString(String xmlPath) {
		try {
//			Resource resource = resourceLoader.getResource(xmlPath);
			File file = new File(xmlPath);
			InputStream is = new FileInputStream(file);
			
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			String line = bufReader.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			String xml2String = sb.toString();
			System.out.println("XML to String using BufferedReader : ");
			System.out.println(xml2String);

			bufReader.close();

			return xml2String;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

	}
	
	private String getMetadataPath(String file, String entityId) 
	{ 
	    try { 
	        // Create an object of filereader 
	        // class with CSV file as a parameter. 
	        FileReader filereader = new FileReader(file); 
	  
	        // create csvReader object passing 
	        // file reader as a parameter 
	        CSVReader csvReader = new CSVReader(filereader); 
	        String[] nextRecord; 
	  
	        // we are going to read data line by line 
	        while ((nextRecord = csvReader.readNext()) != null) { 
	        	String dbEntityID = nextRecord[1]; 
	        	if(entityId.equalsIgnoreCase(dbEntityID)) {
	        		
	        		csvReader.close();
	        		return nextRecord[2]; //Metadata path
	        	}
	        } 
	        csvReader.close();
	    } 
	    catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return "";
	}

}
