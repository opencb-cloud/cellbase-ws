package org.bioinfo.infrared.ws.server.rest;

import java.lang.reflect.Type;



public class IdConverter extends AbstractInfraredRest {

//	public IdConverter(String species, UriInfo uriInfo) {
//		super(species, uriInfo);
//	}

	
	
	@Override
	protected boolean isValidSpecies(String species) {
		return false;
	}


}
