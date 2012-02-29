package org.bioinfo.infrared.ws.server.rest.functgen.jtg.ws;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.io.utils.IOUtils;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.bioinfo.infrared.ws.server.rest.network.DotServer;

import com.google.gson.Gson;


@Path("/{version}/{datasource}")
public class PathwayServer extends BioPaxWSServer {

	private String dataSourceName = null;

	public PathwayServer(@PathParam("version") String version, @PathParam("datasource") String dataSource, @Context UriInfo uriInfo) throws IOException, VersionException {
		super(version, dataSource, uriInfo);

		this.dataSourceName = dataSource;
	}

	@GET
	@Produces("text/plain")
	@Path("/pathways")
	public String getPathways(@QueryParam("subpathways") String subpathways, @QueryParam("search") String search, @QueryParam("contentformat") String contentFormat) {


		boolean onlyTopLevel = true;
		if (subpathways!=null) {
			onlyTopLevel=!Boolean.parseBoolean(subpathways);
		}

		if (contentFormat==null) {
			contentFormat = "text";
		} else if ("json".equalsIgnoreCase(contentFormat)) {
			contentFormat = "json";
		} else if ("jsonp".equalsIgnoreCase(contentFormat)) {
			contentFormat = "jsonp";
		} else {
			contentFormat = "text";
		}

		StringBuilder sb = new StringBuilder();

		BioPaxDBAdaptor dbAdaptor = dbAdaptorFactory.getBioPaxDBAdaptor(this.species);
		
		List<Pathway> pathways = dbAdaptor.getPathways(dataSourceName, search, onlyTopLevel);
//		List<Pathway> pathways = bpServer.getPathways(dataSourceName, search, onlyTopLevel);
		if (pathways!=null) {
			if ("json".equalsIgnoreCase(contentFormat) || "jsonp".equalsIgnoreCase(contentFormat)) {
				if ("jsonp".equalsIgnoreCase(contentFormat)) {
					sb.append("var response = (");
				}
				if (onlyTopLevel) {
					sb.append("{\"pathways\": [");
					Pathway pw = null;
					for(int i=0; i<pathways.size() ; i++) {
						pw = pathways.get(i);
						if (i!=0) {
							sb.append(",");
						}
						sb.append(getJsonPathway(pw));
					}
					sb.append("]}");
				} else {
					sb.append("{\"pathways\": [");
					Pathway pw = null;
					for(int i=0; i<pathways.size() ; i++) {
						pw = pathways.get(i);
						if (i!=0) {
							sb.append(",");
						}
						sb.append("{\"id\": ").append(pw.getPkPathway()).append(", \"name\": \"").append(bpServer.getFirstName(pw.getBioEntity())).append("\", \"description\": \"");
						if (pw.getBioEntity().getComment()!=null) {
							sb.append(pw.getBioEntity().getComment().replace("\"", "'").replace("\n", "").replace("\r", "").replace("\n", ""));
						}
						sb.append("\"}");
					}
					sb.append("]}");
				}
				if ("jsonp".equalsIgnoreCase(contentFormat)) {
					sb.append(");");
				}
			} else {
				sb.append("#id").append("\t").append("name").append("\t").append("description").append("\n");

				for(Pathway pw: pathways) {
					sb.append(pw.getPkPathway()).append("\t").append(bpServer.getFirstName(pw.getBioEntity())).append("\t").append(pw.getBioEntity().getComment()).append("\n");
				}
			}
		} else {
			sb.append("Could not find any pathway"); 			
		}


		// cleaning
		super.clear();

		return sb.toString();
	}

