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


@Path("/{version}/{species}/genomic")
@Produces("text/plain")
public class GenomicWSServer extends GenericRestWSServer {

	/**
	 * Default constructor 
	 * @param version
	 * @param species
	 * @param uriInfo
	 * @throws VersionException
	 * @throws IOException
	 */
	public GenomicWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}


	@Override
	@GET
	@Path("/help")
	public String help() {
		return "genomic help";
	}

	@Override
	public boolean isValidSpecies() {
		return true;
	}

}
