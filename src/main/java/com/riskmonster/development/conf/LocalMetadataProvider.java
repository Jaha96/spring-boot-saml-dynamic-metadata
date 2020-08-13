package com.riskmonster.development.conf;

import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class LocalMetadataProvider extends AbstractReloadingMetadataProvider {
	
	private final String Id;
    private final String xmlData;

    @Autowired
    private ApplicationContext context;

    

    public LocalMetadataProvider(String id, String xmlData) {
        this.Id = id;
        this.xmlData = xmlData;

//        setParserPool(LocalBeanUtil.getBeanOrThrow(ParserPool.class));
        
        setParserPool(context.getBean(ParserPool.class));
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
