package org.bioinfo.infrared.ws.server.rest;

public interface IWSServer {

	public boolean isValidSpecies();
	
	public String help();
	
	/**
	 * 
	 * 
	 * PATH("/stats")
	 * @return
	 */
	public String stats();
	
}
