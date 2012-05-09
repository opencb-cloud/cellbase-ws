package org.bioinfo.infrared.ws.server.rest;

import javax.ws.rs.core.Response;

public interface IWSServer {

	public boolean isValidSpecies();
	
	public Response help();
	
	/**
	 * 
	 * 
	 * PATH("/stats")
	 * @return
	 */
	public String stats();
	
}
