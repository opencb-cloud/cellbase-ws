package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/genomic/cytoband")
@Produces("text/plain")
public class CytobandWSServer extends GenericRestWSServer{

	public CytobandWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

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
	public boolean isValidSpecies() {
		return false;
	}

}
