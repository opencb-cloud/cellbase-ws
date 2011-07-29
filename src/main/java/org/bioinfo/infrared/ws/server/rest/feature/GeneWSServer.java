package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.Exon;
import org.bioinfo.infrared.core.Exon2transcript;
import org.bioinfo.infrared.core.Gene;
import org.bioinfo.infrared.core.GeneDataAdapter;
import org.bioinfo.infrared.core.TranscriptDataAdapter;
import org.bioinfo.infrared.core.Orthologous;
import org.bioinfo.infrared.core.Transcript;
import org.bioinfo.infrared.dao.GenomeSequenceDataAdapter;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/gene")
@Produces("text/plain")
public class GeneWSServer extends GenericRestWSServer {
	
	
	public GeneWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	
	@GET
	@Path("/{geneId}/info")
	public Response getByEnsemblId(@PathParam("geneId") String geneId) {
		try {
			return  generateResponse(geneId, new GeneDataAdapter().getGeneByIds(geneId));//generateResponse(criteria);

		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	

	@GET
	@Path("/{geneId}/transcript")
	public Response getTranscriptsByEnsemblId(@PathParam("geneId") String query) {
		try {
			return  generateResponse(query, new TranscriptDataAdapter().getByGenes(query));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{geneId}/exon")
	public Response getExonsByEnsemblId2(@PathParam("geneId") String geneId) {
		try {
			
			Criteria criteria =  this.getSession().createCriteria(Exon.class).setFetchMode("exon2transcripts", FetchMode.SELECT)
			.createCriteria("exon2transcripts").setFetchMode("transcript", FetchMode.SELECT)
			.createCriteria("transcript").setFetchMode("gene", FetchMode.SELECT)
			.createCriteria("gene").add( Restrictions.eq("stableId", geneId));
			return generateResponse(criteria);
			
			/** HQL 
			Query query = this.getSession().createQuery("select e from Exon e JOIN FETCH e.exon2transcripts et JOIN et.transcript t JOIN  t.gene g where g.stableId in :stable_id").setParameterList("stable_id", StringUtils.toList(geneId, ","));  
			return generateResponse(query);
			**/
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{geneId}/exon2transcript")
	public Response getExon2TranscriptByEnsemblId(@PathParam("geneId") String geneId) {
		try {
			Query query = this.getSession().createQuery("select e from Exon2transcript e JOIN FETCH e.transcript t JOIN FETCH e.exon JOIN FETCH t.gene g where g.stableId in :stable_id").setParameterList("stable_id", StringUtils.toList(geneId, ","));
			return generateResponse(query);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{geneId}/orthologous")
	public Response getOrthologousByEnsemblId(@PathParam("geneId") String geneId) {
		try {
			Criteria criteria =  this.getSession().createCriteria(Orthologous.class)
			.createCriteria("gene").add( Restrictions.eq("stableId", geneId)).setFetchMode("gene", FetchMode.DEFAULT);
			return generateResponse(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", StringUtils.getStackTrace(e));
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}


//	@GET
//	@Path("/{geneId}/sequence")
//	public Response getSequenceByEnsemblId(@PathParam("geneId") String geneId) {
//		try {
//			Criteria criteria =  this.getSession().createCriteria(Gene.class).add(Restrictions.eq("stableId", geneId));
//			String sequence = new String();
//			if (criteria.list().size() > 0){
//				Gene gene = (Gene)criteria.list().get(0);
//				sequence = GenomeSequenceDataAdapter.getSequenceByRegion(gene.getChromosome(), gene.getStart(), gene.getEnd());
//			}
//			this.getSession().close();
//			
//			return Response.ok(sequence).build();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}


}
