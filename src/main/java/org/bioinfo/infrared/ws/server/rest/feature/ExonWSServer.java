package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.bioinfo.infrared.core.cellbase.Exon;
import org.bioinfo.infrared.lib.api.ExonDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/feature/exon")
@Produces("text/plain")
public class ExonWSServer extends GenericRestWSServer {
	
	private ExonDBAdaptor exonDBAdaptor;
	
	public ExonWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}
	
	
	@GET
	@Path("/{exonId}/info")
	public Response getByEnsemblId(@PathParam("exonId") String query) {
		ExonDBAdaptor adaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
		try {
			return  generateResponse(query,adaptor.getAllByEnsemblIdList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	
	@GET
	@Path("/{snpId}/bysnp")
	public Response getAllBySnpIdList(@PathParam("snpId") String query) {
		try {
			exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
			return generateResponse(query, Arrays.asList(exonDBAdaptor.getAllBySnpIdList(StringUtils.toList(query, ","))));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{exonId}/aminos")
	public Response getAminoByExon(@PathParam("exonId") String query) {
		try{
		ExonDBAdaptor adaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
		List<Exon> exons = adaptor.getAllByEnsemblIdList(StringUtils.toList(query, ","));
		
		List<String> sequence = new ArrayList<String>();
			for (Exon exon : exons) {
				if(exon.getStrand().equals("-1")){
					sequence = adaptor.getAllSequencesByIdList(StringUtils.toList(query, ","), -1);
				}
				else{
					sequence = adaptor.getAllSequencesByIdList(StringUtils.toList(query, ","), 1);
				}
			}
			return generateResponse(query, sequence);
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
//	@GET
//	@Path("/{exonId}/sequence")
//	public Response getSequencesByIdList(@DefaultValue("1")@QueryParam("strand")String strand, @PathParam("exonId") String query) {
//		try {
//			if(strand.equals("-1")){
//				return generateResponse(query, Arrays.asList(this.getExonDBAdaptor().getAllSequencesByIdList(StringUtils.toList(query, ","), -1)));
//			}
//			else{
//				return generateResponse(query, Arrays.asList(this.getExonDBAdaptor().getAllSequencesByIdList(StringUtils.toList(query, ","))));
//				
//			}
//		} catch (IOException e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//		}
//	}
	@GET
	@Path("/{exonId}/sequence")
	public Response getSequencesByIdList(@PathParam("exonId") String query) {
		try {
			exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
			return generateResponse(query, Arrays.asList(exonDBAdaptor.getAllSequencesByIdList(StringUtils.toList(query, ","))));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{exonId}/region")
	public Response getRegionsByIdList(@PathParam("exonId") String query) {
		try {
			exonDBAdaptor = dbAdaptorFactory.getExonDBAdaptor(this.species);
			return generateResponse(query, Arrays.asList(exonDBAdaptor.getAllRegionsByIdList(StringUtils.toList(query, ","))));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/{exonId}/transcript")
	public Response getTranscriptsByEnsemblId(@PathParam("exonId") String query) {
		TranscriptDBAdaptor adaptor = dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
		try {
			return generateResponse(query, adaptor.getAllByEnsemblExonId(StringUtils.toList(query, ",")));
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	


}
