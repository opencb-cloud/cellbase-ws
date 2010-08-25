package org.bioinfo.infrared.ws.rest;

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
import org.bioinfo.infrared.core.XRef;
import org.bioinfo.infrared.core.dbsql.GeneDBManager;
import org.bioinfo.infrared.core.dbsql.XRefDBManager;
import org.bioinfo.infrared.funcannot.dbsql.AnnotationDBManager;


@Path("/{version}/{species}/feature/{featureId}")
@Produces("text/plain")
public class FeatureId extends AbstractInfraredRest{


	@GET
	@Path("/xref") //Crear metodo para devolver IDs con y sin dbname list
	public Response getAllDBNames(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			List<String> dbnames = null;
			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
			FeatureList<XRef> xref = new FeatureList<XRef>();
			if(ui.getQueryParameters().get("dbname") != null) {
				dbnames = StringUtils.toList(ui.getQueryParameters().get("dbname").get(0), ",");
			}

			if(dbnames != null) {
				xrefDbManager.getListByDBNames(ids, dbnames);
			}else {
				//				xrefDbManager.get
			}

			return generateResponse(ListUtils.toString(xref, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}

	@GET
	@Path("/annotation")//Crear metodo para devolver annotations segun dbname (con y sin)
	public Response getxxxxxx(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
		try {
			init(version, species, ui);
			connect();
			
			List<String> ids = StringUtils.toList(idsString, ",");
			List<String> dbnames = null;
			AnnotationDBManager annotationDbManager = new AnnotationDBManager(infraredDBConnector);
//			FeatureList<AnnotationObject> annotation = new FeatureList<AnnotationObject>();
			if(ui.getQueryParameters().get("dbname") != null) {
				dbnames = StringUtils.toList(ui.getQueryParameters().get("dbname").get(0), ",");
			}

			if(dbnames != null) {
//				annotation = annotationDbManager.getxxxxxx(ids, dbnames);
			}else {
				//				xrefDbManager.get
			}

			return generateResponse(ListUtils.toString(dbnames, separator), outputFormat, compress);
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/info")
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
	@Path("/sequence") // Crear metodo que devuelva la seq segun featureID
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
	@Path("/snps") // Crear metodo que devuelva los snps que se encuentran en un featureId
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
	@Path("/regulatory") // Crear metodo que devuelva los elementos reguladores de un featureId
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
	@Path("/exons") // Crear metodo que devuelva los exones de un featureId
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
	@Path("/location") // Crear metodo que devuelva la localizacion de un featureId
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

	}
