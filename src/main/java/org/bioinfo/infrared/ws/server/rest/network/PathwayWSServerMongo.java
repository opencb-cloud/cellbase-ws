package org.bioinfo.infrared.ws.server.rest.network;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.io.utils.IOUtils;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.formats.core.graph.dot.Dot;

import org.bioinfo.infrared.lib.api.BioPaxDBAdaptor;
import org.bioinfo.infrared.lib.api.TfbsDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/network/reactome-pathway")
@Produces("text/plain")
public class PathwayWSServerMongo extends GenericRestWSServer {

	public PathwayWSServerMongo(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
	}

	@GET
	@Path("/list")
	public Response getAllPathways(@QueryParam("subpathways") String subpathways, @QueryParam("search") String search) {
		try {
			MongoOptions options = new MongoOptions();
			options.setConnectionsPerHost(20);
			Mongo m = new Mongo("localhost", options);
			new Mongo();
			DB db = m.getDB("reactome");
			DBCollection coll = db.getCollection("pathway");
			
			BasicDBObject query = new BasicDBObject();
			
			BasicDBObject returnFields = new BasicDBObject();
			returnFields.put("_id", 0);
			returnFields.put("name", 1);
			
			BasicDBObject orderBy = new BasicDBObject();
			orderBy.put("name", 1);
			
	        DBCursor cursor = coll.find(query, returnFields).sort(orderBy);
	        
	        List<String> pathways = new ArrayList<String>();
	        try {
	            while(cursor.hasNext()) {
	                pathways.add(cursor.next().toString());
	            }
	        } finally {
	            cursor.close();
	        }
			
	        return generateResponse("", pathways);
			
//			checkVersionAndSpecies();
//			boolean onlyTopLevel = false;
//			if (subpathways!=null) {
//				onlyTopLevel=!Boolean.parseBoolean(subpathways);
//			}
//			
//			StringBuilder sb = new StringBuilder();
//			BioPaxDBAdaptor bioPaxDBAdaptor = dbAdaptorFactory.getBioPaxDBAdaptor(this.species, this.version);
//			List<Pathway> pathways = bioPaxDBAdaptor.getPathways("Reactome", search, onlyTopLevel);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getAllPathways", e.toString());
		}
	}

