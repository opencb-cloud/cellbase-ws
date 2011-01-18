package org.bioinfo.infrared.ws.server.rest.genomic;

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
import org.bioinfo.infrared.core.KaryotypeDBManager;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Cytoband;
import org.bioinfo.infrared.core.feature.Region;
import org.bioinfo.infrared.core.variation.AnnotatedMutation;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;



@Path("/{version}/{species}/genomic/cytoband")
@Produces("text/plain")
public class CytobandWSServer extends GenericRestWSServer{

	public CytobandWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	
	@GET
	@Path("/{chromosomeIds}/chr")
	@Deprecated // /{version}/{species}/genomic/chregion/{region}/cytoband
	public Response getChromosomeIds(@PathParam("chromosomeIds") String chrIds) {
		try {
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			List<String> idList = StringUtils.toList(chrIds, ",");
			FeatureList<Cytoband> chromosomeList = karyotypeDbManager.getCytobandByChromosomes(idList);
			return generateResponseFromFeatureList(chromosomeList, new TypeToken<FeatureList<Cytoband>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	
	
	
	@GET
	@Path("/{region}/region")
	@Deprecated // /{version}/{species}/genomic/chregion/{region}/cytoband
	public Response getCytobandByRegion(@PathParam("region") String region) {
		try {
			List<Region> regions = Region.parseRegions(region);
			
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			FeatureList<Cytoband> CytobandList = karyotypeDbManager.getCytobandByRegion(regions.get(0));
			return generateResponseFromFeatureList(CytobandList, new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorMessage(e.toString());
		}
	}
	
	
	
	@GET
	@Path("/list")
	public Response getListIds() {
		try {
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			FeatureList<Cytoband> chromosomeList = karyotypeDbManager.getAllCytoband();
			return generateResponseFromFeatureList(chromosomeList, new TypeToken<FeatureList<Cytoband>>() {}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return generateErrorMessage(e.toString());
		}
	}
	
	@GET
	@Path("/help")
	public String help() {
		return "cytoband help";
	}
	/*
	@GET
	@Path("/all")
	public String all() {
		return "cytoband help";
	}*/
	
	
	
	@Override
	public boolean isValidSpecies() {
		if("hsa".equalsIgnoreCase(species) || "mmu".equalsIgnoreCase(species) || "rno".equalsIgnoreCase(species)) {
			return true;
		}
		return false;
	}
	

}
