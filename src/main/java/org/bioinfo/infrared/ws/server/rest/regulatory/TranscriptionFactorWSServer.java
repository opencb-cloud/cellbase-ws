package org.bioinfo.infrared.ws.server.rest.regulatory;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/regulatory/tf")
@Produces("text/plain")
public class TranscriptionFactorWSServer extends RegulatoryWSServer {

	
	public TranscriptionFactorWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{tfId}/tfbs")
	public String getTfbsByTfId(@PathParam("tfId") String query) {
//		try {
//			
//		} catch {
//			
//		}
//		returns all TFBSs from a TF
		return null;
	}
	
	@GET
	@Path("/{tfId}/gene")
	public String getAllGenes() {
//		returns all genes regulated by this TF
		return null;
	}
	
	@GET
	@Path("/{tfId}/pwm")
	public String getAllPwms() {
//		returns all PWMs for this TF
		return null;
	}
	
	@GET
	@Path("/info")
	public String help() {
		return null;
	}
	
}
