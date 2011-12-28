package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
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
import org.bioinfo.infrared.core.cellbase.Snp;
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
	
	private SnpDBAdaptor getSnpDBAdaptor(){
		return dbAdaptorFactory.getSnpDBAdaptor(this.species);
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

	
	
	
//	private int snpId;
//	private String name;
//	private String chromosome;
//	private int start;
//	private int end;
//	private String strand;
//	private int mapWeight;
//	private String alleleString;
//	private String ancestralAllele;
//	private String source;
//	private String displaySoConsequence;
//	private String soConsequenceType;
//	private String displayConsequence;
//	private String sequence;
	
	@GET
	@Path("/{snpId}/fullinfo")
	public Response getFullInfoById(@PathParam("snpId") String query) {
		try {
			List<List<Snp>> snpLists = getSnpDBAdaptor().getByDbSnpIdList(StringUtils.toList(query, ","));
			
			StringBuilder response = new StringBuilder();
			response.append("[");
			for(List<Snp> snps : snpLists){
				response.append("[");
				for (int i = 0; i < snps.size(); i++) {		
					response.append("{");
					response.append("\"name\":"+"\""+snps.get(i).getName()+"\",");
					response.append("\"chromosome\":"+"\""+snps.get(i).getChromosome()+"\",");
					response.append("\"start\":"+snps.get(i).getStart()+",");
					response.append("\"end\":"+snps.get(i).getEnd()+",");
					response.append("\"strand\":"+"\""+snps.get(i).getStrand()+"\",");
					response.append("\"mapWeight\":"+snps.get(i).getEnd()+",");
					response.append("\"alleleString\":"+"\""+snps.get(i).getAlleleString()+"\",");
					response.append("\"ancestralAllele\":"+"\""+snps.get(i).getAncestralAllele()+"\",");
					response.append("\"source\":"+"\""+snps.get(i).getSource()+"\",");
					response.append("\"displaySoConsequence\":"+"\""+snps.get(i).getDisplaySoConsequence()+"\",");
					response.append("\"soConsequenceType\":"+"\""+snps.get(i).getSoConsequenceType()+"\",");
					response.append("\"displayConsequence\":"+"\""+snps.get(i).getDisplayConsequence()+"\",");
					response.append("\"sequence\":"+"\""+snps.get(i).getSequence()+"\"");
					response.append("},");
				}
				response.append("],");
				response.replace(response.length()-3, response.length()-2, "");
			}
			response.append("]");
			
			//Remove the last comma
			response.replace(response.length()-2, response.length()-1, "");
			return  generateResponse(query,Arrays.asList(response));
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
