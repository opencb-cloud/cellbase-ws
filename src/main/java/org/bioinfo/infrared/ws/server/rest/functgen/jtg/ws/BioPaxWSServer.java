package org.bioinfo.infrared.ws.server.rest.functgen.jtg.ws;

import javax.ws.rs.core.UriInfo;

import org.bioinfo.biopax.server.BioPaxServer;
import org.bioinfo.biopax.graph.DotServer;
import org.bioinfo.biopax.model.Pathway;
import org.bioinfo.commons.io.utils.IOUtils;

public class BioPaxWSServer {

	public String version;
	public UriInfo uriInfo;
	
	public BioPaxServer bpServer = null;

	public BioPaxWSServer() {
		this(null, null);
	}

	public BioPaxWSServer(String version, UriInfo uriInfo) {
		this.version = version;
		this.uriInfo = uriInfo;
		
		bpServer = new BioPaxServer();		
	}

	public void clear() {
		bpServer.getSession().clear();
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
