package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.db.handler.ResultSetHandler;
import org.bioinfo.infrared.core.ExonDBManager;
import org.bioinfo.infrared.core.GeneDBManager;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Exon;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;


@Path("/{version}/{species}/feature/gene")
@Produces("text/plain")
public class GeneWSServer extends FeatureWSServer implements IFeature {


	public GeneWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	
	private GeneDBManager getGeneDBManager()
	{
		return new GeneDBManager(infraredDBConnector);
	}
	
	@GET
	@Path("/dbnames")
	public String getDbNames() {
		return null;
	}

	@GET
	@Path("/list")
	public Response getList() {
		try {
			GeneDBManager geneDBManager = getGeneDBManager();
			FeatureList<Gene> geneList = geneDBManager.getAll();
			return generateResponseFromFeatureList(geneList, new TypeToken<FeatureList<Gene>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	@GET
	@Path("/{geneId}")
	// GeneDBManager: public FeatureList<Gene> getByEnsemblId(String ensemblId)
	public Response getById(@PathParam("geneId") String geneId) {
		return getByEnsemblId(geneId);
	}
	
	@GET
	@Path("/{geneId}/info")
	// GeneDBManager: public FeatureList<Gene> getByEnsemblId(String ensemblId)
	public Response getByEnsemblId(@PathParam("geneId") String geneId) {
		try {
			GeneDBManager geneDBManager = getGeneDBManager();
			FeatureList<Gene> geneList;
			List<String> ids = StringUtils.toList(geneId, ",");
			if(uriInfo.getQueryParameters().get("biotype") != null) {
				String biotype = uriInfo.getQueryParameters().get("biotype").get(0);
				geneList = geneDBManager.getAllByBiotype(biotype);
			}
			else{
				geneList = geneDBManager.getByEnsemblId(ids);
			}

			System.out.println(ids);
			System.out.println(geneList);
			return generateResponseFromFeatureList(geneId, geneList, new TypeToken<FeatureList<Gene>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	
	@GET
	@Path("/{geneId}/external")
	public Response getInfo(@PathParam("geneId") String geneId) {
		try {
			GeneDBManager geneDBManager = getGeneDBManager();
			List<String> ids = StringUtils.toList(geneId, ",");
			List<FeatureList<Gene>> geneList =  geneDBManager.getAllByExternalIds(ids);
			return generateResponseFromListFeatureList(geneId, geneList, new TypeToken<List<FeatureList<Gene>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	@GET
	@Path("/{geneId}/coordinate")
	public Response getCoordinate(@PathParam("geneId") String geneId) {
		return null;
		/*try {
			GeneDBManager geneDBManager = getGeneDBManager();
			List<String> ids = StringUtils.toList(geneId, ",");
			List<FeatureList<Gene>> geneList =  geneDBManager.getAllByExternalIds(ids);
			return generateResponseFromListFeatureList(geneId, geneList, new TypeToken<List<FeatureList<Gene>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}*/
	}

	@GET
	@Path("/{geneId}/exon")
	public Response getExons(@PathParam("geneId") String geneId) {
		try {
			List<String> ids = StringUtils.toList(geneId, ",");
			ExonDBManager exonDBManager = new ExonDBManager(infraredDBConnector);
			List<FeatureList<Exon>> exons = exonDBManager.getAllByIds(ids);
			return generateResponseFromListFeatureList(geneId, exons, new TypeToken<List<FeatureList<Exon>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	@GET
	@Path("/{geneId}/transcript")
	public String getTranscripts(@PathParam("geneId") String geneId) {
		return null;
	}
	
	@GET
	@Path("/{geneId}/protein")
	public String getProteins(@PathParam("geneId") String geneId) {
		return null;
	}
	
	@GET
	@Path("/{geneId}/orthologous")
	public String getOrthologus(@PathParam("geneId") String geneId) {
		return null;
	}
	
	
	
	@GET
	@Path("/{geneId}/fullinfo")
	public String getFullInfo(@PathParam("geneId") String geneId) {
		return null;
	}
	
	@GET
	@Path("/{geneId}/snp")
	public String getSnps(@PathParam("geneId") String geneId) {
		return null;
	}
	
	@GET
	@Path("/{geneId}/mutation")
	public String getMutations(@PathParam("geneId") String geneId) {
		return null;
	}

	@GET
	@Path("/{geneId}/xref")
	public String getXrefs(@PathParam("geneId") String geneId) {
		return null;
	}
	
	@GET
	@Path("/{geneId}/tfbs")
	public String getTfbs(@PathParam("geneId") String geneId) {
		return null;
	}
	
	@GET
	@Path("/{geneId}/pathway")
	public String getPathways(@PathParam("geneId") String geneId) {
		return null;
	}
	
	
	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidSpecies() {
		if("hsa".equalsIgnoreCase(species) || "mmu".equalsIgnoreCase(species) || "rno".equalsIgnoreCase(species)) {
			return true;
		}
		return false;
	}
	
	@GET
	@Path("/{geneId}/sequence")
	@Override
	public String sequence(@PathParam("geneId") String geneId) {
		return null;
	}
	
}
