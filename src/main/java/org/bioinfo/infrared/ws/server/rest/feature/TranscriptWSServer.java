package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

public class TranscriptWSServer extends GenericRestWSServer {

	public TranscriptWSServer(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

}
