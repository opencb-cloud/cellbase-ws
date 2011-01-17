package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/feature/gene")
@Produces("text/plain")
public class GeneWSServer extends FeatureWSServer implements IFeature {

	public GeneWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/list")
	public String getList() {
		return null;
	}
	
	@GET
	@Path("/dbnames")
	public String getDbNames() {
		return null;
	}
	
	@GET
	@Path("/{geneId}/coordinates")
	public String getCoordinates(@PathParam("geneId") String geneId) {
		return null;
	}

	@GET
	@Path("/{geneId}/exon")
	public String getExons(@PathParam("geneId") String geneId) {
		return null;
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
	@Path("/{geneId}/info")
	public String getInfo(@PathParam("geneId") String geneId) {
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
		// TODO Auto-generated method stub
		return false;
	}
	
	@GET
	@Path("/{geneId}/sequence")
	@Override
	public String sequence(@PathParam("geneId") String geneId) {
		return null;
	}
	
}
