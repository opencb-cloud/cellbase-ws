package org.bioinfo.cellbase.ws.server.rest.feature;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.cellbase.lib.api.ChromosomeDBAdaptor;
import org.bioinfo.cellbase.lib.api.CytobandDBAdaptor;
import org.bioinfo.cellbase.ws.server.rest.GenericRestWSServer;
import org.bioinfo.cellbase.ws.server.rest.exception.VersionException;
import org.bioinfo.commons.utils.StringUtils;

@Path("/{version}/{species}/feature/chromosome")
@Produces("text/plain")
public class ChromosomeWSServer extends GenericRestWSServer {
	
	
	public ChromosomeWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
	}
	@GET
	@Path("/list")
	public Response getChromosomes() {
		try {
			checkVersionAndSpecies();
			ChromosomeDBAdaptor dbAdaptor = dbAdaptorFactory.getChromosomeDBAdaptor(this.species, this.version);
			return generateResponse("", dbAdaptor.getChromosomeNames());
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("list", e.toString());
		}
	}
	@GET
	@Path("/all")
	public Response getChromosomesAll() {
		try {
			checkVersionAndSpecies();
			ChromosomeDBAdaptor dbAdaptor = dbAdaptorFactory.getChromosomeDBAdaptor(this.species, this.version);
			return generateResponse("", dbAdaptor.getChromosomes());
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("all", e.toString());
		}
	}

	@GET
	@Path("/{chromosomeName}/info")
	public Response getChromosomes(@PathParam("chromosomeName") String query) {
		try {
			checkVersionAndSpecies();
			ChromosomeDBAdaptor dbAdaptor = dbAdaptorFactory.getChromosomeDBAdaptor(this.species, this.version);
			return generateResponse("", dbAdaptor.getChromosomeByNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("/{chromosomeName}/info", e.toString());
		}
	}
	
	@GET
	@Path("/{chromosomeName}/cytoband")
	public Response getByChromosomeName(@PathParam("chromosomeName") String query) {
		try {
			checkVersionAndSpecies();
			ChromosomeDBAdaptor dbAdaptor = dbAdaptorFactory.getChromosomeDBAdaptor(this.species, this.version);
			return generateResponse("", dbAdaptor.getCytobandByNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("/{chromosomeName}/cytoband", e.toString());
		}
	}
	

	
	@GET
	@Path("/{chromosomeName}/size")
	public Response getChromosomeSize(@PathParam("chromosomeName") String query) {
		return createOkResponse("TODO");
	}
	
	@GET
	public Response getHelp() {
		return help();
	}
	@GET
	@Path("/help")
	public Response help() {
		return createOkResponse("Usage:");
	}
}
