package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.lib.api.ProteinDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/protein")
@Produces("text/plain")
public class ProteinWSServer extends GenericRestWSServer {

	public ProteinWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
	}
	
	@GET
	@Path("/{proteinId}/info")
	public Response getByEnsemblId(@PathParam("proteinId") String query) {
		try {
			checkVersionAndSpecies();
			ProteinDBAdaptor adaptor = dbAdaptorFactory.getProteinDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
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
	@Path("/{proteinId}/feature")
	public Response getFeatures(@PathParam("proteinId") String query, @DefaultValue("") @QueryParam("type") String type) {
		try {
			checkVersionAndSpecies();
			ProteinDBAdaptor adaptor = dbAdaptorFactory.getProteinDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllProteinFeaturesByProteinXrefList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getFeatures", e.toString());
		}
	}
	
	@GET
	@Path("/{proteinId}/association")
	public Response getInteraction(@PathParam("proteinId") String query, @DefaultValue("") @QueryParam("type") String type) {
		return null;
	}
	
	@GET
	@Path("/{proteinId}/xref")
	public Response getXrefs(@PathParam("proteinId") String proteinId, @DefaultValue("") @QueryParam("dbname") String dbname) {
		try {
			checkVersionAndSpecies();
			ProteinDBAdaptor adaptor = dbAdaptorFactory.getProteinDBAdaptor(this.species);
			return generateResponse(proteinId, adaptor.getAllProteinXrefsByProteinNameList(StringUtils.toList(proteinId, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getXrefs", e.toString());
		}
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
	
	@GET
	public Response getHelp() {
		return help();
	}

	@GET
	@Path("/help")
	public Response help() {
		StringBuilder sb = new StringBuilder();
		sb.append("Input:\n");
		sb.append("all id formats are accepted.\n\n\n");
		sb.append("Resources:\n");
		sb.append("- info: Get protein information: name, UniProt ID and description.\n");
		sb.append(" Output columns: UniProt accession, protein name, full name, gene name, organism.\n\n");
		sb.append("- feature: Get particular features for the protein sequence: natural variants in the aminoacid sequence, mutagenesis sites, etc.\n");
		sb.append(" Output columns: feature type, aa start, aa end, original, variation, identifier, description.\n\n\n");
		sb.append("Documentation:\n");
		sb.append("http://docs.bioinfo.cipf.es/projects/cellbase/wiki/Feature_rest_ws_api#Protein");
		
		return createOkResponse(sb.toString());
	}
	
}
