package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/feature")
@Produces("text/plain")
public class FeatureWSServer extends GenericRestWSServer {


	public FeatureWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	//	public FeatureWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
	//		init(version, species, uriInfo);
	//		connect();
	//	}
	// Returns all possible DB Names
//	@GET
//	@Path("/dbnames")
//	public Response getAllDBNames() {
//		try {
//
//			List<DBName> dbnames = new ArrayList<DBName>();
//			List<DBName> aux;
//			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
//			if (uriInfo.getQueryParameters().get("type") != null) {
//				//				String dbTypeString = ui.getQueryParameters().get("type").get(0);
//				//				List<String> types = StringUtils.toList(dbTypeString, ",");
//				List<String> types = StringUtils.toList(uriInfo.getQueryParameters().get("type").get(0), ",");
//				types = ListUtils.unique(types);
//				for(String type: types) {
//					aux = xrefDbManager.getAllDBNamesByType(type);
//					if(aux != null) {
//						dbnames.addAll(aux);
//					}
//				}
//			}else {
//				dbnames.addAll(xrefDbManager.getAllDBNames());
//			}
//			//return generateResponse(ListUtils.toString(dbnames, querySeparator), outputFormat, outputCompress);
//			this.listType = new TypeToken<List<DBName>>() {}.getType();
//			return generateResponse(dbnames, outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//
//	@GET
//	@Path("/{featureId}/xref")
//	public Response getAllIdentifiers(@PathParam("featureId") String idsString) {
//		try {
//
//			List<String> ids = StringUtils.toList(idsString, ",");
//			List<XRef> xrefs = new FeatureList<XRef>();
//			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
//			if(uriInfo.getQueryParameters().get("dbname") != null) {
//				List<String> dbnames = StringUtils.toList(uriInfo.getQueryParameters().get("dbname").get(0), ",");
//				xrefs = xrefDbManager.getByDBName(ids, dbnames);
//			}else {
//				xrefs = xrefDbManager.getAllIdentifiersByIds(ids);
//			}
//			//return generateResponse(ListUtils.toString(xrefs, querySeparator), outputFormat, outputCompress);
//			this.listType = new TypeToken<List<XRef>>() {}.getType();
//			return generateResponse(xrefs, outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{featureId}/functionalannotation")
//	public Response getAllFunctionalAnnotations(@PathParam("featureId") String idsString) {
//		try {
//
//			List<String> ids = StringUtils.toList(idsString, ",");
//			List<XRef> xrefs = new FeatureList<XRef>();
//			XRefDBManager xrefDbManager = new XRefDBManager(infraredDBConnector);
//			if(uriInfo.getQueryParameters().get("dbname") != null) {
//				List<String> dbnames = StringUtils.toList(uriInfo.getQueryParameters().get("dbname").get(0), ",");
//				xrefs = xrefDbManager.getByDBName(ids, dbnames);
//			}else {
//				xrefs = xrefDbManager.getAllFunctionalAnnotByIds(ids);
//			}
//			//			return generateResponse(ListUtils.toString(xrefs, querySeparator), outputFormat, outputCompress);
//			this.listType = new TypeToken<List<XRef>>() {}.getType();
//			return generateResponse(xrefs, outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{featureId}/snps") // Crear metodo que devuelva los snps que se encuentran en un featureId
//	public Response getxxx1(@PathParam("featureId") String idsString) {
//		try {
//			List<String> ids = StringUtils.toList(idsString, ",");
//			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
//			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
////			return generateResponse(createResultString(ids, genes), outputFormat, outputCompress);
////			this.listType = new TypeToken<List<FeatureList<Gene>>>() {}.getType();
//			return generateResponseFromListFeatureList(genes, new TypeToken<List<FeatureList<Gene>>>() {}.getType());
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{featureId}/info")
//	public Response getAllByExternalId( @PathParam("featureId") String idsString) {
//		try {
//			List<String> ids = StringUtils.toList(idsString, ",");
//			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
//			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
////			return generateResponse(createResultString(ids, genes), outputFormat, outputCompress);
////			this.listType = new TypeToken<List<FeatureList<Gene>>>() {}.getType();
//			return generateResponseFromListFeatureList(genes, new TypeToken<List<FeatureList<Gene>>>() {}.getType());
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{featureId}/sequence") // Crear metodo que devuelva la seq segun featureID
//	public Response getxxx(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
//		try {
//			//			init(version, species, ui);
//			//			connect();
//
//			List<String> ids = StringUtils.toList(idsString, ",");
//			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
//			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
//			return generateResponse(createResultString(ids, genes), outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//
//	@GET
//	@Path("/{featureId}/regulatory") // Crear metodo que devuelva los elementos reguladores de un featureId
//	public Response getxxx2(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
//		try {
//			//			init(version, species, ui);
//			//			connect();
//
//			List<String> ids = StringUtils.toList(idsString, ",");
//			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
//			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
//			return generateResponse(createResultString(ids, genes), outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{featureId}/exons") // Crear metodo que devuelva los exones de un featureId
//	public Response getxxx3(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
//		try {
//			//			init(version, species, ui);
//			//			connect();
//
//			List<String> ids = StringUtils.toList(idsString, ",");
//			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
//			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
//			return generateResponse(createResultString(ids, genes), outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	@GET
//	@Path("/{featureId}/location") // Crear metodo que devuelva la localizacion de un featureId
//	public Response getxxx4(@PathParam("version") String version, @PathParam("species") String species, @PathParam("featureId") String idsString, @Context UriInfo ui) {
//		try {
//			//			init(version, species, ui);
//			//			connect();
//
//			List<String> ids = StringUtils.toList(idsString, ",");
//			GeneDBManager geneDbManager = new GeneDBManager(infraredDBConnector);
//			List<FeatureList<Gene>> genes = geneDbManager.getAllByExternalIds(ids);
//			return generateResponse(createResultString(ids, genes), outputFormat, outputCompress);
//		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
//		}
//	}
//
//	//	public Response getSnpsByRegion(@PathParam("species") String species, @PathParam("region") String regionString, @Context UriInfo ui) {
//	//		init(species, ui);
//	//		List<Region> regions = Region.parseRegion(regionString);
//	//		try {
//	//			connect();
//	//			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
//	//			FeatureList<SNP> snps = new FeatureList<SNP>();
//	//			FeatureList<SNP> snpsByConsequenceType = new FeatureList<SNP>();
//	//			for(Region region: regions) {
//	//				if(region != null && region.getChromosome() != null && !region.getChromosome().equals("")) {
//	//					if(region.getStart() == 0 && region.getEnd() == 0) {
//	//						snps.addAll(snpDbManager.getAllByLocation(region.getChromosome(), 1, Integer.MAX_VALUE));
//	//					}else {
//	//						snps.addAll(snpDbManager.getAllByLocation(region.getChromosome(), region.getStart(), region.getEnd()));
//	//					}
//	//				}
//	//			}
//	//			// if there is a consequence type filter lets filter!
//	//			if(ui.getQueryParameters().get("consequence_type") != null) {
//	//				List<String> consequencetype = StringUtils.toList(ui.getQueryParameters().get("consequence_type").get(0), ",");
//	//				for(SNP snp: snps) {
//	//					for(String consquenceType: snp.getConsequence_type()) {
//	//						if(consequencetype.contains(consquenceType)) {
//	//							snpsByConsequenceType.add(snp);
//	//						}
//	//					}
//	//				}
//	//				snps = snpsByConsequenceType;
//	//			}
//	//			return generateResponse(ListUtils.toString(snps, querySeparator), outputFormat, outputCompress);
//	//		} catch (Exception e) {
//	//			return generateErrorMessage(e.toString());
//	//		}
//	//	}

	@Override
	public boolean isValidSpecies() {
		return true;
	}
}
