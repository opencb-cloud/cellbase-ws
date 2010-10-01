package org.bioinfo.infrared.ws.server.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.Config;
import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.common.DBConnector;
import org.bioinfo.infrared.core.common.Feature;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.variation.TranscriptConsequenceType;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class AbstractInfraredRest {

	protected DBConnector infraredDBConnector;
	protected Config config;

	protected String version;
	protected String species;
	protected UriInfo uriInfo;

	// common parameters
	protected String separator;
	protected String outputFormat;
	protected boolean compress;
	protected  Type listType;

	//	public AbstractInfraredRest(String species, UriInfo uriInfo) {
	//		this.species = species;
	//		this.uriInfo = uriInfo;
	//		parseCommonQueryParameters(uriInfo.getQueryParameters());
	//	}

	protected abstract boolean isValidSpecies(String species);

	protected void init(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		this.version = version;
		this.species = species;
		this.uriInfo = uriInfo;

		// load properties file
		ResourceBundle databaseConfig = ResourceBundle.getBundle("org.bioinfo.infrared.ws.application");
		config = new Config(databaseConfig);

		// check public version
		if(StringUtils.toList(config.getProperty("PUBLIC.VERSION"), ",").contains(version)) {
			parseCommonQueryParameters(uriInfo.getQueryParameters());
		}else {
			if(StringUtils.toList(config.getProperty("PRIVATE.VERSION"), ",").contains(version)) {
				if(uriInfo.getQueryParameters().get("user") != null && uriInfo.getQueryParameters().get("user").get(0).equals(config.getProperty("PRIVATE.VERSION.USER")) && uriInfo.getQueryParameters().get("password") != null && uriInfo.getQueryParameters().get("password").get(0).equals(config.getProperty("PRIVATE.VERSION.PASSWORD"))) {
					parseCommonQueryParameters(uriInfo.getQueryParameters());
				}else {
					throw new VersionException("No user or password valid");
				}		
			}else {
				throw new VersionException("Version '"+version+"' not valid");
			}
		}
	}

	protected void parseCommonQueryParameters(MultivaluedMap<String, String> multivaluedMap) {
		separator = (multivaluedMap.get("separator") != null) ? multivaluedMap.get("separator").get(0) : "\n";
		outputFormat = (multivaluedMap.get("output") != null) ? multivaluedMap.get("output").get(0) : "txt";
		compress = (multivaluedMap.get("compress") != null) ? Boolean.parseBoolean(multivaluedMap.get("compress").get(0)) : false;
		version = (multivaluedMap.get("version") != null) ? multivaluedMap.get("version").get(0) : "v1";
	}

	protected void connect() throws IOException {
		infraredDBConnector = new DBConnector(species, config.getProperty("INFRARED.HOST"), config.getProperty("INFRARED.PORT", "3306"), config.getProperty("INFRARED."+species.toUpperCase()+".DATABASE"), config.getProperty("INFRARED.USER"), config.getProperty("INFRARED.PASSWORD"));
	}

	protected void disconnect() throws SQLException {
		if(infraredDBConnector != null) {
			infraredDBConnector.disconnect();
		}
	}

	protected <E extends Feature> String createResultString(List<String> ids, FeatureList<E> features) {
		if(outputFormat.equals("txt")) {
			StringBuilder result = new StringBuilder();
			for(int i=0; i<ids.size(); i++) {
				if(features.get(i) != null) {
					result.append(ids.get(i)).append(":\t").append(features.get(i).toString()).append(separator);
				}else {
					result.append(ids.get(i)).append(":\t").append("not found").append(separator);
				}
			}
			return result.toString().trim();	
		}else {
			if(outputFormat.equals("json")) {
				return new Gson().toJson(features);
			}
		}
		return "output format '"+outputFormat+"' not valid";
	}

	protected <E extends Feature> String createResultString(List<String> ids, List<FeatureList<E>> features) {
		if(outputFormat.equals("txt")) {
			StringBuilder result = new StringBuilder();
			for(int i=0; i<ids.size(); i++) {
				if(features.get(i) != null && features.get(i).size() > 0) {
					for(E feature: features.get(i)) {
						if(feature != null) {
							result.append(ids.get(i)).append(":\t").append(feature.toString()).append(separator);
						}else {
							result.append(ids.get(i)).append(":\t").append("not found").append(separator);
						}
					}
				}else {
					result.append(ids.get(i)).append(":\t").append("not found").append(separator);
				}
			}
			return result.toString().trim();
		}
		return "output format '"+outputFormat+"' not valid";
	}
	
	protected String createResultStringByTranscriptConsequenceType(List<String> ids, List<List<TranscriptConsequenceType>> features) {
		if(outputFormat.equals("txt")) {
			StringBuilder result = new StringBuilder();
			for(int i=0; i<ids.size(); i++) {
				if(features.get(i) != null && features.get(i).size() > 0) {
					for(TranscriptConsequenceType feature: features.get(i)) {
						if(feature != null) {
							result.append(ids.get(i)).append(":\t").append(feature.toString()).append(separator);
						}else {
							result.append(ids.get(i)).append(":\t").append("not found").append(separator);
						}
					}
				}else {
					result.append(ids.get(i)).append(":\t").append("not found").append(separator);
				}
			}
			return result.toString().trim();
		}else {
			if(outputFormat.equals("json")) {
				return new Gson().toJson(features);
			}
		}
		return "output format '"+outputFormat+"' not valid";
	}

	protected Response generateResponse(String entity, String outputFormat, boolean compress) throws IOException {
		MediaType mediaType = MediaType.valueOf("text/plain");
		if(outputFormat != null && outputFormat.equals("json")) {
			mediaType =  MediaType.valueOf("application/json");
			Gson gson = new Gson();
			System.out.println("Entro: "+entity);
			entity = gson.toJson(entity);
			System.out.println(entity);
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
	protected <E extends Object> Response generateResponse(List<E> entityList, String outputFormat, boolean compress) throws IOException {
		MediaType mediaType = MediaType.valueOf("text/plain");
		String entity = "";
		String zipEntity = "";
		if(outputFormat != null && outputFormat.equals("json")) {
			mediaType =  MediaType.valueOf("application/json");
			Gson gson = new Gson();
			System.out.println("Entro: "+entityList);
			entity = gson.toJson(entityList, this.listType);
			System.out.println(entity);
			zipEntity = Arrays.toString(StringUtils.gzipToBytes(entity)).replace(" " , "");
		}
		if(outputFormat != null && outputFormat.equals("xml")) {
			mediaType =  MediaType.valueOf("text/xml");
		}
		if(compress) {
			mediaType =  MediaType.valueOf("application/zip");
			return Response.ok(zipEntity, mediaType).build();
		}else {
			return Response.ok(entity, mediaType).build();
		}
	}
//	protected Response generateResponse(List<E> entityList, String outputFormat, boolean compress) throws IOException {
//		MediaType mediaType = MediaType.valueOf("text/plain");
//		if(outputFormat != null && outputFormat.equals("json")) {
//			mediaType =  MediaType.valueOf("application/json");
//			Gson gson = new Gson();
//			entity = gson.toJson(entity);
//		}
//		if(outputFormat != null && outputFormat.equals("xml")) {
//			mediaType =  MediaType.valueOf("text/xml");
//		}
//		if(compress) {
//			mediaType =  MediaType.valueOf("application/zip");
//			return Response.ok(StringUtils.zipToBytes(entity), mediaType).build();
//		}else {
//			return Response.ok(entity, mediaType).build();
//		}
//	}
	protected <E extends Feature> Response generateResponse2(FeatureList<E> features, String outputFormat, boolean compress) throws IOException {
		MediaType mediaType = MediaType.valueOf("text/plain");
		String entity = "";
		String zipEntity = "";
		Gson gson = new GsonBuilder().serializeNulls().create();
		//Gson gson = new Gson();
		if(outputFormat != null && outputFormat.equals("txt")) {
			mediaType = MediaType.valueOf("text/plain");
			entity = ListUtils.toString(features, separator);
		}
		if(outputFormat != null && outputFormat.equals("json")) {
			mediaType =  MediaType.valueOf("application/json");
			if(features != null && features.size() > 0 /*&& features.get(0) != null*/) {

				//System.err.println("FeatureList Object Class: "+features.get(0).getClass());
//				if(features.get(0) instanceof Gene) {
//					listType = new TypeToken<FeatureList<Gene>>() {}.getType();
//					
//				}else if(features.get(0) instanceof Transcript) {
//					listType = new TypeToken<FeatureList<Transcript>>() {}.getType();
//				}else if(features.get(0) instanceof Exon) {
//					listType = new TypeToken<FeatureList<Exon>>() {}.getType();
//				}else if(features.get(0) instanceof SNP) {
//					listType = new TypeToken<FeatureList<SNP>>() {}.getType();
//				}else if(features.get(0) instanceof SpliceSite) {
//					listType = new TypeToken<FeatureList<SpliceSite>>() {}.getType();
//				}else if(features.get(0) instanceof ConservedRegion) {
//					listType = new TypeToken<FeatureList<ConservedRegion>>() {}.getType();
//				}else if(features.get(0) instanceof OregannoTfbs) {
//					listType = new TypeToken<FeatureList<OregannoTfbs>>() {}.getType();
//				}else if(features.get(0) instanceof JasparTfbs) {
//					listType = new TypeToken<FeatureList<JasparTfbs>>() {}.getType();
//				}
//				else
//					System.out.println("errrrrrrrrrrrrrrororr");
				if(listType != null && features != null ) {
					System.out.println("Entro3");
					System.out.println("Creating JSON object...");
					entity = gson.toJson(features, listType);
					System.err.println("done!");
					System.err.println("Entity json: "+entity);
					zipEntity = Arrays.toString(StringUtils.gzipToBytes(entity)).replace(" " , "");
					System.err.println("zipEntryBytes: "+StringUtils.gzipToBytes(entity));
					System.err.println("zipEntry: "+zipEntity);
					System.out.println("entity.length(): "+entity.length());
					System.out.println("zipEntity.length(): "+zipEntity.length());
					
				}else {
					System.err.println("AbstractInfraredRest: TypeToken from Gson equals null");
				}
			}
		}
		
		if(outputFormat != null && outputFormat.equals("xml")) {
			mediaType =  MediaType.valueOf("text/xml");
		}
		if(compress) {
			mediaType =  MediaType.valueOf("application/zip");
//			return Response.ok(StringUtils.zipToBytes(entity), mediaType).build();
			return Response.ok(zipEntity, mediaType).build();
		}else {
			System.out.println("No zipEntity: "+zipEntity);
			return Response.ok(entity, mediaType).build();
		}
	}
	
	protected <E extends Feature> Response generateResponse2(List<FeatureList<E>> features, String outputFormat, boolean compress) throws IOException {
		MediaType mediaType = MediaType.valueOf("text/plain");
		String entity = null;
		String zipEntity = "";
		//Gson gson = new GsonBuilder().serializeNulls().create();
		Gson gson = new Gson();
		
		if(outputFormat != null && outputFormat.equals("txt")) {
			mediaType = MediaType.valueOf("text/plain");
			entity = ListUtils.toString(features, separator);
		}
		if(outputFormat != null && outputFormat.equals("json")) {
			mediaType =  MediaType.valueOf("application/json");
System.out.println("featres: " +features);
			if(features != null && features.size() > 0 /*&& features.get(0) != null && features.get(0).get(0) != null*/) {
				System.err.println("FeatureList Object Class: "+features.get(0).getClass());
//				if(features.get(0).get(0) instanceof Gene) {
//					listType = new TypeToken<List<FeatureList<Gene>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof Transcript) {
//					listType = new TypeToken<List<FeatureList<Transcript>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof Exon) {
//					listType = new TypeToken<List<FeatureList<Exon>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof SNP) {
//					listType = new TypeToken<List<FeatureList<SNP>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof SpliceSite) {
//					listType = new TypeToken<List<FeatureList<SpliceSite>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof ConservedRegion) {
//					listType = new TypeToken<List<FeatureList<ConservedRegion>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof OregannoTfbs) {
//					listType = new TypeToken<List<FeatureList<OregannoTfbs>>>() {}.getType();
//				}else if(features.get(0).get(0) instanceof JasparTfbs) {
//					listType = new TypeToken<List<FeatureList<JasparTfbs>>>() {}.getType();
//				}
				if(listType != null) {
					System.out.println("Creating JSON object...");
					entity = gson.toJson(features, listType);
					System.err.println("done!");
					System.err.println("Entity json: "+entity);
					zipEntity = Arrays.toString(StringUtils.gzipToBytes(entity)).replace(" " , "");
					System.err.println("zipEntryBytes: "+StringUtils.gzipToBytes(entity));
					System.err.println("zipEntry: "+zipEntity);
					System.out.println("entity.length(): "+entity.length());
					System.out.println("zipEntity.length(): "+zipEntity.length());
					
				}else {
					System.err.println("AbstractInfraredRest: TypeToken from Gson equals null");
				}
			}
		}
		if(outputFormat != null && outputFormat.equals("xml")) {
			mediaType =  MediaType.valueOf("text/xml");
		}
		if(compress) {
			mediaType =  MediaType.valueOf("application/zip");
//			return Response.ok(StringUtils.zipToBytes(entity), mediaType).build();
			return Response.ok(zipEntity, mediaType).build();
		}else {
			System.out.println("No zipEntity: "+zipEntity);
			return Response.ok(entity, mediaType).build();
		}
	}
	protected Response generateErrorMessage(String errorMessage) {
		return Response.ok("An error occurred: "+errorMessage, MediaType.valueOf("text/plain")).build();
	}
	
}
