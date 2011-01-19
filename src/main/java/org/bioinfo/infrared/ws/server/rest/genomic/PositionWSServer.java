package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.ArrayList;
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
import org.bioinfo.infrared.core.GeneDBManager;
import org.bioinfo.infrared.core.KaryotypeDBManager;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Cytoband;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.core.feature.Position;
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
import org.bioinfo.infrared.variation.TranscriptConsequenceTypeDBManager;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;


@Path("/{version}/{species}/genomic/position")
@Produces("text/plain")
public class PositionWSServer extends GenomicWSServer {

	public PositionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
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
		return "position help";
	}

	@Override
	@GET
	@Path("/stats")
	public String stats() {
		return "position stats";
	}

	@GET
	@Path("/{position}/snp")
	public Response getSnpByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			List<FeatureList<SNP>> snps = snpDbManager.getAllByPositions(positions);
			return generateResponseFromListFeatureList(snps, new TypeToken<List<FeatureList<SNP>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}
	
	@GET
	@Path("/{position}/cytoband")
	public Response getCytobandByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			List<FeatureList<Cytoband>> CytobandList = karyotypeDbManager.getCytobandByPosition(positions);
			return generateResponseFromListFeatureList(CytobandList, new TypeToken<List<FeatureList<Cytoband>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}
	
	
	@GET
	@Path("/{position}/annotated_mutation")
	public Response getAnnotatedMutationByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			AnnotatedMutationDBManager annotMutationDbManager = new AnnotatedMutationDBManager(infraredDBConnector);
			List<FeatureList<AnnotatedMutation>> annotatedMutations = annotMutationDbManager.getAllByPositions(positions);
			return generateResponseFromListFeatureList(annotatedMutations, new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}

	@GET
	@Path("/{position}/consequence_type")
	public Response getConsequenceType(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			TranscriptConsequenceTypeDBManager transcriptConsequenceTypeDBManager = new TranscriptConsequenceTypeDBManager(infraredDBConnector);
			List<List<TranscriptConsequenceType>> ct = transcriptConsequenceTypeDBManager.getConsequenceTypes(positions);
			return generateResponseFromListList(ct, new TypeToken<List<List<TranscriptConsequenceType>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{position}/gene")
	public Response getGeneByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			GeneDBManager geneDBManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDBManager.getAllByPositions(positions);
			return generateResponseFromListFeatureList(genes, new TypeToken<List<FeatureList<Gene>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}

	@GET
	@Path("/{position}/tfbs")
	public Response getTfbsByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);

			List<String> sources = null;
			if(uriInfo.getQueryParameters().get("source") == null) {
				sources = Arrays.asList("jaspar");
			}else {
				sources = StringUtils.toList(uriInfo.getQueryParameters().get("source").get(0), ",");
			}

			if(sources != null) {
				if(sources.contains("jaspar")) {
					JasparTfbsDBManager jasparTfbsDbManager = new JasparTfbsDBManager(infraredDBConnector);
					FeatureList<JasparTfbs> jasparTfbs = new FeatureList<JasparTfbs>();
					List<FeatureList<JasparTfbs>> tfbs = new ArrayList<FeatureList<JasparTfbs>>(positions.size());
					for(Position position: positions) {
						if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
							jasparTfbs = jasparTfbsDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
						}else {
							jasparTfbs = null;
						}
						tfbs.add(jasparTfbs);
					}
					return generateResponseFromListFeatureList(tfbs, new TypeToken<List<FeatureList<JasparTfbs>>>() {}.getType());
				}
				if(sources.contains("oreganno")) {
					OregannoTfbsDBManager oregannoTfbsDBManager = new OregannoTfbsDBManager(infraredDBConnector);
					FeatureList<OregannoTfbs> oregannoTfbs = new FeatureList<OregannoTfbs>();
					List<FeatureList<OregannoTfbs>> tfbs = new ArrayList<FeatureList<OregannoTfbs>>(positions.size());
					for(Position position: positions) {
						if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
							oregannoTfbs = oregannoTfbsDBManager.getAllByPosition(position.getChromosome(), position.getPosition());
						}else {
							oregannoTfbs = null;
						}
						tfbs.add(oregannoTfbs);
					}
					return generateResponseFromListFeatureList(tfbs, new TypeToken<List<FeatureList<OregannoTfbs>>>() {}.getType());
				}else {
					return generateResponse("No valid filter provided, please select filter: jaspar or oreganno, eg:  ?filter=jaspar", outputFormat, outputCompress);
				}
			}else {
				return generateResponse("No filter provided, please add filter: jaspar or oreganno, eg:  ?filter=jaspar", outputFormat, outputCompress);	
			}
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{position}/mirna_target")
	public Response getMiRnaTargetByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			MiRnaTargetDBManager miRnaTargetDbManager = new MiRnaTargetDBManager(infraredDBConnector);
			List<FeatureList<MiRnaTarget>> miRnasTargetList = new ArrayList<FeatureList<MiRnaTarget>>();
			FeatureList<MiRnaTarget> miRnaTargetFeatureList = new FeatureList<MiRnaTarget>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					miRnaTargetFeatureList = miRnaTargetDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					miRnaTargetFeatureList = null;
				}
				miRnasTargetList.add(miRnaTargetFeatureList);
			}
			return generateResponseFromListFeatureList(miRnasTargetList, new TypeToken<List<FeatureList<MiRnaTarget>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{position}/triplex")
	public Response getTriplexByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			TriplexDBManager triplexDBManager = new TriplexDBManager(infraredDBConnector);
			List<FeatureList<Triplex>> triplexList = new ArrayList<FeatureList<Triplex>>(positions.size());
			FeatureList<Triplex> triplexFeatureList = new FeatureList<Triplex>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					triplexFeatureList = triplexDBManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					triplexFeatureList = null;
				}
				triplexList.add(triplexFeatureList);
			}
			return generateResponseFromListFeatureList(triplexList, new TypeToken<List<FeatureList<Triplex>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{position}/conserved_region")
	public Response getConservedRegionByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			ConservedRegionDBManager conservedRegionDbManager = new ConservedRegionDBManager(infraredDBConnector);
			List<FeatureList<ConservedRegion>> conservedRegions = new ArrayList<FeatureList<ConservedRegion>>(positions.size());
			FeatureList<ConservedRegion> conservedRegion = new FeatureList<ConservedRegion>();
			for(Position position: positions) {
				if(position != null && position.getChromosome() != null && !position.getChromosome().equals("") && position.getPosition() != 0) {
					conservedRegion = conservedRegionDbManager.getAllByPosition(position.getChromosome(), position.getPosition());
				}else {
					conservedRegion = null;
				}
				conservedRegions.add(conservedRegion);
			}
			return generateResponseFromListFeatureList(conservedRegions, new TypeToken<List<FeatureList<ConservedRegion>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	
	
	
	@Deprecated
	@GET
	@Path("/{position}/mutation")
	public Response getMutationByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			AnnotatedMutationDBManager annotMutationDbManager = new AnnotatedMutationDBManager(infraredDBConnector);
			List<FeatureList<AnnotatedMutation>> mutations = annotMutationDbManager.getAllByPositions(positions);
			return generateResponseFromListFeatureList(mutations, new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}

	@Deprecated
	@GET
	@Path("/{position}/mirna_gene")
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
			return generateResponseFromListFeatureList(miRnasGene, new TypeToken<List<FeatureList<MiRnaGene>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@Deprecated
	@GET
	@Path("/{position}/splicesite")
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
			return generateResponseFromListFeatureList(spliceSites, new TypeToken<List<FeatureList<SpliceSite>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
}