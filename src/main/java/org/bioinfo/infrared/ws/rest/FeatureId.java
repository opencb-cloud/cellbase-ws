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
import org.bioinfo.infrared.core.dbsql.GeneDBManager;
import org.bioinfo.infrared.core.dbsql.XRefDBManager;
import org.bioinfo.infrared.core.feature.DBName;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.core.feature.XRef;


@Path("/{version}/{species}/feature")
@Produces("text/plain")
public class FeatureId extends AbstractInfraredRest{

	@Override
	protected boolean isValidSpecies(String species) {
		// TODO Auto-generated method stub
		return false;
	}
/*
	// Returns all possible DB Names
	@GET
	@Path("/dbnames")
	public Response getAllDBNames(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();

			List<DBName> dbnames = new ArrayList<DBName>();
			List<DBName> aux;
			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
			if (ui.getQueryParameters().get("type") != null) {
//				String dbTypeString = ui.getQueryParameters().get("type").get(0);
//				List<String> types = StringUtils.toList(dbTypeString, ",");
				List<String> types = StringUtils.toList(ui.getQueryParameters().get("type").get(0), ",");
				types = ListUtils.unique(types);
				for(String type: types) {
					aux = xrefDbManager.getAllDBNamesByType(type);
					if(aux != null) {
						dbnames.addAll(aux);
					}
				}
			}else {
				dbnames.addAll(xrefDbManager.getAllDBNames());
			}
			return generateResponse(ListUtils.toString(dbnames, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
			
	
	@GET
	@Path("/{featureId}/xref")
	public Response getAllIdentifiers(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			List<XRef> xrefs = new FeatureList<XRef>();
			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
			if(ui.getQueryParameters().get("dbname") != null) {
				List<String> dbnames = StringUtils.toList(ui.getQueryParameters().get("dbname").get(0), ",");
				xrefs = xrefDbManager.getByDBName(ids, dbnames);
			}else {
				xrefs = xrefDbManager.getAllIdentifiersByIds(ids);
			}
			return generateResponse(ListUtils.toString(xrefs, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/{featureId}/fuctionalannotation")
	public Response getAllFunctionalAnnotations(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			List<XRef> xrefs = new FeatureList<XRef>();
			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
			if(ui.getQueryParameters().get("dbname") != null) {
				List<String> dbnames = StringUtils.toList(ui.getQueryParameters().get("dbname").get(0), ",");
				xrefs = xrefDbManager.getByDBName(ids, dbnames);
			}else {
				xrefs = xrefDbManager.getAllFunctionalAnnotByIds(ids);
			}
			return generateResponse(ListUtils.toString(xrefs, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{featureId}/snps") // Crear metodo que devuelva los snps que se encuentran en un featureId
	public Response getxxx1(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
			return generateResponse(createResultString(ids, genes), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{featureId}/info")
	public Response getAllByExternalId(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
			return generateResponse(createResultString(ids, genes), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{featureId}/sequence") // Crear metodo que devuelva la seq segun featureID
	public Response getxxx(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
			return generateResponse(createResultString(ids, genes), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
			
	@GET
	@Path("/{featureId}/regulatory") // Crear metodo que devuelva los elementos reguladores de un featureId
		public Response getxxx2(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
			return generateResponse(createResultString(ids, genes), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{featureId}/exons") // Crear metodo que devuelva los exones de un featureId
			public Response getxxx3(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
			return generateResponse(createResultString(ids, genes), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/{featureId}/location") // Crear metodo que devuelva la localizacion de un featureId
			public Response getxxx4(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
			return generateResponse(createResultString(ids, genes), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
		//	public Response getSnpsByRegion(@PathParam("species") String species, @PathParam("region") String regionString, @Context UriInfo ui) {
		//		init(species, ui);
		//		List<Region> regions = Region.parseRegion(regionString);
		//		try {
		//			connect();
		//			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
		//			FeatureList<SNP> snps = new FeatureList<SNP>();
		//			FeatureList<SNP> snpsByConsequenceType = new FeatureList<SNP>();
		//			for(Region region: regions) {
		//				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
		//					if(region.getStart() == 0 && region.getEnd() == 0) {
		//						snps.addAll(snpDbManager.getAllByLocation(region.getChromosome(), 1, Integer.MAX_VALUE));
		//					}else {
		//						snps.addAll(snpDbManager.getAllByLocation(region.getChromosome(), region.getStart(), region.getEnd()));
		//					}
		//				}
		//			}
		//			// if there is a consequence type filter lets filter!
		//			if(ui.getQueryParameters().get("consequence_type") != null) {
		//				List<String> consequencetype = StringUtils.toList(ui.getQueryParameters().get("consequence_type").get(0), ",");
		//				for(SNP snp: snps) {
		//					for(String consquenceType: snp.getConsequence_type()) {
		//						if(consequencetype.contains(consquenceType)) {
		//							snpsByConsequenceType.add(snp);
		//						}
		//					}
		//				}
		//				snps = snpsByConsequenceType;
		//			}
		//			return generateResponse(ListUtils.toString(snps, separator), outputFormat, compress);
		//		} catch (Exception e) {
		//			return generateErrorMessage(e.toString());
		//		}
		//	}

		@Override
		protected boolean isValidSpecies(String species) {
			return true;
		}
*/
	}
