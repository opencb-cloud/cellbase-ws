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
import org.bioinfo.infrared.lib.api.PathwayDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.Gson;
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
	public Response getAllPathways() {
		try {
//			checkVersionAndSpecies();
			
			PathwayDBAdaptor pathwayDBAdaptor = dbAdaptorFactory.getPathwayDBAdaptor(this.species, this.version);
	        String pathways = pathwayDBAdaptor.getPathways();
	        return createOkResponse(pathways);
	        
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getAllPathways", e.toString());
		}
	}
	
	@GET
	@Path("/tree")
	public Response getTree() {
		try {
			PathwayDBAdaptor pathwayDBAdaptor = dbAdaptorFactory.getPathwayDBAdaptor(this.species, this.version);
			String result = pathwayDBAdaptor.getTree();
			return createOkResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getAllPathways", e.toString());
		}
	}

	@GET
	@Path("/{pathwayId}/info")
	public Response getPathwayInfo(@PathParam("pathwayId") String pathwayId) {
		try {
			PathwayDBAdaptor pathwayDBAdaptor = dbAdaptorFactory.getPathwayDBAdaptor(this.species, this.version);
			String result = pathwayDBAdaptor.getPathway(pathwayId);
			return createOkResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getPathwayInfo", e.toString());
		}
	}
	
	@GET
	@Path("/search")
	public Response search(@QueryParam("by") String searchBy, @QueryParam("text") String searchText , @QueryParam("onlyIds") boolean returnOnlyIds) {
		try {
			PathwayDBAdaptor pathwayDBAdaptor = dbAdaptorFactory.getPathwayDBAdaptor(this.species, this.version);
			String result = pathwayDBAdaptor.search(searchBy, searchText, returnOnlyIds);
			return createOkResponse(result);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("search", e.toString());
		}
	}
}
