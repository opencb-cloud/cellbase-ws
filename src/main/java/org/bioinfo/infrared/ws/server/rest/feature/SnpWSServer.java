package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.lib.api.SnpDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/snp")
@Produces("text/plain")
public class SnpWSServer extends GenericRestWSServer {
	public SnpWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	@GET
	@Path("/{snpId}/info")
	public Response getByEnsemblId(@PathParam("snpId") String query) {
		
		try {
			
			SnpDBAdaptor adapter = dbAdaptorFactory.getSnpDBAdaptor(this.species);
			return  generateResponse(query, adapter.getByDbSnpIdList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	
//	@GET
//	@Path("/{snpId}/info")
//	public Response getBySnpName(@PathParam("snpId") String snpId) {
//		try {
//			List<String> identifiers = StringUtils.toList(snpId, ",");
//			Criteria criteria = this.getSession().createCriteria(Snp.class);
//			Disjunction disjunction = Restrictions.disjunction();
//			for (String id : identifiers) {
//				disjunction.add(Restrictions.eq("name", id.trim()));
//			}
//			criteria.add(disjunction);
//			return  generateResponse(criteria);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
	
	
//	@GET
//	@Path("/{snpId}/population")
//	public Response getPopulationBySnpName(@PathParam("snpId") String snpId) {
//		try {
//			Criteria criteria =  this.getSession().createCriteria(PopulationFrequency.class)
//			.createCriteria("snp").add( Restrictions.eq("name", snpId));
//			return generateResponse(criteria);
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}



}