	@GET
	@Path("/{pathwayId}/info")
	public Response getPathwayInfo(@PathParam("pathwayId") String pathwayId) {
		try {
			MongoOptions options = new MongoOptions();
			options.setConnectionsPerHost(20);
			Mongo m = new Mongo("localhost", options);
			new Mongo();
			DB db = m.getDB("reactome");
			DBCollection coll = db.getCollection("pathway");
			
			BasicDBObject query = new BasicDBObject();
			query.put("name", pathwayId);
			
			BasicDBObject returnFields = new BasicDBObject();
			returnFields.put("_id", 0);
			
	        DBCursor cursor = coll.find(query, returnFields);
	        
	        List<String> result = new ArrayList<String>();
	        try {
	            while(cursor.hasNext()) {
	                result.add(cursor.next().toString());
	            }
	        } finally {
	            cursor.close();
	        }
			return generateResponse("", result);
//			checkVersionAndSpecies();
//			StringBuilder sb = new StringBuilder();
//			BioPaxDBAdaptor dbAdaptor = dbAdaptorFactory.getBioPaxDBAdaptor(this.species, this.version);
//			Pathway pathway = dbAdaptor.getPathway(query, "Reactome");
//			return generateResponse("", Arrays.asList(pathway));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getPathwayInfo", e.toString());
		}
	}

	
//	@GET
//	@Path("/{pathwayId}/image")
//	@Produces("image/jpeg")
//	public Response getPathwayImage(@PathParam("pathwayId") String query) {
//		try {
//			checkVersionAndSpecies();
//			BioPaxDBAdaptor dbAdaptor = dbAdaptorFactory.getBioPaxDBAdaptor(this.species, this.version);
//			Pathway pathway = dbAdaptor.getPathway(query, "Reactome");
//			
//			if (pathway!=null) {
//				String contentType = "image/jpeg";
//				String outFormat = "jpg";
//				
//				String filename = query.replace(" ", "_").replace("(", "").replace(")", "").replace("/", "_").replace(":", "_");
//
//				DotServer dotServer = new DotServer();
//				Dot dot = dotServer.generateDot(pathway);
//
//				try {
//
//					File dotFile = new File("/tmp/" + filename + ".in");
//					File imgFile = new File("/tmp/" + filename + "." + outFormat);
//
//					dot.save(dotFile);
//					String cmd;
//					if ("dot".equalsIgnoreCase(outFormat) || "dotp".equalsIgnoreCase(outFormat)) {
//						cmd = "dot " + dotFile.getAbsolutePath() + " -o " + imgFile.getAbsolutePath();
//					} else {
//						cmd = "dot -T" + outFormat + " " + dotFile.getAbsolutePath() + " -o " + imgFile.getAbsolutePath();
//					}
//					System.out.println("-----------------------> cmd = " + cmd);
//					Runtime.getRuntime().exec(cmd);
//					Thread.sleep(2000);
//					if (imgFile.exists()) {
//						System.out.println("-----------------------> image exists !!!");			
//						if ("dotp".equalsIgnoreCase(outFormat)) {
//							String out = "var response = (" +  new Gson().toJson(IOUtils.readLines(imgFile)) + ")";
//							return Response.ok(out).build();
//						} else {
//							return Response.ok(imgFile, contentType).header("content-disposition","attachment; filename ="+query+"_image."+outFormat).build();
//						}					
//					} else {
//						System.out.println("-----------------------> image DO NOT exist !!!");
//						return Response.ok("An error occurred generating image for pathway '" + query + "'", MediaType.valueOf("text/plain")).build();
//					}
//				} catch (Exception e) {
//					return Response.ok("An error occurred generating image for pathway '" + query + "': " + e.getMessage(), MediaType.valueOf("text/plain")).build();
//				}
//			} else {
//				return Response.ok("Could not find pathway '" + query + "'", MediaType.valueOf("text/plain")).build(); 
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return createErrorResponse("getPathwayImage", e.toString());
//		}
//	}
//	
//	
//	@GET
//	@Path("/annotation")
//	public Response getPathwayAnnotation() {
//		try {
//			checkVersionAndSpecies();
//			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
//			return null;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return createErrorResponse("getPathwayAnnotation", e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{pathwayId}/element")
//	public Response getAllElements(@PathParam("pathwayId") String query) {
//		try {
//			checkVersionAndSpecies();
//			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
//			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ","), null, Integer.MIN_VALUE, Integer.MIN_VALUE));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return createErrorResponse("getAllElements", e.toString());
//		}
//	}
//
//
//	@GET
//	@Path("/{pathwayId}/gene")
//	public Response getAllGenes(@PathParam("pathwayId") String query) {
//		try {
//			checkVersionAndSpecies();
//			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
//			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ","), null, Integer.MIN_VALUE, Integer.MIN_VALUE));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return createErrorResponse("getAllGenes", e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{pathwayId}/protein")
//	public Response getAllByTfbs(@PathParam("pathwayId") String query) {
//		try {
//			checkVersionAndSpecies();
//			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
//			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ","), null, Integer.MIN_VALUE, Integer.MIN_VALUE));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return createErrorResponse("getAllByTfbs", e.toString());
//		}
//	}
//	
//	private String getJsonPathway(Pathway pw) {
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("{\"type\": \"pathway\",");
//		sb.append("\"id\": ").append(pw.getPkPathway()).append(",");
//		sb.append("\"name\": \"").append(getFirstName(pw.getBioEntity())).append("\",");
//		sb.append("\"description\": \"");
//		if (pw.getBioEntity().getComment()!=null) {
//			sb.append(pw.getBioEntity().getComment().replace("\"", "'").replace("\n", "").replace("\r", "").replace("\n", ""));
//		}
//		sb.append("\",");
//		sb.append("\"components\": [");
//		if (pw.getPathwaiesForPathwayComponent()!=null) {
//			int c=0;
//			Iterator it = pw.getPathwaiesForPathwayComponent().iterator();
//			while (it.hasNext()) {
//				if (c!=0) {
//					sb.append(",");
//				}
//				sb.append(getJsonPathway((Pathway) it.next()));
//				c++;
//			}
//			it = pw.getInteractions().iterator();
//			Interaction interaction = null;
//			while (it.hasNext()) {
//				if (c!=0) {
//					sb.append(",");
//				}
//				interaction = (Interaction) it.next();
//				sb.append("{\"type\": \"interaction\",");
//				sb.append("\"id\": ").append(interaction.getPkInteraction()).append(",");
//				sb.append("\"name\": \"").append(getFirstName(interaction.getBioEntity())).append("\",");
//				sb.append("\"description\": \"");
//				if (interaction.getBioEntity().getComment()!=null) {
//					sb.append(interaction.getBioEntity().getComment().replace("\"", "'").replace("\n", "").replace("\r", "").replace("\n", ""));
//				}
//				sb.append("\"}");
//				c++;
//			}
//		}
//		sb.append("]}");
//		
//		return sb.toString();
//	}
//	
//	public String getFirstName(BioEntity entity) {
//		String name = "NO-NAME";
//		try {
//			String aux = "";
//			Iterator it = entity.getNameEntities().iterator();
//			NameEntity ne = null;
//			while (it.hasNext()) {
//				ne = (NameEntity) it.next();
//				if (name.equalsIgnoreCase("NO-NAME") || ne.getNameEntity().length()<name.length()) {
//					name = ne.getNameEntity();
//				}
//			}
//			name = name.replace("\"", "'");
//		} catch (Exception e) {
//			name = "NO-NAME";
//		}
//		return name;
//	}
//	
//	@GET
//	public Response getHelp() {
//		return help();
//	}
//	
//	@GET
//	@Path("/help")
//	public Response help() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Input:\n");
//		sb.append("all id formats are accepted.\n\n\n");
//		sb.append("Resources:\n");
//		sb.append("- list: This subcategory is an informative WS that show the complete list of available pathways. This is an special resource which does not need a pathway name as input.\n");
//		sb.append(" Output columns: internal ID, pathway name, description.\n\n");
//		sb.append("- info: Prints descriptive information about a pathway.\n");
//		sb.append(" Output columns: internal ID, pathway name, description.\n\n");
//		sb.append("- image: Download an image of the selected pathway.\n\n\n");
//		sb.append("Documentation:\n");
//		sb.append("http://docs.bioinfo.cipf.es/projects/cellbase/wiki/Network_rest_ws_api#Pathway");
//		
//		return createOkResponse(sb.toString());
//	}

}