	@GET
	@Path("/pathway/{pathwayid}")
	public Response getPathway(@PathParam("pathwayid") String id, @QueryParam("format") String format) {

		Response response = null;

		String contentType = "image/jpeg";
		String outFormat = "jpg";
		if (format!=null) {
			if ("png".equalsIgnoreCase(format)) {
				outFormat = "png";
				contentType = "image/png";
			} else if ("dot".equalsIgnoreCase(format)) {
				outFormat = "dot";
				contentType = "text/plain";
			} else if ("dotp".equalsIgnoreCase(format)) {
				outFormat = "dotp";
				contentType = "text/plain";
			} else {
				outFormat = "jpg";
				contentType = "image/jpeg";
			}
		}

		Pathway pathway = getPathway(id);

		if (pathway!=null) {
			String filename = id.replace(" ", "_").replace("(", "").replace(")", "").replace("/", "_").replace(":", "_");

			DotServer dotServer = new DotServer();
			Dot dot = dotServer.generateDot(pathway);

			try {

				File dotFile = new File("/tmp/" + filename + ".in");
				File imgFile = new File("/tmp/" + filename + "." + outFormat);

				dot.save(dotFile);
				String cmd;
				if ("dot".equalsIgnoreCase(outFormat) || "dotp".equalsIgnoreCase(outFormat)) {
					cmd = "dot " + dotFile.getAbsolutePath() + " -o " + imgFile.getAbsolutePath();
				} else {
					cmd = "dot -T" + outFormat + " " + dotFile.getAbsolutePath() + " -o " + imgFile.getAbsolutePath();
				}
				System.out.println("-----------------------> cmd = " + cmd);
				Runtime.getRuntime().exec(cmd);
				Thread.sleep(2000);
				if (imgFile.exists()) {
					System.out.println("-----------------------> image exists !!!");			
					if ("dotp".equalsIgnoreCase(outFormat)) {
						String out = "var response = (" +  new Gson().toJson(IOUtils.readLines(imgFile)) + ")";
						response = Response.ok(out).build();
					} else {
						response = Response.ok(imgFile, contentType).build();
					}					
				} else {
					System.out.println("-----------------------> image DO NOT exist !!!");
					response = Response.ok("An error occurred generating image for pathway '" + id + "'", MediaType.valueOf("text/plain")).build();
				}
			} catch (Exception e) {
				response = Response.ok("An error occurred generating image for pathway '" + id + "': " + e.getMessage(), MediaType.valueOf("text/plain")).build();
			}
		} else {
			response = Response.ok("Could not find pathway '" + id + "'", MediaType.valueOf("text/plain")).build(); 
		}

		// cleaning
		super.clear();

		return response;
	}

