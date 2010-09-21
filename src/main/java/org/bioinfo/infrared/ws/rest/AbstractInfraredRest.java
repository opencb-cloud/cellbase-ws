package org.bioinfo.infrared.ws.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.Config;
import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.common.dbsql.DBConnector;
import org.bioinfo.infrared.core.common.Feature;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Exon;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.core.feature.Transcript;
import org.bioinfo.infrared.core.feature.XRef;
import org.bioinfo.infrared.core.variation.SNP;
import org.bioinfo.infrared.core.variation.TranscriptConsequenceType;
import org.bioinfo.infrared.ws.rest.exception.VersionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


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
		}else {
			if(outputFormat.equals("json")) {
				return new Gson().toJson(features);
			}
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
			System.out.println(entity);
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

	protected <E extends Feature> Response generateResponse2(FeatureList<Gene> features, String outputFormat, boolean compress) throws IOException {
		MediaType mediaType = MediaType.valueOf("text/plain");
		String entity = null;
		if(outputFormat != null && outputFormat.equals("txt")) {
			mediaType = MediaType.valueOf("text/plain");
			entity = ListUtils.toString(features, separator);
		}
		if(outputFormat != null && outputFormat.equals("json")) {
			mediaType =  MediaType.valueOf("application/json");
			
			
			/**
			 * 	Type listType = new TypeToken<List<String>>() {}.getType();
				gson.toJson(myStrings, listType);

			 */
			
			List<Gene> myStrings = new ArrayList<Gene>();
			Type listType = new TypeToken<List<Gene>>() {}.getType();
			
			Gene g1 = new Gene("aaa", "1", 12, 56, "-1", "mieeerda");
			FeatureList<Transcript> t = new FeatureList<Transcript>();
			t.add(new Transcript("t1", "1", 1, 2, "1", "biotye"));
			
			g1.setTranscripts(t);
			g1.setExons(new FeatureList<Exon>());
			g1.setSnps(new FeatureList<SNP>());
			g1.setXrefs(new HashMap<String, FeatureList<XRef>>());
//			g1.setRosettaDBConnector(new DBConnector());
			
			myStrings.add(g1);
			myStrings.add(new Gene("bbb", "1", 12, 56, "-1", "mieeerda"));
			myStrings.add(new Gene("ccc", "1", 12, 56, "-1", "mieeerda"));
//			myStrings.add((Gene)features.get(0));
//			myStrings.add((Gene)features.get(1));
//			myStrings.add((Gene)features.get(2));
			System.err.println("1: "+List.class);
//			Type listType = new TypeToken<Gene[]>() {}.getType();
			System.err.println("2: "+ArrayList.class);
			Gson gson = new Gson();
			System.err.println("3");
			entity = gson.toJson(myStrings, listType);
//			entity = gson.toJson(features);
//			entity = gson.toJson(new Gene("aaa", "1", 12, 56, "-1", "mieeerda"));
			System.err.println("4");
			
			System.err.println(entity);
//			entity = new Gson().toJson(new Gene("aaa", "1", 12, 56, "-1", "mieeerda"));
//			entity = new Gson().toJson(features, listType);
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
//	protected <E extends Feature> Response generateResponse3(List<FeatureList<E>> features, String outputFormat, boolean compress) throws IOException {
//		
//	}
	protected Response generateErrorMessage(String errorMessage) {
		return Response.ok("An error occurred: "+errorMessage, MediaType.valueOf("text/plain")).build();
	}

}
