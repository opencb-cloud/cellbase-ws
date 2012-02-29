package org.bioinfo.infrared.ws.server.rest.functgen.jtg.ws;

import java.io.File;
import java.io.IOException;
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
import org.bioinfo.formats.core.graph.dot.Dot;
import org.bioinfo.infrared.core.biopax.v3.Complex;
import org.bioinfo.infrared.core.biopax.v3.DataSource;
import org.bioinfo.infrared.core.biopax.v3.Interaction;
import org.bioinfo.infrared.core.biopax.v3.Pathway;
import org.bioinfo.infrared.core.biopax.v3.Protein;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;
import org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib.ComplexComponent;
import org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib.DotServer;

import com.google.gson.Gson;

@Path("/{version}/{datasource}/browser")
public class Browser extends BioPaxWSServer {


	private String dataSourceName = null;

	public Browser(@PathParam("version") String version, @PathParam("datasource") String dataSource, @Context UriInfo uriInfo) throws IOException, VersionException {
		super(version, dataSource, uriInfo);

		this.dataSourceName = dataSource;
	}

	@GET
	@Path("/complex/{complexid}")
	public Response getComplex(@PathParam("complexid") String id, @QueryParam("format") String format) {

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

		Complex complex = getComplex(id);

		if (complex!=null) {
			String filename = id.replace(" ", "_").replace("(", "").replace(")", "").replace("/", "_").replace(":", "_");

			DotServer dotServer = new DotServer();
			Dot dot = dotServer.generateDot(complex);

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
					response = Response.ok("An error occurred generating image for complex '" + id + "'", MediaType.valueOf("text/plain")).build();
				}
			} catch (Exception e) {
				response = Response.ok("An error occurred generating image for complex '" + id + "': " + e.getMessage(), MediaType.valueOf("text/plain")).build();
			}
		} else {
			response = Response.ok("Could not find complex '" + id + "'", MediaType.valueOf("text/plain")).build(); 
		}

		// cleaning
		super.clear();

		return response;
	}

	@GET
	@Produces("text/plain")
	@Path("/complex/{complexid}/components")
	public String getComplexComponents(@PathParam("complexid") int id, @QueryParam("contentformat") String contentFormat) {
		contentFormat = this.getFormat(contentFormat);

		StringBuilder sb = new StringBuilder();

		Complex complex = bpServer.getComplex(id);
		if (complex!=null) {
			List<ComplexComponent> components = bpServer.getComplexComponents(complex);

			if ("json".equalsIgnoreCase(contentFormat)) {
				sb.append(new Gson().toJson(components));
			} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
				sb.append("var response = (").append(new Gson().toJson(components)).append(");");
			} else {
				sb.append(ComplexComponent.getHeader()).append("\n");
				for(ComplexComponent cc: components) {
					sb.append(cc.toString()).append("\n");
				}
			}
		} else {
			sb.append("Complex " + id + " not found !!!");
		}

		// cleaning
		super.clear();

		return sb.toString();
	}

	@GET
	@Produces("text/plain")
	@Path("/complex/{complexid}/interactions")
	public String getComplexInteractions(@PathParam("complexid") int id, @QueryParam("contentformat") String contentFormat) {
		contentFormat = this.getFormat(contentFormat);

		StringBuilder sb = new StringBuilder();

		Complex complex = bpServer.getComplex(id);
		if (complex!=null) {
			List<Interaction> interactions = bpServer.getInteractions(complex);

			if ("json".equalsIgnoreCase(contentFormat)) {
				sb.append("[").append(bpServer.toJsonInteractions(interactions)).append("]");
			} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
				sb.append("var response = ([").append(bpServer.toJsonInteractions(interactions)).append("]);");
			} else {
				sb.append(bpServer.toStringInteractions(interactions));
			}
		} else {
			sb.append("Complex " + id + " not found !!!");
		}

		// cleaning
		super.clear();

		return sb.toString();
	}	

	@GET
	@Produces("text/plain")
	@Path("/complex/{complexid}/pathways")
	public String getComplexPathways(@PathParam("complexid") int id, @QueryParam("contentformat") String contentFormat) {
		contentFormat = this.getFormat(contentFormat);

		StringBuilder sb = new StringBuilder();

		Complex complex = bpServer.getComplex(id);
		if (complex!=null) {
			List<Pathway> pathways = bpServer.getPathways(complex);

			if ("json".equalsIgnoreCase(contentFormat)) {
				sb.append("[").append(bpServer.toJsonPathways(pathways)).append("]");
			} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
				sb.append("var response = ([").append(bpServer.toJsonPathways(pathways)).append("]);");
			} else {
				sb.append(bpServer.toStringPathways(pathways));
			}
		} else {
			sb.append("Complex " + id + " not found !!!");
		}

		// cleaning
		super.clear();

		return sb.toString();
	}	

	@GET
	@Produces("text/plain")
	@Path("/protein/{proteinid}/complexes")
	public String getProteinComplexes(@PathParam("proteinid") String ids, @QueryParam("xref") String xref, @QueryParam("contentformat") String contentFormat) {

		contentFormat = this.getFormat(contentFormat);

		StringBuilder sb = new StringBuilder();
		Protein protein = null;
		
		String proteinId;
		String[] proteinIds = ids.split(",");
		
		if ("json".equalsIgnoreCase(contentFormat)) {
			sb.append("[");
		} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
			sb.append("var response = ([");
		}
			
		for (int i=0 ; i<proteinIds.length ; i++) {
			proteinId = proteinIds[i];
			try {
				if (xref==null) {
					protein = bpServer.getProtein(Integer.parseInt(proteinId));
				} else {
					System.out.println("-----------> protein id = " + proteinId + ", data source name = " + dataSourceName);
					DataSource ds = bpServer.getDataSource(dataSourceName); 
					protein = bpServer.getProteinByXrefId(proteinId, ds);
				}
			} catch (Exception e) {
				e.printStackTrace();
				protein = null;
			}
			System.out.println("Browser:getProteinComplexes, is protein null ? " +  (protein==null));
			if (protein!=null) {
				List<Complex> complexes = bpServer.getComplexes(protein);

				if ("json".equalsIgnoreCase(contentFormat) || "jsonp".equalsIgnoreCase(contentFormat)) {
					if (i>0) { sb.append(","); }
					sb.append("{\"proteinId\": \"").append(proteinId).append("\", \"id\": ").append(protein.getPkProtein()).append(", \"complexes\": ").append(bpServer.toJsonComplexes(complexes)).append("}");
				} else {
					if (i>0) { sb.append("\n"); }
					sb.append(proteinId).append("\t").append(bpServer.toStringComplexes(complexes));
				}
			} else {
				System.out.println("Protein " + proteinId + " not found !!!");
			}
		}

		if ("json".equalsIgnoreCase(contentFormat)) {
			sb.append("]");
		} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
			sb.append("]);");
		}

		// cleaning
		super.clear();

