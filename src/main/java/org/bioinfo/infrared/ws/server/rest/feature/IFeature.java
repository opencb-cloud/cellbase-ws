package org.bioinfo.infrared.ws.server.rest.feature;

import org.bioinfo.infrared.ws.server.rest.IWSServer;

public interface IFeature extends IWSServer{

	public String sequence(String featureId);
	
	
}
