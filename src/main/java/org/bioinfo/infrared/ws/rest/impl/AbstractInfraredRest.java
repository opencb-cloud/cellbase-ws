package org.bioinfo.infrared.ws.rest.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.Config;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.common.dbsql.DBConnector;


public abstract class AbstractInfraredRest {

	protected DBConnector infraredDBConnector;
	protected Config config;
	protected String species;
	protected UriInfo uriInfo;
	
	// common parameters
	protected String separator;
	protected String outputFormat;
	protected boolean compress;
	protected String version;
	
	public AbstractInfraredRest(String species, UriInfo uriInfo) {
		this.species = species;
		this.uriInfo = uriInfo;
		parseCommonQueryParameters(uriInfo.getQueryParameters());
	}
	
	protected abstract boolean isValidSpecies(String species);
    
	protected void parseCommonQueryParameters(MultivaluedMap<String, String> multivaluedMap) {
		separator = (multivaluedMap.get("separator") != null) ? multivaluedMap.get("separator").get(0) : "\n";
		outputFormat = (multivaluedMap.get("output") != null) ? multivaluedMap.get("output").get(0) : "txt";
		compress = (multivaluedMap.get("compress") != null) ? Boolean.parseBoolean(multivaluedMap.get("compress").get(0)) : false;
		version = (multivaluedMap.get("version") != null) ? multivaluedMap.get("version").get(0) : "v1";
	}
	
    protected void connect() throws IOException {
    	ResourceBundle databaseConfig = ResourceBundle.getBundle("org.bioinfo.infrared.ws.database");
		config = new Config(databaseConfig);
    	infraredDBConnector = new DBConnector(species, config.getProperty("INFRARED.HOST"), config.getProperty("INFRARED.PORT", "3306"), config.getProperty("INFRARED."+species.toUpperCase()+".DATABASE"), config.getProperty("INFRARED.USER"), config.getProperty("INFRARED.PASSWORD"));
    }
    
    protected void disconnect() throws SQLException {
    	if(infraredDBConnector != null) {
    		infraredDBConnector.disconnect();
    	}
    }
    
    protected Response generateResponse(String entity, String outputFormat, boolean compress) throws IOException {
    	MediaType mediaType = MediaType.valueOf("text/plain");
    	if(outputFormat != null && outputFormat.equals("json")) {
    		mediaType =  MediaType.valueOf("application/json");
    	}
    	if(outputFormat != null && outputFormat.equals("xml")) {
    		mediaType =  MediaType.valueOf("text/xml");
    	}
    	if(compress) {
    		mediaType =  MediaType.valueOf("application/zip");
    		return Response.ok(StringUtils.zipToBytes(entity), mediaType).build();
    	}else {
    		return Response.ok(entity, mediaType).build();
    	}
	}

    protected Response generateErrorMessage(String errorMessage) {
    	return Response.ok("An error occurred: "+errorMessage, MediaType.valueOf("text/plain")).build();
    }
    
}
