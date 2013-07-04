package org.bioinfo.cellbase.ws.server.rest.feature;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;

import org.bioinfo.cellbase.lib.api.*;
import org.bioinfo.cellbase.lib.common.Region;
import org.bioinfo.cellbase.lib.common.core.Exon;
import org.bioinfo.cellbase.lib.common.core.Gene;
import org.bioinfo.cellbase.lib.common.core.Transcript;
import org.bioinfo.cellbase.lib.common.variation.MutationPhenotypeAnnotation;
//import org.bioinfo.cellbase.lib.common.variation.Snp;
import org.bioinfo.cellbase.lib.common.variation.Variation;
import org.bioinfo.cellbase.ws.server.rest.GenericRestWSServer;
import org.bioinfo.cellbase.ws.server.rest.exception.VersionException;
import org.bioinfo.commons.utils.StringUtils;

@Path("/{version}/{species}/feature/gene")
@Produces("text/plain")
public class GeneWSServer extends GenericRestWSServer {

    private List<String> exclude = new ArrayList<>();

	public GeneWSServer(@PathParam("version") String version, @PathParam("species") String species,
                        @DefaultValue("") @QueryParam("exclude") String exclude,
                        @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
        this.exclude = Arrays.asList(exclude.trim().split(","));
	}

	@GET
	@Path("/list")
	public Response getList(@DefaultValue("")@QueryParam("biotype") List<String> biotype, @DefaultValue("false")@QueryParam("id") Boolean id) {
		try {
			checkVersionAndSpecies();
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
			return generateResponse("list", "GENE", geneDBAdaptor.getAll(biotype, id));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}

	@GET
	@Path("/{geneId}/info")
	public Response getByEnsemblId(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
			return generateResponse(query, "GENE", geneDBAdaptor.getAllByNameList(StringUtils.toList(query, ","),exclude));
			//	return generateResponse(query, Arrays.asList(this.getGeneDBAdaptor().getAllByEnsemblIdList(StringUtils.toList(query, ","))));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}
	
	@GET
	@Path("/{geneId}/fullinfo")
	public Response getFullInfoByEnsemblId(@PathParam("geneId") String query, @DefaultValue("") @QueryParam("sources") String sources) {
		try {
			checkVersionAndSpecies();
			
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
			return generateResponse(query, "GENE", geneDBAdaptor.getAllByNameList(StringUtils.toList(query, ","), exclude));
			
			
//			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
//			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species, this.version);
//			XRefsDBAdaptor xRefsDBAdaptor = dbAdaptorFactory.getXRefDBAdaptor(this.species, this.version);
//			TfbsDBAdaptor tfbsDBAdaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
//			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species, this.version);
//			ProteinDBAdaptor proteinDBAdaptor = dbAdaptorFactory.getProteinDBAdaptor(this.species, this.version);
//			
//			List<List<Gene>> geneListList = geneDBAdaptor.getAllByNameList(StringUtils.toList(query, ","));
//
////			List<String> ensemblIds = new ArrayList<String>();
////			for(List<Gene> geneList : geneListList) {
////				if(geneList.size() > 0){
////					for(Gene gene : geneList) {
////						ensemblIds.add(gene.getStableId());
////					}
////				}
////				else{
////					ensemblIds.add(null);
////				}
////			}
//			
//			List<List<Transcript>> transcriptList = transcriptDBAdaptor.getByEnsemblGeneIdList(StringUtils.toList(query, ","));
//			List<List<Xref>> goLists = xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"go");
//			List<List<Xref>> interproLists = xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"interpro");
//			List<List<Xref>> reactomeLists = xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"reactome");
//			List<List<Tfbs>> tfbsLists = tfbsDBAdaptor.getAllByTargetGeneNameList(StringUtils.toList(query, ","));
//			List<List<MirnaTarget>> mirnaTargetsList = mirnaDBAdaptor.getAllMiRnaTargetsByGeneNameList(StringUtils.toList(query, ","));
//			List<List<ProteinFeature>> proteinFeaturesList = proteinDBAdaptor.getAllProteinFeaturesByProteinXrefList(StringUtils.toList(query, ","));
//			
//			StringBuilder response = new StringBuilder();
//			response.append("[");
//			for(int i = 0; i < geneListList.size(); i++) {
//				response.append("[");
//				boolean removeComma = false;
//				for(int j = 0; j < geneListList.get(i).size(); j++) {
//					removeComma = true;
//					response.append("{");
//					response.append("\"stableId\":"+"\""+geneListList.get(i).get(j).getId()+"\",");
//					response.append("\"externalName\":"+"\""+geneListList.get(i).get(j).getName()+"\",");
////					response.append("\"externalDb\":"+"\""+geneListList.get(i).get(j).getExternalDb()+"\",");
//					response.append("\"biotype\":"+"\""+geneListList.get(i).get(j).getBiotype()+"\",");
//					response.append("\"status\":"+"\""+geneListList.get(i).get(j).getStatus()+"\",");
//					response.append("\"chromosome\":"+"\""+geneListList.get(i).get(j).getChromosome()+"\",");
//					response.append("\"start\":"+geneListList.get(i).get(j).getStart()+",");
//					response.append("\"end\":"+geneListList.get(i).get(j).getEnd()+",");
//					response.append("\"strand\":"+"\""+geneListList.get(i).get(j).getStrand()+"\",");
//					response.append("\"source\":"+"\""+geneListList.get(i).get(j).getSource()+"\",");
//					response.append("\"description\":"+"\""+geneListList.get(i).get(j).getDescription()+"\",");
//					response.append("\"transcripts\":"+gson.toJson(transcriptList.get(i))+",");
//					response.append("\"go\":"+gson.toJson(goLists.get(i))+",");
//					response.append("\"interpro\":"+gson.toJson(interproLists.get(i))+",");
//					response.append("\"reactome\":"+gson.toJson(reactomeLists.get(i))+",");
//					response.append("\"tfbs\":"+gson.toJson(tfbsLists.get(i))+",");
//					response.append("\"mirnaTargets\":"+gson.toJson(mirnaTargetsList.get(i))+",");
//					response.append("\"proteinFeatures\":"+gson.toJson(proteinFeaturesList.get(i))+"");
//					response.append("},");
//				}
//				if(removeComma){
//					response.replace(response.length()-1, response.length(), "");
//				}
//				response.append("],");
//			}
//			response.replace(response.length()-1, response.length(), "");
//			response.append("]");
//			return  generateResponse(query,Arrays.asList(response));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getFullInfoByEnsemblId", e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/{geneId}/transcript")
	public Response getTranscriptsByXref(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();

            TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species, this.version);
            List<List<List<Transcript>>> transcriptsList = transcriptDBAdaptor.getAllByNameList(StringUtils.toList(query, ","),exclude);

			return generateResponse(query, "TRANSCRIPT", transcriptsList);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getTranscriptsByEnsemblId", e.toString());
		}
	}


	@GET
	@Path("/{geneId}/snp")
	public Response getSNPByGene(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();

			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
            List<String> geneExclude = Arrays.asList("_id", "id", "name", "biotype", "status", "strand", "source", "description", "transcripts", "mirna");
			List<List<Gene>> geneList = geneDBAdaptor.getAllByNameList(StringUtils.toList(query, ","), geneExclude);

			VariationDBAdaptor variationDBAdaptor = dbAdaptorFactory.getVariationDBAdaptor(this.species, this.version);
			List<List<List<Variation>>> result = new ArrayList<List<List<Variation>>>();

//            if (consequenceTypes.equals("")) {
//
//            }
//			List<List<Variant>> result = variationDBAdaptor.getByX(StringUtils.toList(query, ","));
//			SnpDBAdaptor snpAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species, this.version);

			for(List<Gene> genes: geneList) {
				List<List<Variation>> snps = new ArrayList<List<Variation>>();
				for(Gene gene: genes) {
                    snps.add(variationDBAdaptor.getByRegion(gene.getChromosome(), gene.getStart(), gene.getEnd(), null, exclude));
				}
                result.add(snps);
			}
			
			return generateResponse(query, "SNP", result);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getSNPByGene", e.toString());
		}
	}
	
	@GET
	@Path("/{geneId}/mutation")
	public Response getMutationByGene(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			MutationDBAdaptor mutationAdaptor = dbAdaptorFactory.getMutationDBAdaptor(this.species, this.version);
			List<List<MutationPhenotypeAnnotation>> geneList = mutationAdaptor.getAllMutationPhenotypeAnnotationByGeneNameList(StringUtils.toList(query, ","));
			return generateResponse(query, "MUTATION", geneList);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getMutationByGene", e.toString());
		}
	}
	
	
	@GET
	@Path("/{geneId}/tfbs")
	public Response getAllTfbs(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			TfbsDBAdaptor tfbsDBAdaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species, this.version);
			return  generateResponse(query, "TFBS", tfbsDBAdaptor.getAllByTargetGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getAllTfbs", e.toString());
		}
	}

	@GET
	@Path("/{geneId}/mirna_target")
	public Response getAllMirna(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species, this.version);
			return  generateResponse(query, "MIRNA_TARGET", mirnaDBAdaptor.getAllMiRnaTargetsByGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getAllMirna", e.toString());
		}
	}


	@GET
	@Path("/{geneId}/protein_feature")
	public Response getProteinFeature(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			ProteinDBAdaptor proteinDBAdaptor = dbAdaptorFactory.getProteinDBAdaptor(this.species, this.version);
			return  generateResponse(query, "PROTEIN_FEATURE", proteinDBAdaptor.getAllProteinFeaturesByGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getProteinFeature", e.toString());
		}
	}
	
	
	@GET
	@Path("/{geneId}/exon")
	public Response getExonByGene(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			ExonDBAdaptor exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species, this.version);
			List<List<List<Exon>>> exonList = exonDBAdaptor.getAllByNameList(StringUtils.toList(query, ","), exclude);
			return  generateResponse(query, "EXON", exonList);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getExonByGene", e.toString());
		}
	}
	
	@GET
	@Path("/{geneId}/reactome")
	public Response getReactomeByEnsemblId(@PathParam("geneId") String query) {
		try {
			checkVersionAndSpecies();
			XRefsDBAdaptor xRefsDBAdaptor = dbAdaptorFactory.getXRefDBAdaptor(this.species, this.version);
			return generateResponse(query, xRefsDBAdaptor.getAllByDBName(StringUtils.toList(query, ","),"reactome"));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
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
		sb.append("all id formats are accepted.\n\n\n");
		sb.append("Resources:\n");
		sb.append("- info: Get gene information: name, position, biotype.\n");
		sb.append(" Output columns: Ensembl gene, external name, external name source, biotype, status, chromosome, start, end, strand, source, description.\n\n");
		sb.append("- transcript: Get all transcripts for this gene.\n");
		sb.append(" Output columns: Ensembl ID, external name, external name source, biotype, status, chromosome, start, end, strand, coding region start, coding region end, cdna coding start, cdna coding end, description.\n\n");
		sb.append("- tfbs: Get transcription factor binding sites (TFBSs) that map to the promoter region of this gene.\n");
		sb.append(" Output columns: TF name, target gene name, chromosome, start, end, cell type, sequence, score.\n\n");
		sb.append("- mirna_target: Get all microRNA target sites for this gene.\n");
		sb.append(" Output columns: miRBase ID, gene target name, chromosome, start, end, strand, pubmed ID, source.\n\n");
		sb.append("- protein_feature: Get protein information related to this gene.\n");
		sb.append(" Output columns: feature type, aa start, aa end, original, variation, identifier, description.\n\n\n");
		sb.append("Documentation:\n");
		sb.append("http://docs.bioinfo.cipf.es/projects/cellbase/wiki/Feature_rest_ws_api#Gene");
		
		return createOkResponse(sb.toString());
	}

}
