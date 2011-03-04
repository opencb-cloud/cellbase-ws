package org.bioinfo.infrared.ws.server.rest.feature;

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
import org.bioinfo.infrared.core.XRefDBManager;
import org.bioinfo.infrared.core.feature.DBName;
import org.bioinfo.infrared.core.feature.XRef;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;


@Path("/{version}/{species}/feature/id")
@Produces("text/plain")
public class IdWSServer extends FeatureWSServer implements IFeature {

	public IdWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@Override
	public boolean isValidSpecies() {
		return true;
	}

	@Override
	@GET
	@Path("/help")
	public String help() {
		return null;
	}
	
	@Override
	@GET
	@Path("/stats")
	public String stats() {
		return null;
	}

	@Override
	@GET
	@Path("/{id}/sequence")
	public String sequence(@PathParam("id") String idString) {
		return null;
	}

	@GET
	@Path("/{id}/xref")
	public Response xref(@PathParam("id") String idString) {
		List<String> ids = StringUtils.toList(idString, ",");
		XRefDBManager xRefDBManager = new XRefDBManager(infraredDBConnector);
		List<XRef> xrefs;
		
		try {
			
			List<String> dbNames = null;
			if(uriInfo.getQueryParameters().get("dbname") != null) {
				dbNames = StringUtils.toList(uriInfo.getQueryParameters().get("dbname").get(0), ",");
			}else {
				dbNames = new ArrayList<String>(100);
				for(DBName dbName: xRefDBManager.getAllDBNames()) {
					dbNames.add(dbName.getDbname());
				}
				logger.debug("No 'dbname' query parameter set, using all dbnames: "+dbNames.toString());
			}
			
			xrefs = xRefDBManager.getByDBName(ids, dbNames);
			return generateResponseFromList(idString, xrefs, new TypeToken<List<XRef>>() {}.getType());
		}catch(Exception e) {
			return generateErrorResponse(StringUtils.getStackTrace(e));
		}
	}
	@GET
	@Path("/{id}/dbname")
	public Response dbnames(@PathParam("id") String idString) {
		List<String> ids = StringUtils.toList(idString, ",");
		XRefDBManager xRefDBManager = new XRefDBManager(infraredDBConnector);
		List<List<DBName>> listOfDbnames = new ArrayList<List<DBName>>();
		List<DBName> dbnames;
		
		try {
			for(String id: ids){
				dbnames = xRefDBManager.getAllDBNamesById(id);
				listOfDbnames.add(dbnames);
			}
			
			return generateResponseFromListOftList(idString, listOfDbnames, new TypeToken<List<List<DBName>>>() {}.getType());
		}catch(Exception e) {
			return generateErrorResponse(StringUtils.getStackTrace(e));
		}
	}
	
}
