package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.lib.api.GenomicRegionFeatureDBAdaptor;
import org.bioinfo.infrared.lib.common.Position;
import org.bioinfo.infrared.lib.common.Region;
import org.bioinfo.infrared.lib.common.GenomicVariant;
import org.bioinfo.infrared.lib.common.GenomicVariantEffect;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/{version}/{species}/genomic/variant")
@Produces("text/plain")
public class VariantWSServer extends GenericRestWSServer {


	public VariantWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{positionId}/consequence_type")
	public Response getConsequenceTypeByPositionByGet(@PathParam("positionId") String query) {
		try {
			return getConsequenceTypeByPosition(query);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@POST
	@Path("/consequence_type")
	public Response getConsequenceTypeByPositionByPost(@FormParam("positionId") String query) {
			return getConsequenceTypeByPosition(query);
	}
	
	
	private Response getConsequenceTypeByPosition(String query){
		try {
			
			logger.debug("VARIANT TOOL: " + query);
			List<GenomicVariant> variants = GenomicVariant.parseVariants(query) ;
			GenomicVariantEffect gv = new GenomicVariantEffect(this.species);
			return generateResponse(query, gv.getConsequenceType(variants));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	

	
	
}
