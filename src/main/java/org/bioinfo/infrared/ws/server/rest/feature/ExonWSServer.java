package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

@Path("/{version}/{species}/feature/exon")
@Produces("text/plain")
public class ExonWSServer extends GenericRestWSServer {
	
	
	
	public ExonWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@GET
	@Path("/{exonId}/info")
	public Response getByEnsemblId(@PathParam("exonId") String query) {
		return null;
//		try {
//			return  generateResponse(query, new ExonDBAdapter().getById(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}
	
	@GET
	@Path("/{exonId}/transcript")
	public Response getTranscriptsByEnsemblId(@PathParam("exonId") String query) {
		return null;
//		try {
//			return  generateResponse(query, new TranscriptDBAdapter().getByExonId(StringUtils.toList(query, ",")));
////			return  generateResponse(query, new Exon2TranscriptDBAdapter().getByExonId(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}
	
	


}
