package com.riskmonster.development.service.impl;

import org.springframework.stereotype.Service;

import com.riskmonster.development.service.SamlAuthProviderService;

@Service
public class SamlAuthServiceImpl implements SamlAuthProviderService{

	@Override
	public String getMetadataForConnection(String entityID) {
//		Bring XML Data from DB here
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + 
				"<EntityDescriptor entityID=\"http://idp.ssocircle.com\" xmlns=\"urn:oasis:names:tc:SAML:2.0:metadata\">\r\n" + 
				"    <IDPSSODescriptor WantAuthnRequestsSigned=\"false\" protocolSupportEnumeration=\"urn:oasis:names:tc:SAML:2.0:protocol\">\r\n" + 
				"        <KeyDescriptor use=\"signing\">\r\n" + 
				"            <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\r\n" + 
				"                <ds:X509Data>\r\n" + 
				"                    <ds:X509Certificate>\r\n" + 
				"                        MIICjDCCAXSgAwIBAgIFAJRvxcMwDQYJKoZIhvcNAQEEBQAwLjELMAkGA1UEBhMCREUxEjAQBgNV\r\n" + 
				"                        BAoTCVNTT0NpcmNsZTELMAkGA1UEAxMCQ0EwHhcNMTEwNTE3MTk1NzIxWhcNMTYwODE3MTk1NzIx\r\n" + 
				"                        WjBLMQswCQYDVQQGEwJERTESMBAGA1UEChMJU1NPQ2lyY2xlMQwwCgYDVQQLEwNpZHAxGjAYBgNV\r\n" + 
				"                        BAMTEWlkcC5zc29jaXJjbGUuY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbzDRkudC/\r\n" + 
				"                        aC2gMqRVVaLdPJJEwpFB4o71fR5bnNd2ocnnNzJ/W9CoCargzKx+EJ4Nm3vWmX/IZRCFvrvy9C78\r\n" + 
				"                        fP1cmt6Sa091K9luaMAyWn7oC8h/YBXH7rB42tdvWLY4Kl9VJy6UCclvasyrfKx+SR4KU6zCsM62\r\n" + 
				"                        2Kvp5wW67QIDAQABoxgwFjAUBglghkgBhvhCAQEBAf8EBAMCBHAwDQYJKoZIhvcNAQEEBQADggEB\r\n" + 
				"                        AJ0heua7mFO3QszdGu1NblGaTDXtf6Txte0zpYIt+8YUcza2SaZXXvCLb9DvGxW1TJWaZpPGpHz5\r\n" + 
				"                        tLXJbdYQn7xTAnL4yQOKN6uNqUA/aTVgyyUJkWZt2giwEsWUvG0UBMSPS1tp2pV2c6/olIcbdYU6\r\n" + 
				"                        ZecUz6N24sSS7itEBC6nwCVBoHOL8u6MsfxMLDzJIPBI68UZjz3IMKTDUDv6U9DtYmXLc8iMVZBn\r\n" + 
				"                        cYJn9NgNi3ghl9fYPpHcc6QbXeDUjhdzXXUqG+hB6FabGqdTdkIZwoi4gNpyr3kacKRVWJssDgak\r\n" + 
				"                        eL2MoDNqJyQ0fXC6Ze3f79CKy/WjeU5FLwDZR0Q=\r\n" + 
				"                    </ds:X509Certificate>\r\n" + 
				"                </ds:X509Data>\r\n" + 
				"            </ds:KeyInfo>\r\n" + 
				"        </KeyDescriptor>\r\n" + 
				"        <KeyDescriptor use=\"encryption\">\r\n" + 
				"            <ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\r\n" + 
				"                <ds:X509Data>\r\n" + 
				"                    <ds:X509Certificate>\r\n" + 
				"                        MIICjDCCAXSgAwIBAgIFAJRvxcMwDQYJKoZIhvcNAQEEBQAwLjELMAkGA1UEBhMCREUxEjAQBgNV\r\n" + 
				"                        BAoTCVNTT0NpcmNsZTELMAkGA1UEAxMCQ0EwHhcNMTEwNTE3MTk1NzIxWhcNMTYwODE3MTk1NzIx\r\n" + 
				"                        WjBLMQswCQYDVQQGEwJERTESMBAGA1UEChMJU1NPQ2lyY2xlMQwwCgYDVQQLEwNpZHAxGjAYBgNV\r\n" + 
				"                        BAMTEWlkcC5zc29jaXJjbGUuY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbzDRkudC/\r\n" + 
				"                        aC2gMqRVVaLdPJJEwpFB4o71fR5bnNd2ocnnNzJ/W9CoCargzKx+EJ4Nm3vWmX/IZRCFvrvy9C78\r\n" + 
				"                        fP1cmt6Sa091K9luaMAyWn7oC8h/YBXH7rB42tdvWLY4Kl9VJy6UCclvasyrfKx+SR4KU6zCsM62\r\n" + 
				"                        2Kvp5wW67QIDAQABoxgwFjAUBglghkgBhvhCAQEBAf8EBAMCBHAwDQYJKoZIhvcNAQEEBQADggEB\r\n" + 
				"                        AJ0heua7mFO3QszdGu1NblGaTDXtf6Txte0zpYIt+8YUcza2SaZXXvCLb9DvGxW1TJWaZpPGpHz5\r\n" + 
				"                        tLXJbdYQn7xTAnL4yQOKN6uNqUA/aTVgyyUJkWZt2giwEsWUvG0UBMSPS1tp2pV2c6/olIcbdYU6\r\n" + 
				"                        ZecUz6N24sSS7itEBC6nwCVBoHOL8u6MsfxMLDzJIPBI68UZjz3IMKTDUDv6U9DtYmXLc8iMVZBn\r\n" + 
				"                        cYJn9NgNi3ghl9fYPpHcc6QbXeDUjhdzXXUqG+hB6FabGqdTdkIZwoi4gNpyr3kacKRVWJssDgak\r\n" + 
				"                        eL2MoDNqJyQ0fXC6Ze3f79CKy/WjeU5FLwDZR0Q=\r\n" + 
				"                    </ds:X509Certificate>\r\n" + 
				"                </ds:X509Data>\r\n" + 
				"            </ds:KeyInfo>\r\n" + 
				"            <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes128-cbc\">\r\n" + 
				"                <xenc:KeySize xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">128</xenc:KeySize>\r\n" + 
				"            </EncryptionMethod>\r\n" + 
				"        </KeyDescriptor>\r\n" + 
				"        <ArtifactResolutionService index=\"0\" isDefault=\"true\" Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:SOAP\"\r\n" + 
				"                                   Location=\"https://idp.ssocircle.com:443/sso/ArtifactResolver/metaAlias/ssocircle\"/>\r\n" + 
				"        <SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/IDPSloRedirect/metaAlias/ssocircle\"\r\n" + 
				"                             ResponseLocation=\"https://idp.ssocircle.com:443/sso/IDPSloRedirect/metaAlias/ssocircle\"/>\r\n" + 
				"        <SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/IDPSloPost/metaAlias/ssocircle\"\r\n" + 
				"                             ResponseLocation=\"https://idp.ssocircle.com:443/sso/IDPSloPost/metaAlias/ssocircle\"/>\r\n" + 
				"        <SingleLogoutService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:SOAP\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/IDPSloSoap/metaAlias/ssocircle\"/>\r\n" + 
				"        <ManageNameIDService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/IDPMniRedirect/metaAlias/ssocircle\"\r\n" + 
				"                             ResponseLocation=\"https://idp.ssocircle.com:443/sso/IDPMniRedirect/metaAlias/ssocircle\"/>\r\n" + 
				"        <ManageNameIDService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/IDPMniPOSTmetaAlias/ssocircle\"\r\n" + 
				"                             ResponseLocation=\"https://idp.ssocircle.com:443/sso/IDPMniPOST/metaAlias/ssocircle\"/>\r\n" + 
				"        <ManageNameIDService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:SOAP\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/IDPMniSoap/metaAlias/ssocircle\"/>\r\n" + 
				"        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:persistent</NameIDFormat>\r\n" + 
				"        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:transient</NameIDFormat>\r\n" + 
				"        <NameIDFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified</NameIDFormat>\r\n" + 
				"        <NameIDFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress</NameIDFormat>\r\n" + 
				"        <NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:kerberos</NameIDFormat>\r\n" + 
				"        <SingleSignOnService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/SSORedirect/metaAlias/ssocircle\"/>\r\n" + 
				"        <SingleSignOnService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/SSOPOST/metaAlias/ssocircle\"/>\r\n" + 
				"        <SingleSignOnService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:SOAP\"\r\n" + 
				"                             Location=\"https://idp.ssocircle.com:443/sso/SSOSoap/metaAlias/ssocircle\"/>\r\n" + 
				"        <NameIDMappingService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:SOAP\"\r\n" + 
				"                              Location=\"https://idp.ssocircle.com:443/sso/NIMSoap/metaAlias/ssocircle\"/>\r\n" + 
				"    </IDPSSODescriptor>\r\n" + 
				"</EntityDescriptor>\r\n" + 
				"";
		return xml;
	}

}
