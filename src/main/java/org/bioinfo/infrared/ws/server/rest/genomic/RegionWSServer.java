package org.bioinfo.infrared.ws.server.rest.genomic;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.core.cellbase.ConservedRegion;
import org.bioinfo.infrared.core.cellbase.CpGIsland;
import org.bioinfo.infrared.core.cellbase.MutationPhenotypeAnnotation;
import org.bioinfo.infrared.core.cellbase.RegulatoryRegion;
import org.bioinfo.infrared.core.cellbase.StructuralVariation;
import org.bioinfo.infrared.lib.api.CpGIslandDBAdaptor;
import org.bioinfo.infrared.lib.api.CytobandDBAdaptor;
import org.bioinfo.infrared.lib.api.ExonDBAdaptor;
import org.bioinfo.infrared.lib.api.GeneDBAdaptor;
import org.bioinfo.infrared.lib.api.GenomeSequenceDBAdaptor;
import org.bioinfo.infrared.lib.api.MirnaDBAdaptor;
import org.bioinfo.infrared.lib.api.MutationDBAdaptor;
import org.bioinfo.infrared.lib.api.RegulatoryRegionDBAdaptor;
import org.bioinfo.infrared.lib.api.SnpDBAdaptor;
import org.bioinfo.infrared.lib.api.StructuralVariationDBAdaptor;
import org.bioinfo.infrared.lib.api.TfbsDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.lib.common.GenomeSequenceFeature;
import org.bioinfo.infrared.lib.common.Region;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/{version}/{species}/genomic/region")
@Produces("text/plain")
public class RegionWSServer extends GenericRestWSServer {
	private int histogramIntervalSize = 1000000;
	
