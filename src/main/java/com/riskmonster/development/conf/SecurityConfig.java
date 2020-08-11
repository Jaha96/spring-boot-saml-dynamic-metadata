package com.riskmonster.development.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.ulisesbocchio.spring.boot.security.saml.bean.SAMLConfigurerBean;

//@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
    SAMLConfigurerBean saml() {
        return new SAMLConfigurerBean();
    }
	
	@Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	 
	@Configuration
	public static class MvcConfig implements WebMvcConfigurer {
	
	    @Override
	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/").setViewName("index");
	        registry.addViewController("/protected").setViewName("protected");
	        registry.addViewController("/afterlogout").setViewName("afterlogout");
	
	    }
	}
    
    //Needed in some cases to prevent infinite loop 
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(null);
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.httpBasic()
	        .disable()
	        .csrf()
	        .disable()
	        .anonymous()
	    .and()
	    .apply(saml())
	        .serviceProvider()
		        .metadataGenerator() //(1)
		        .entityId("riskmonster-demo")
		    .and()
		        .sso() //(2)
		        .defaultSuccessURL("/home")
		        .idpSelectionPageURL("/idpselection")
		    .and()
		        .logout() //(3)
		        .defaultTargetURL("/")
		    .and()
		        .metadataManager() //(4)
		        .metadataLocations("classpath:/idp-ssocircle.xml")
		        .refreshCheckInterval(0)
		    .and()
		        .extendedMetadata() //(5)
		        .idpDiscoveryEnabled(true)
		    .and()
		        .keyManager() //(6)
		        .privateKeyDERLocation("classpath:/localhost.key.der")
		        .publicKeyPEMLocation("classpath:/localhost.cert")
	        .and()
				.http()
				    .authorizeRequests()
				    .requestMatchers(saml().endpointsMatcher())
				    .permitAll()
			.and()
			    .authorizeRequests()
			    .regexMatchers("/")
			    .permitAll();
    
	}
}
