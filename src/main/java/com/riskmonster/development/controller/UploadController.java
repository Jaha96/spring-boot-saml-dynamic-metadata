package com.riskmonster.development.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.riskmonster.development.conf.CommonValues;

import au.com.bytecode.opencsv.CSVWriter;

@Controller
public class UploadController {
	
	@Autowired
    ResourceLoader resourceLoader;

	@Value("${server.ssl.key-store-password}")
	private String passphrase;

	@Value("${server.ssl.key-store}")
	private String keyStore;

	// Save the uploaded file to this folder
	private String UPLOADED_FOLDER = CommonValues.UPLOADED_FOLDER;

	@GetMapping("/upload")
	public String index() {
		return "upload";
	}

	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("companyId") String companyId,
			@RequestParam("xmlFile") MultipartFile xmlFile, @RequestParam("certFile") MultipartFile certFile,
			RedirectAttributes redirectAttributes)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException {
		List<String> xmlFileRequirement = Arrays.asList("text/xml", "application/xml");
		List<String> certFileRequirement = Arrays.asList("application/pkix-cert", "application/x-x509-ca-cert");

		if (xmlFile.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a xmlFile file to upload");
			return "redirect:uploadStatus";
		}

		if (certFile.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a certFile file to upload");
			return "redirect:uploadStatus";
		}
		
		try {
			File dir = new File(UPLOADED_FOLDER);
			boolean results = dir.mkdirs();
			System.out.print(results);
		} catch (Exception e) {
			// TODO: handle exception
		}

		String xmlFileContentType = xmlFile.getContentType();
		if (!xmlFileRequirement.contains(xmlFileContentType)) {
			redirectAttributes.addFlashAttribute("message", "xml metadata file format incorrect");
			return "redirect:uploadStatus";
		}

		String cerFileContentType = certFile.getContentType();
		if (!certFileRequirement.contains(cerFileContentType)) {
			redirectAttributes.addFlashAttribute("message", "certificate file format incorrect");
			return "redirect:uploadStatus";
		}

		try {

			// Get the file and save it somewhere
			byte[] bytes = xmlFile.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + xmlFile.getOriginalFilename());
			Files.write(path, bytes);

			byte[] certbytes = certFile.getBytes();
			Path certpath = Paths.get(UPLOADED_FOLDER + certFile.getOriginalFilename());
			Files.write(certpath, certbytes);

			// 1. Write to text file Actually DB file
			// 2. Import cert file to jks file

			String entityId = extractIdPEntityId(path.toString());

			writeToCsv(companyId, entityId, path);
			importCertificate(certpath.toString(), companyId);

			redirectAttributes.addFlashAttribute("message", "You successfully uploaded '"
					+ xmlFile.getOriginalFilename() + ", " + certFile.getOriginalFilename() + "'");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

	private void importCertificate(String certPath, String companyId)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		File certificationFile = new File(certPath);
		InputStream azureCert = new FileInputStream(certificationFile);
//		InputStream certIn = ClassLoader.class.getResourceAsStream(certPath);
		
		Resource resource = resourceLoader.getResource(keyStore);

		File jksKeyStoreFile = resource.getFile();
		InputStream localKeyStore = new FileInputStream(jksKeyStoreFile);
		String alias = companyId + "_alias";

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(localKeyStore, passphrase.toCharArray());
		if (keystore.containsAlias(alias)) {
			azureCert.close();
			localKeyStore.close();
			return;
		}
		
		localKeyStore.close();

		BufferedInputStream bis = new BufferedInputStream(azureCert);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		while (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			keystore.setCertificateEntry(alias, cert);
		}
		
		azureCert.close();

		OutputStream out = new FileOutputStream(jksKeyStoreFile);
		keystore.store(out, passphrase.toCharArray());
		out.close();
	}

	private void writeToCsv(String companyId, String entityId, Path xmlPath) throws IOException {
		Path certpath = Paths.get(CommonValues.CSV_DB);
		
		CSVWriter writer = new CSVWriter(new FileWriter(certpath.toString(), true));
	    
	    String xmlPathString = pathToPortableString(xmlPath);

		String[] record = { companyId, entityId, xmlPathString };

		writer.writeNext(record);
		// close the writer
		writer.close();
	}
	
	private String extractIdPEntityId(String metadataXmlPath) {

		try {
			File fXmlFile = new File(metadataXmlPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("EntityDescriptor");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String entityId = eElement.getAttribute("entityID");

					System.out.println("entityID : " + entityId);
					return entityId;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "";
	}
	
	
	
	private String pathToPortableString(Path p)
	{
	    StringBuilder sb = new StringBuilder();
	    boolean first = true;
	    Path root = p.getRoot();
	    if (root != null)
	    {
	        sb.append(root.toString().replace('\\','/'));
	        /* root elements appear to contain their
	         * own ending separator, so we don't set "first" to false
	         */            
	    }
	    for (Path element : p)
	    {
	       if (first)
	          first = false;
	       else
	          sb.append("/");
	       sb.append(element.toString());
	    }
	    return sb.toString();        
	}
	
}
