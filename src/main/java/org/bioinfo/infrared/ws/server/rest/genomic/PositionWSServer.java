package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.infrared.lib.common.Position;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/genomic/position")
@Produces("text/plain")
public class PositionWSServer extends GenericRestWSServer {

	public PositionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{positionId}/consequence_type")
	public Response getConsequenceTypeByPositionGet(@PathParam("positionId") String positionId, @DefaultValue("") @QueryParam("gene") String gene, @DefaultValue("") @QueryParam("transcript") String transcript) {
		return getConsequenceTypeByPosition(positionId);
	}
	
	@POST
	@Path("/{positionId}/consequence_type")
	public Response getConsequenceTypeByPositionPost(@PathParam("positionId") String positionId) {
		return getConsequenceTypeByPosition(positionId);
	}
	
	public Response getConsequenceTypeByPosition(@PathParam("positionId") String positionId) {
		List<Position> positionList = Position.parsePositions(positionId);
		return null;
	}
	
	
	@GET
	@Path("/{positionId}/functional")
	public Response getFunctionalByPositionGet(@PathParam("positionId") String positionId, @DefaultValue("") @QueryParam("source") String source) {
		return getFunctionalByPosition(positionId);
	}
	
	@POST
	@Path("/{positionId}/functional")
	public Response getFunctionalTypeByPositionPost(@PathParam("positionId") String positionId) {
		return getFunctionalByPosition(positionId);
	}
	
	public Response getFunctionalByPosition(@PathParam("positionId") String positionId) {
		List<Position> positionList = Position.parsePositions(positionId);
		return null;
	}
	
	
}
