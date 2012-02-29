package org.bioinfo.infrared.ws.server.rest.functgen.jtg.ws;

import java.io.IOException;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.lib.api.BioPaxDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib.BioPaxServer;

public class BioPaxWSServer extends GenericRestWSServer {

	public String version;
	public UriInfo uriInfo;
	
	public BioPaxServer bpServer = null;
	
	private BioPaxDBAdaptor bioPaxDBAdaptor;

	public BioPaxWSServer() throws VersionException, IOException {
		this(null, null, null);
	}

	public BioPaxWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
		
//		bpServer = new BioPaxServer();		
	}

	public void clear() {
//		bpServer.getSession().clear();
	}

	protected String getFormat(String input) {
		String output = "text";
		
		if (input==null) {
			output = "text";
		} else if ("json".equalsIgnoreCase(input)) {
			output = "json";
		} else if ("jsonp".equalsIgnoreCase(input)) {
			output = "jsonp";
		} else {
			output = "text";
		}
		
		return output;
	}
}
