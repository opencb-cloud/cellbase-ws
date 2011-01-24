package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.ArrayList;
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
	@Override
	protected List<String> getPathsNicePrint(){
		List<String> paths = new ArrayList<String>();
		paths.add("list");
		paths.add("{cytobandId}");
		paths.add("{cytobandId}/info");
		paths.add("{cytobandId}/snp");
		return paths;
	}
	
	@Override
	protected List<String> getExamplesNicePrint(){
		List<String> examples = new ArrayList<String>();
		examples.add("/infrared-ws/api/v1/hsa/genomic/cytoband/list");
		examples.add("/infrared-ws/api/v1/hsa/genomic/cytoband/p36.33");
		examples.add("/infrared-ws/api/v1/hsa/genomic/cytoband/p36.33/info");
		examples.add("/infrared-ws/api/v1/hsa/genomic/cytoband/p36.33/snp");
		return examples;
	}

	public CytobandWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@GET
	@Path("/{cytobandId}/info")
	public Response getCytobandByName(@PathParam("cytobandId") String cytobandId) {
		return getCytobandById(cytobandId);
	}
	
	@GET
	@Path("/{cytobandId}")
	public Response getCytobandById(@PathParam("cytobandId") String cytobandId) {
		try {
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			List<String> idList = StringUtils.toList(cytobandId, ",");
			
			FeatureList<Cytoband> cytobandList = karyotypeDbManager.getCytobandById(idList);
			return generateResponseFromFeatureList(cytobandId, cytobandList, new TypeToken<FeatureList<Cytoband>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	
	@GET
	@Path("/{cytobandId}/snp")
	public Response getSNPByCytoband(@PathParam("cytobandId") String cytobandId) {
		try {
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			List<String> idList = StringUtils.toList(cytobandId, ",");
			FeatureList<Cytoband> cytobandList = karyotypeDbManager.getCytobandById(idList);
			List<Region> regions = new ArrayList<Region>(idList.size());
			for (Cytoband cytoband : cytobandList) {
				if (cytoband!=null){
					Region region = new Region(cytoband.getChromosome(), cytoband.getStart(), cytoband.getEnd());
					regions.add(region);
				}
				else{
					regions.add(null);
				}
			}
			String regionQuery = Region.parseRegion(regions);
			return  new ChromosomeRegionServer(this.version, this.species, this.uriInfo).getSnpsByRegion(regionQuery);
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	@GET
	@Path("/{chromosomeIds}/chr")
	@Deprecated // /{version}/{species}/genomic/chregion/{region}/cytoband
	public Response getChromosomeIds(@PathParam("chromosomeIds") String chrIds) {
		try {
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			List<String> idList = StringUtils.toList(chrIds, ",");
			FeatureList<Cytoband> chromosomeList = karyotypeDbManager.getCytobandByChromosomes(idList);
			return generateResponseFromFeatureList(chrIds, chromosomeList, new TypeToken<FeatureList<Cytoband>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
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
			return generateResponseFromFeatureList(region, CytobandList, new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	@GET
	@Path("/list")
	public Response getListIds() {
		try {
			KaryotypeDBManager karyotypeDbManager = new KaryotypeDBManager(infraredDBConnector);
			FeatureList<Cytoband> chromosomeList = karyotypeDbManager.getAllCytoband();
			return generateResponseFromFeatureList("", chromosomeList, new TypeToken<FeatureList<Cytoband>>() {}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			return generateErrorResponse(e.toString());
		}
	}
	/*
	@GET
	@Path("/help")
	public String help() {
		StringBuilder br = new StringBuilder();
		br.append("@Path(/{version}/{species}/genomic/cytoband)");
		br.append(System.getProperty("line.separator"));
		br.append(System.getProperty("line.separator"));
		br.append("Path:\n");
		for (String path : getPathsNicePrint()) {
			br.append("\t"+path+"\n");
		}
		br.append(System.getProperty("line.separator"));
		br.append("Examples:\n");
		for (String path : getExamplesNicePrint()) {
			br.append("\t"+path+"\n");
		}
		
		
		br.append("");
		
		return br.toString();
	}

	*/
	
	@Override
	public boolean isValidSpecies() {
		if("hsa".equalsIgnoreCase(species) || "mmu".equalsIgnoreCase(species) || "rno".equalsIgnoreCase(species)) {
			return true;
		}
		return false;
	}
	

}
