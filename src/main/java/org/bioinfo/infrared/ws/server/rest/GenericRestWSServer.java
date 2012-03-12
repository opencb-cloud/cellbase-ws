package org.bioinfo.infrared.ws.server.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.Config;
import org.bioinfo.commons.log.Logger;
import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.dao.utils.HibernateUtil;
import org.bioinfo.infrared.lib.impl.DBAdaptorFactory;
import org.bioinfo.infrared.lib.impl.hibernate.HibernateDBAdaptorFactory;
import org.bioinfo.infrared.lib.io.output.JsonWriter;
import org.bioinfo.infrared.lib.io.output.StringWriter;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/{version}")
@Produces("text/plain")
public class GenericRestWSServer implements IWSServer {

	protected Config config;

	// Common application parameters
	protected String version;
	protected String species;
	protected UriInfo uriInfo;

	// Common output parameters
	protected String resultSeparator;
	protected String querySeparator;

	// output format file type: null or txt or text, xml, excel
	protected String fileFormat;

	// file name without extension which server will give back when file format is !null
	private String filename;

	// output content format: txt or text, json, jsonp, xml, das
	protected String outputFormat;
	
	// in file output produces a zip file, in text outputs generates a gzipped output
	protected String outputCompress;

	// only in text format
	protected String outputRowNames;
	protected String outputHeader;

	protected String user;
	protected String password;

	protected Type listType;

	//	private MediaType mediaType;
	protected Gson gson; 
	private GsonBuilder gsonBuilder;
	protected Logger logger;


	/**
	 * DBAdaptorFactory creation, this object can be initialize
	 * with an HibernateDBAdaptorFactory or an HBaseDBAdaptorFactory.
	 * This object is a factory for creating adaptors like GeneDBAdaptor
	 */
	protected static DBAdaptorFactory dbAdaptorFactory;
	static{
		dbAdaptorFactory = new HibernateDBAdaptorFactory();
	}

	private static final String NEW_LINE = "newline";
	private static final String TAB = "tab";


	public GenericRestWSServer(@PathParam("version") String version) {
		this.version = version;
	}

	public GenericRestWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		this.version = version;
		this.species = species;
		this.uriInfo = uriInfo;

