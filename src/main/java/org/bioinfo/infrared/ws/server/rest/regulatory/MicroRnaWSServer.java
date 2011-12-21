package org.bioinfo.infrared.ws.server.rest.regulatory;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/regulatory/mirna")
@Produces("text/plain")
public class MicroRnaWSServer extends RegulatoryWSServer {

//	public MicroRnaWSServer(@PathParam("version") String version, @PathParam("species") String species, UriInfo uriInfo) throws VersionException, IOException {
	public MicroRnaWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{mirnaId}/gene")
	public String getAllTfbs() {
		return null;
	}

	@GET
	@Path("/{mirnaId}/mature")
	public String getMatureMirna() {
		return null;
	}

	@GET
	@Path("/{mirnaId}/target")
	public String getMirnaTarget() {
		return null;
	}

	@GET
	@Path("/{mirnaId}/disease")
	public String getMinaDisease() {
		return null;
	}

}
