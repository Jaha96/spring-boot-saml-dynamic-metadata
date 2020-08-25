package com.riskmonster.development.controller;

import java.io.FileReader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Ulises Bocchio
 */

import com.github.ulisesbocchio.spring.boot.security.saml.annotation.SAMLUser;
import com.github.ulisesbocchio.spring.boot.security.saml.user.SAMLUserDetails;
import com.riskmonster.development.conf.CommonValues;

import au.com.bytecode.opencsv.CSVReader;

@Controller
public class HomeController {

	@RequestMapping("/home")
	public ModelAndView home(@SAMLUser SAMLUserDetails user) {
		ModelAndView homeView = new ModelAndView("home");
		homeView.addObject("userId", user.getUsername());
		homeView.addObject("samlAttributes", user.getAttributes());
		return homeView;
	}

	@PostMapping("/loginwithsaml")
	public String home(@RequestParam(name = "companyId") String companyId) {

		if (companyId != null) {
			String entityId = getEntityId(CommonValues.CSV_DB, companyId);
			String redirectUrl = "/saml/login?idp=" + entityId;
			return "redirect:" + redirectUrl;
		}

		return "";
	}

	private String getEntityId(String file, String companyId) {
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
				String dbCompanyID = nextRecord[0];
				if (companyId.equalsIgnoreCase(dbCompanyID)) {

					csvReader.close();
					return nextRecord[1]; // EntityId
				}
			}
			csvReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
