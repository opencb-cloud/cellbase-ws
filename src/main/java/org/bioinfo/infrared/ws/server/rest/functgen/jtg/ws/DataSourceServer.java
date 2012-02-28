package org.bioinfo.infrared.ws.server.rest.functgen.jtg.ws;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.bioinfo.biopax.model.DataSource;
import org.bioinfo.biopax.server.DataSourceStats;


@Path("/{version}/datasources")
public class DataSourceServer extends BioPaxWSServer {

	public DataSourceServer(@PathParam("version") String version, @Context UriInfo uriInfo) throws IOException {
		//System.out.println("********************* DataSourceServer");
		
		super(version, uriInfo);
		
	}

	@GET
	@Produces("text/plain")
	//@Path("/{datasources")
	public String getDataSources(@QueryParam("contentformat") String contentFormat) {
		StringBuilder sb = new StringBuilder();

		//System.out.println("********************* DataSourceServer:getDataSources");
		
		try {
			List<DataSource> dataSources = bpServer.getDataSources();
			
			if ("jsonp".equalsIgnoreCase(contentFormat)) {
				DataSource ds = null;
				DataSourceStats dsStats = null;
				sb.append("var response = ({\"datasources\": [");
				for(int i=0 ; i<dataSources.size() ; i++) {
					ds = dataSources.get(i);
					dsStats = bpServer.getDataSourceStats(ds);
					if (i!=0) {
						sb.append(",");
					}
					
					sb.append("{\"name\": \"").append(ds.getName()).append("\", \"filename\" : \"").append(ds.getFilename()).append("\", \"description\" : \"").append(ds.getDescription()).append("\",").append(dsStats.toJson()).append("}");
				}
				sb.append("]});");
			} else {
				sb.append("#name").append("\t").append("filename").append("\t").append("description").append("\n");

				for(DataSource ds: dataSources) {
					sb.append(ds.getName()).append("\t").append(ds.getFilename()).append("\t").append(ds.getDescription()).append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// cleaning
		super.clear();

		return sb.toString();
	}

	@GET
	@Produces("text/plain")
	@Path("/{datasource}/stats")
	public String getDataSourceStats(@PathParam("datasource") String dataSourceName, @QueryParam("contentformat") String contentFormat) {
		StringBuilder sb = new StringBuilder();

		//System.out.println("********************* DataSourceServer:getDataSourceStats: " + dataSourceName);
		
		try {
			DataSourceStats dsStats = bpServer.getDataSourceStats(dataSourceName);
			System.err.println(dsStats.toString());
			
			if ("jsonp".equalsIgnoreCase(contentFormat)) {
				sb.append("var response = ({").append(dsStats.toJson()).append("});");
			} else {
				sb.append(dsStats.toString());				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// cleaning
		super.clear();

		return sb.toString();
	}

}
