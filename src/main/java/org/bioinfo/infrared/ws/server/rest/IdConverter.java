package org.bioinfo.infrared.ws.server.rest;



public class IdConverter extends AbstractInfraredRest {

//	public IdConverter(String species, UriInfo uriInfo) {
//		super(species, uriInfo);
//	}

	
	
	@Override
	protected boolean isValidSpecies(String species) {
		return false;
	}

}