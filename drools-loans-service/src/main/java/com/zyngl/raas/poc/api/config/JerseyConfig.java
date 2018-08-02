package com.zyngl.raas.poc.api.config;

import javax.annotation.PostConstruct;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zyngl.raas.poc.api.impl.LoanServiceImpl;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/**
 * Jersey Configuration
 * 
 * <p>
 * 
 * The endpoint will expose not only the real APPs,
 * but also two important documents:
 * <li> - swagger spec: /swagger.json</li>
 * <li> - WADL spec: /application.wadl</li>
 * 
 * </p>
 * 
 *
 */
@Component
public class JerseyConfig extends ResourceConfig {

	@Value("${spring.jersey.application-path:/api}")
	private String apiPath;

	public JerseyConfig() {
		this.registerEndpoints();
	}
	
	@PostConstruct
	public void init() {
		this.configureSwagger();
	}

	//FIXME - every time if I need to add the class here, its not gong to scale
	private void registerEndpoints() {
		this.register(LoanServiceImpl.class);
		
		this.register(WadlResource.class);
	}

	private void configureSwagger() {
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig config = new BeanConfig();
		config.setTitle("POC - Restful API by Spring Boot, Jersey, Swagger");
		config.setVersion("v1");
		config.setContact("Reuben John");
		config.setSchemes(new String[] { "http", "https" });
		config.setBasePath(this.apiPath);
		config.setResourcePackage("com.zyngl.raas.poc.api.impl"); //package where all the end points are defined
		config.setPrettyPrint(true);
		config.setScan(true);
	}

}
