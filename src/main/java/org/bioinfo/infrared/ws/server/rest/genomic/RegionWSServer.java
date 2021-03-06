package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.ArrayList;
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

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.cellbase.CpGIsland;
import org.bioinfo.infrared.core.cellbase.ExonToTranscript;
import org.bioinfo.infrared.core.cellbase.Gene;
import org.bioinfo.infrared.core.cellbase.MirnaTarget;
import org.bioinfo.infrared.core.cellbase.MutationPhenotypeAnnotation;
import org.bioinfo.infrared.core.cellbase.RegulatoryRegion;
import org.bioinfo.infrared.core.cellbase.StructuralVariation;
import org.bioinfo.infrared.core.cellbase.Tfbs;
import org.bioinfo.infrared.core.cellbase.Transcript;
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
import org.bioinfo.infrared.lib.common.IntervalFeatureFrequency;
import org.bioinfo.infrared.lib.common.Region;
import org.bioinfo.infrared.lib.io.output.StringWriter;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

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
	public Response getGenesByRegion(@PathParam("chrRegionId") String chregionId, @DefaultValue("false") @QueryParam("transcript") String transcripts, @DefaultValue("") @QueryParam("biotype") String biotype) {
		try {
			checkVersionAndSpecies();
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(chregionId);

			if (hasHistogramQueryParam()) {
				long t1 = System.currentTimeMillis();
				//				Response resp = generateResponse(chregionId, getHistogramByFeatures(dbAdaptor.getAllByRegionList(regions)));
				Response resp = generateResponse(chregionId, geneDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()));
				logger.info("Old histogram: "+(System.currentTimeMillis()-t1)+",  resp: "+resp.toString());
				return resp;
			}else {
				if(transcripts != null && transcripts.equalsIgnoreCase("true") && outputFormat.equalsIgnoreCase("json")) {

					List<List<Gene>> geneListList = null;
					if(biotype != null && !biotype.equals("")) {
						geneListList = geneDBAdaptor.getAllByRegionList(regions, StringUtils.toList(biotype, ","));
					}else {
						geneListList = geneDBAdaptor.getAllByRegionList(regions);
					}

					// Getting Gene IDs
					List<List<String>> geneStringList = new ArrayList<List<String>>(geneListList.size() * geneListList.get(0).size());
					for(int i=0; i<geneListList.size(); i++) {
						geneStringList.add(new ArrayList<String>());
						for(int j=0; j<geneListList.get(i).size(); j++) {
							geneStringList.get(i).add(geneListList.get(i).get(j).getStableId());
						}	
					}
					StringBuilder response = new StringBuilder();
					response.append("[");
					for(int i = 0; i < geneListList.size(); i++) {
						response.append("[");
						// This query get Genes filled up with Transcript and Exons
						List<Gene> geneList = geneDBAdaptor.getAllByEnsemblIdList(geneStringList.get(i), true);
						System.out.println(">>>"+geneListList.get(i).size()+" == "+geneList.size());
						boolean removeComma = false;
						for(int j = 0; j < geneList.size(); j++) {
							removeComma = true;
							response.append("{");
							response.append("\"stableId\":"+"\""+geneList.get(j).getStableId()+"\",");
							response.append("\"externalName\":"+"\""+geneList.get(j).getExternalName()+"\",");
							response.append("\"externalDb\":"+"\""+geneList.get(j).getExternalDb()+"\",");
							response.append("\"biotype\":"+"\""+geneList.get(j).getBiotype()+"\",");
							response.append("\"status\":"+"\""+geneList.get(j).getStatus()+"\",");
							response.append("\"chromosome\":"+"\""+geneList.get(j).getChromosome()+"\",");
							response.append("\"start\":"+geneList.get(j).getStart()+",");
							response.append("\"end\":"+geneList.get(j).getEnd()+",");
							response.append("\"strand\":"+"\""+geneList.get(j).getStrand()+"\",");
							response.append("\"source\":"+"\""+geneList.get(j).getSource()+"\",");
							response.append("\"description\":"+"\""+geneList.get(j).getDescription()+"\",");
							response.append("\"transcripts\":[");

							for(Transcript trans: geneList.get(j).getTranscripts()) {
								response.append(gson.toJson(trans));
								// remove last '}'
								response.replace(response.length()-1, response.length(), "");
								response.append(",\"exonToTranscripts\":[");
								for(ExonToTranscript e2t: trans.getExonToTranscripts()) { 
									response.append(gson.toJson(e2t));
									// remove last '}'
									response.replace(response.length()-1, response.length(), "");
									response.append(",\"exon\":{");
									response.append("\"stableId\":\""+e2t.getExon().getStableId()+"\",");
									response.append("\"chromosome\":\""+e2t.getExon().getChromosome()+"\",");
									response.append("\"start\":\""+e2t.getExon().getStart()+"\",");
									response.append("\"end\":\""+e2t.getExon().getEnd()+"\",");
									response.append("\"strand\":\""+e2t.getExon().getStrand()+"\"");
									response.append("}},");
								}
								response.replace(response.length()-1, response.length(), "");
								response.append("]},");
							}
							response.replace(response.length()-1, response.length(), "");
							response.append("]");
							response.append("},");
						}
						if(removeComma){
							response.replace(response.length()-1, response.length(), "");
						}
						response.append("],");
					}
					response.replace(response.length()-1, response.length(), "");
					response.append("]");
					//					return generateResponse(chregionId, "GENE", geneDBAdaptor.getAllByRegionList(regions));		
					//					return generateResponse(chregionId, "GENE", Arrays.asList(response));
					return createOkResponse(response.toString());
				}else {
					if(biotype != null && !biotype.equals("")) {
						return generateResponse(chregionId, "GENE", geneDBAdaptor.getAllByRegionList(regions, StringUtils.toList(biotype, ",")));
					}else {
						return generateResponse(chregionId, "GENE", geneDBAdaptor.getAllByRegionList(regions));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getGenesByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/transcript")
	public Response getTranscriptByRegion(@PathParam("chrRegionId") String chregionId, @DefaultValue("") @QueryParam("biotype") String biotype) {
		try {
			checkVersionAndSpecies();
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(chregionId);
			if(biotype != null && !biotype.equals("")) {
				return generateResponse(chregionId, "TRANSCRIPT", transcriptDBAdaptor.getAllByRegionList(regions, StringUtils.toList(biotype, ",")));
			}else {
				return generateResponse(chregionId, "TRANSCRIPT", transcriptDBAdaptor.getAllByRegionList(regions));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return createErrorResponse("getTranscriptByRegion", e.toString());
		}

	}

	@GET
	@Path("/{chrRegionId}/exon")
	public Response getExonByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			checkVersionAndSpecies();
			ExonDBAdaptor exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(chregionId);
			return generateResponse(chregionId, "EXON", exonDBAdaptor.getAllByRegionList(regions));
		}catch(Exception e) {
			e.printStackTrace();
			return createErrorResponse("getExonByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/snp")
	public Response getSnpByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			checkVersionAndSpecies();
			SnpDBAdaptor snpDBAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(chregionId);

			if(hasHistogramQueryParam()){
				//				long t1 = System.currentTimeMillis();
				//				Response resp = generateResponse(chregionId, getHistogramByFeatures(dbAdaptor.getAllByRegionList(regions)));
				Response resp = generateResponse(chregionId, snpDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()));
				//				logger.info("Old histogram: "+(System.currentTimeMillis()-t1)+",  resp: "+resp.toString());
				return resp;
			}else {
				// remove regions bigger than 10Mb
				if(regions != null) {
					for(Region region: regions) {
						if((region.getEnd() - region.getStart()) > 10000000) {
							return createErrorResponse("getSNpByRegion", "Regions must be smaller than 10Mb");
						}
					}
				}
				return generateResponse(chregionId, "SNP", snpDBAdaptor.getAllByRegionList(regions));
			}

		}catch(Exception e) {
			e.printStackTrace();
			return createErrorResponse("getSnpByRegion", e.toString());
		}
	}

	

	@GET
	@Path("/{chrRegionId}/mutation")
	public Response getMutationByRegion(@PathParam("chrRegionId") String query) {
		try {
			checkVersionAndSpecies();
			MutationDBAdaptor mutationDBAdaptor =  dbAdaptorFactory.getMutationDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(query);

			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = mutationDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				List<List<MutationPhenotypeAnnotation>> mutationList = mutationDBAdaptor.getAllByRegionList(regions);
				return this.generateResponse(query, "MUTATION", mutationList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getMutationByRegion", e.toString());
		}
	}

	
	@GET
	@Path("/{chrRegionId}/structural_variation")
	public Response getStructuralVariationByRegion(@PathParam("chrRegionId") String query, @QueryParam("min_length") Integer minLength, @QueryParam("max_length") Integer maxLength) {
		try {
			checkVersionAndSpecies();
			StructuralVariationDBAdaptor structuralVariationDBAdaptor = dbAdaptorFactory.getStructuralVariationDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(query);

			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = structuralVariationDBAdaptor.getAllIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return generateResponse(query, intervalList);
			}else{
				List<List<StructuralVariation>> structuralVariationList = null;
				if(minLength == null && maxLength == null) {
					structuralVariationList = structuralVariationDBAdaptor.getAllByRegionList(regions);					
				}else {
					if(minLength == null) {
						minLength = 1;
					}
					if(maxLength == null) {
						maxLength = Integer.MAX_VALUE;
					}
					structuralVariationList = structuralVariationDBAdaptor.getAllByRegionList(regions, minLength, maxLength);
				}
				return this.generateResponse(query, "STRUCTURAL_VARIATION", structuralVariationList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getStructuralVariationByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/cytoband")
	public Response getCytobandByRegion(@PathParam("chrRegionId") String chregionId) {
		try {
			checkVersionAndSpecies();
			CytobandDBAdaptor cytobandDBAdaptor = dbAdaptorFactory.getCytobandDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(chregionId);
			return generateResponse(chregionId, cytobandDBAdaptor.getAllByRegionList(regions));
		}catch(Exception e) {
			e.printStackTrace();
			return createErrorResponse("getCytobandByRegion", e.toString());
		}
	}


	@GET
	@Path("/{chrRegionId}/sequence")
	public Response getSequenceByRegion(@PathParam("chrRegionId") String chregionId, @DefaultValue("1") @QueryParam("strand") String strandParam, @DefaultValue("") @QueryParam("format") String format) {
		try {
			checkVersionAndSpecies();
			List<Region> regions = Region.parseRegions(chregionId);
			GenomeSequenceDBAdaptor genomeSequenceDBAdaptor =  dbAdaptorFactory.getGenomeSequenceDBAdaptor(this.species, this.version);
			int strand = 1;
			//			String result;
			try {
				strand = Integer.parseInt(strandParam);
				//				List<GenomeSequence> gs = genomeSequenceDBAdaptor.getByRegionList(regions, strand);
				//				if(gs != null && gs.size() > 0) {
				//				}
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
			checkVersionAndSpecies();
			List<Region> regions = Region.parseRegions(chregionId);

			GenomeSequenceDBAdaptor dbAdaptor =  dbAdaptorFactory.getGenomeSequenceDBAdaptor(this.species, this.version);
			List<GenomeSequenceFeature> result = dbAdaptor.getByRegionList(regions, -1);
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
			checkVersionAndSpecies();
			TfbsDBAdaptor tfbsDBAdaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(query);

			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = tfbsDBAdaptor.getAllTfIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return generateResponse(query, intervalList);
			}else{
				List<List<Tfbs>> tfList = tfbsDBAdaptor.getAllByRegionList(regions);
				return this.generateResponse(query, "TFBS", tfList);
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
			checkVersionAndSpecies();
			List<Region> regions = Region.parseRegions(chregionId);

			RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor = dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species, this.version);

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
			checkVersionAndSpecies();
			RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor = dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species, this.version);
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
				return generateResponse(chregionId, "REGULATORY_REGION", results);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getRegulatoryByRegion", e.toString());
		}
	}

	
	@GET
	@Path("/{chrRegionId}/mirna_target")
	public Response getMirnaTargetByRegion(@PathParam("chrRegionId") String query, @DefaultValue("") @QueryParam("source") String source) {
		try {
			checkVersionAndSpecies();
			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(query);

			if (hasHistogramQueryParam()){
				System.out.println("PAKO:"+"si");
				List<IntervalFeatureFrequency> intervalList = mirnaDBAdaptor.getAllMirnaTargetsIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return  generateResponse(query, intervalList);
			}else{
				System.out.println("PAKO:"+"NO");
				List<List<MirnaTarget>> mirnaTargetList = mirnaDBAdaptor.getAllMiRnaTargetsByRegionList(regions);
				return this.generateResponse(query, "MIRNA_TARGET", mirnaTargetList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getMirnaTargetByRegion", e.toString());
		}
	}

	
	@GET
	@Path("/{chrRegionId}/cpg_island")
	public Response getCpgIslandByRegion(@PathParam("chrRegionId") String query) {
		try {
			checkVersionAndSpecies();
			CpGIslandDBAdaptor cpGIslandDBAdaptor =  dbAdaptorFactory.getCpGIslandDBAdaptor(this.species, this.version);
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
	@Path("/{chrRegionId}/conserved_region")
	public Response getConservedRegionByRegion(@PathParam("chrRegionId") String query) {
		try {
			checkVersionAndSpecies();
			RegulatoryRegionDBAdaptor regulatoryRegionDBAdaptor =  dbAdaptorFactory.getRegulatoryRegionDBAdaptor(this.species, this.version);
			List<Region> regions = Region.parseRegions(query);

			if (hasHistogramQueryParam()){
				List<IntervalFeatureFrequency> intervalList = regulatoryRegionDBAdaptor.getAllConservedRegionIntervalFrequencies(regions.get(0), getHistogramIntervalSize()); 
				return generateResponse(query, intervalList);
			}else{
				return this.generateResponse(query, regulatoryRegionDBAdaptor.getAllConservedRegionByRegionList(regions));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getConservedRegionByRegion", e.toString());
		}
	}



	@GET
	@Path("/{chrRegionId}/peptide")
	public Response getPeptideByRegion(@PathParam("chrRegionId") String region) {
		try {
			checkVersionAndSpecies();
			List<Region> regions = Region.parseRegions(region);
			boolean isUTR = false;
			List<String> peptide = new ArrayList<String>(0);
			//			GenomicRegionFeatureDBAdaptor genomicRegionFeatureDBAdaptor = dbAdaptorFactory.getFeatureMapDBAdaptor(this.species);
			//			if (regions != null && !regions.get(0).equals("")){
			//				for (Region reg: regions){
			//					List<FeatureMap> featureMapList = genomicRegionFeatureDBAdaptor.getFeatureMapsByRegion(reg);
			//					if(featureMapList != null){
			//						for(FeatureMap featureMap: featureMapList) {
			//							String line = "";
			//							if(featureMap.getFeatureType().equalsIgnoreCase("5_prime_utr") || featureMap.getFeatureType().equalsIgnoreCase("3_prime_utr")) {
			//								isUTR = true;
			//								line = featureMap.getTranscriptStableId()+"\tNo-coding\t\t";
			//								peptide.add(line);
			//							}else{
			//								isUTR = false;
			//								if(featureMap.getFeatureType().equalsIgnoreCase("exon")) {
			//									if (!isUTR && featureMap.getBiotype().equalsIgnoreCase("protein_coding")) {
			//										System.out.println("Exon: "+featureMap.getFeatureId());
			//										System.out.println("Phase: "+featureMap.getExonPhase());
			//										if(!featureMap.getExonPhase().equals("") && !featureMap.getExonPhase().equals("-1")) {
			//											System.out.println("with phase");
			//											int aaPositionStart = -1;
			//											int aaPositionEnd = -1;
			//											if(featureMap.getStrand().equals("1")) {
			//												aaPositionStart = ((reg.getStart()-featureMap.getStart()+1+featureMap.getExonCdnaCodingStart()-featureMap.getTranscriptCdnaCodingStart())/3)+1;
			//												aaPositionEnd = ((reg.getEnd()-featureMap.getStart()+1+featureMap.getExonCdnaCodingStart()-featureMap.getTranscriptCdnaCodingStart())/3)+1;
			//											}else {
			//												aaPositionStart = ((featureMap.getEnd()-reg.getStart()+1+featureMap.getExonCdnaCodingStart()-featureMap.getTranscriptCdnaCodingStart())/3)+1;
			//												aaPositionEnd = ((featureMap.getEnd()-reg.getEnd()+1+featureMap.getExonCdnaCodingStart()-featureMap.getTranscriptCdnaCodingStart())/3)+1;
			//											}
			//											line = featureMap.getTranscriptStableId()+"\t"+"Protein"+"\t"+aaPositionStart+"\t"+aaPositionEnd;
			//											peptide.add(line);
			//										}else{
			//											if(!featureMap.getExonPhase().equals("") && !featureMap.getExonPhase().equals("-1")) {
			//												
			//											}
			//										}
			//									}
			//								}
			//							}
			//						}
			//					}
			//				}
			//				
			//			}
			return createOkResponse(StringWriter.serialize(peptide));
			//			return generateResponse(region, exonIds);

		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getPeptideByRegion", e.toString());
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

	//	private class GeneListDeserializer implements JsonSerializer<List> {
	//
	//		@Override
	//		public JsonElement serialize(List geneListList, Type typeOfSrc, JsonSerializationContext context) {
	//			System.out.println("GeneListDeserializer - gene JSON elem: ");
	//			Gson gsonLocal = gson = new GsonBuilder().serializeNulls().setExclusionStrategies(new FeatureExclusionStrategy()).create();
	//			//logger.debug("SnpWSCLient - FeatureListDeserializer - json FeatureList<SNP> size: "+json.getAsJsonArray().size());
	//			List<List<Gene>> snps = new ArrayList<List<Gene>>(json.getAsJsonArray().size());
	//			List<Gene> geneList;
	//			JsonArray ja = new JsonArray();
	//			for(JsonElement geneArray: json.getAsJsonArray()) {
	//				System.out.println("GeneListDeserializer - gene JSON elem: ");
	//				geneList = new ArrayList<Gene>(geneArray.getAsJsonArray().size());
	//				for(JsonElement gene: geneArray.getAsJsonArray()) {
	//					geneList.add(gsonLocal.fromJson(gene, Gene.class));					
	//				}
	//				snps.add(geneList);
	//			}
	//			return null;
	//		}
	//		
	//		@Override
	//		public List<List<Gene>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	//			System.out.println("GeneListDeserializer - gene JSON elem: ");
	//			Gson gsonLocal = gson = new GsonBuilder().serializeNulls().setExclusionStrategies(new FeatureExclusionStrategy()).create();
	//			//logger.debug("SnpWSCLient - FeatureListDeserializer - json FeatureList<SNP> size: "+json.getAsJsonArray().size());
	//			List<List<Gene>> snps = new ArrayList<List<Gene>>(json.getAsJsonArray().size());
	//			List<Gene> geneList;
	//			for(JsonElement geneArray: json.getAsJsonArray()) {
	//				System.out.println("GeneListDeserializer - gene JSON elem: ");
	//				geneList = new ArrayList<Gene>(geneArray.getAsJsonArray().size());
	//				for(JsonElement gene: geneArray.getAsJsonArray()) {
	//					geneList.add(gsonLocal.fromJson(gene, Gene.class));					
	//				}
	//				snps.add(geneList);
	//			}
	//			return snps;
	//		}
	//	}

}
