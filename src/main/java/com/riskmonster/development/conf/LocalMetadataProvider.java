package com.riskmonster.development.conf;

import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;

import com.riskmonster.development.service.impl.LocalBeanUtil;

public class LocalMetadataProvider extends AbstractReloadingMetadataProvider {
	
	private final String Id;
    private final String xmlData;


    public LocalMetadataProvider(String id, String xmlData) {
        this.Id = id;
        this.xmlData = xmlData;

        setParserPool(LocalBeanUtil.getBeanOrThrow(ParserPool.class));
        
    }

    @Override
    protected String getMetadataIdentifier() {
        return this.Id;
    }

    @Override
    protected byte[] fetchMetadata() throws MetadataProviderException {
        return xmlData.getBytes();
    }
}
