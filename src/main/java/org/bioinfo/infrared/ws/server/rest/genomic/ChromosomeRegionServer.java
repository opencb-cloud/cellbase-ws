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
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.core.feature.Position;
import org.bioinfo.infrared.core.feature.Region;
import org.bioinfo.infrared.core.regulatory.ConservedRegion;
import org.bioinfo.infrared.core.regulatory.JasparTfbs;
import org.bioinfo.infrared.core.regulatory.MiRnaGene;
import org.bioinfo.infrared.core.regulatory.MiRnaTarget;
import org.bioinfo.infrared.core.regulatory.OregannoTfbs;
import org.bioinfo.infrared.core.regulatory.Triplex;
import org.bioinfo.infrared.core.variation.AnnotatedMutation;
import org.bioinfo.infrared.core.variation.SNP;
import org.bioinfo.infrared.core.variation.SpliceSite;
import org.bioinfo.infrared.core.variation.TranscriptConsequenceType;
import org.bioinfo.infrared.regulatory.ConservedRegionDBManager;
import org.bioinfo.infrared.regulatory.JasparTfbsDBManager;
import org.bioinfo.infrared.regulatory.MiRnaGeneDBManager;
import org.bioinfo.infrared.regulatory.MiRnaTargetDBManager;
import org.bioinfo.infrared.regulatory.OregannoTfbsDBManager;
import org.bioinfo.infrared.regulatory.TriplexDBManager;
import org.bioinfo.infrared.variation.AnnotatedMutationDBManager;
import org.bioinfo.infrared.variation.SNPDBManager;
import org.bioinfo.infrared.variation.SpliceSiteDBManager;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;


@Path("/{version}/{species}/genomic/chregion")
@Produces("text/plain")
public class ChromosomeRegionServer extends GenomicWSServer {

	public ChromosomeRegionServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
//		connect();
	}
	
	@GET
	@Path("/help")
	public String help() {
		return "region help";
	}
	
	@GET
	@Path("/all")
	public String all() {
		return "1,2,3,4,5...";
	}
	
	@GET
	@Path("/{region}/gene")
	public  Response getGenesByRegion(@PathParam("region") String regionString) {
		try {
			List<Region> regions = Region.parseRegions(regionString);
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			FeatureList<Gene> genes = new FeatureList<Gene>();
			FeatureList<Gene> genesByBiotype = new FeatureList<Gene>();

			List<String> biotypes = null;
			if(uriInfo.getQueryParameters().get("biotype") != null) {
				biotypes = StringUtils.toList(uriInfo.getQueryParameters().get("biotype").get(0), ",");
			}
			
			for(Region region: regions) {
				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
					if(region.getStart() == 0 && region.getEnd() == 0) {
						genes.addAll(geneDbManager.getAllByRegion(region.getChromosome(), 1, Integer.MAX_VALUE, biotypes));
					}else {
						genes.addAll(geneDbManager.getAllByRegion(region.getChromosome(), region.getStart(), region.getEnd(), biotypes));
					}
				}
			}
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
			this.listType = new TypeToken<FeatureList<Gene>>(){}.getType();
			return generateResponse2(genes, outputFormat, compress);
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
	//			return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, compress);
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

	@GET
	@Path("/{region}/snp")
	public Response getSnpsByRegion(@PathParam("region") String regionString) {
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
			
			
			this.listType = new TypeToken<FeatureList<SNP>>() {}.getType();
			return generateResponse2(snpList, outputFormat, compress);
			//return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{region}/mutation")
	public Response getAnnotatedMutationsByRegion(@PathParam("region") String regionString) {
		try {
			List<Region> regions = Region.parseRegions(regionString);
			AnnotatedMutationDBManager snpDbManager = new AnnotatedMutationDBManager(infraredDBConnector);
			List<FeatureList<AnnotatedMutation>> annotMutations = snpDbManager.getAllByRegions(regions);
			this.listType = new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType();
			return generateResponse2(annotMutations, outputFormat, compress);
			//return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
//	@GET
//	@Path("/position/{position}/consequencetype")
//	public Response getConsequenceType(@PathParam("position") String positionString) {
//		try {
//			List<Position> positions = Position.parsePosition(positionString);
//			VariationPositionDBManager variationPositionDBManager = new VariationPositionDBManager(infraredDBConnector);
//			List<List<TranscriptConsequenceType>> ct = new ArrayList<List<TranscriptConsequenceType>>();
//			ct = variationPositionDBManager.getConsequenceType(positions);
//			return generateResponse(createResultStringByTranscriptConsequenceType(StringUtils.toList(positionString, ","), ct), outputFormat, compress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
	
	@GET
	@Path("/position/{position}/splicesite")
	public Response getSpliceSiteByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			SpliceSiteDBManager splicesiteDbManager = new SpliceSiteDBManager(infraredDBConnector);
			FeatureList<SpliceSite> spliceSite = new FeatureList<SpliceSite>();
			List<FeatureList<SpliceSite>> spliceSites = new ArrayList<FeatureList<SpliceSite>>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					spliceSite = splicesiteDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					spliceSite = null;
				}
				spliceSites.add(spliceSite);
			}
			//return generateResponse(createResultString(StringUtils.toList(positionString, ","), spliceSites), outputFormat, compress);
			this.listType = new TypeToken<List<FeatureList<SpliceSite>>>() {}.getType();
			return generateResponse2(spliceSites, outputFormat, compress);

		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/position/{position}/conservedregion")
	public Response getConservedRegionByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			ConservedRegionDBManager conservedRegionDbManager = new ConservedRegionDBManager(infraredDBConnector);
			FeatureList<ConservedRegion> conservedRegion = new FeatureList<ConservedRegion>();
			List<FeatureList<ConservedRegion>> conservedRegions = new ArrayList<FeatureList<ConservedRegion>>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					conservedRegion = conservedRegionDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					conservedRegion = null;
				}
				conservedRegions.add(conservedRegion);
			}
