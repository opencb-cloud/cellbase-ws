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
import org.bioinfo.infrared.core.cellbase.Exon;
import org.bioinfo.infrared.core.cellbase.Gene;
import org.bioinfo.infrared.core.cellbase.Snp;
import org.bioinfo.infrared.core.cellbase.Transcript;
import org.bioinfo.infrared.core.cellbase.Xref;
import org.bioinfo.infrared.lib.api.ExonDBAdaptor;
import org.bioinfo.infrared.lib.api.GeneDBAdaptor;
import org.bioinfo.infrared.lib.api.SnpDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.lib.api.XRefsDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/transcript")
@Produces("text/plain")
public class TranscriptWSServer extends GenericRestWSServer {
	
	
	public TranscriptWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{transcriptId}/info")
	public Response getByEnsemblId(@PathParam("transcriptId") String query) {
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			return generateResponse(query, Arrays.asList(transcriptDBAdaptor.getAllByEnsemblIdList(StringUtils.toList(query, ","))));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getByEnsemblId", e.toString());
		}
	}

	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{transcriptId}/all")
	public Response getAll() {
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			return generateResponse(new String(), Arrays.asList(transcriptDBAdaptor.getAll()));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getAll", e.toString());
		}
	}

	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{transcriptId}/sequence")
	public Response getSequencesByIdList(@PathParam("transcriptId") String query) {
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			return generateResponse(query, Arrays.asList(transcriptDBAdaptor.getAllSequencesByIdList(StringUtils.toList(query, ","))));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getSequencesByIdList", e.toString());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{transcriptId}/region")
	public Response getRegionsByIdList(@PathParam("transcriptId") String query) {
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			return generateResponse(query, Arrays.asList(transcriptDBAdaptor.getAllRegionsByIdList(StringUtils.toList(query, ","))));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getRegionsByIdList", e.toString());
		}
	}
	
	
	@GET
	@Path("/{transcriptId}/fullinfo")
	public Response getFullInfoByEnsemblId(@PathParam("transcriptId") String query) {
		
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
			ExonDBAdaptor exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
			SnpDBAdaptor snpDBAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species);
			XRefsDBAdaptor xRefsDBAdaptor = dbAdaptorFactory.getXRefDBAdaptor(this.species);
			
			
			List<Transcript> transcripts = transcriptDBAdaptor.getAllByEnsemblIdList(StringUtils.toList(query, ","));
			List<Gene> genes = geneDBAdaptor.getAllByEnsemblTranscriptIdList(StringUtils.toList(query, ","));
			List<List<Exon>> exonLists = exonDBAdaptor.getByEnsemblTranscriptIdList(StringUtils.toList(query, ","));
			List<List<Snp>> snpLists = snpDBAdaptor.getAllByEnsemblTranscriptIdList(StringUtils.toList(query, ","));
			List<List<Xref>> goLists = xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"go");
			List<List<Xref>> interproLists = xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"interpro");
			List<List<Xref>> reactomeLists = xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"reactome");
			
			StringBuilder response = new StringBuilder();
			response.append("[");
			for (int i = 0; i < transcripts.size(); i++) {		
				response.append("{");
				response.append("\"stableId\":"+"\""+transcripts.get(i).getStableId()+"\",");
				response.append("\"externalName\":"+"\""+transcripts.get(i).getExternalName()+"\",");
				response.append("\"externalDb\":"+"\""+transcripts.get(i).getExternalDb()+"\",");
				response.append("\"biotype\":"+"\""+transcripts.get(i).getBiotype()+"\",");
				response.append("\"status\":"+"\""+transcripts.get(i).getStatus()+"\",");
				response.append("\"chromosome\":"+"\""+transcripts.get(i).getChromosome()+"\",");
				response.append("\"start\":"+transcripts.get(i).getStart()+",");
				response.append("\"end\":"+transcripts.get(i).getEnd()+",");
				response.append("\"strand\":"+"\""+transcripts.get(i).getStrand()+"\",");
				response.append("\"codingRegionStart\":"+transcripts.get(i).getCodingRegionStart()+",");
				response.append("\"codingRegionEnd\":"+transcripts.get(i).getCodingRegionEnd()+",");
				response.append("\"cdnaCodingStart\":"+transcripts.get(i).getCdnaCodingStart()+",");
				response.append("\"cdnaCodingEnd\":"+transcripts.get(i).getCdnaCodingEnd()+",");
				response.append("\"description\":"+"\""+transcripts.get(i).getDescription()+"\",");
				response.append("\"gene\":"+gson.toJson(genes.get(i))+",");
				response.append("\"exons\":"+gson.toJson(exonLists.get(i))+",");
				response.append("\"snps\":"+gson.toJson(snpLists.get(i))+",");
				response.append("\"go\":"+gson.toJson(goLists.get(i))+",");
				response.append("\"interpro\":"+gson.toJson(interproLists.get(i))+",");
				response.append("\"reactome\":"+gson.toJson(reactomeLists.get(i))+"");
				response.append("},");
			}
			response.append("]");
			
			//Remove the last comma
			response.replace(response.length()-2, response.length()-1, "");
			return  generateResponse(query,Arrays.asList(response));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getFullInfoByEnsemblId", e.toString());
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
		try {
			ExonDBAdaptor dbAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
			return  generateResponse(query, dbAdaptor.getByEnsemblTranscriptIdList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getExonsByEnsemblId2", e.toString());
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
	
	@GET
	public Response getHelp() {
		return help();
	}
	
	@GET
	@Path("/help")
	public Response help() {
		StringBuilder sb = new StringBuilder();
		sb.append("Resources/actions\n\n");
		sb.append("\t- info: Get transcript information: name, position, biotype.\n");
		sb.append("\t Output columns: Ensembl ID, external name, external name source, biotype, status, chromosome, start, end, strand, coding region start, coding region end, cdna coding start, cdna coding end, description.\n\n");
		sb.append("\t- gene: Get the corresponding gene for this transcript.\n");
		sb.append("\t Output columns: Ensembl gene, external name, external name source, biotype, status, chromosome, start, end, strand, source, description.\n\n");
		sb.append("\t- sequence: Get transcript sequence.\n\n");
		sb.append("\t- exon: Get transcript's exons.\n");
		sb.append("\t Output columns: Ensembl ID, chromosome, start, end, strand.\n\n");
		
		return createOkResponse(sb.toString());
	}

}