	@GET
	@Produces("text/plain")
	@Path("/pathway/{pathwayid}/info")
	public String getPathwayInfo(@PathParam("pathwayid") String id, @QueryParam("contentformat") String contentFormat) {


		boolean onlyTopLevel = true;

		if (contentFormat==null) {
			contentFormat = "text";
		} else if ("json".equalsIgnoreCase(contentFormat)) {
			contentFormat = "json";
		} else if ("jsonp".equalsIgnoreCase(contentFormat)) {
			contentFormat = "jsonp";
		} else {
			contentFormat = "text";
		}

		StringBuilder sb = new StringBuilder();

		Pathway pathway = getPathway(id);
		if (pathway!=null) {
			if ("json".equalsIgnoreCase(contentFormat) || "jsonp".equalsIgnoreCase(contentFormat)) {
				if ("jsonp".equalsIgnoreCase(contentFormat)) {
					sb.append("var response = (");
				}
				sb.append(getJsonPathway(pathway));
				if ("jsonp".equalsIgnoreCase(contentFormat)) {
					sb.append(");");
				}
			} else {
				sb.append("#id").append("\t").append("name").append("\t").append("description").append("\n");
				sb.append(pathway.getPkPathway()).append("\t").append(bpServer.getFirstName(pathway.getBioEntity())).append("\t").append(pathway.getBioEntity().getComment()).append("\n");
			}
		} else {
			sb.append("Could not find any pathway"); 			
		}


		// cleaning
		super.clear();

		return sb.toString();
	}

//	@GET
//	@Produces("text/plain")
//	@Path("/pathway/{pathwayid}/info")
//	public Response getPathwayInfo(@PathParam("pathwayid") String id, @QueryParam("format") String format) {
//
//		Response response = null;
//		StringBuilder sb = new StringBuilder();
//
//		Pathway pathway = getPathway(id);
//
//		if (pathway!=null) {
//
//			if (pathway.getBioEntity().getNameEntities()!=null) {
//				sb.append("\"name\": [");
//				Iterator it = pathway.getBioEntity().getNameEntities().iterator();
//				while (it.hasNext()) {
//					sb.append("\"").append(((NameEntity)it.next()).getNameEntity()).append("\",");
//				}
//				sb.append("],");
//			}
//
//			if (pathway.getBioEntity().getComment()!=null) {
//				sb.append("\"comment\": \"").append(pathway.getBioEntity().getComment()).append("\",");
//			}
//
//			if (pathway.getBioEntity().getXrefs()!=null) {
//				Iterator it = pathway.getBioEntity().getXrefs().iterator();
//				sb.append("\"xrefs\": [");
//				while(it.hasNext()) {
//					sb.append("{");
//					Xref xref = (Xref) it.next();
//					if (xref.getDb()!=null) {
//						sb.append("\"db\": \"").append(xref.getDb()).append("\",");
//					}
//					if (xref.getDbVersion()!=null) {
//						sb.append("\"dbversion\": \"").append(xref.getDbVersion()).append("\",");
//					}
//					if (xref.getId()!=null) {
//						sb.append("\"id\": \"").append(xref.getId()).append("\",");
//					}
//					if (xref.getIdVersion()!=null) {
//						sb.append("\"idversion\": \"").append(xref.getIdVersion()).append("\",");
//					}
//					if (xref.getComment()!=null) {
//						sb.append("\"comment\": \"").append(xref.getComment()).append("\",");
//					}
//					sb.append("}, ");
//				}
//				sb.append("],");
//			}
//
//			if (pathway.getBioSource()!=null) {
//				sb.append("\"organism\": {");
//				if (pathway.getBioSource().getName()!=null) {
//					sb.append("\"name\": \"").append(pathway.getBioSource().getName()).append("\",");
//				}
//				if (pathway.getBioSource().getUnificationXref()!=null) {
//					if (pathway.getBioSource().getUnificationXref().getXref()!=null) {
//						sb.append("\"xref\": {");
//						if (pathway.getBioSource().getUnificationXref().getXref().getDb()!=null) {
//							sb.append("\"db\": \"").append(pathway.getBioSource().getUnificationXref().getXref().getDb()).append("\",");
//						}
//						if (pathway.getBioSource().getUnificationXref().getXref().getDbVersion()!=null) {
//							sb.append("\"dbversion\": \"").append(pathway.getBioSource().getUnificationXref().getXref().getDbVersion()).append("\",");
//						}
//						if (pathway.getBioSource().getUnificationXref().getXref().getId()!=null) {
//							sb.append("\"id\": \"").append(pathway.getBioSource().getUnificationXref().getXref().getId()).append("\",");
//						}
//						if (pathway.getBioSource().getUnificationXref().getXref().getIdVersion()!=null) {
//							sb.append("\"idversion\": \"").append(pathway.getBioSource().getUnificationXref().getXref().getIdVersion()).append("\",");
//						}
//						if (pathway.getBioSource().getUnificationXref().getXref().getComment()!=null) {
//							sb.append("\"comment\": \"").append(pathway.getBioSource().getUnificationXref().getXref().getComment()).append("\",");
//						}
//						sb.append("},");
//					}
//				}
//				sb.append("}");
//			}
//			response = Response.ok(sb.toString()).build();
//		} else {
//			response = Response.ok("Could not find pathway '" + id + "'").build();
//		}
//
//		// cleaning
//		super.clear();
//
//		return response;
//	}

	//-------------------------------------------------------------------

	private String getJsonPathway(Pathway pw) {
		StringBuilder sb = new StringBuilder();

		sb.append("{\"type\": \"pathway\",");
		sb.append("\"id\": ").append(pw.getPkPathway()).append(",");
		sb.append("\"name\": \"").append(bpServer.getFirstName(pw.getBioEntity())).append("\",");
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
				sb.append("\"name\": \"").append(bpServer.getFirstName(interaction.getBioEntity())).append("\",");
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

	//-------------------------------------------------------------------

	private Pathway getPathway(String id) {
		Pathway pathway = null;

		try {
			int pathwayId = Integer.parseInt(id);
			pathway = bpServer.getPathway(pathwayId);
		} catch (Exception e) {
			try {
				pathway = bpServer.getPathway(id, dataSourceName);
			} catch (Exception e1) {
				pathway = null;
			}
		}

		return pathway;
	}

}
