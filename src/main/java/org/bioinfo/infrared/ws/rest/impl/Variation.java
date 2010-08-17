package org.bioinfo.infrared.ws.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.infrared.common.feature.FeatureList;
import org.bioinfo.infrared.variation.SNP;
import org.bioinfo.infrared.variation.dbsql.SNPDBManager;


public class Variation extends AbstractInfraredRest {

	private SNPDBManager snpDbManager;

	public Variation(String species, UriInfo uriInfo) {
		super(species, uriInfo);
	}
	
	public Response getSnps() {
		try {
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

	public Response getSnpsByConsequenceType() {
		try {
			connect();
			snpDbManager = new SNPDBManager(infraredDBConnector);
			if(uriInfo.getQueryParameters().get("consequencetype") != null) {
				FeatureList<SNP> snps = snpDbManager.getAllByConsequenceType(uriInfo.getQueryParameters().get("consequencetype").get(0));
				return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
			}else {
				return generateResponse(ListUtils.toString(snpDbManager.getAllConsequenceTypes(), separator), outputFormat, compress);
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
