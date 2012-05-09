package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.lib.api.XRefsDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/id")
@Produces("text/plain")
public class IdWSServer extends GenericRestWSServer {
	
	public IdWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	
	@GET
	@Path("/{id}/xref")
	public Response getByEnsemblId(@PathParam("id") String query, @DefaultValue("") @QueryParam("dbname") String dbName) {
		try{
			XRefsDBAdaptor x = dbAdaptorFactory.getXRefDBAdaptor(this.species);
			if (dbName.equals("")){
				return generateResponse(query, x.getAllByDBNameList(StringUtils.toList(query, ","), null));
			}
			else{
				return generateResponse(query, x.getAllByDBNameList(StringUtils.toList(query, ","), (StringUtils.toList(dbName, ","))));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse(uriInfo.getAbsolutePath().toString(), "getByEnsemblId", e.toString());
		}
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
