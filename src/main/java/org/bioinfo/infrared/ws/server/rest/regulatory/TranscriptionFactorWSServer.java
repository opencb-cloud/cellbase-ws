package org.bioinfo.infrared.ws.server.rest.regulatory;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/regulatory/tf")
@Produces("text/plain")
public class TranscriptionFactorWSServer extends RegulatoryWSServer {

	public TranscriptionFactorWSServer(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{tfId}/tfbs")
	public String getAllTfbs() {
		return null;
	}
	
	@GET
	@Path("/{tfId}/gene")
	public String getAllGenes() {
		return null;
	}
	
	
	@GET
	@Path("/info")
	public String help() {
		return null;
	}
	
}
