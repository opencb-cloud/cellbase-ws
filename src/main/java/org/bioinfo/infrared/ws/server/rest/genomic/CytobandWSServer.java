package org.bioinfo.infrared.ws.server.rest.genomic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.bioinfo.infrared.ws.server.rest.AbstractRestWSServer;


@Path("/{version}/{species}/genomic/cytoband")
@Produces("text/plain")
public class CytobandWSServer extends AbstractRestWSServer{

	@GET
	@Path("/help")
	public String help() {
		return "cytoband help";
	}
	
	@GET
	@Path("/all")
	public String all() {
		return "cytoband help";
	}
	
	@Override
	protected boolean isValidSpecies(String species) {
		return false;
	}

}
