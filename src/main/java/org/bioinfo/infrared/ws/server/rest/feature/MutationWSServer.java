package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

public class MutationWSServer extends FeatureWSServer implements IFeature {

	public MutationWSServer(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidSpecies() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sequence(String feature) {
		// TODO Auto-generated method stub
		return null;
	}

}
