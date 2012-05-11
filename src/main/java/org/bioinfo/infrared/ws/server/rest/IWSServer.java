package org.bioinfo.infrared.ws.server.rest;

import javax.ws.rs.core.Response;

import org.bioinfo.infrared.ws.server.rest.exception.SpeciesException;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

public interface IWSServer {

	public void checkVersionAndSpecies() throws VersionException, SpeciesException;
		
	public String stats();
	
	public Response help();

}
