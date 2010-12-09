package org.bioinfo.infrared.ws.server.rest.pathway;

import java.io.IOException;

import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

public class PathwayWSServer extends GenericRestWSServer {

	public PathwayWSServer(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

}
