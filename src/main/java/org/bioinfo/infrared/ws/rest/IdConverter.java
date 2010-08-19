package org.bioinfo.infrared.ws.rest;

import javax.ws.rs.core.UriInfo;


public class IdConverter extends AbstractInfraredRest {

//	public IdConverter(String species, UriInfo uriInfo) {
//		super(species, uriInfo);
//	}

	
	
	@Override
	protected boolean isValidSpecies(String species) {
		return false;
	}

}
