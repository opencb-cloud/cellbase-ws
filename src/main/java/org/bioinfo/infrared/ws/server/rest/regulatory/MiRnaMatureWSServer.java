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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.cellbase.Gene;
import org.bioinfo.infrared.core.cellbase.MirnaDisease;
import org.bioinfo.infrared.core.cellbase.MirnaGene;
import org.bioinfo.infrared.core.cellbase.MirnaMature;
import org.bioinfo.infrared.core.cellbase.Transcript;
import org.bioinfo.infrared.lib.api.GeneDBAdaptor;
import org.bioinfo.infrared.lib.api.MirnaDBAdaptor;
import org.bioinfo.infrared.lib.api.TranscriptDBAdaptor;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;


@Path("/{version}/{species}/regulatory/mirna_mature")
@Produces("text/plain")
public class MiRnaMatureWSServer extends RegulatoryWSServer {

	public MiRnaMatureWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	private GeneDBAdaptor getGeneDBAdaptor(){
		return dbAdaptorFactory.getGeneDBAdaptor(this.species);
	}
	private MirnaDBAdaptor getMirnaDBAdaptor(){
		return dbAdaptorFactory.getMirnaDBAdaptor(this.species);
	}
	private TranscriptDBAdaptor getTranscriptDBAdaptor(){
		return dbAdaptorFactory.getTranscriptDBAdaptor(this.species);
	}
	
	@GET
	@Path("/{mirnaId}/info")
	public Response getMiRnaMatureInfo(@PathParam("mirnaId") String query) {
		try {
			// miRnaGene y Ensembl Genes + Transcripts
			// mirnaDiseases
			// mirnaTargets
			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			logger.debug("En getMiRnaMatureInfo: "+query);
			return generateResponse(query, mirnaDBAdaptor.getAllMiRnaMaturesByNameList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
		
	@GET
	@Path("/{mirnaId}/fullinfo")
	public Response getMiRnaMatureFullInfo(@PathParam("mirnaId") String query) {
		try {
			
			
			List<List<MirnaMature>> mirnaMature = getMirnaDBAdaptor().getAllMiRnaMaturesByNameList(StringUtils.toList(query, ","));
			List<List<MirnaGene>> mirnaGenes = getMirnaDBAdaptor().getAllMiRnaGenesByMiRnaMatureList(StringUtils.toList(query, ","));
			
			List<List<Gene>> genes = getGeneDBAdaptor().getAllByMiRnaMatureList(StringUtils.toList(query, ","));
			List<List<Transcript>> transcripts = getTranscriptDBAdaptor().getAllByMirnaMatureList(StringUtils.toList(query, ","));
			
			List<List<Gene>> targetGenes = getGeneDBAdaptor().getAllTargetsByMiRnaMatureList(StringUtils.toList(query, ","));
			List<List<MirnaDisease>> mirnaDiseases = getMirnaDBAdaptor().getAllMiRnaDiseasesByMiRnaMatureList(StringUtils.toList(query, ""));
			
			StringBuilder response = new StringBuilder();
			response.append("[");
			for (int i = 0; i < genes.size(); i++) {
				response.append("{");
				response.append("\"mirna\":{");
				response.append("\"mirnaMature\":"+gson.toJson(mirnaMature.get(i))+",");
				response.append("\"mirnaGenes\":"+gson.toJson(mirnaGenes.get(i))+"");
				response.append("},");
				response.append("\"genes\":"+gson.toJson(genes.get(i))+",");
				response.append("\"transcripts\":"+gson.toJson(transcripts.get(i))+",");
				response.append("\"targetGenes\":"+gson.toJson(targetGenes.get(i))+",");
				response.append("\"mirnaDiseases\":"+gson.toJson(mirnaDiseases.get(i))+"");
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
	@Path("/{mirnaId}/gene")
	public Response getEnsemblGene(@PathParam("mirnaId") String query) {
		try {
			GeneDBAdaptor adaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllByMiRnaMatureList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{mirnaId}/mirna_gene")
	public Response getMiRnaGene(@PathParam("mirnaId") String query) {
		try {
			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			return generateResponse(query, mirnaDBAdaptor.getAllMiRnaGenesByMiRnaMatureList(StringUtils.toList(query, ",")));
//			return  generateResponse(query, mirnaDBAdaptor.getAllByMiRnaList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/{mirnaId}/target_gene")
	public Response getEnsemblTargetGenes(@PathParam("mirnaId") String query) {
		try {
			GeneDBAdaptor adaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllTargetsByMiRnaMatureList(StringUtils.toList(query, ","))); // Renombrar a getAllTargetGenesByMiRnaList
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{mirnaId}/target")
	public Response getMirnaTargets(@PathParam("mirnaId") String query) {
		try {
			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			return  generateResponse(query, adaptor.getAllMiRnaTargetsByMiRnaMatureList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/{mirnaId}/disease")
	public Response getMinaDisease(@PathParam("mirnaId") String query) {
		try {
			MirnaDBAdaptor mirnaDBAdaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);
			return  generateResponse(query, mirnaDBAdaptor.getAllMiRnaDiseasesByMiRnaMatureList(StringUtils.toList(query, ",")));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/annotation")
	public Response getAnnotation(@DefaultValue("") @QueryParam("source") String source) {
		try {
			MirnaDBAdaptor adaptor = dbAdaptorFactory.getMirnaDBAdaptor(this.species);

			List<?> results;
			if(source.equals("")){
				results = adaptor.getAllAnnotation();
			}else{
				results = adaptor.getAllAnnotationBySourceList(StringUtils.toList(source, ","));
			}

			List<String> lista = new ArrayList<String>();
			if(results != null && results.size() > 0) {
				for(Object result : results) {
					lista.add(((Object [])result)[0].toString()+"\t" + ((Object [])result)[1].toString());
				}	
			}

			return generateResponse(new String(), lista);
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
