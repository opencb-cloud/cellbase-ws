package org.bioinfo.infrared.ws.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.infrared.common.feature.FeatureList;
import org.bioinfo.infrared.variation.SNP;
import org.bioinfo.infrared.variation.SnpEffect;
import org.bioinfo.infrared.variation.dbsql.SNPDBManager;
import org.bioinfo.infrared.variation.dbsql.SnpEffectDBManager;
import org.bioinfo.infrared.variation.dbsql.SpliceSiteDBManager;


@Path("/{version}/{species}/variation/{variationId}")
@Produces("text/plain")
public class Variation extends AbstractInfraredRest {

	private SNPDBManager snpDbManager;
	private SnpEffectDBManager snpEffectDbManager;
	private SpliceSiteDBManager spliceSiteDBManager;

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
	public Response getAllSnps(@PathParam("version") String version, @PathParam("species") String species, ) {
		try {
			init(version, species, ui);
			
			connect();
			snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snps;
			if(uriInfo.getQueryParameters().get("chromosome") != null) {
				snps = snpDbManager.getAllByLocation(uriInfo.getQueryParameters().get("chromosome").get(0), 1, 20000);
			}else {
				snps = snpDbManager.getAllByConsequenceType("NON_SYNONYMOUS_CODING");
			}
			return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
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
			snpDbManager = new SNPDBManager(infraredDBConnector);

			if(uriInfo.getPathParameters().get("consequencetype") != null) {
				FeatureList<SNP> snps = snpDbManager.getAllByConsequenceType(uriInfo.getQueryParameters().get("consequencetype").get(0));
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
			snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snps;
			String region = uriInfo.getPathParameters().get("region").get(0);
			String[] items = region.split("[:-]");
			if(items.length == 3) {
				snps = snpDbManager.getAllByLocation(items[0], Integer.parseInt(items[1]), Integer.parseInt(items[2]));
			}else {
				snps = snpDbManager.getAllByConsequenceType("NON_SYNONYMOUS_CODING");
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

}
