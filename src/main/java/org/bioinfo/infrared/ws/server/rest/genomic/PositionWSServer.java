package org.bioinfo.infrared.ws.server.rest.genomic;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.commons.utils.StringUtils;
import org.bioinfo.infrared.core.common.FeatureList;
import org.bioinfo.infrared.core.feature.Position;
import org.bioinfo.infrared.core.variation.AnnotatedMutation;
import org.bioinfo.infrared.core.variation.SNP;
import org.bioinfo.infrared.variation.AnnotatedMutationDBManager;
import org.bioinfo.infrared.variation.SNPDBManager;
import org.bioinfo.infrared.ws.server.rest.GenericRestWSServer;
import org.bioinfo.infrared.ws.server.rest.exception.VersionException;

import com.google.gson.reflect.TypeToken;


@Path("/{version}/{species}/genomic/position")
@Produces("text/plain")
public class PositionWSServer extends GenericRestWSServer {

	public PositionWSServer(@PathParam("version") String version, @PathParam("species") String species, @Context UriInfo uriInfo) throws VersionException, IOException {
		super(version, species, uriInfo);
	}

	@GET
	@Path("/help")
	public String help() {
		return "position help";
	}
	
	@Override
	protected boolean isValidSpecies(String species) {
		// TODO Auto-generated method stub
		return true;
	}

	
	@GET
	@Path("/{position}/snp")
	public Response getSnpByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			SNPDBManager snpDbManager = new SNPDBManager(infraredDBConnector);
			List<FeatureList<SNP>> snps = snpDbManager.getAllByPositions(positions);
			this.listType = new TypeToken<List<FeatureList<SNP>>>() {}.getType();
			return generateResponse2(snps, outputFormat, compress);
		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}
	
	@GET
	@Path("/{position}/mutation")
	public Response getMutationByPosition(@PathParam("position") String positionString) {
		try {
			List<Position> positions = Position.parsePositions(positionString);
			AnnotatedMutationDBManager annotMutationDbManager = new AnnotatedMutationDBManager(infraredDBConnector);
			List<FeatureList<AnnotatedMutation>> mutations = annotMutationDbManager.getAllByPositions(positions);
			this.listType = new TypeToken<List<FeatureList<AnnotatedMutation>>>() {}.getType();
			return generateResponse2(mutations, outputFormat, compress);
		} catch (Exception e) {
//			return generateErrorMessage(e.toString());
			return generateErrorMessage(StringUtils.getStackTrace(e));
		}
	}
	
}