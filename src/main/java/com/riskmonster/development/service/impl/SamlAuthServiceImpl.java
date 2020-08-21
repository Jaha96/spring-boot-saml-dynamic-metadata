package com.riskmonster.development.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.riskmonster.development.service.SamlAuthProviderService;

@Service
public class SamlAuthServiceImpl implements SamlAuthProviderService {

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public String getMetadataForConnection(String entityID) {
//		Bring XML Data from DB here
		if ("http://idp.ssocircle.com".equals(entityID))
			return XMLtoString("classpath:idp-ssocircle.xml");

		if ("https://sts.windows.net/53bf0138-a54b-4cec-90c6-0dfee29fed3e/".equals(entityID))
			return XMLtoString("classpath:azure-saml-app-test.xml");
		
		if ("http://www.okta.com/exkqbohppTmU9ZXFM4x6".equals(entityID))
//			http://www.okta.com/exkqbohppTmU9ZXFM4x6
			return XMLtoString("classpath:idp-okta.xml");

		return "";
	}

	private String XMLtoString(String xmlPath) {
		try {
			Resource resource = resourceLoader.getResource(xmlPath);
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}
