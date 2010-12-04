package org.bioinfo.infrared.ws.server.rest.genomic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.bioinfo.infrared.ws.server.rest.AbstractRestWSServer;


@Path("/{version}/{species}/genomic/position")
@Produces("text/plain")
public class PositionWSServer extends AbstractRestWSServer {

	@GET
	@Path("/help")
	public String help() {
		return "position help";
	}
	
	@Override
	protected boolean isValidSpecies(String species) {
		// TODO Auto-generated method stub
		return false;
	}

}