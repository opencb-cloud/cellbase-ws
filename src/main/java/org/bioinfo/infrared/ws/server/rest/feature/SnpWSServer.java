package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.cellbase.Snp;
import org.bioinfo.infrared.core.cellbase.SnpPhenotypeAnnotation;
import org.bioinfo.infrared.core.cellbase.SnpPopulationFrequency;
import org.bioinfo.infrared.core.cellbase.SnpToTranscript;
import org.bioinfo.infrared.lib.api.SnpDBAdaptor;
import org.bioinfo.infrared.lib.api.TfbsDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/snp")
@Produces("text/plain")
public class SnpWSServer extends GenericRestWSServer {
	
	public SnpWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{snpId}/info")
	public Response getByEnsemblId(@PathParam("snpId") String query) {
		try {
			SnpDBAdaptor adapter = dbAdaptorFactory.getSnpDBAdaptor(this.species);
			return  generateResponse(query, adapter.getAllBySnpIdList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}

	//	private int snpId;
	//	private String name;
	//	private String chromosome;
	//	private int start;
	//	private int end;
	//	private String strand;
	//	private int mapWeight;
	//	private String alleleString;
	//	private String ancestralAllele;
	//	private String source;
	//	private String displaySoConsequence;
	//	private String soConsequenceType;
	//	private String displayConsequence;
	//	private String sequence;
	@GET
	@Path("/{snpId}/fullinfo")
	public Response getFullInfoById(@PathParam("snpId") String query) {
		try {
			SnpDBAdaptor snpDBAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species);
			
			
			List<List<Snp>> snpLists = snpDBAdaptor.getAllBySnpIdList(StringUtils.toList(query, ","));
			List<List<SnpToTranscript>> snpToTranscript = snpDBAdaptor.getAllSnpToTranscriptList(StringUtils.toList(query, ","));
			List<List<SnpPopulationFrequency>> snpPopulation = snpDBAdaptor.getAllSnpPopulationFrequencyList(StringUtils.toList(query, ","));
			List<List<SnpPhenotypeAnnotation>> snpPhenotype = snpDBAdaptor.getAllSnpPhenotypeAnnotationList(StringUtils.toList(query, ","));
			
//			List<List<Transcript>> transcripts = new ArrayList<List<Transcript>>(StringUtils.toList(query, ",").size());
//			for (int i = 0; i < snpToTranscript.size(); i++) {
//				List<Transcript> transcript = new ArrayList<Transcript>();
//				for (int j = 0; j < snpToTranscript.get(i).size(); j++) {
//					transcript.add(snpToTranscript.get(i).get(j).getTranscript());
//				}
//				transcripts.add(transcript);
//			}
			
			StringBuilder response = new StringBuilder();
			response.append("[");
			for (int i = 0; i < snpLists.size(); i++) {
				response.append("[");
				for (int j = 0; j < snpLists.get(i).size(); j++) {
					response.append("{");
					response.append("\"name\":"+"\""+snpLists.get(i).get(j).getName()+"\",");
					response.append("\"chromosome\":"+"\""+snpLists.get(i).get(j).getChromosome()+"\",");
					response.append("\"start\":"+snpLists.get(i).get(j).getStart()+",");
					response.append("\"end\":"+snpLists.get(i).get(j).getEnd()+",");
					response.append("\"strand\":"+"\""+snpLists.get(i).get(j).getStrand()+"\",");
					response.append("\"mapWeight\":"+snpLists.get(i).get(j).getEnd()+",");
					response.append("\"alleleString\":"+"\""+snpLists.get(i).get(j).getAlleleString()+"\",");
					response.append("\"ancestralAllele\":"+"\""+snpLists.get(i).get(j).getAncestralAllele()+"\",");
					response.append("\"source\":"+"\""+snpLists.get(i).get(j).getSource()+"\",");
					response.append("\"displaySoConsequence\":"+"\""+snpLists.get(i).get(j).getDisplaySoConsequence()+"\",");
					response.append("\"soConsequenceType\":"+"\""+snpLists.get(i).get(j).getSoConsequenceType()+"\",");
					response.append("\"displayConsequence\":"+"\""+snpLists.get(i).get(j).getDisplayConsequence()+"\",");
					response.append("\"sequence\":"+"\""+snpLists.get(i).get(j).getSequence()+"\",");
					response.append("\"population\":"+gson.toJson(snpPopulation.get(i))+",");
					
					String aux = gson.toJson(snpToTranscript.get(i));
//					System.out.println(aux);
					for (int k = 0; k < snpToTranscript.get(i).size(); k++) {
						aux = aux.replace("\"snpToTranscriptId\":"+snpToTranscript.get(i).get(k).getSnpToTranscriptId(), "\"transcript\":"+gson.toJson(snpToTranscript.get(i).get(k).getTranscript())+", \"consequenceType\":"+gson.toJson(snpToTranscript.get(i).get(k).getConsequenceType()));
					}
					response.append("\"snptotranscript\":"+aux+",");
//					System.out.println(aux);
					
					response.append("\"phenotype\":"+gson.toJson(snpPhenotype.get(i))+"");
					response.append("},");
				}
				response.append("],");
				response.replace(response.length()-3, response.length()-2, "");
			}
			response.append("]");

			//Remove the last comma
			response.replace(response.length()-2, response.length()-1, "");
			return  generateResponse(query,Arrays.asList(response));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getFullInfoById", e.toString());
		}
	}	
	
	@GET
	@Path("/{snpId}/consequence_type")
	public Response getConsequenceTypeByGetMethod(@PathParam("snpId") String snpId) {
		return getConsequenceType(snpId);
	}
	
	@POST
	@Path("/consequence_type")
	public Response getConsequenceTypeByPostMethod(@QueryParam("id") String snpId) {
		return getConsequenceType(snpId);
	}

	private Response getConsequenceType(String snpId) {
		try {
			SnpDBAdaptor snpDBAdaptor = dbAdaptorFactory.getSnpDBAdaptor(species, version);
			return generateResponse(snpId, snpDBAdaptor.getAllConsequenceTypesBySnpIdList(StringUtils.toList(snpId, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getConsequenceTypeByPostMethod", e.toString());
		}
	}
	
	@GET
	@Path("/{snpId}/population_frequency")
	public Response getPopulationFrequency(@PathParam("snpId") String snpId) {
		try {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getPopulationFrequency", e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/snpId}/phenotype")
	public Response getPhenotype(@PathParam("geneId") String query) {
		try {
			TranscriptDBAdaptor transcriptDBAdaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
			return  generateResponse(query, Arrays.asList(transcriptDBAdaptor.getByEnsemblGeneIdList(StringUtils.toList(query, ","))));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getPhenotype", e.toString());
		}
	}
	
	@GET
	@Path("/snpId}/xref")
	public Response getXrefs(@PathParam("geneId") String query) {
		try {
			TfbsDBAdaptor tfbsDBAdaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return  generateResponse(query, tfbsDBAdaptor.getAllByTargetGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getXrefs", e.toString());
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
		sb.append("Resources/actions\n\n");
		sb.append("- info: Get SNP information: name, position, consequence type, adjacent nucleotides, ...\n");
		sb.append(" Output columns: rsID, chromosome, position, Ensembl consequence type, SO consequence type, sequence.\n\n");
		sb.append("- consequence_type: Get SNP effect on the transcript\n");
		sb.append(" Output columns: chromosome, start, end, feature ID, feature name, consequence type, biotype, feature chromosome, feature start, feature end, feature strand, snp ID, ancestral allele, alternative allele, gene Ensembl ID, Ensembl transcript ID, gene name, SO consequence type ID, SO consequence type name, consequence type description, consequence type category, aminoacid change, codon change.\n\n");
		sb.append("- population_frequency: Get the allelic and genotypic frequencies for this SNP acroos populations.\n\n");
		sb.append("- phenotype: Get the phenotypes that have been previously associated to this SNP.\n\n");
		sb.append("- xref: Get the external references for this SNP.\n");
		
		return createOkResponse(sb.toString());
	}

}
