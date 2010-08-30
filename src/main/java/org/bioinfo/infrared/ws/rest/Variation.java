package org.bioinfo.infrared.ws.rest;

import java.io.IOException;
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
import org.bioinfo.infrared.variation.Omega;
import org.bioinfo.infrared.variation.SNP;
import org.bioinfo.infrared.variation.SnpEffect;
import org.bioinfo.infrared.variation.dbsql.OmegaDBManager;
import org.bioinfo.infrared.variation.dbsql.SNPDBManager;
import org.bioinfo.infrared.variation.dbsql.SnpEffectDBManager;


@Path("/{version}/{species}/variation/{variationId}")
@Produces("text/plain")
public class Variation extends AbstractInfraredRest {

//	public Variation(String species, UriInfo uriInfo) {
//		super(species, uriInfo);
//	}

	public Response getTest(String longText) {
		try {
			System.err.println("Length: "+longText.length()+", text: "+longText);
			return generateResponse("Length: "+longText.length()+", text: "+longText, outputFormat, compress);
		} catch (IOException e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	
	@GET
	@Path("/info")
	public Response getSNPListByIds(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> snps = StringUtils.toList(snpIds, ",");
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snplist = snpDbManager.getSNPListByIds(snps);
			return generateResponse(createResultString(snps, snplist), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/consequencetype")
	public Response getxxx(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> snps = StringUtils.toList(snpIds, ",");
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snplist = snpDbManager.getSNPListByIds(snps);
			return generateResponse(createResultString(snps, snplist), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/omegas")
	public Response getAllBySnpIds(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> snps = StringUtils.toList(snpIds, ",");
			OmegaDBManager omegaDbManager = new OmegaDBManager(infraredDBConnector);

			if(ui.getQueryParameters().get("min") != null && ui.getQueryParameters().get("max") != null) {
				
			}else{
				List<FeatureList<Omega>> omegas = omegaDbManager.getAllBySnpIds(snps);
			}
			return generateResponse(createResultString(snps,omegas)), outputFormat, compress);
			
				List<String> biotypes = StringUtils.toList(ui.getQueryParameters().get("biotype").get(0), ",");
				for(Gene gene: genes) {
					if(biotypes.contains(gene.getBiotype())) {
						genesByBiotype.add(gene);
					}
				}
				genes = genesByBiotype;
			}
			
		try {
			init(version, species, ui);
			connect();
			
			List<String> snps = StringUtils.toList(snpIds, ",");
			OmegaDBManager omegaDbManager = new OmegaDBManager(infraredDBConnector);
			FeatureList<SNP> snplist = omegaDbManager.getSNPListByIds(snps);
			return generateResponse(createResultString(snps, snplist), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	
	public Response getSnpInfo() {
		try {
			return generateResponse("funciona!!!", outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	//	try {
	//		connect();
	//		snpDbManager = new SNPDBManager(infraredDBConnector);
	//			snpInfo = snpDbManager.getByName(uriInfo.getPathParameters().get("snp").get(0));
	//		return generateResponse(snpInfo, outputFormat, compress);
	//	} catch (Exception e) {
	//		return generateErrorMessage(e.toString());
	//	}
	//}

	public Response getSnpsByConsequenceType() {
		try {
			connect();
			omegaDbManager = new SNPDBManager(infraredDBConnector);

			if(uriInfo.getPathParameters().get("consequencetype") != null) {
				FeatureList<SNP> snps = omegaDbManager.getAllByConsequenceType(uriInfo.getQueryParameters().get("consequencetype").get(0));
				return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
			}else {
				return generateResponse("Please, type a valid consequence type.", outputFormat, compress);
			}
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	public Response getSnpsByRegion() {
		try {
			connect();
			omegaDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snps;
			String region = uriInfo.getPathParameters().get("region").get(0);
			String[] items = region.split("[:-]");
			if(items.length == 3) {
				snps = omegaDbManager.getAllByLocation(items[0], Integer.parseInt(items[1]), Integer.parseInt(items[2]));
			}else {
				snps = omegaDbManager.getAllByConsequenceType("NON_SYNONYMOUS_CODING");
			}
			return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	//	public Response getSnpsByConsequenceType2() {
	//		try {
	//			connect();
	//			snpDbManager = new SNPDBManager(infraredDBConnector);
	//			
	//			if(uriInfo.getQueryParameters().get("consequencetype") != null) {
	//				FeatureList<SNP> snps = snpDbManager.getAllByConsequenceType(uriInfo.getQueryParameters().get("consequencetype").get(0));
	//				return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
	//			}else {
	//				return generateResponse(ListUtils.toString(snpDbManager.getAllConsequenceTypes(), separator), outputFormat, compress);
	//			}
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

	//	public Response getSpliceSites() {
	//		try {
	//			connect();
	//			spliceSiteDbManager = new SpliceSiteDBManager(infraredDBConnector);
	//			if(uriInfo.getQueryParameters().get("consequencetype") != null) {
	//				FeatureList<SNP> snps = snpDbManager.getAllByConsequenceType(uriInfo.getQueryParameters().get("consequencetype").get(0));
	//				return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
	//			}else {
	//				return generateResponse(ListUtils.toString(snpDbManager.getAllConsequenceTypes(), separator), outputFormat, compress);
	//			}
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

	public Response getSnpEffectByType() {
		try {
			connect();
			snpEffectDbManager = new SnpEffectDBManager(infraredDBConnector);
			if(uriInfo.getQueryParameters().get("type") != null && !uriInfo.getQueryParameters().get("type").get(0).equals("")) {
				FeatureList<SnpEffect> effect = snpEffectDbManager.getAllByType(uriInfo.getQueryParameters().get("type").get(0));
				return generateResponse(ListUtils.toString(effect, separator), outputFormat, compress);	
			}else {
				return generateResponse("No 'type' parameter provided, accepted values: a, b", outputFormat, compress);
			}
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@Override
	protected boolean isValidSpecies(String species) {
		if("hsa".equalsIgnoreCase(species) || "mmu".equalsIgnoreCase(species) || "rno".equalsIgnoreCase(species)) {
			return true;
		}
		return false;
	}
	
	private String createVariationResultString(List<String> ids, FeatureList<SNP> features) {
		StringBuilder result = new StringBuilder();
		for(int i=0; i<ids.size(); i++) {
			if(features.get(i) != null) {
				result.append(ids.get(i)).append("\t").append(features.get(i).getChromosome()+"\t"+features.get(i).getStart()+"\t"+features.get(i).getEnd()+"\t"+features.get(i).getStrand()+"\t"+features.get(i).getAllele()+"\t"+features.get(i).getConsequence_type()+"\t"+features.get(i).getSequence()).append(separator);
			}else {
				result.append(ids.get(i)).append("\t").append("not found").append(separator);
			}
		}
		return result.toString().trim();
	}
}
