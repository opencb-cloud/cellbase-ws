package org.bioinfo.infrared.ws.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.common.feature.FeatureList;
import org.bioinfo.infrared.core.Gene;
import org.bioinfo.infrared.core.dbsql.GeneDBManager;
import org.bioinfo.infrared.variation.SNP;
import org.bioinfo.infrared.variation.dbsql.SNPDBManager;


@Path("/{version}/{species}/genomic/{region}")
@Produces("text/plain")
public class Genomic extends AbstractInfraredRest{


	@GET
	@Path("/gene")
	public Response getGenesByRegion(@PathParam("version") String version, @PathParam("species") String species, @PathParam("region") String regionString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			List<Region> regions = Region.parseRegion(regionString);
			connect();
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			FeatureList<Gene> genes = new FeatureList<Gene>();
			FeatureList<Gene> genesByBiotype = new FeatureList<Gene>();
			for(Region region: regions) {
				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
					if(region.getStart() == 0 && region.getEnd() == 0) {
						genes.addAll(geneDbManager.getAllByLocation(region.getChromosome(), 1, Integer.MAX_VALUE));
					}else {
						genes.addAll(geneDbManager.getAllByLocation(region.getChromosome(), region.getStart(), region.getEnd()));
					}
				}
			}
			// if there is a biotype filter lets filter!
			if(ui.getQueryParameters().get("biotype") != null) {
				List<String> biotypes = StringUtils.toList(ui.getQueryParameters().get("biotype").get(0), ",");
				for(Gene gene: genes) {
					if(biotypes.contains(gene.getBiotype())) {
						genesByBiotype.add(gene);
					}
				}
				genes = genesByBiotype;
			}
			return generateResponse(ListUtils.toString(genes, separator), outputFormat, compress);
		} catch (Exception e) {
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
	//			return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

	@GET
	@Path("/snp")
	public Response getSnpsByRegion(@PathParam("version") String version, @PathParam("species") String species, @PathParam("region") String regionString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			List<Region> regions = Region.parseRegion(regionString);
			connect();
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snps = new FeatureList<SNP>();
			FeatureList<SNP> snpsByConsequenceType = new FeatureList<SNP>();
			for(Region region: regions) {
				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
					if(region.getStart() == 0 && region.getEnd() == 0) {
						snps.addAll(snpDbManager.getAllByLocation(region.getChromosome(), 1, Integer.MAX_VALUE));
					}else {
						snps.addAll(snpDbManager.getAllByLocation(region.getChromosome(), region.getStart(), region.getEnd()));
					}
				}
			}
			// if there is a consequence type filter lets filter!
			if(ui.getQueryParameters().get("consequence_type") != null) {
				List<String> consequencetype = StringUtils.toList(ui.getQueryParameters().get("consequence_type").get(0), ",");
				for(SNP snp: snps) {
					for(String consquenceType: snp.getConsequence_type()) {
						if(consequencetype.contains(consquenceType)) {
							snpsByConsequenceType.add(snp);
						}
					}
				}
				snps = snpsByConsequenceType;
			}
			return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@Override
	protected boolean isValidSpecies(String species) {
		return true;
	}

	static class Region {
		private String chromosome;
		private int start;
		private int end;

		//		public Region() {
		////			parseRegion(region);
		//		}

		public Region(String chromosome, int start, int end) {
			this.chromosome = chromosome;
			this.start = start;
			this.end = end;
		}

		public static List<Region> parseRegion(String regionsString) {
			List<Region> regions = new ArrayList<Region>();
			String[] regionsItems = regionsString.split(",");
			for(String regionString: regionsItems) {
				if(regionString.indexOf(':') != -1) {
					String[] fields = regionString.split("[:-]", -1);
					if(fields.length == 3) {
						regions.add(new Region(fields[0], Integer.parseInt(fields[1]), Integer.parseInt(fields[2])));
						//						chromosome = fields[0];
						//						start = Integer.parseInt(fields[1]);
						//						end = Integer.parseInt(fields[2]);
					}
				}else {
					regions.add(new Region(regionString, 0, 0));
					//					chromosome = regionsString;
					//					start = 0;
					//					end = 0;
				}
			}
			return regions;
		}

		@Override
		public String toString() {
			return chromosome+":"+start+"-"+end; 
		}

		public String getChromosome() {
			return chromosome;
		}

		public void setChromosome(String chromosome) {
			this.chromosome = chromosome;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}
	}

}
