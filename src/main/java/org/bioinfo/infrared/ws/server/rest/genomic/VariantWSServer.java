package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.cellbase.Transcript;
import org.bioinfo.infrared.lib.api.GenomicVariantEffectDBAdaptor;
import org.bioinfo.infrared.lib.api.MutationDBAdaptor;
import org.bioinfo.infrared.lib.api.SnpDBAdaptor;
import org.bioinfo.infrared.lib.common.GenomicVariant;
import org.bioinfo.infrared.lib.common.GenomicVariantConsequenceType;
import org.bioinfo.infrared.lib.common.Position;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.multipart.FormDataParam;

@Path("/{version}/{species}/genomic/variant")
@Produces("text/plain")
public class VariantWSServer extends GenericRestWSServer {

	protected static HashMap<String, List<Transcript>> CACHE_TRANSCRIPT = new HashMap<String, List<Transcript>>();

	public VariantWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
	}

	@GET
	@Path("/{variants}/consequence_type")
	public Response getConsequenceTypeByPositionByGet(@PathParam("variants") String variants, 
			@DefaultValue("") @QueryParam("exclude") String excludeSOTerms) {
		try {
			//			return getConsequenceTypeByPosition(query, features, variation, regulatory, diseases);
			return getConsequenceTypeByPosition(variants, excludeSOTerms);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getConsequenceTypeByPositionByGet", e.toString());
		}
	}

	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED})//MediaType.MULTIPART_FORM_DATA, 
	@Path("/consequence_type")
	public Response getConsequenceTypeByPositionByPost( @FormDataParam("of") String outputFormat, 
			@FormDataParam("variants") String postQuery, 
			@DefaultValue("") @FormDataParam("exclude") String excludeSOTerms) {
		//		return getConsequenceTypeByPosition(postQuery, features, variation, regulatory, diseases);
		return getConsequenceTypeByPosition(postQuery, excludeSOTerms);
	}

	private Response getConsequenceTypeByPosition(String variants, String excludes) {
		List<GenomicVariant> genomicVariantList = null;
		String[] excludeArray = null;
		Set<String> excludeSet = null;
		List<GenomicVariantConsequenceType> genomicVariantConsequenceTypes = null;
		GenomicVariantEffectDBAdaptor gv = null;
		try {
			checkVersionAndSpecies();
//			System.out.println("PAKO: "+ variants);
			genomicVariantList = GenomicVariant.parseVariants(variants);
			if(genomicVariantList != null && excludes != null) {
				logger.debug("VariantWSServer: number of variants: "+ genomicVariantList.size());
				//			GenomicVariantEffect gv = new GenomicVariantEffect(this.species);
				gv = dbAdaptorFactory.getGenomicVariantEffectDBAdaptor(species, this.version);
				excludeArray = excludes.split(",");
				excludeSet = new HashSet<String>(Arrays.asList(excludeArray));
				//				return generateResponse(variants, gv.getAllConsequenceTypeByVariantList(genomicVariantList));
				genomicVariantConsequenceTypes = gv.getAllConsequenceTypeByVariantList(genomicVariantList, excludeSet);
//				System.out.println("VariantWSServer: genomicVariantConsequenceTypes => "+genomicVariantConsequenceTypes);
				return generateResponse(variants, "CONSEQUENCE_TYPE", genomicVariantConsequenceTypes);
//				return generateResponse(variants, gv.getAllConsequenceTypeByVariantList(genomicVariantList, excludeSet));
			} else {
				logger.error("ERRRORRRRRR EN VARIATNWSSERVER");
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("VariantWSServer: response.status => "+Response.status(Status.INTERNAL_SERVER_ERROR));
//			System.out.println("ERROR: getConsequenceTypeByPosition: VARIANTS: "+variants);
			System.out.println("ERROR: getConsequenceTypeByPosition: "+StringUtils.getStackTrace(e));
			if(genomicVariantList != null && excludes != null) {
				try {
					gv = dbAdaptorFactory.getGenomicVariantEffectDBAdaptor(species, this.version);
					excludeArray = excludes.split(",");
					excludeSet = new HashSet<String>(Arrays.asList(excludeArray));
					genomicVariantConsequenceTypes = gv.getAllConsequenceTypeByVariantList(genomicVariantList, excludeSet);
					logger.warn("VariantWSServer: in catch of genomicVariantConsequenceTypes => "+genomicVariantConsequenceTypes);
					return generateResponse(variants, "CONSEQUENCE_TYPE", genomicVariantConsequenceTypes);
				} catch (IOException e1) {
					e1.printStackTrace();
					return createErrorResponse("getConsequenceTypeByPositionByGet", e.toString());
				}
			}
			return createErrorResponse("getConsequenceTypeByPositionByGet", e.toString());
		}
	}

	
	
	@GET
	@Path("/{variants}/snp_phenotype")
	public Response getSnpPhenotypesByPositionByGet(@PathParam("variants") String variants) {
		return getSnpPhenotypesByPosition(variants, outputFormat);
	}

	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED})//MediaType.MULTIPART_FORM_DATA, 
	@Path("/snp_phenotype")
	public Response getSnpPhenotypesByPositionByPost(@FormDataParam("of") String outputFormat, @FormDataParam("variants") String variants) {
		return getSnpPhenotypesByPosition(variants, outputFormat);
	}
	
	public Response getSnpPhenotypesByPosition(String variants, String outputFormat) {
		try {
			checkVersionAndSpecies();
			SnpDBAdaptor snpDBAdaptor = dbAdaptorFactory.getSnpDBAdaptor(this.species, this.version);
			List<GenomicVariant> variantList=  GenomicVariant.parseVariants(variants);
			List<Position> positionList = new ArrayList<Position>(variantList.size());
			for(GenomicVariant gv: variantList) {
				positionList.add(new Position(gv.getChromosome(), gv.getPosition()));
			}
			return generateResponse(variants, "SNP_PHENOTYPE", snpDBAdaptor.getAllSnpPhenotypeAnnotationListByPositionList(positionList));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getSnpPhenotypesByPositionByGet", e.toString());
		}
	}
	
	
	
	
	@GET
	@Path("/{variants}/mutation_phenotype")
	public Response getMutationPhenotypesByPositionByGet(@PathParam("variants") String variants) {
		return getMutationPhenotypesByPosition(variants, outputFormat);
	}

	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED})//MediaType.MULTIPART_FORM_DATA, 
	@Path("/mutation_phenotype")
	public Response getMutationPhenotypesByPositionByPost( @FormDataParam("of") String outputFormat, @FormDataParam("variants") String variants) {
		return getMutationPhenotypesByPosition(variants, outputFormat);
	}
	
	public Response getMutationPhenotypesByPosition(String variants, String outputFormat) {
		try {
			checkVersionAndSpecies();
			MutationDBAdaptor mutationDBAdaptor = dbAdaptorFactory.getMutationDBAdaptor(this.species, this.version);
			List<GenomicVariant> variantList =  GenomicVariant.parseVariants(variants);
			List<Position> positionList = new ArrayList<Position>(variantList.size());
			for(GenomicVariant gv: variantList) {
				positionList.add(new Position(gv.getChromosome(), gv.getPosition()));
			}
			return generateResponse(variants, "variants", mutationDBAdaptor.getAllMutationPhenotypeAnnotationByPositionList(positionList));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getMutationPhenotypesByPositionByGet", e.toString());
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
		sb.append("Variant format: chr:position:new allele (i.e.: 1:150044250:G)\n\n\n");
		sb.append("Resources:\n");
		sb.append("- consequence_type: Suppose that we have obtained some variants from a resequencing analysis and we want to obtain the consequence type of a variant over the transcripts\n");
		sb.append(" Output columns: chromosome, start, end, feature ID, feature name, consequence type, biotype, feature chromosome, feature start, feature end, feature strand, snp ID, ancestral allele, alternative allele, gene Ensembl ID, Ensembl transcript ID, gene name, SO consequence type ID, SO consequence type name, consequence type description, consequence type category, aminoacid change, codon change.\n\n\n");
		sb.append("Documentation:\n");
		sb.append("http://docs.bioinfo.cipf.es/projects/cellbase/wiki/Genomic_rest_ws_api#Variant");
		
		return createOkResponse(sb.toString());
	}

}
