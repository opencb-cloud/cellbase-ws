package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/feature/id")
@Produces("text/plain")
public class IdConverter extends GenericRestWSServer {

//	public IdConverter(String species, UriInfo uriInfo) {
//		super(species, uriInfo);
//	}

	
	
	public IdConverter(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@Override
	protected boolean isValidSpecies(String species) {
		return false;
	}


}