//		contentFormat = this.getFormat(contentFormat);
//
//		StringBuilder sb = new StringBuilder();
//
//		Protein protein = bpServer.getProtein(id);
//		if (protein!=null) {
//			List<Complex> complexes = bpServer.getComplexes(protein);
//
//			if ("json".equalsIgnoreCase(contentFormat)) {
//				sb.append("[").append(bpServer.toJsonComplexes(complexes)).append("]");
//			} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
//				sb.append("var response = ([").append(bpServer.toJsonComplexes(complexes)).append("]);");
//			} else {
//				sb.append(bpServer.toStringComplexes(complexes));
//			}
//		} else {
//			sb.append("Protein " + id + " not found !!!");
//		}
//
//		// cleaning
//		super.clear();

		return sb.toString();
	}

	@GET
	@Produces("text/plain")
	@Path("/protein/{proteinid}/interactions")
	public String getProteinInteractions(@PathParam("proteinid") int id, @QueryParam("contentformat") String contentFormat) {
		contentFormat = this.getFormat(contentFormat);

		StringBuilder sb = new StringBuilder();

		Protein protein = bpServer.getProtein(id);
		if (protein!=null) {
			List<Interaction> interactions = bpServer.getInteractions(protein);

			if ("json".equalsIgnoreCase(contentFormat)) {
				sb.append("[").append(bpServer.toJsonInteractions(interactions)).append("]");
			} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
				sb.append("var response = ([").append(bpServer.toJsonInteractions(interactions)).append("]);");
			} else {
				sb.append(bpServer.toStringInteractions(interactions));
			}
		} else {
			sb.append("Protein " + id + " not found !!!");
		}

		// cleaning
		super.clear();

		return sb.toString();
	}	

	@GET
	@Produces("text/plain")
	@Path("/protein/{proteinid}/pathways")
	public String getProteinPathways(@PathParam("proteinid") String ids, @QueryParam("xref") String xref, @QueryParam("contentformat") String contentFormat) {

		contentFormat = this.getFormat(contentFormat);

		StringBuilder sb = new StringBuilder();
		Protein protein = null;
		
		String proteinId;
		String[] proteinIds = ids.split(",");
		
		if ("json".equalsIgnoreCase(contentFormat)) {
			sb.append("[");
		} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
			sb.append("var response = ([");
		}
			
		for (int i=0 ; i<proteinIds.length ; i++) {
			proteinId = proteinIds[i];
			try {
				if (xref==null) {
					protein = bpServer.getProtein(Integer.parseInt(proteinId));
				} else {
					System.out.println("-----------> protein id = " + proteinId + ", data source name = " + dataSourceName);
					DataSource ds = bpServer.getDataSource(dataSourceName); 
					protein = bpServer.getProteinByXrefId(proteinId, ds);
				}
			} catch (Exception e) {
				e.printStackTrace();
				protein = null;
			}
			System.out.println("Browser:getProteinPathways, is protein null ? " +  (protein==null));
			if (protein!=null) {
				List<Pathway> pathways = bpServer.getPathways(protein);

				if ("json".equalsIgnoreCase(contentFormat) || "jsonp".equalsIgnoreCase(contentFormat)) {
					if (i>0) { sb.append(","); }
					sb.append("{\"proteinId\": \"").append(proteinId).append("\", \"id\": ").append(protein.getPkProtein()).append(", \"pathways\": ").append(bpServer.toJsonPathways(pathways)).append("}");
				} else {
					if (i>0) { sb.append("\n"); }
					sb.append(proteinId).append("\t").append(bpServer.toStringPathways(pathways));
				}
			} else {
				System.out.println("Protein " + proteinId + " not found !!!");
			}
		}

		if ("json".equalsIgnoreCase(contentFormat)) {
			sb.append("]");
		} else if ("jsonp".equalsIgnoreCase(contentFormat)) { 
			sb.append("]);");
		}

		// cleaning
		super.clear();

		return sb.toString();
	}	

	//-------------------------------------------------------------------

	private Complex getComplex(String id) {
		Complex complex = null;

		try {
			int complexId = Integer.parseInt(id);
			complex = bpServer.getComplex(complexId);
		} catch (Exception e) {
			complex = null;
		}

		return complex;
	}

}
