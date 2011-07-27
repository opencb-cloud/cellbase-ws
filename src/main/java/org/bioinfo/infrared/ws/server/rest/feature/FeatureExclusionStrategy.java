package org.bioinfo.infrared.ws.server.rest.feature;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class FeatureExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		if (arg0.isInterface()) return true;
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes arg0) {
		return false;
	}

}
