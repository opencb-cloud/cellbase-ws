package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.GeneDBManager;
import org.bioinfo.infrared.core.KaryotypeDBManager;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Cytoband;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.core.feature.Region;
import org.bioinfo.infrared.core.variation.AnnotatedMutation;
import org.bioinfo.infrared.core.variation.SNP;
import org.bioinfo.infrared.core.variation.TranscriptConsequenceType;
import org.bioinfo.infrared.variation.AnnotatedMutationDBManager;
import org.bioinfo.infrared.variation.SNPDBManager;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;


@Path("/{version}/{species}/genomic/chregion")
@Produces("text/plain")
public class ChromosomeRegionServer extends GenomicWSServer {

	public ChromosomeRegionServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@Override
	public boolean isValidSpecies() {
		return true;
	}

	@Override
	@GET
	@Path("/help")
	public String help() {
		return "region help";
	}
	
	@Override
	@GET
	@Path("/stats")
	public String stats() {
		return "region stats";
	}
	@GET
	@Path("/all")
	public String all() {
		return "1,2,3,4,5...";
	}
	
	
	@GET
	@Path("/{region}/snp")
	public Response getSnpsByRegion(@PathParam("region") String regionString) {
		logger.debug("in getSnpsByRegion1");
		try {
			List<Region> regions = Region.parseRegions(regionString);
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
		
			List<FeatureList<SNP>> snpList = snpDbManager.getAllByRegions(regions);
			// if there is a consequence type filter lets filter!
			if(uriInfo.getQueryParameters().get("consequence_type") != null) {
				List<FeatureList<SNP>> snpsByConsequenceType = new ArrayList<FeatureList<SNP>>(snpList.size());
				FeatureList<SNP> featListByConsequenceType = null;
				List<String> consequencetype = StringUtils.toList(uriInfo.getQueryParameters().get("consequence_type").get(0), ",");
				for(FeatureList<SNP> featList: snpList) {
					featListByConsequenceType = new FeatureList<SNP>();
					for(SNP snp: featList){
						for(TranscriptConsequenceType consquenceType: snp.getTranscriptConsequenceTypes()) {
							if(consequencetype.contains(consquenceType.getConsequenceType())) {
								featListByConsequenceType.add(snp);
								break;
							}
						}						
					}
					if(featListByConsequenceType != null && featListByConsequenceType.size() > 0) {
						snpsByConsequenceType.add(featListByConsequenceType);						
					}else {
						snpsByConsequenceType.add(null);
					}
				}
				snpList = snpsByConsequenceType;
			}
//			FeatureList<SNP> snps = new FeatureList<SNP>();
////			List<SNP> snps = new ArrayList<SNP>();
//			FeatureList<SNP> snpsByConsequenceType = new FeatureList<SNP>();
//			for(Region region: regions) {
//				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
//					if(region.getStart() == 0 && region.getEnd() == 0) {
//						snps.addAll(snpDbManager.getAllByRegion(region.getChromosome(), 1, Integer.MAX_VALUE));
//					}else {
//						snps.addAll(snpDbManager.getAllByRegion(region.getChromosome(), region.getStart(), region.getEnd()));
//					}
//				}
//			}
//			// if there is a consequence type filter lets filter!
//			if(uriInfo.getQueryParameters().get("consequence_type") != null) {
//				List<String> consequencetype = StringUtils.toList(uriInfo.getQueryParameters().get("consequence_type").get(0), ",");
//				for(SNP snp: snps) {
//					for(String consquenceType: snp.getConsequenceType()) {
//						if(consequencetype.contains(consquenceType)) {
//							snpsByConsequenceType.add(snp);
//						}
//					}
//				}
//				snps = snpsByConsequenceType;
//			}
//			this.listType = new TypeToken<FeatureList<SNP>>() {}.getType();
			return generateResponseFromListFeatureList(snpList, new TypeToken<List<FeatureList<SNP>>>() {}.getType());
			//return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, outputCompress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{region}/annotated_mutation")
	public Response getAnnotatedMutationsByRegion(@PathParam("region") String regionString) {
		try {
			List<Region> regions = Region.parseRegions(regionString);
			AnnotatedMutationDBManager snpDbManager = new AnnotatedMutationDBManager(infraredDBConnector);
			List<FeatureList<AnnotatedMutation>> annotMutations = snpDbManager.getAllByRegions(regions);
			return generateResponseFromListFeatureList(annotMutations, new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{region}/cytoband")
	public Response getCytobandByRegion(@PathParam("region") String region) {
		try {
			List<Region> regions = Region.parseRegions(region);
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			List<FeatureList<Cytoband>> CytobandList = karyotypeDbManager.getCytobandByRegions(regions);
			return generateResponseFromListFeatureList(CytobandList, new TypeToken<List<FeatureList<Cytoband>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{region}/gene")
	public  Response getGenesByRegion(@PathParam("region") String regionString) {
		try {
			List<Region> regions = Region.parseRegions(regionString);
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes  = geneDbManager.getAllByRegions(regions);
//			FeatureList<Gene> genes = new FeatureList<Gene>();
			FeatureList<Gene> genesByBiotype = new FeatureList<Gene>();

			List<String> biotypes = null;
			if(uriInfo.getQueryParameters().get("biotype") != null) {
				biotypes = StringUtils.toList(uriInfo.getQueryParameters().get("biotype").get(0), ",");
			}
			
//			for(Region region: regions) {
//				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
//					if(region.getStart() == 0 && region.getEnd() == 0) {
//						genes.addAll(geneDbManager.getAllByRegion(region.getChromosome(), 1, Integer.MAX_VALUE, biotypes));
//					}else {
//						genes.addAll(geneDbManager.getAllByRegion(region.getChromosome(), region.getStart(), region.getEnd(), biotypes));
//					}
//				}
//			}
			// if there is a biotype filter lets filter!
//			if(uriInfo.getQueryParameters().get("biotype") != null) {
//				List<String> biotypes = StringUtils.toList(uriInfo.getQueryParameters().get("biotype").get(0), ",");
//				for(Gene gene: genes) {
//					if(biotypes.contains(gene.getBiotype())) {
//						genesByBiotype.add(gene);
//					}
//				}
//				genes = genesByBiotype;
//			}
//			this.listType = new TypeToken<FeatureList<Gene>>(){}.getType();
			return generateResponseFromListFeatureList(genes, new TypeToken<List<FeatureList<Gene>>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return generateErrorMessage(e.toString());
		}
	}

	//	@GET
	//	@Path("/transcript")
	//	public Response getTranscriptByRegion(@PathParam("species") String species, @PathParam("region") String regionString, @Context UriInfo ui) {
	//		init(species, ui);
	//		Region region = new Region(regionString);
	//		try {
	//			connect();
	//			TranscriptDBManager geneDbManager = new TranscriptDBManager(infraredDBConnector);
	//			FeatureList<Transcript> snps = null;
	//			if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
	//				if(region.getStart() == 0 && region.getEnd() == 0) {
	//					snps = geneDbManager.getAllByLocation(region.getChromosome(), 1, Integer.MAX_VALUE);
	//				}else {
	//					snps = geneDbManager.getAllByLocation(region.getChromosome(), region.getStart(), region.getEnd());
	//				}
	//			}
	//			return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, outputCompress);
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

	
}
