package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.AbstractRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/genomic")
@Produces("text/plain")
public class AbstractGenomicWSServer extends AbstractRestWSServer {

	public AbstractGenomicWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		init(version, species, uriInfo);
//		connect();
	}
	
	
	@GET
	@Path("/help")
	public String help() {
		return "genomic help";
	}
	
	@Override
	protected boolean isValidSpecies(String species) {
		// TODO Auto-generated method stub
		return false;
	}

}
