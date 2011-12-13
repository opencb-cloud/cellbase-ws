package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.cellbase.Gene;
import org.bioinfo.infrared.core.cellbase.Transcript;
import org.bioinfo.infrared.lib.api.ExonDBAdaptor;
import org.bioinfo.infrared.lib.api.GeneDBAdaptor;
import org.bioinfo.infrared.lib.api.SnpDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/transcript")
@Produces("text/plain")
public class TranscriptWSServer extends GenericRestWSServer {
	
	
	public TranscriptWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	private GeneDBAdaptor getGeneDBAdaptor(){
		return dbAdaptorFactory.getGeneDBAdaptor(this.species);
	}
	private TranscriptDBAdaptor getTranscriptDBAdaptor(){
		return dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
	}
	private ExonDBAdaptor getExonDBAdaptor(){
		return dbAdaptorFactory.getExonDBAdaptor(this.species);
	}
	private SnpDBAdaptor getSnpDBAdaptor(){
		return dbAdaptorFactory.getSnpDBAdaptor(this.species);
	}
	
	@GET
	@Path("/{transcriptId}/info")
	public Response getByEnsemblId(@PathParam("transcriptId") String query) {
		try {
			return generateResponse(query, Arrays.asList(getTranscriptDBAdaptor().getAllByEnsemblIdList(StringUtils.toList(query, ","))));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
//		return null;
//		try {
//			System.out.println("transcriptId " + "info");
//			return  generateResponse(query, new TranscriptDBAdapter().getByIdList(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}

	
	@GET
	@Path("/{transcriptId}/sequence")
	public Response getSequencesByIdList(@PathParam("transcriptId") String query) {
		try {
			return generateResponse(query, Arrays.asList(getTranscriptDBAdaptor().getAllSequencesByIdList(StringUtils.toList(query, ","))));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{transcriptId}/region")
	public Response getRegionsByIdList(@PathParam("transcriptId") String query) {
		try {
			return generateResponse(query, Arrays.asList(getTranscriptDBAdaptor().getAllRegionsByIdList(StringUtils.toList(query, ","))));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	
	@GET
	@Path("/{transcriptId}/fullinfo")
	public Response getFullInfoByEnsemblId(@PathParam("transcriptId") String query) {
		
		try {
			StringBuilder response = new StringBuilder();
			List<Transcript> transcripts = getTranscriptDBAdaptor().getAllByEnsemblIdList(StringUtils.toList(query, ","));
			response.append("[");
			for(Transcript transcript: transcripts){
				response.append("{");
				response.append("\"stableId\":"+"\""+transcript.getStableId()+"\",");
				response.append("\"externalName\":"+"\""+transcript.getExternalName()+"\",");
				response.append("\"externalDb\":"+"\""+transcript.getExternalDb()+"\",");
				response.append("\"biotype\":"+"\""+transcript.getBiotype()+"\",");
				response.append("\"status\":"+"\""+transcript.getStatus()+"\",");
				response.append("\"chromosome\":"+"\""+transcript.getChromosome()+"\",");
				response.append("\"start\":"+transcript.getStart()+",");
				response.append("\"end\":"+transcript.getEnd()+",");
				response.append("\"strand\":"+"\""+transcript.getStrand()+"\",");
				response.append("\"codingRegionStart\":"+transcript.getCodingRegionStart()+",");
				response.append("\"codingRegionEnd\":"+transcript.getCodingRegionEnd()+",");
				response.append("\"cdnaCodingStart\":"+transcript.getCdnaCodingStart()+",");
				response.append("\"cdnaCodingEnd\":"+transcript.getCdnaCodingEnd()+",");
				response.append("\"description\":"+"\""+transcript.getDescription()+"\",");
				response.append("\"gene\":"+gson.toJson(getGeneDBAdaptor().getByEnsemblTranscriptId(query))+",");
				response.append("\"exons\":"+gson.toJson(getExonDBAdaptor().getByEnsemblTranscriptId(query))+",");
				response.append("\"snps\":"+gson.toJson(getSnpDBAdaptor().getAllByEnsemblTranscriptId(query))+"");
				response.append("},");
			}
			response.append("]");
			
			//Remove the last comma
			response.replace(response.length()-2, response.length()-1, "");
			
			// bean
			// gene
			// exons
			// snps
			// xrefs TODO
			
			return createOkResponse(response.toString());
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
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
		ExonDBAdaptor dbAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
		try {
			return  generateResponse(query, dbAdaptor.getByEnsemblTranscriptIdList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	
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
