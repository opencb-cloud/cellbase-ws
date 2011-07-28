package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.core.Cytoband;
import org.bioinfo.infrared.core.Exon;
import org.bioinfo.infrared.core.Gene;
import org.bioinfo.infrared.core.Orthologous;
import org.bioinfo.infrared.core.Snp;
import org.bioinfo.infrared.core.Transcript;
import org.bioinfo.infrared.dao.GenomeSequenceDataAdapter;
import org.bioinfo.infrared.dao.Region;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/{version}/{species}/chregion/")
@Produces("text/plain")
public class RegionWSServer extends GenericRestWSServer {
	public RegionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@GET
	@Path("/{chrRegionId}/gene")
	public Response getGenesByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			Criteria criteria =  this.getSession().createCriteria(Gene.class);
			return getFeaturesByRegion(chregionId, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/transcript")
	public Response getTranscriptByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			Criteria criteria =  this.getSession().createCriteria(Transcript.class);
			return getFeaturesByRegion(chregionId, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/snp")
	public Response getSnpByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			Criteria criteria =  this.getSession().createCriteria(Snp.class);
			return getFeaturesByRegion(chregionId, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/exon")
	public Response getExonByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			Criteria criteria =  this.getSession().createCriteria(Exon.class);
			return getFeaturesByRegion(chregionId, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/cytoband")
	public Response getCytobandByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			Criteria criteria =  this.getSession().createCriteria(Cytoband.class);
			return getFeaturesByRegion(chregionId, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/sequence")
	public Response getSequenceByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			Region region = Region.parseRegion(chregionId);
			List sequences = GenomeSequenceDataAdapter.getSequenceByRegion(region.getChromosome(), region.getStart(), region.getEnd());
			return this.generateResponse(chregionId, sequences);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private Response getFeaturesByRegion(String chregionId, Criteria criteria){
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			Disjunction disjunction = Restrictions.disjunction();
			for (Region region : regions) {
				Conjunction disjunctionRegion = Restrictions.conjunction();
				disjunctionRegion.add(Restrictions.eq("chromosome", region.getChromosome())).add( Restrictions.ge("start", region.getStart())).add(Restrictions.le("end", region.getEnd()));
				disjunction.add(disjunctionRegion);
			}
			criteria.add(disjunction);
			return generateResponse(criteria);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
