
package org.bioinfo.infrared.ws.server.rest;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class FeatureExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
//		System.out.println(arg0.getName());
//		if (arg0.isInterface()){
//			
//			return true;
//		}
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes arg0) {
//		System.out.println(arg0.getDeclaredType().toString());
		if (arg0.getDeclaredType().toString().contains("infrared")){
			return true;
		}
		return false;
	}

}
