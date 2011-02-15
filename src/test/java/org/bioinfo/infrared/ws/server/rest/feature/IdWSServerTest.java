package org.bioinfo.infrared.ws.server.rest.feature;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class IdWSServerTest {

	protected WebResource webResource;

	@Test
	public void testStats() {
		webResource = Client.create().resource("http://localhost:8080/infrared-ws/api").path("v1").path("hsa").path("feature").path("id");
		webResource.path("stats");
		//		fail("Not yet implemented");
	}

	@Test
	public void testXref() {
		webResource = Client.create().resource("http://localhost:8080/infrared-ws/api").path("v1").path("hsa").path("feature").path("id");
		webResource = webResource.path("brca2,bcl2").path("xref");
		System.out.println(webResource.toString());
		String result = webResource.get(String.class);
		if(result != null) {
			System.out.println(result);
		}else {
			fail("Not yet implemented");
		}
	}

}
