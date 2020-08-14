package com.riskmonster.development.conf;

import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.security.saml.metadata.CachingMetadataManager;

import com.riskmonster.development.service.SamlAuthProviderService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalMetadataManagerAdapter extends CachingMetadataManager {
	
	private final SamlAuthProviderService samlAuthProviderService;

    public LocalMetadataManagerAdapter(SamlAuthProviderService samlAuthProviderService) throws MetadataProviderException {
        super(null);
        this.samlAuthProviderService = samlAuthProviderService;
    }
    
    @Override
    public boolean isRefreshRequired() {
    	return false;
    }
    
    @Override
    public EntityDescriptor getEntityDescriptor(String entityID) throws MetadataProviderException {
    	// we don't really want to use our default at all, so we're going to throw an error
        // this string value is defined in the "classpath:/saml/idp-metadata.xml" file:
        // which is then referenced in application.properties as saml.sso.idp.metadata-location=classpath:/saml/idp-metadata.xml
        if("defaultidpmetadata".equals(entityID)) {
        	System.out.println("Jaha. Unable to process requests for default idp. Please select idp with ?idp=x parameter.");
//            exNotFound("Unable to process requests for default idp. Please select idp with ?idp=x parameter.");
            throw new MetadataProviderException("Unable to process requests for default idp. Please select idp with ?idp=x parameter.");
        }

        EntityDescriptor staticEntity = super.getEntityDescriptor(entityID);

        if(staticEntity != null)
            return staticEntity;

        // we need to inject one, and try again:
        injectProviderMetadata(entityID);

        return super.getEntityDescriptor(entityID);
    }
    
    @SneakyThrows
    private void injectProviderMetadata(String entityID) {
        String xml =
            samlAuthProviderService.getMetadataForConnection(entityID);
//                .orElseThrow(() -> exRuntime("Unable to find metadata for entity: " + entityID));

        try {
			addMetadataProvider(new LocalMetadataProvider(entityID, xml));
		} catch (MetadataProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // this will force a refresh/re-wrap of the new entity
        super.refreshMetadata();
    }
    
}
