package org.bioinfo.infrared.ws.server.rest.feature;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.GeneDBManager;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Gene;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;

public class TranscriptWSServer extends FeatureWSServer implements IFeature {

	public TranscriptWSServer(String version, String species, UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}

	@GET
	@Path("/{transcriptId}/gen")
	// GeneDBManager: public Gene getByEnsemblTranscript(String ensemblId)
	public Response getByEnsemblTranscript(@PathParam("transcriptId") String transcriptId) {
		try {
			GeneDBManager geneDBManager = new GeneDBManager(infraredDBConnector);
			List<String> ids = StringUtils.toList(transcriptId, ",");
			FeatureList<Gene> geneList = geneDBManager.getByEnsemblTranscriptId(ids);
			return generateResponseFromFeatureList(geneList, new TypeToken<FeatureList<Gene>>() {}.getType());
		} catch (Exception e) {
			return generateErrorResponse(e.toString());
		}
	}
	
	@Override
	public boolean isValidSpecies() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String sequence(String feature) {
		// TODO Auto-generated method stub
		return null;
	}

}
