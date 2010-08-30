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
import org.bioinfo.infrared.variation.Omega;
import org.bioinfo.infrared.variation.SNP;
import org.bioinfo.infrared.variation.VariationFrequency;
import org.bioinfo.infrared.variation.dbsql.OmegaDBManager;
import org.bioinfo.infrared.variation.dbsql.SNPDBManager;
import org.bioinfo.infrared.variation.dbsql.VariationFrequencyDBManager;


@Path("/{version}/{species}/variation")
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
	@Path("/ct")
	public Response getConsequenceType(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			List<String> ids = snpDbManager.getAllConsequenceTypes();
			return generateResponse(ListUtils.toString(ids, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{variationId}/info")
	public Response getSNPListByIds(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();

			List<String> ids = StringUtils.toList(snpIds, ",");
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snplist = snpDbManager.getByName(ids);
			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{variationId}/consequencetype")
	public Response getAllFilteredByConsequenceType(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();

			List<String> ids = StringUtils.toList(snpIds, ",");
			FeatureList<SNP> snplist;
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			if(ui.getQueryParameters().get("consequencetype") != null) {
				List<String> consequenceTypes = StringUtils.toList(ui.getQueryParameters().get("consequencetype").get(0), ",");
				snplist = snpDbManager.getAllFilteredByConsequenceType(ids, consequenceTypes);
			}else {
				snplist = snpDbManager.getByName(ids);
			}
			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{variationId}/frequencies")
	public Response getxxx(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();

			List<String> ids = StringUtils.toList(snpIds, ",");
			FeatureList<VariationFrequency> snplist;
			VariationFrequencyDBManager variationFrequencyDbManager = new VariationFrequencyDBManager(infraredDBConnector);
			if(ui.getQueryParameters().get("population") != null) {
				List<String> populations = StringUtils.toList(ui.getQueryParameters().get("populations").get(0), ",");
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}else {
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}
			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{variationId}/omegas")
	public Response getAllBySnpIds(@PathParam("version") String version, @PathParam("species") String species, @PathParam("variationId") String snpIds, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();

			List<String> snps = StringUtils.toList(snpIds, ",");
			OmegaDBManager omegaDbManager = new OmegaDBManager(infraredDBConnector);
			List<FeatureList<Omega>> omegas;
			if(ui.getQueryParameters().get("min") != null && ui.getQueryParameters().get("max") != null) {
				omegas = omegaDbManager.getAllBySnpIds(snps, Double.parseDouble(ui.getQueryParameters().get("min").get(0)), Double.parseDouble(ui.getQueryParameters().get("max").get(0)));
			}else{
				omegas = omegaDbManager.getAllBySnpIds(snps);
			}
			return generateResponse(createResultString(snps, omegas), outputFormat, compress);
		}catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}






	//	public Response getSnpsByRegion() {
	//		try {
	//			connect();
	//			omegaDbManager = new SNPDBManager(infraredDBConnector);
	//			FeatureList<SNP> snps;
	//			String region = uriInfo.getPathParameters().get("region").get(0);
	//			String[] items = region.split("[:-]");
	//			if(items.length == 3) {
	//				snps = omegaDbManager.getAllByLocation(items[0], Integer.parseInt(items[1]), Integer.parseInt(items[2]));
	//			}else {
	//				snps = omegaDbManager.getAllByConsequenceType("NON_SYNONYMOUS_CODING");
	//			}
	//			return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

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
				result.append(ids.get(i)).append("\t").append(features.get(i).getChromosome()+"\t"+features.get(i).getStart()+"\t"+features.get(i).getEnd()+"\t"+features.get(i).getStrand()+"\t"+features.get(i).getAllele()+"\t"+features.get(i).getConsequenceType()+"\t"+features.get(i).getSequence()).append(separator);
			}else {
				result.append(ids.get(i)).append("\t").append("not found").append(separator);
			}
		}
		return result.toString().trim();
	}
}
