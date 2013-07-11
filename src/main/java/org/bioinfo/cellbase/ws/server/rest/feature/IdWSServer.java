package org.bioinfo.cellbase.ws.server.rest.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.cellbase.lib.api.GeneDBAdaptor;
import org.bioinfo.cellbase.lib.api.SnpDBAdaptor;
import org.bioinfo.cellbase.lib.api.VariationDBAdaptor;
import org.bioinfo.cellbase.lib.api.XRefsDBAdaptor;
import org.bioinfo.cellbase.lib.common.core.Xref;
import org.bioinfo.cellbase.lib.impl.dbquery.QueryOptions;
import org.bioinfo.cellbase.ws.server.rest.GenericRestWSServer;
import org.bioinfo.cellbase.ws.server.rest.exception.VersionException;
import org.bioinfo.commons.utils.StringUtils;

@Path("/{version}/{species}/feature/id")
@Produces("text/plain")
public class IdWSServer extends GenericRestWSServer {

    private List<String> exclude = new ArrayList<>();

	public IdWSServer(@PathParam("version") String version, @PathParam("species") String species,
                      @DefaultValue("") @QueryParam("exclude") String exclude,
                      @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws VersionException, IOException {
		super(version, species, uriInfo, hsr);
        this.exclude = Arrays.asList(exclude.trim().split(","));
	}
	
	@GET
	@Path("/{id}/xref")
	public Response getByFeatureId(@PathParam("id") String query, @DefaultValue("") @QueryParam("dbname") String dbName) {
		try{
			checkVersionAndSpecies();
			XRefsDBAdaptor x = dbAdaptorFactory.getXRefDBAdaptor(this.species, this.version);
			if(dbName.equals("")){
				return generateResponse(query, "XREF",  x.getAllByDBNameList(StringUtils.toList(query, ","), null));
			}else {
				return generateResponse(query, "XREF", x.getAllByDBNameList(StringUtils.toList(query, ","), (StringUtils.toList(dbName, ","))));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}
	
	@GET
	@Path("/{id}/gene")
	public Response getGeneByEnsemblId(@PathParam("id") String query) {
		try{
			checkVersionAndSpecies();
			GeneDBAdaptor geneDBAdaptor = dbAdaptorFactory.getGeneDBAdaptor(this.species, this.version);
			
			QueryOptions queryOptions = new QueryOptions("exclude", exclude);
			
			return createJsonResponse(geneDBAdaptor.getAllByIdList(StringUtils.toList(query, ","), queryOptions));
			
//			return generateResponse(query, "GENE",  x.getAllByNameList(StringUtils.toList(query, ","),exclude));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}
	
	@GET
	@Path("/{id}/snp")
	public Response getSnpByFeatureId(@PathParam("id") String query) {
		try{
			checkVersionAndSpecies();
			VariationDBAdaptor variationDBAdaptor = dbAdaptorFactory.getVariationDBAdaptor(this.species, this.version);
            return generateResponse(query, "SNP",  variationDBAdaptor.getByIdList(StringUtils.toList(query, ","),exclude));
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}
	
	@GET
	@Path("/{id}/starts_with")
	public Response getByLikeQuery(@PathParam("id") String query) {
		try{
			checkVersionAndSpecies();
			XRefsDBAdaptor x = dbAdaptorFactory.getXRefDBAdaptor(this.species, this.version);
			List<List<Xref>> xrefs = x.getByStartsWithQueryList(StringUtils.toList(query, ","));
			if(query.startsWith("rs") || query.startsWith("AFFY_") || query.startsWith("SNP_") || query.startsWith("VAR_") || query.startsWith("CRTAP_") || query.startsWith("FKBP10_") || query.startsWith("LEPRE1_") || query.startsWith("PPIB_")) {
				List<List<Xref>> snpXrefs = x.getByStartsWithSnpQueryList(StringUtils.toList(query, ","));
				for(List<Xref> xrefList: snpXrefs) {
					xrefs.get(0).addAll(xrefList);
				}
			}
			return generateResponse(query, "XREF", xrefs);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}
	
	@GET
	@Path("/{id}/contains")
	public Response getByContainsQuery(@PathParam("id") String query) {
		try{
			checkVersionAndSpecies();
			XRefsDBAdaptor x = dbAdaptorFactory.getXRefDBAdaptor(this.species, this.version);
			List<List<Xref>> xrefs = x.getByContainsQueryList(StringUtils.toList(query, ","));
			if(query.startsWith("rs") || query.startsWith("AFFY_") || query.startsWith("SNP_") || query.startsWith("VAR_") || query.startsWith("CRTAP_") || query.startsWith("FKBP10_") || query.startsWith("LEPRE1_") || query.startsWith("PPIB_")) {
				List<List<Xref>> snpXrefs = x.getByStartsWithSnpQueryList(StringUtils.toList(query, ","));
				for(List<Xref> xrefList: snpXrefs) {
					xrefs.get(0).addAll(xrefList);
				}
			}
			return generateResponse(query, xrefs);
		} catch (Exception e) {
			e.printStackTrace();
			return createErrorResponse("getByEnsemblId", e.toString());
		}
	}
	
	@GET
	public Response getHelp() {
		return help();
	}
	@GET
	@Path("/help")
	public Response help() {
		return createOkResponse("Usage:");
	}

}