		if(version != null && species != null) {
			init(version, species, uriInfo);
		}
	}

	protected void init(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		// load properties file
		ResourceBundle databaseConfig = ResourceBundle.getBundle("org.bioinfo.infrared.ws.application");
		config = new Config(databaseConfig);

		// mediaType = MediaType.valueOf("text/plain");
		gson = new GsonBuilder().serializeNulls().setExclusionStrategies(new FeatureExclusionStrategy()).create();
//		gsonBuilder = new GsonBuilder();

		logger = new Logger();
		logger.setLevel(Logger.DEBUG_LEVEL);

		// this code MUST be run before the checking 
		parseCommonQueryParameters(uriInfo.getQueryParameters());
	}

	private void parseCommonQueryParameters(MultivaluedMap<String, String> multivaluedMap) {
		if(multivaluedMap.get("result_separator") != null) {
			if(multivaluedMap.get("result_separator").get(0).equalsIgnoreCase(NEW_LINE)) {
				resultSeparator = "\n";				
			}else {
				if(multivaluedMap.get("result_separator").get(0).equalsIgnoreCase(TAB)) {
					resultSeparator = "\t";	
				}else {
					resultSeparator = multivaluedMap.get("result_separator").get(0);
				}
			}
		}else {
			resultSeparator = "//";
		}

		if(multivaluedMap.get("query_separator") != null) {
			if(multivaluedMap.get("query_separator").get(0).equalsIgnoreCase(NEW_LINE)) {
				querySeparator = "\n";				
			}else {
				if(multivaluedMap.get("query_separator").get(0).equalsIgnoreCase(TAB)) {
					querySeparator = "\t";	
				}else {
					querySeparator = multivaluedMap.get("query_separator").get(0);
				}
			}
		}else {
			querySeparator = "\n";
		}

		fileFormat = (multivaluedMap.get("fileformat") != null) ? multivaluedMap.get("fileformat").get(0) : "";
		outputFormat = (multivaluedMap.get("of") != null) ? multivaluedMap.get("of").get(0) : "txt";
//		outputFormat = (multivaluedMap.get("contentformat") != null) ? multivaluedMap.get("contentformat").get(0) : "txt";
		filename = (multivaluedMap.get("filename") != null) ? multivaluedMap.get("filename").get(0) : "result";
		outputRowNames = (multivaluedMap.get("outputrownames") != null) ? multivaluedMap.get("outputrownames").get(0) : "false";
		outputHeader = (multivaluedMap.get("outputheader") != null) ? multivaluedMap.get("outputheader").get(0) : "false";
		outputCompress = (multivaluedMap.get("outputcompress") != null) ? multivaluedMap.get("outputcompress").get(0) : "false";

		user = (multivaluedMap.get("user") != null) ? multivaluedMap.get("user").get(0) : "anonymous";
		password = (multivaluedMap.get("password") != null) ? multivaluedMap.get("password").get(0) : "";
	}

	//This method are for nice printing help URL
	protected List<String> getPathsNicePrint(){
		return new ArrayList<String>();
	}

	protected List<String> getExamplesNicePrint(){
		return new ArrayList<String>();
	}


	protected Session getSession(){
		return HibernateUtil.getSessionFactory().openSession();
	}

	@Override
	public String stats() {
		return null;
	}

	@Override
	public boolean isValidSpecies() {
		return true;
	}

	@GET
	@Path("/help")
	public String help() {
		return "No help avalaible";
	}

	@GET
	@Path("/species")
	public String getSpecies() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("#short").append("\t").append("common").append("\t").append("scientific").append("\n");
		stringBuilder.append("hsa").append("\t").append("human").append("\t").append("Homo sapiens").append("\n");
		stringBuilder.append("mus").append("\t").append("mouse").append("\t").append("Mus musculus").append("\n");
		stringBuilder.append("rno").append("\t").append("rat").append("\t").append("Rattus norvegicus");
		return stringBuilder.toString();
	}


	protected Response generateResponse(Criteria criteria) throws IOException {
		List result = criteria.list();
		this.getSession().close();
		return generateResponse("", result); 
	}

	protected Response generateResponse(Query query) throws IOException {
		List result = query.list();
		this.getSession().close();
		return generateResponse("", result); 
	}

	@SuppressWarnings("unchecked")
	protected Response generateResponse(String queryString, List features) throws IOException {
		logger.info("-------------");
		if (queryString.length()>99){
			logger.debug("\t\t - Response: " + queryString.substring(0,100) + ".....");
		}
		else{
			logger.debug("\t\t - Response: " + queryString);
		}

		//		logger.info(features.toString());
		MediaType mediaType = MediaType.valueOf("text/plain");

		String response = "";
		if (outputFormat != null) {
			if(outputFormat.equalsIgnoreCase("txt") || outputFormat.equalsIgnoreCase("text") || outputFormat.equalsIgnoreCase("jsontext")) {
				if(outputFormat.equalsIgnoreCase("jsontext")) {
					mediaType = MediaType.valueOf("text/javascript");
					response = convertToJsonText(response);
				}else {
					mediaType = MediaType.TEXT_PLAIN_TYPE;
					response = StringWriter.serialize(features);
				}
			}

			if((outputFormat.equalsIgnoreCase("json") || outputFormat.equalsIgnoreCase("jsonp"))) {
				mediaType = MediaType.APPLICATION_JSON_TYPE;
				if(features != null && features.size() > 0) {
					response = gson.toJson(features);
//					JsonWriter jsonWriter = new JsonWriter(new FeatureExclusionStrategy());
//					response = jsonWriter.serialize(features);
				}

				if(outputFormat.equals("jsonp")) {
					mediaType = MediaType.valueOf("text/javascript");
					response = convertToJson(response);
				}
			}

			if(outputFormat.equalsIgnoreCase("xml") ) {
				mediaType = MediaType.TEXT_XML_TYPE;
				response = ListUtils.toString(features, resultSeparator);
			}

			if(outputFormat.equalsIgnoreCase("das") ) {
				mediaType = MediaType.TEXT_XML_TYPE;
				response = ListUtils.toString(features, resultSeparator);
			}
		}

		return createResponse(response, mediaType);
	}

	protected Response createResponse(String response, MediaType mediaType) throws IOException {
		logger.debug("\tQuery Params");
		logger.debug("\t\t - FileFormat: " + fileFormat);
		logger.debug("\t\t - ContentFormat: " + outputFormat);
		logger.debug("\t\t - Compress: " + outputCompress);
		logger.debug("\t\t -------------------------------");
		logger.debug("\t\t - Inferred media type: " + mediaType.toString());

		if (response.length()>99){
			logger.debug("\t\t -Response: " + response.substring(0,100) + ".....");
		}
		else{
			logger.debug("\t\t -Response: " + response);
		}

		if(fileFormat == null || fileFormat.equalsIgnoreCase("")) {
			if(outputCompress != null && outputCompress.equalsIgnoreCase("true") && !outputFormat.equalsIgnoreCase("jsonp")&& !outputFormat.equalsIgnoreCase("jsontext")) {
				response = Arrays.toString(StringUtils.gzipToBytes(response)).replace(" " , "");
			}
		}else {

			mediaType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
			logger.debug("\t\t - Creating byte stream ");

			if(outputCompress != null && outputCompress.equalsIgnoreCase("true")) {
				OutputStream bos = new ByteArrayOutputStream();
				bos.write(response.getBytes());

				ZipOutputStream zipstream = new ZipOutputStream(bos);
				zipstream.setLevel(9);

				logger.debug("\t\t - zipping.... ");
				logger.debug("\t\tFinal media Type: " + mediaType.toString());

				//				return Response.ok(zipstream, mediaType).header("content-disposition","attachment; filename = "+ filename + ".zip").build();
				return this.createOkResponse(zipstream, mediaType, filename+".zip");

			}else {
				if(fileFormat.equalsIgnoreCase("xml")) {
					//mediaType =  MediaType.valueOf("application/xml");	
				}

				if(fileFormat.equalsIgnoreCase("excel")) {
					//mediaType =  MediaType.valueOf("application/vnd.ms-excel");
				}
				if(fileFormat.equalsIgnoreCase("txt") || fileFormat.equalsIgnoreCase("text")) {
					logger.debug("\t\t - text File ");

					byte[] streamResponse = response.getBytes();
					//					return Response.ok(streamResponse, mediaType).header("content-disposition","attachment; filename = "+ filename + ".txt").build();
					return this.createOkResponse(streamResponse, mediaType, filename+".txt");
				}

			}
		}
		logger.debug("");
		logger.debug("\t\tFinal media Type: " + mediaType.toString());
		logger.debug(" ------------ ");
		//		return Response.ok(response, mediaType).build();
		return this.createOkResponse(response, mediaType);
	}


	protected Response createErrorResponse(String errorMessage) {
		return Response.ok("An error occurred: "+errorMessage, MediaType.valueOf("text/plain")).header("Access-Control-Allow-Origin", "*").build();
	}

	protected Response createOkResponse(Object obj){                
		return Response.ok(obj).header("Access-Control-Allow-Origin", "*").build();
	}

	protected Response createOkResponse(Object obj, MediaType mediaType){
		return Response.ok(obj, mediaType).header("Access-Control-Allow-Origin", "*").build();
	}

	protected Response createOkResponse(Object obj, MediaType mediaType, String fileName){
		return Response.ok(obj, mediaType).header("content-disposition","attachment; filename ="+fileName).header("Access-Control-Allow-Origin", "*").build();
	}


	protected Response generateErrorResponse(String errorMessage) {
		return Response.ok("An error occurred: "+errorMessage, MediaType.valueOf("text/plain")).build();
	}


	private String convertToJsonText(String response) {
		String jsonpQueryParam = (uriInfo.getQueryParameters().get("callbackParam") != null) ? uriInfo.getQueryParameters().get("callbackParam").get(0) : "callbackParam";
		response = "var " + jsonpQueryParam+ " = \"" + response +"\"";
		return response;
	}

	protected String convertToJson(String response) {
		String jsonpQueryParam = (uriInfo.getQueryParameters().get("callbackParam") != null) ? uriInfo.getQueryParameters().get("callbackParam").get(0) : "callbackParam";	
		response = "var " + jsonpQueryParam+ " = (" + response +")";
		return response;
	}

}
