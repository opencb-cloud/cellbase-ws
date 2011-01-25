package org.bioinfo.infrared.ws.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class GenomicTest {

	//@Test
	public void testGetGenesByRegion() {
		Client c = Client.create();
//		WebResource r = c.resource("http://bioinfo.cipf.es/infrared-ws/api/hsa/genomic/3:100-2000000/gene");
		WebResource r = c.resource("http://bioinfo.cipf.es/infrared-ws/api");
		String response;
//		response = r.accept(MediaType.TEXT_PLAIN).header("X-FOO", "BAR").get(String.class);
//		System.out.println(response);
		
//		response = r.path("hsa").path("genomic").path("3:100-2000000").path("gene").accept(MediaType.TEXT_PLAIN).header("X-FOO", "BAR").get(String.class);
//		System.out.println(response);
//		fail("Not yet implemented");
	}
/*
	public void testGetSnpsByRegion() {
		fail("Not yet implemented");
	}
	*/
	
	@Test
	public void getCytobandsList() {
		Client c = Client.create();
		/*
		String url = "http://localhost:8080/infrared-ws-server/api/v1/hsa/genomic/cytoband/1,2,3,4,5/chr";
		WebResource r = c.resource(url);
		
		String response =  r.accept(MediaType.TEXT_PLAIN).header("X-FOO", "BAR").get(String.class);
		System.out.println(response);
		*/
		
	
	}

}
