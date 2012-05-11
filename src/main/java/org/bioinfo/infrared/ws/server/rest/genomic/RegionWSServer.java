package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.bioinfo.infrared.core.cellbase.GenomeSequence;
import org.bioinfo.infrared.core.cellbase.MirnaTarget;
import org.bioinfo.infrared.core.cellbase.MutationPhenotypeAnnotation;
import org.bioinfo.infrared.core.cellbase.RegulatoryRegion;
import org.bioinfo.infrared.core.cellbase.StructuralVariation;
import org.bioinfo.infrared.core.cellbase.Tfbs;
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
import org.bioinfo.infrared.lib.common.IntervalFeatureFrequency;
import org.bioinfo.infrared.lib.common.Region;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/genomic/region")
@Produces("text/plain")
public class RegionWSServer extends GenericRestWSServer {
	//	private int histogramIntervalSize = 1000000;
	private int histogramIntervalSize = 200000;

	public RegionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
	}

	//	private	RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor =  dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);
	//	private MutationDBAdaptor mutationDBAdaptor =  dbAdaptorFactory.getMutationDBAdaptor(this.species);
	//	private CpGIslandDBAdaptor cpGIslandDBAdaptor =  dbAdaptorFactory.getCpGIslandDBAdaptor(this.species);
	//	private	StructuralVariationDBAdaptor structuralVariationDBAdaptor = dbAdaptorFactory.getStructuralVariationDBAdaptor(this.species);
	//	private MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
	//	private TfbsDBAdaptor tfbsDBAdaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);

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
				logger.debug("Interval: "+value);
				return value;
			}catch(Exception exp) {
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
		}else{
			return false;
		}
	}

	@GET
	@Path("/{chrRegionId}/gene")
	public Response getGenesByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(chregionId);

			if (hasHistogramQueryParam()){
				long t1 = System.currentTimeMillis();
				//				Response resp = generateResponse(chregionId, getHistogramByFeatures(dbAdaptor.getAllByRegionList(regions)));
				Response resp = generateResponse(chregionId, geneDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()));
				logger.info("Old histogram: "+(System.currentTimeMillis()-t1)+",  resp: "+resp.toString());
				return resp;
			}else {
				return generateResponse(chregionId, geneDBAdaptor.getAllByRegionList(regions));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return createErrorResponse("getGenesByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/transcript")
	public Response getTranscriptByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(chregionId);
			
			return generateResponse(chregionId, transcriptDBAdaptor.getAllByRegionList(regions));
		}catch(IOException e) {
			e.printStackTrace();
			return createErrorResponse("getTranscriptByRegion", e.toString());
		}

	}

	@GET
	@Path("/{chrRegionId}/exon")
	public Response getExonByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			ExonDBAdaptor exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(chregionId);
			
			return generateResponse(chregionId, exonDBAdaptor.getAllByRegionList(regions));
		}catch(IOException e) {
			e.printStackTrace();
			return createErrorResponse("getExonByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/snp")
	public Response getSnpByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			SnpDBAdaptor snpDBAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(chregionId);
			
			if(hasHistogramQueryParam()){
				//				long t1 = System.currentTimeMillis();
				//				Response resp = generateResponse(chregionId, getHistogramByFeatures(dbAdaptor.getAllByRegionList(regions)));
				Response resp = generateResponse(chregionId, snpDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()));
				//				logger.info("Old histogram: "+(System.currentTimeMillis()-t1)+",  resp: "+resp.toString());
				return resp;
			}else {
				return generateResponse(chregionId, snpDBAdaptor.getAllByRegionList(regions));
			}

		}catch(IOException e) {
			e.printStackTrace();
			return createErrorResponse("getSnpByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/cytoband")
	public Response getCytobandByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			CytobandDBAdaptor cytobandDBAdaptor = dbAdaptorFactory.getCytobandDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(chregionId);
			return generateResponse(chregionId, cytobandDBAdaptor.getAllByRegionList(regions));
		}catch(Exception e) {
			e.printStackTrace();
			return createErrorResponse("getCytobandByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/sequence")
	public Response getSequenceByRegion(@PathParam("chrRegionId") String chregionId, @DefaultValue("1") @QueryParam("strand") String strandParam) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			GenomeSequenceDBAdaptor genomeSequenceDBAdaptor =  dbAdaptorFactory.getGenomeSequenceDBAdaptor(this.species);
			int strand = 1;
			try {
				strand = Integer.parseInt(strandParam);
			}catch(Exception e) {
				strand = 1;
				logger.warn("RegionWSServer: method getSequence could not convert strand to integer");
			}
			return this.generateResponse(chregionId, genomeSequenceDBAdaptor.getByRegionList(regions, strand));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getSequenceByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/reverse")
	@Deprecated
	public Response getReverseSequenceByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			
			GenomeSequenceDBAdaptor dbAdaptor =  dbAdaptorFactory.getGenomeSequenceDBAdaptor(this.species);
			List<GenomeSequence> result = dbAdaptor.getByRegionList(regions, -1);
			return this.generateResponse(chregionId, result);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getReverseSequenceByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/tfbs")
	public Response getTfByRegion(@PathParam("chrRegionId") String query) {
		try {
			TfbsDBAdaptor tfbsDBAdaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(query);

			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = tfbsDBAdaptor.getAllTfIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				List<List<Tfbs>> tfList = tfbsDBAdaptor.getAllByRegionList(regions);
				return this.generateResponse(query, tfList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getTfByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/feature")
	public Response getFeatureMap(@PathParam("chrRegionId") String chregionId) {
		try {
			List<Region> regions = Region.parseRegions(chregionId);
			
			RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor = dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);

			return this.generateResponse(chregionId, regulatoryRegionDBAdaptor.getAllFeatureMapByRegion(regions));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getFeatureMap", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/regulatory")
	public Response getRegulatoryByRegion(@PathParam("chrRegionId") String chregionId, @DefaultValue("") @QueryParam("type") String type) {
		try {
			RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor = dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);
			/** type ["open chromatin", "Polymerase", "HISTONE", "Transcription Factor"] **/
			List<Region> regions = Region.parseRegions(chregionId);
			
			if(hasHistogramQueryParam()) {
				//				return generateResponse(chregionId, getHistogramByFeatures(results));
				return generateResponse(chregionId, regulatoryRegionDBAdaptor.getAllRegulatoryRegionIntervalFrequencies(regions.get(0), getHistogramIntervalSize(), type));
			}else {
				List<List<RegulatoryRegion>> results;
				if(type.equals("")){
					results =  regulatoryRegionDBAdaptor.getAllByRegionList(regions);
				}else {
					results = regulatoryRegionDBAdaptor.getAllByRegionList(regions, Arrays.asList(type.split(",")));
				}
				return generateResponse(chregionId, results);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getRegulatoryByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/mirnatarget")
	public Response getMirnaTargetByRegion(@PathParam("chrRegionId") String query) {
		try {
			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(query);
			
			if (hasHistogramQueryParam()){
				System.out.println("PAKO:"+"si");
				List<IntervalFeatureFrequency> intervalList = mirnaDBAdaptor.getAllMirnaTargetsIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				System.out.println("PAKO:"+"NO");
				List<List<MirnaTarget>> mirnaTargetList = mirnaDBAdaptor.getAllMiRnaTargetsByRegionList(regions);
				return this.generateResponse(query, mirnaTargetList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getMirnaTargetByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/conservedregion")
	public Response getConservedRegionByRegion(@PathParam("chrRegionId") String query) {
		try {
			RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor =  dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(query);
			
			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = regulatoryRegionDBAdaptor.getAllConservedRegionIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				List<List<ConservedRegion>> ConservedRegionList = regulatoryRegionDBAdaptor.getAllConservedRegionByRegionList(regions);
				return this.generateResponse(query, ConservedRegionList);
			}


		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getConservedRegionByRegion", e.toString());
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
			MutationDBAdaptor mutationDBAdaptor =  dbAdaptorFactory.getMutationDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(query);
			
			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = mutationDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				List<List<MutationPhenotypeAnnotation>> mutationList = mutationDBAdaptor.getAllByRegionList(regions);
				return this.generateResponse(query, mutationList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getMutationByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/cpgisland")
	public Response getCpgIslandByRegion(@PathParam("chrRegionId") String query) {
		try {
			CpGIslandDBAdaptor cpGIslandDBAdaptor =  dbAdaptorFactory.getCpGIslandDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(query);
			
			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = cpGIslandDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				List<List<CpGIsland>> cpGIslandList = cpGIslandDBAdaptor.getAllByRegionList(regions);
				return this.generateResponse(query, cpGIslandList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getCpgIslandByRegion", e.toString());
		}
	}

	@GET
	@Path("/{chrRegionId}/structuralvariation")
	public Response getStructuralVariationByRegion(@PathParam("chrRegionId") String query) {
		try {
			StructuralVariationDBAdaptor structuralVariationDBAdaptor = dbAdaptorFactory.getStructuralVariationDBAdaptor(this.species);
			List<Region> regions = Region.parseRegions(query);
			
			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = structuralVariationDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				List<List<StructuralVariation>> structuralVariationList = structuralVariationDBAdaptor.getAllByRegionList(regions);
				return this.generateResponse(query, structuralVariationList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getStructuralVariationByRegion", e.toString());
		}
	}


	@Deprecated
	private  List<?>  getHistogramByFeatures(List<?> list){
		Histogram histogram = new Histogram(list, this.getHistogramIntervalSize());
		return histogram.getIntervals();
	}
	
	@GET
	public Response getHelp() {
		return help();
	}
	
	@GET
	@Path("/help")
	public Response help() {
		StringBuilder sb = new StringBuilder();
		sb.append("Input:\n");
		sb.append("Chr. region format: chr:start-end (i.e.: 7:245000-501560)\n\n\n");
		sb.append("Resources:\n");
		sb.append("- gene: This resource obtain the genes belonging to one of the regions specified.\n");
		sb.append(" Output columns: Ensembl ID, external name, external name source, biotype, status, chromosome, start, end, strand, source, description.\n\n");
		sb.append("- transcript: This resource obtain the transcripts belonging to one of the regions specified.\n");
		sb.append(" Output columns: Ensembl ID, external name, external name source, biotype, status, chromosome, start, end, strand, coding region start, coding region end, cdna coding start, cdna coding end, description.\n\n");
		sb.append("- snp: To obtain the SNPs belonging to one of the regions specified write snp\n");
		sb.append(" Output columns: rsID, chromosome, position, Ensembl consequence type, SO consequence type, sequence.\n\n");
		sb.append("- sequence: To obtain the genomic sequence of one region write sequence as resource\n\n");
		sb.append("- tfbs: To obtain the TFBS of one region write sequence as resource\n");
		sb.append(" Output columns: TF name, target gene name, chromosome, start, end, cell type, sequence, score.\n\n");
		sb.append("- regulatory: To obtain the regulatory elements of one region write sequence as resource\n");
		sb.append(" Output columns: name, type, chromosome, start, end, cell type, source.\n\n\n");
		sb.append("Documentation:\n");
		sb.append("http://docs.bioinfo.cipf.es/projects/cellbase/wiki/Genomic_rest_ws_api#Region");
		
		return createOkResponse(sb.toString());
	}

}
