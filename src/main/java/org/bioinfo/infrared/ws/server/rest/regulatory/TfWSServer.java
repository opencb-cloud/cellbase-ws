package org.bioinfo.infrared.ws.server.rest.regulatory;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.cellbase.Gene;
import org.bioinfo.infrared.core.cellbase.Protein;
import org.bioinfo.infrared.core.cellbase.ProteinFeature;
import org.bioinfo.infrared.core.cellbase.ProteinXref;
import org.bioinfo.infrared.core.cellbase.Pwm;
import org.bioinfo.infrared.core.cellbase.Transcript;
import org.bioinfo.infrared.lib.api.GeneDBAdaptor;
import org.bioinfo.infrared.lib.api.ProteinDBAdaptor;
import org.bioinfo.infrared.lib.api.TfbsDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/{version}/{species}/regulatory/tf")
@Produces("text/plain")
public class TfWSServer extends RegulatoryWSServer {

	
	public TfWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	private GeneDBAdaptor getGeneDBAdaptor(){
		return dbAdaptorFactory.getGeneDBAdaptor(this.species);
	}
	private ProteinDBAdaptor getProteinDBAdaptor(){
		return dbAdaptorFactory.getProteinDBAdaptor(this.species);
	}
	private TranscriptDBAdaptor getTranscriptDBAdaptor(){
		return dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
	}
	private TfbsDBAdaptor getTfbsDBAdaptor(){
		return dbAdaptorFactory.getTfbsDBAdaptor(this.species);
	}
	
	@GET
	@Path("/{tfId}/info") // Devuelve los TFBSs para el TFId que le das
	public Response getTfInfo(@PathParam("tfId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ",")));
			
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@GET
	@Path("/{tfId}/fullinfo") // Devuelve los TFBSs para el TFId que le das
	public Response getTfFullInfo(@PathParam("tfId") String query) {
		try {
			//!!!!!suponemos q viene uno solo
			
//			System.out.println("PAKO query: "+query);
			
			List<List<Gene>> genes = getGeneDBAdaptor().getAllByTfNameList(StringUtils.toList(query, ","));
			List<String> ensemblGeneList = new ArrayList<String>();
			List<String> externalNameList = new ArrayList<String>();
			for (List<Gene> g : genes) {
				if (g.size()>0){
					ensemblGeneList.add(g.get(0).getStableId());
					externalNameList.add(g.get(0).getExternalName());
				}
			}
			
			List<List<Protein>> proteinList = getProteinDBAdaptor().getAllByGeneNameList(externalNameList);
			List<List<Transcript>> transcriptList = getTranscriptDBAdaptor().getAllByProteinNameList(externalNameList);
			List<List<Gene>> targetGeneList = getGeneDBAdaptor().getAllByTfList(StringUtils.toList(query, ","));
			List<List<Pwm>> pwmGeneList =  getTfbsDBAdaptor().getAllPwmByTfGeneNameList(StringUtils.toList(query, ","));
			
			List<List<ProteinXref>> proteinXrefList = getProteinDBAdaptor().getAllProteinXrefsByProteinNameList(externalNameList);
			List<List<ProteinFeature>> proteinFeature = getProteinDBAdaptor().getAllProteinFeaturesByProteinXrefList(externalNameList);
			
			StringBuilder response = new StringBuilder();
			response.append("[");
			for (int i = 0; i < genes.size(); i++) {
				response.append("{");
				response.append("\"proteins\":"+gson.toJson(proteinList.get(i))+",");
				response.append("\"gene\":"+gson.toJson(genes.get(i).get(0))+",");
				response.append("\"transcripts\":"+gson.toJson(transcriptList.get(i))+",");
				response.append("\"pwm\":"+gson.toJson(pwmGeneList.get(i))+",");
				response.append("\"targetGenes\":"+gson.toJson(targetGeneList.get(i))+",");
				response.append("\"protein_xref\":"+gson.toJson(proteinXrefList.get(i))+",");
				response.append("\"protein_feature\":"+gson.toJson(proteinFeature.get(i))+"");
				response.append("},");
			}
			response.append("]");
			//Remove the last comma
			response.replace(response.length()-2, response.length()-1, "");
			
			return  generateResponse(query,Arrays.asList(response));
			
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{tfId}/tfbs")
	public Response getAllByTfbs(@PathParam("tfId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllByTfGeneNameList(StringUtils.toList(query, ",")));
			
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/{tfId}/gene")
	public Response getEnsemblGenes(@PathParam("tfId") String query) {
		try {
			GeneDBAdaptor adaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllByTfList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/{tfId}/pwm")
	public Response getAllPwms(@PathParam("tfId") String query) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			return generateResponse(query, adaptor.getAllPwmByTfGeneNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GET
	@Path("/annotation")
	public Response getAnnotation(@DefaultValue("")@QueryParam("celltype") String celltype) {
		try {
			TfbsDBAdaptor adaptor = dbAdaptorFactory.getTfbsDBAdaptor(this.species);
			List<Object> results;
			if (celltype.equals("")){
				results = adaptor.getAllAnnotation();
			}
			else{
				results = adaptor.getAllAnnotationByCellTypeList(StringUtils.toList(celltype, ","));
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
