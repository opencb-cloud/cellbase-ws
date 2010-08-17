package org.bioinfo.infrared.ws.rest.impl;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


public class Genomic extends AbstractInfraredRest{

	public Genomic(String species, UriInfo uriInfo) {
		super(species, uriInfo);
	}

	public Response getChromosomes() {
		try {
			return generateResponse("aaaaaaaaaaaaahhhhhhhhhhh", outputFormat, compress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Response getChromosomes2() {
		try {
			return generateResponse("aaaaaaaaaaaaahhhhhhhhhhh: "+uriInfo.getQueryParameters().toString(), outputFormat, compress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected boolean isValidSpecies(String species) {
		return true;
	}

}
