package org.bioinfo.cellbase.ws.server.rest.regulatory;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.cellbase.ws.server.rest.GenericRestWSServer;
import org.bioinfo.cellbase.ws.server.rest.exception.VersionException;

@Path("/{version}/{species}/regulatory")
@Produces("text/plain")
public class RegulatoryWSServer extends GenericRestWSServer {

	public RegulatoryWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
	}
	
	
	
 
	
}
