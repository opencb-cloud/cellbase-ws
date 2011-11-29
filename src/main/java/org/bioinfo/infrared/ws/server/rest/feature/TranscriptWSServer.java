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

@Path("/{version}/{species}/feature/transcript")
@Produces("text/plain")
public class TranscriptWSServer extends GenericRestWSServer {
	
	
	public TranscriptWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@GET
	@Path("/{transcriptId}/info")
	public Response getByEnsemblId(@PathParam("transcriptId") String query) {
		return null;
//		try {
//			System.out.println("transcriptId " + "info");
//			return  generateResponse(query, new TranscriptDBAdapter().getByIdList(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}

	@GET
	@Path("/{transcriptId}/gene")
	public Response getByGene(@PathParam("transcriptId") String query) {
		return null;
//		try {
//			return  generateResponse(query, new GeneDBAdapter().getGeneByTranscriptList(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}
	


	@GET
	@Path("/{transcriptId}/exon")
	public Response getExonsByEnsemblId2(@PathParam("transcriptId") String query) {
		return null;
//		try {
//			return  generateResponse(query, new ExonDBAdapter().getByTranscriptIdList(StringUtils.toList(query, ",")));
//			
//			/** HQL 
//			Query query = this.getSession().createQuery("select e from Exon e JOIN FETCH e.exon2transcripts et JOIN et.transcript t JOIN  t.gene g where g.stableId in :stable_id").setParameterList("stable_id", StringUtils.toList(geneId, ","));  
//			return generateResponse(query);
//			**/
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}
	
	@GET
	@Path("/{geneId}/exon2transcript")
	public Response getExon2TranscriptByEnsemblId(@PathParam("geneId") String query) {
		return null;
//		try {
//			return  generateResponse(query, new Exon2TranscriptDBAdapter().getByTranscriptIdList(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}
	


}
