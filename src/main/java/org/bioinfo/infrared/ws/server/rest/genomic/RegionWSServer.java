package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.dao.GenomeSequenceDataAdapter;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/{version}/{species}/chregion/")
@Produces("text/plain")
public class RegionWSServer extends GenericRestWSServer {
	public RegionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	
	public String getChromosome(String chregionId){
		
		return chregionId.split(",")
		
	}
	
	@GET
	@Path("/{chrRegionId}/sequence")
	public Response getBySnpName(@PathParam("chrRegionId") String chregionId) {
		try {
			String sequence = GenomeSequenceDataAdapter.getSequenceByRegion("1",5,500);
			return  Response.ok(sequence).build(); //generateResponse(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	


}