	public RegionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	private RegulatoryRegionDBAdaptor getRegulatoryRegionDBAdaptor(){
		return dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);
	}
	private MutationDBAdaptor getMutationDBAdaptor(){
		return dbAdaptorFactory.getMutationDBAdaptor(this.species);
	}
	private CpGIslandDBAdaptor getCpGIslandDBAdaptor(){
		return dbAdaptorFactory.getCpGIslandDBAdaptor(this.species);
	}
	private StructuralVariationDBAdaptor getStructuralVariationDBAdaptor(){
		return dbAdaptorFactory.getStructuralVariationDBAdaptor(this.species);
	}
	
	private String getHistogramParameter() {
		MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();
		return (parameters.get("histogram") != null) ? parameters.get("histogram").get(0) : "false";
	}
	
	private int getHistogramIntervalSize() {
		MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();
		if (parameters.containsKey("interval")){
			int value = this.histogramIntervalSize;
			try{
				value =  Integer.parseInt(parameters.get("interval").get(0));
				return value;
			}
			catch(Exception exp){
				exp.printStackTrace();
				/** malformed string y no se puede castear a int **/
				return value;
			}
		}
		else{
			return this.histogramIntervalSize;
		}
	}
	
	private boolean hasHistogramQueryParam() {
		if (getHistogramParameter().toLowerCase().equals("true")){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	@GET
	@Path("/{chrRegionId}/gene")
	public Response getGenesByRegion(@PathParam("chrRegionId") String chregionId) {
		GeneDBAdaptor dbAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
		List<Region> regions = Region.parseRegions(chregionId);
		try {
			if (hasHistogramQueryParam()){
				long t1 = System.currentTimeMillis();
//				Response resp = generateResponse(chregionId, getHistogramByFeatures(dbAdaptor.getAllByRegionList(regions)));
				Response resp = generateResponse(chregionId, dbAdaptor.getAllIntervalFrequencies(regions.get(0), histogramIntervalSize));
				logger.info("Old histogram: "+(System.currentTimeMillis()-t1)+",  resp: "+resp.toString());
				return resp;
			}
			else{
				return generateResponse(chregionId, dbAdaptor.getAllByRegionList(regions));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/transcript")
	public Response getTranscriptByRegion(@PathParam("chrRegionId") String chregionId) {
		TranscriptDBAdaptor dbAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
		List<Region> regions = Region.parseRegions(chregionId);
		try {
			return generateResponse(chregionId, dbAdaptor.getAllByRegionList(regions));
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@GET
	@Path("/{chrRegionId}/exon")
	public Response getExonByRegion(@PathParam("chrRegionId") String chregionId) {
		ExonDBAdaptor dbAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
		List<Region> regions = Region.parseRegions(chregionId);
		try {
			return generateResponse(chregionId, dbAdaptor.getAllByRegionList(regions));
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/{chrRegionId}/snp")
	public Response getSnpByRegion(@PathParam("chrRegionId") String chregionId) {
		SnpDBAdaptor dbAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species);
		List<Region> regions = Region.parseRegions(chregionId);
		try {
			return generateResponse(chregionId, dbAdaptor.getAllByRegionList(regions));
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/cytoband")
	public Response getCytobandByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			CytobandDBAdaptor dbAdaptor = dbAdaptorFactory.getCytobandDBAdaptor(this.species);
			return generateResponse(chregionId, dbAdaptor.getAllByRegionList(regions));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	
	@GET
	@Path("/{chrRegionId}/sequence")
	public Response getSequenceByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			GenomeSequenceDBAdaptor dbAdaptor =  dbAdaptorFactory.getGenomeSequenceDBAdaptor(this.species);
			return this.generateResponse(chregionId, dbAdaptor.getByRegionList(regions));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/reverse")
	public Response getReverseSequenceByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			GenomeSequenceDBAdaptor dbAdaptor =  dbAdaptorFactory.getGenomeSequenceDBAdaptor(this.species);
			List<GenomeSequenceFeature> result = dbAdaptor.getByRegionList(regions, -1);
			return this.generateResponse(chregionId, result);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/{chrRegionId}/tfbs")
	public Response getTfByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return this.generateResponse(chregionId, adaptor.getAllByRegionList(regions));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/feature")
	public Response getFeatureMap(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			RegulatoryRegionDBAdaptor adaptor = dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);
			
			return this.generateResponse(chregionId, adaptor.getAllFeatureMapByRegion(regions));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	
	@GET
	@Path("/{chrRegionId}/regulatory")
	public Response getRegulatoryByRegion(@DefaultValue("")@QueryParam("type")String type, @PathParam("chrRegionId") String chregionId) {
		try {
			/** type ["open chromatin", "Polymerase", "HISTONE", "Transcription Factor"] **/
			List<Region> regions = Region.parseRegions(chregionId);
			RegulatoryRegionDBAdaptor adaptor = dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);
			
			List<List<RegulatoryRegion>> results;
			if (type.equals("")){
				results =  adaptor.getAllByRegionList(regions);
			}
			else{
				results = adaptor.getAllByRegionList(regions, Arrays.asList(type.split(",")));
			}
			
			if (hasHistogramQueryParam()){
//				return generateResponse(chregionId, getHistogramByFeatures(results));
				return generateResponse(chregionId, adaptor.getAllRegulatoryRegionIntervalFrequencies(regions.get(0), histogramIntervalSize));
			}
			else{
				return generateResponse(chregionId, results);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/mirnatarget")
	public Response getMirnaTargetByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			return this.generateResponse(chregionId, adaptor.getAllMiRnaTargetsByRegionList(regions));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/{chrRegionId}/conservedregion")
	public Response getConservedRegionByRegion(@PathParam("chrRegionId") String query) {
		try {
			List<Region> regions = Region.parseRegions(query);
			List<List<ConservedRegion>> ConservedRegionList = getRegulatoryRegionDBAdaptor().getAllConservedRegionByRegionList(regions);
			return this.generateResponse(query, ConservedRegionList);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
//		try {
//			List<Region> regions = Region.parseRegions(chregionId);
//			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
//			return this.generateResponse(chregionId, adaptor.getAllMiRnaTargetsByRegionList(regions));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
	}

	@GET
	@Path("/{chrRegionId}/mutation")
	public Response getMutationByRegion(@PathParam("chrRegionId") String query) {
		try {
			List<Region> regions = Region.parseRegions(query);
			List<List<MutationPhenotypeAnnotation>> mutationList = getMutationDBAdaptor().getAllByRegionList(regions);
			return this.generateResponse(query, mutationList);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{chrRegionId}/cpgisland")
	public Response getCpgIslandByRegion(@PathParam("chrRegionId") String query) {
		try {
			List<Region> regions = Region.parseRegions(query);
			List<List<CpGIsland>> cpGIslandList = getCpGIslandDBAdaptor().getAllByRegionList(regions);
			return this.generateResponse(query, cpGIslandList);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{chrRegionId}/structuralvariation")
	public Response getStructuralVariationByRegion(@PathParam("chrRegionId") String query) {
		try {
			List<Region> regions = Region.parseRegions(query);
			List<List<StructuralVariation>> structuralVariationList = getStructuralVariationDBAdaptor().getAllByRegionList(regions);
			return this.generateResponse(query, structuralVariationList);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	private  List<?>  getHistogramByFeatures(List<?> list){
		Histogram histogram = new Histogram(list, this.getHistogramIntervalSize());
		
		return histogram.getIntervals();
	}
	
	
}