//			return generateResponse(createResultString(StringUtils.toList(positionString, ","), conservedRegions), outputFormat, compress);
			this.listType = new TypeToken<List<FeatureList<ConservedRegion>>>() {}.getType();
			return generateResponse2(conservedRegions, outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/position/{position}/tfbs")
	public Response getTfbsByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			if(uriInfo.getQueryParameters().get("filter") != null) {
				if(uriInfo.getQueryParameters().get("filter").get(0).equals("jaspar")) {
					JasparTfbsDBManager jasparTfbsDbManager = new JasparTfbsDBManager(infraredDBConnector);
					FeatureList<JasparTfbs> jasparTfbs = new FeatureList<JasparTfbs>();
					List<FeatureList<JasparTfbs>> tfbs = new ArrayList<FeatureList<JasparTfbs>>();
					for(Position position: positions) {
						if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
							jasparTfbs = jasparTfbsDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
						}else {
							jasparTfbs = null;
						}
						tfbs.add(jasparTfbs);
					}
					this.listType = new TypeToken<List<FeatureList<JasparTfbs>>>() {}.getType();
					return generateResponse2(tfbs, outputFormat, compress);
				}else {
					if(uriInfo.getQueryParameters().get("filter").get(0).equals("oreganno")) {
						OregannoTfbsDBManager oregannoTfbsDBManager = new OregannoTfbsDBManager(infraredDBConnector);
						FeatureList<OregannoTfbs> oregannoTfbs = new FeatureList<OregannoTfbs>();
						List<FeatureList<OregannoTfbs>> tfbs = new ArrayList<FeatureList<OregannoTfbs>>();
						for(Position position: positions) {
							if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
								oregannoTfbs = oregannoTfbsDBManager.getAllByPosition(position.getChromosome(), position.getPosition());
							}else {
								oregannoTfbs = null;
							}
							tfbs.add(oregannoTfbs);
						}
//						return generateResponse(createResultString(StringUtils.toList(positionString, ","), tfbs), outputFormat, compress);
						this.listType = new TypeToken<List<FeatureList<OregannoTfbs>>>() {}.getType();
						return generateResponse2(tfbs, outputFormat, compress);

					}else {
						return generateResponse("No valid filter provided, please select filter: jaspar or oreganno, eg:  ?filter=jaspar", outputFormat, compress);
					}
				}
			}else {
				return generateResponse("No filter provided, please add filter: jaspar or oreganno, eg:  ?filter=jaspar", outputFormat, compress);	
			}
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/position/{position}/mirna_gene")
	public Response getMiRnaGeneByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			MiRnaGeneDBManager miRnaGeneDbManager = new MiRnaGeneDBManager(infraredDBConnector);
			FeatureList<MiRnaGene> miRnaGene = new FeatureList<MiRnaGene>();
			List<FeatureList<MiRnaGene>> miRnasGene = new ArrayList<FeatureList<MiRnaGene>>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					miRnaGene = miRnaGeneDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					miRnaGene = null;
				}
				miRnasGene.add(miRnaGene);
			}
			this.listType = new TypeToken<List<FeatureList<MiRnaGene>>>() {}.getType();
			return generateResponse2(miRnasGene, outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/position/{position}/mirna_target")
	public Response getMiRnaTargetByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			MiRnaTargetDBManager miRnaTargetDbManager = new MiRnaTargetDBManager(infraredDBConnector);
			FeatureList<MiRnaTarget> miRnaTarget = new FeatureList<MiRnaTarget>();
			List<FeatureList<MiRnaTarget>> miRnasTarget = new ArrayList<FeatureList<MiRnaTarget>>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					miRnaTarget = miRnaTargetDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					miRnaTarget = null;
				}
				miRnasTarget.add(miRnaTarget);
			}
//			return generateResponse(createResultString(StringUtils.toList(positionString, ","), miRnasTarget), outputFormat, compress);
			this.listType = new TypeToken<List<FeatureList<MiRnaTarget>>>() {}.getType();
			return generateResponse2(miRnasTarget, outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/position/{position}/triplex")
	public Response getTriplexByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			TriplexDBManager triplexDBManager = new TriplexDBManager(infraredDBConnector);
			FeatureList<Triplex> triplex = new FeatureList<Triplex>();
			List<FeatureList<Triplex>> triplexList = new ArrayList<FeatureList<Triplex>>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					triplex = triplexDBManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					triplex = null;
				}
				triplexList.add(triplex);
			}
//			return generateResponse(createResultString(StringUtils.toList(positionString, ","), triplexList), outputFormat, compress);
			this.listType = new TypeToken<List<FeatureList<Triplex>>>() {}.getType();
			return generateResponse2(triplexList, outputFormat, compress);

		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	

	@Override
	protected boolean isValidSpecies(String species) {
		return true;
	}

}
