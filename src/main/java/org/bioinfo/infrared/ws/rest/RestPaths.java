package org.bioinfo.infrared.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.rest.impl.Genomic;
import org.bioinfo.infrared.ws.rest.impl.Variation;

@Path("/{species}")
@Produces("text/plain")
public class RestPaths {
	
	/********************************************************
	 * VARIATION METHODS
	 ********************************************************/

	@GET
	@Path("/listsnps")
	public Response getSnps(@PathParam("species") String species, @Context UriInfo ui) {
		return new Variation(species, ui).getSnps();
	}
	
	@GET
	@Path("/getsnpsbyconsequencetype")
	public Response getSnpsByConsequenceType(@PathParam("species") String species, @Context UriInfo ui) {
		return new Variation(species, ui).getSnpsByConsequenceType();
	}

	
	
	/********************************************************
	 * GENOMIC METHODS
	 ********************************************************/

	@GET
	@Path("/getchromosomes")
	public Response getChromosomes(@PathParam("species") String species, @Context UriInfo ui) {
		return new Genomic(species, ui).getChromosomes2();
	}
	
	
}
