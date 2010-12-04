package org.bioinfo.infrared.ws.server.rest.core;

import java.lang.reflect.Type;

import org.bioinfo.infrared.ws.server.rest.AbstractRestWSServer;



public class IdConverter extends AbstractRestWSServer {

//	public IdConverter(String species, UriInfo uriInfo) {
//		super(species, uriInfo);
//	}

	
	
	@Override
	protected boolean isValidSpecies(String species) {
		return false;
	}


}
