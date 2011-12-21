package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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

@Path("/{version}/{species}/feature/gene")
@Produces("text/plain")
public class GeneWSServer extends GenericRestWSServer {
	
	public GeneWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
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
	private XRefsDBAdaptor getXRefDBAdaptor(){
		return dbAdaptorFactory.getXRefDBAdaptor(this.species);
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{geneId}/info")
	public Response getByEnsemblId(@PathParam("geneId") String query) {
		try {
			return generateResponse(query, this.getGeneDBAdaptor().getAllByNameList(StringUtils.toList(query, ",")));
			
		//	return generateResponse(query, Arrays.asList(this.getGeneDBAdaptor().getAllByEnsemblIdList(StringUtils.toList(query, ","))));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{geneId}/fullinfo")
	public Response getFullInfoByEnsemblId(@PathParam("geneId") String query, @DefaultValue("") @QueryParam("sources") String sources) {
		
		try {
			List<Gene> genes = getGeneDBAdaptor().getAllByEnsemblIdList(StringUtils.toList(query, ","));
			List<List<Transcript>> transcriptList = getTranscriptDBAdaptor().getByEnsemblGeneIdList(StringUtils.toList(query, ","));
			List<List<Xref>> goLists = getXRefDBAdaptor().getAllByDBName(StringUtils.toList(query, ","),"go");
			List<List<Xref>> interproLists = getXRefDBAdaptor().getAllByDBName(StringUtils.toList(query, ","),"interpro");
			List<List<Xref>> reactomeLists = getXRefDBAdaptor().getAllByDBName(StringUtils.toList(query, ","),"reactome");
			
			StringBuilder response = new StringBuilder();
			response.append("[");
			for (int i = 0; i < genes.size(); i++) {		
				response.append("{");
				response.append("\"stableId\":"+"\""+genes.get(i).getStableId()+"\",");
				response.append("\"externalName\":"+"\""+genes.get(i).getExternalName()+"\",");
				response.append("\"externalDb\":"+"\""+genes.get(i).getExternalDb()+"\",");
				response.append("\"biotype\":"+"\""+genes.get(i).getBiotype()+"\",");
				response.append("\"status\":"+"\""+genes.get(i).getStatus()+"\",");
				response.append("\"chromosome\":"+"\""+genes.get(i).getChromosome()+"\",");
				response.append("\"start\":"+genes.get(i).getStart()+",");
				response.append("\"end\":"+genes.get(i).getEnd()+",");
				response.append("\"strand\":"+"\""+genes.get(i).getStrand()+"\",");
				response.append("\"source\":"+"\""+genes.get(i).getSource()+"\",");
				response.append("\"description\":"+"\""+genes.get(i).getDescription()+"\",");
				response.append("\"transcripts\":"+gson.toJson(transcriptList.get(i))+",");
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
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/{geneId}/transcript")
	public Response getTranscriptsByEnsemblId(@PathParam("geneId") String query) {
		try {
			TranscriptDBAdaptor dbAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			return  generateResponse(query, Arrays.asList(dbAdaptor.getByEnsemblGeneIdList(StringUtils.toList(query, ","))));
			
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{geneId}/tf")
	public String getAllTfbs() {
		return null;
	}
	
	@GET
	@Path("/{geneId}/mirna")
	public String getAllMirna() {
		return null;
	}

//	@GET
//	@Path("/{geneId}/exon")
//	public Response getExonsByEnsemblId2(@PathParam("geneId") String query) {
//		try {
//			return generateResponse(query, new ExonDBAdapter().getByGeneIdList(StringUtils.toList(query, ",")));
//			/** HQL 
//			Query query = this.getSession().createQuery("select e from Exon e JOIN FETCH e.exon2transcripts et JOIN et.transcript t JOIN  t.gene g where g.stableId in :stable_id").setParameterList("stable_id", StringUtils.toList(geneId, ","));  
//			return generateResponse(query);
//			**/
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
	
//	@GET
//	@Path("/{geneId}/exon2transcript")
//	public Response getExon2TranscriptByEnsemblId(@PathParam("geneId") String query) {
//		try {
//			return generateResponse(query, new Exon2TranscriptDBAdapter().getByGeneIdList(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
	
//	@GET
//	@Path("/{geneId}/orthologous")
//	public Response getOrthologousByEnsemblId(@PathParam("geneId") String query) {
//		try {
//			return generateResponse(query, new OrthologousDBAdapter().getByGeneIdList(StringUtils.toList(query, ",")));
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("", StringUtils.getStackTrace(e));
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
}
