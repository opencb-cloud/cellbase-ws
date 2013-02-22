package org.bioinfo.cellbase.ws.server.rest;

import javax.ws.rs.core.Response;

import org.bioinfo.cellbase.ws.server.rest.exception.SpeciesException;
import org.bioinfo.cellbase.ws.server.rest.exception.VersionException;

public interface IWSServer {

	public void checkVersionAndSpecies() throws VersionException, SpeciesException;
		
	public String stats();
	
	public Response help();

}
