package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.variation.AnnotatedSNP;
import org.bioinfo.infrared.core.variation.Omega;
import org.bioinfo.infrared.core.variation.SNP;
import org.bioinfo.infrared.core.variation.VariationFrequency;
import org.bioinfo.infrared.variation.AnnotatedSnpDBManager;
import org.bioinfo.infrared.variation.OmegaDBManager;
import org.bioinfo.infrared.variation.SNPDBManager;
import org.bioinfo.infrared.variation.VariationFrequencyDBManager;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;

@Path("/{version}/{species}/feature/snp")
@Produces("text/plain")
public class SnpWSServer extends FeatureWSServer implements IFeature {

	public SnpWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{snpId}")
	public Response getByIds(@PathParam("snpId") String snpIds) {
		return getSNPListByIds(snpIds);
	}

	@GET
	@Path("/list")
	public Response getListIds() {
		try {
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			List<String> snplist = null;
			if(uriInfo.getQueryParameters().get("consequence_type") != null) {
				List<String> consequenceTypes = StringUtils.toList(uriInfo.getQueryParameters().get("consequence_type").get(0), ",");
				snplist = snpDbManager.getAllNamesByConsequenceTypes(consequenceTypes);
			}else {
				snplist = snpDbManager.getAllNames();
			}
			return generateResponseFromList(snplist);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{snpId}/info")
	public Response getSNPListByIds(@PathParam("snpId") String snpIds) {
		try {
			List<String> ids = StringUtils.toList(snpIds, ",");
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			FeatureList<SNP> snplist = snpDbManager.getByNames(ids);
//			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
//			this.listType = new TypeToken<FeatureList<SNP>>() {}.getType();
			return generateResponseFromFeatureList(snplist, new TypeToken<FeatureList<SNP>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{snpId}/consequence_type")
	public Response getAllFilteredByConsequenceType(@PathParam("snpId") String snpIds) {
		try {
			List<String> ids = StringUtils.toList(snpIds, ",");
			FeatureList<SNP> snplist;
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			if(uriInfo.getQueryParameters().get("consequencetype") != null) {
				List<String> consequenceTypes = StringUtils.toList(uriInfo.getQueryParameters().get("consequence_type").get(0), ",");
				snplist = snpDbManager.getAllFilteredByConsequenceType(ids, consequenceTypes);
			}else {
				snplist = snpDbManager.getByNames(ids);
			}
//			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
//			this.listType = new TypeToken<FeatureList<SNP>>() {}.getType();
			return generateResponseFromFeatureList(snplist, new TypeToken<FeatureList<SNP>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{snpId}/annotated")
	public Response getAnnotatedSNPListByIds(@PathParam("snpId") String snpIds) {
		try {
			List<String> ids = StringUtils.toList(snpIds, ",");
			AnnotatedSnpDBManager annotatedSnpDBManager = new AnnotatedSnpDBManager(infraredDBConnector);
			List<FeatureList<AnnotatedSNP>> snplist = annotatedSnpDBManager.getAllByIds(ids);
			//			this.listType = new TypeToken<List<FeatureList<AnnotatedSNP>>>() {}.getType();
			return generateResponseFromListFeatureList(snplist, new TypeToken<List<FeatureList<AnnotatedSNP>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{snpId}/frequency")
	public Response getFrequencies(@PathParam("snpId") String snpIds) {
		try {
			List<String> ids = StringUtils.toList(snpIds, ",");
			FeatureList<VariationFrequency> snplist;
			VariationFrequencyDBManager variationFrequencyDbManager = new VariationFrequencyDBManager(infraredDBConnector);
			if(uriInfo.getQueryParameters().get("population") != null) {
				List<String> populations = StringUtils.toList(uriInfo.getQueryParameters().get("populations").get(0), ",");
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}else {
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}
			//			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
			//			this.listType = new TypeToken<FeatureList<VariationFrequency>>() {}.getType();
			return generateResponseFromFeatureList(snplist, new TypeToken<FeatureList<VariationFrequency>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{snpId}/allele_frequency")
	public Response getAlleleFrequencies(@PathParam("snpId") String snpIds) {
		try {
			List<String> ids = StringUtils.toList(snpIds, ",");
			FeatureList<VariationFrequency> snplist;
			VariationFrequencyDBManager variationFrequencyDbManager = new VariationFrequencyDBManager(infraredDBConnector);
			if(uriInfo.getQueryParameters().get("population") != null) {
				List<String> populations = StringUtils.toList(uriInfo.getQueryParameters().get("populations").get(0), ",");
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}else {
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}
			//			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
			//			this.listType = new TypeToken<FeatureList<VariationFrequency>>() {}.getType();
			return generateResponseFromFeatureList(snplist, new TypeToken<FeatureList<VariationFrequency>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{snpId}/genotype_frequency")
	public Response getGenotypeFrequencies(@PathParam("snpId") String snpIds) {
		try {
			List<String> ids = StringUtils.toList(snpIds, ",");
			FeatureList<VariationFrequency> snplist;
			VariationFrequencyDBManager variationFrequencyDbManager = new VariationFrequencyDBManager(infraredDBConnector);
			if(uriInfo.getQueryParameters().get("population") != null) {
				List<String> populations = StringUtils.toList(uriInfo.getQueryParameters().get("populations").get(0), ",");
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}else {
				snplist = variationFrequencyDbManager.getBySnpIds(ids);
			}
			//			return generateResponse(createResultString(ids, snplist), outputFormat, compress);
			//			this.listType = new TypeToken<FeatureList<VariationFrequency>>() {}.getType();
			return generateResponseFromFeatureList(snplist, new TypeToken<FeatureList<VariationFrequency>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}


	@GET
	@Path("/{snpId}/functional")
	public Response getFunctionalSnps(@PathParam("snpId") String snpIds) {
		try {
			List<String> snps = StringUtils.toList(snpIds, ",");
			OmegaDBManager omegaDbManager = new OmegaDBManager(infraredDBConnector);
			List<FeatureList<Omega>> omegas;
			if(uriInfo.getQueryParameters().get("min") != null && uriInfo.getQueryParameters().get("max") != null) {
				omegas = omegaDbManager.getAllBySnpIds(snps, Double.parseDouble(uriInfo.getQueryParameters().get("min").get(0)), Double.parseDouble(uriInfo.getQueryParameters().get("max").get(0)));
			}else{
				omegas = omegaDbManager.getAllBySnpIds(snps);
			}
			//			return generateResponse(createResultString(snps, omegas), outputFormat, compress);
			//			this.listType = new TypeToken<List<FeatureList<Omega>>>() {}.getType();
			return generateResponseFromListFeatureList(omegas, new TypeToken<List<FeatureList<Omega>>>() {}.getType());
		}catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@Deprecated
	@GET
	@Path("/{snpId}/omegas")
	public Response getAllBySnpIds(@PathParam("snpId") String snpIds) {
		try {
			List<String> snps = StringUtils.toList(snpIds, ",");
			OmegaDBManager omegaDbManager = new OmegaDBManager(infraredDBConnector);
			List<FeatureList<Omega>> omegas;
			if(uriInfo.getQueryParameters().get("min") != null && uriInfo.getQueryParameters().get("max") != null) {
				omegas = omegaDbManager.getAllBySnpIds(snps, Double.parseDouble(uriInfo.getQueryParameters().get("min").get(0)), Double.parseDouble(uriInfo.getQueryParameters().get("max").get(0)));
			}else{
				omegas = omegaDbManager.getAllBySnpIds(snps);
			}
			//			return generateResponse(createResultString(snps, omegas), outputFormat, compress);
			//			this.listType = new TypeToken<List<FeatureList<Omega>>>() {}.getType();
			return generateResponseFromListFeatureList(omegas, new TypeToken<List<FeatureList<Omega>>>() {}.getType());
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
	//			return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, compress);
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
	//				return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, compress);
	//			}else {
	//				return generateResponse(ListUtils.toString(snpDbManager.getAllConsequenceTypes(), querySeparator), outputFormat, compress);
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
	//				return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, compress);
	//			}else {
	//				return generateResponse(ListUtils.toString(snpDbManager.getAllConsequenceTypes(), querySeparator), outputFormat, compress);
	//			}
	//		} catch (Exception e) {
	//			return generateErrorMessage(e.toString());
	//		}
	//	}

	@Override
	public boolean isValidSpecies() {
		if("hsa".equalsIgnoreCase(species) || "mmu".equalsIgnoreCase(species) || "rno".equalsIgnoreCase(species)) {
			return true;
		}
		return false;
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}


	@GET
	@Path("/{snpId}/sequence")
	@Override
	public String sequence(@PathParam("snpId") String feature) {
		return null;
	}

	//	private String createVariationResultString(List<String> ids, FeatureList<SNP> features) {
	//		StringBuilder result = new StringBuilder();
	//		for(int i=0; i<ids.size(); i++) {
	//			if(features.get(i) != null) {
	//				result.append(ids.get(i)).append("\t").append(features.get(i).getChromosome()+"\t"+features.get(i).getStart()+"\t"+features.get(i).getEnd()+"\t"+features.get(i).getStrand()+"\t"+features.get(i).getAllele()+"\t"+features.get(i).getConsequenceTypeList()+"\t"+features.get(i).getSequence()).append(querySeparator);
	//			}else {
	//				result.append(ids.get(i)).append("\t").append("not found").append(querySeparator);
	//			}
	//		}
	//		return result.toString().trim();
	//	}

}
