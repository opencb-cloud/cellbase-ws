package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

@Path("/{version}/{species}/feature/protein")
@Produces("text/plain")
public class ProteinWSServer extends GenericRestWSServer {

	public ProteinWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{proteinId}/info")
	public Response getByEnsemblId(@PathParam("proteinId") String query) {
		return null;
	}

	@GET
	@Path("/{proteinId}/fullinfo")
	public Response getFullInfoByEnsemblId(@PathParam("proteinId") String query, @DefaultValue("") @QueryParam("sources") String sources) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/gene")
	public Response getGene(@PathParam("proteinId") String query) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/transcript")
	public Response getTranscript(@PathParam("proteinId") String query) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/features")
	public Response getFeatures(@PathParam("proteinId") String query, @DefaultValue("") @QueryParam("type") String type) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/association")
	public Response getInteraction(@PathParam("proteinId") String query, @DefaultValue("") @QueryParam("type") String type) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/xref")
	public Response getTargetGene(@PathParam("proteinId") String query, @DefaultValue("") @QueryParam("dbname") String dbname) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/reference")
	public Response getReference(@PathParam("proteinId") String query) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/sequence")
	public Response getSequence(@PathParam("proteinId") String query) {
		return null;
	}
	
}
