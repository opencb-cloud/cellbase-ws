package org.bioinfo.infrared.ws.server.rest.network;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.biopax.v3.BioEntity;
import org.bioinfo.infrared.core.biopax.v3.Interaction;
import org.bioinfo.infrared.core.biopax.v3.NameEntity;
import org.bioinfo.infrared.core.biopax.v3.Pathway;
import org.bioinfo.infrared.lib.api.BioPaxDBAdaptor;
import org.bioinfo.infrared.lib.api.TfbsDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{version}/{species}/network/pathway")
@Produces("text/plain")
public class PathwayWSServer extends GenericRestWSServer {

	public PathwayWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/list")
	public Response getAllPathways(@QueryParam("subpathways") String subpathways, @QueryParam("search") String search) {
		try {
			boolean onlyTopLevel = true;
			if (subpathways!=null) {
				onlyTopLevel=!Boolean.parseBoolean(subpathways);
			}
			
			StringBuilder sb = new StringBuilder();
			BioPaxDBAdaptor dbAdaptor = dbAdaptorFactory.getBioPaxDBAdaptor(this.species);
			List<Pathway> pathways = dbAdaptor.getPathways("Reactome", search, onlyTopLevel);
			return generateResponse("", pathways);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/annotation")
	public Response getPathwayAnnotation() {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return null;
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{pathwayId}/info")
	public Response getPathwayInfo(@PathParam("pathwayId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{pathwayId}/element")
	public Response getAllElements(@PathParam("pathwayId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}


	@GET
	@Path("/{pathwayId}/gene")
	public Response getAllGenes
	(@PathParam("pathwayId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{pathwayId}/protein")
	public Response getAllByTfbs(@PathParam("pathwayId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private String getJsonPathway(Pathway pw) {
		StringBuilder sb = new StringBuilder();

		sb.append("{\"type\": \"pathway\",");
		sb.append("\"id\": ").append(pw.getPkPathway()).append(",");
		sb.append("\"name\": \"").append(getFirstName(pw.getBioEntity())).append("\",");
		sb.append("\"description\": \"");
		if (pw.getBioEntity().getComment()!=null) {
			sb.append(pw.getBioEntity().getComment().replace("\"", "'").replace("\n", "").replace("\r", "").replace("\n", ""));
		}
		sb.append("\",");
		sb.append("\"components\": [");
		if (pw.getPathwaiesForPathwayComponent()!=null) {
			int c=0;
			Iterator it = pw.getPathwaiesForPathwayComponent().iterator();
			while (it.hasNext()) {
				if (c!=0) {
					sb.append(",");
				}
				sb.append(getJsonPathway((Pathway) it.next()));
				c++;
			}
			it = pw.getInteractions().iterator();
			Interaction interaction = null;
			while (it.hasNext()) {
				if (c!=0) {
					sb.append(",");
				}
				interaction = (Interaction) it.next();
				sb.append("{\"type\": \"interaction\",");
				sb.append("\"id\": ").append(interaction.getPkInteraction()).append(",");
				sb.append("\"name\": \"").append(getFirstName(interaction.getBioEntity())).append("\",");
				sb.append("\"description\": \"");
				if (interaction.getBioEntity().getComment()!=null) {
					sb.append(interaction.getBioEntity().getComment().replace("\"", "'").replace("\n", "").replace("\r", "").replace("\n", ""));
				}
				sb.append("\"}");
				c++;
			}
		}
		sb.append("]}");
		
		return sb.toString();
	}
	
	public String getFirstName(BioEntity entity) {
		String name = "NO-NAME";
		try {
			String aux = "";
			Iterator it = entity.getNameEntities().iterator();
			NameEntity ne = null;
			while (it.hasNext()) {
				ne = (NameEntity) it.next();
				if (name.equalsIgnoreCase("NO-NAME") || ne.getNameEntity().length()<name.length()) {
					name = ne.getNameEntity();
				}
			}
			name = name.replace("\"", "'");
		} catch (Exception e) {
			name = "NO-NAME";
		}
		return name;
	}

}
