package org.bioinfo.infrared.ws.server.rest.regulatory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.bioinfo.infrared.lib.api.GeneDBAdaptor;
import org.bioinfo.infrared.lib.api.TfbsDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.bioinfo.infrared.lib.api.MirnaDBAdaptor;

import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/{version}/{species}/regulatory/mirna")
@Produces("text/plain")
public class MicroRnaWSServer extends RegulatoryWSServer {

	public MicroRnaWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/{mirnaId}/gene")
	public Response getAllTfbs(@PathParam("mirnaId") String query) {
		try {
			GeneDBAdaptor adaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllByMiRna(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{mirnaId}/mature")
	public String getMatureMirna() {
		return null;
	}

	@GET
	@Path("/{mirnaId}/target")
	public Response getMirnaTarget(@PathParam("mirnaId") String query) {
		try {
			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllByMirbaseId(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{mirnaId}/disease")
	public Response getMinaDisease(@PathParam("mirnaId") String query) {
		try {
			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllDiseasesByMirnaIdList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/annotation")
	public Response getAnnotation(@DefaultValue("")@QueryParam("source")String source) {
		try {
			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			
			List results;
			if (source.equals("")){
				results = adaptor.getAnnotation();
			}
			else{
				results = adaptor.getAnnotation(StringUtils.toList(source, ","));
				
			}
			
			List lista = new ArrayList<String>();			
			for (Object result : results) {
				lista.add(((Object [])result)[0].toString()+"\t" + ((Object [])result)[1].toString());
			}
			
			return  generateResponse(new String(), lista);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
