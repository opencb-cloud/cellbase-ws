package org.bioinfo.infrared.ws.rest;



@Deprecated
public class RestPaths {
	
	/********************************************************
	 * VARIATION METHODS
	 ********************************************************/

//	@GET
//	@Path("/gettest/{longtext}")
//	public Response getTest(@PathParam("species") String species, @PathParam("longtext") String longText, @Context UriInfo ui) {
//		return new Variation(species, ui).getTest(longText);
//	}
//	
//	@POST
//	@Path("/gettest2/{longtext}")
//	public Response getTest2(@PathParam("species") String species, @PathParam("longtext") String longText, @Context UriInfo ui) {
//		return new Variation(species, ui).getTest(longText);
//	}
//	
////	@GET
////	@Path("/all/snps")
////	public Response getAllSnps(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getAllSnps();
////	}
//	
//	@GET
//	@Path("/{snp}/info")
//	public Response getSnpInfo(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/variation/{consequencetype}/snps")
//	public Response getSnpsByConsequenceType(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByConsequenceType();
//	}
//	@GET
//	@Path("/genomic/{region}/snps")
//	public Response getSnpByRegion(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByRegion();
//	}
//
//	
//	
//	
//	
//	
//	
//	
////	@GET
////	@Path("/snp/region/{chr}/{start}/{end}")
////	public Response getSnpInfo222(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
////	@GET
////	@Path("/snp/region/{chr}/{start}")
////	public Response getSnpInfo2222(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
////	@GET
////	@Path("/snp/region/{chr}")
////	public Response getSnpInfo22222(@PathParam("species") String species, @PathParam("chr") String chr, @PathParam("start") String start, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
////	@GET
////	@Path("/snp/region")
////	public Response getSnpInfo22232(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
//	//http://localhost:8080/infrared-ws/fetch/hsa/Variation/consequence/exonic,intronic/region/3:23423-2342324,5:12312-343463/population/ceu,asw
//	@GET
//	@Path("/Variation/name/{idlist}") // ?chromosme=...
//	public Response getSnpInfo22(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/SNP/region/{chr}") // ?chromosme=...
//	public Response getSnpInfo2qwe2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/CNV/region/{chr}") // ?chromosme=...
//	public Response getSnpInfo2qwegh2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/consequence/{cons}")
//	public Response getSnpInfo1242sdfsd232(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByConsequenceType();
//	}
//	@GET
//	@Path("/Variation/consequence/{cons}/region/{region}")
//	public Response getSnpInfo1242232(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByConsequenceType();
//	}
//	@GET
//	@Path("/Variation/consequence/{cons}/region/{region}/population/{pop}")
//	public Response getSnpInfo1242232rwewe(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByConsequenceType();
//	}
//	@GET
//	@Path("/Variation/population/{population}")
//	public Response getSnpInfo124223asdas2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/gene/{gene}")
//	public Response getSnpInfo2232(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/pathway/{path}")
//	public Response getSnpInfo223232(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/disease/{dis}")
//	public Response getSnpInfo2232asda32(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/omega/{02}")
//	public Response getSnpInfo232asda32(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Core/Gene/id/{gene}")
//	public Response getGeneInfo2232(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
////	@GET
////	@Path("/Core/trancript/{trans}")
////	public Response getGeneInfo232232(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
//	@GET
//	@Path("/Core/cytobands")
//	public Response getGeneInfo223(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Core/IdConverter/id/{bcl2brca2}/dbname/{ensembl_geneunigene}")
//	public Response getGeneInfo223asas(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/IdConverter/id/{bcl2brca2}/dbname/{ensembl_geneunigene}")
//	public Response getGeneInfo2232sdf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Annotation/id/{bcl2bcr2}/annotation/{gokegg}")
//	public Response getGeneInfo2weq2sdf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Annotation/id/{bcl2bcr2}/annotation/{gokegg}/keyword/{cancer}")
//	public Response getGeneInfo2wedf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Regulatory/Tfbs/id/{bcl2bcr2}/annotation/{gkegg}/keyword/{cancer}")
//	public Response getGeneInfo2wedasddf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Regulatory/Mirna/id/{bcl2bcr2}/annotation/{gokegg}/keyword/{cancer}")
//	public Response getGeneInfo2wedasddsdf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Compara/id/{bcl2bcr2}/orthologous/{mmurno}")
//	public Response getGeneInfo2wedaf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Compara/conservedregion/{3111111111}")
//	public Response getGeneInfwedaf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	/**********************************************************************************************
//	 **********************************************************************************************/
//	@GET
//	@Path("/Variation/{region}/consequencetype")
//	public Response getSnpInfo1242232rwwe(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByConsequenceType();
//	}
////	@GET
////	@Path("/Variation/name/{idlist}") // ?chromosme=...
////	public Response getSnpInfo52(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
//	@GET
//	@Path("/Variation/{chr}/snps") // ?chromosme=...
//	public Response getSngh2qwe2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Variation/{chr}/cnv") // ?chromosme=...
//	public Response getSnpInfohgegh2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Core/{gene}/info")
//	public Response getGeneInd232(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Core/chromosomes")
//	public Response getGeneInd23(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Core/IdConverter/{id1id2id3}/{ensembl_geneunigene}")
//	public Response getGens23asas(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/IdConverter/dbname")
//	public Response getGeneInfo223qwgeq2sdf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Annotation/{gene1gene2}/{gokegg}/annotation/")
//	public Response getGeneInfogq2sdf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Annotation/{gene1gene2}/{gokegg}/{keyword}/annotation")
//	public Response getGendf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Regulatory/{gene1gene2}/{gokegg}/{cancer}/tfbs")
//	public Response getGeneId2wedasddf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
////	@GET
////	@Path("/Regulatory/{gene1, gene2}/annotation/{go, kegg}/keyword/{cancer}/tfbs")
////	public Response getGeneId2wedasddf(@PathParam("species") String species, @Context UriInfo ui) {
////		return new Variation(species, ui).getSnpInfo();
////	}
//	@GET
//	@Path("/Regulatory/{gene1gene2}/{gokegg}/{cancer}/mirna")
//	public Response getGenegedasddsdf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Compara/{gene1gene2}/{mmurno}/orthologs")
//	public Response getGeneh2wedaf(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	@GET
//	@Path("/Compara/{3111111111}/conservedregion")
//	public Response getGeneInfwef(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	/**********************************************************************************************
//	 **********************************************************************************************/
//	@GET
//	@Path("/getSnpInfoByPosition/{chrlist}/{positionlist}")
//	public Response getSnpInfo333(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/getSnpInfoByRegion/{chr}/{start}/{end}")
//	public Response getSnpInfo33(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/getSnpInfoByPathway/{pathway}")
//	public Response getSnpInfo44(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/getSnpInfoByConsequenceType/{consequence}")
//	public Response getSnpInfo55(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	
//	
//	
//	
//	@GET
//	@Path("/omegainfo")
//	public Response getSnpInfo2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/splicesiteinfo")
//	public Response getSnpInfo3(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/geneinfo")
//	public Response getSnpInfo4(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/pathwaysinfo")
//	public Response getSnpInfo5(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/getsnpsbyconsequencetype")
//	public Response getSnpsByConsequenceType2(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpsByConsequenceType();
//	}
//
//	@GET
//	@Path("/listsnps")
//	public Response getSnps1(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpInfo();
//	}
//	
//	@GET
//	@Path("/snpeffectbytype")
//	public Response getSnpEffectbyType(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Variation(species, ui).getSnpEffectByType();
//	}
//	
//	/********************************************************
//	 * GENOMIC METHODS
//	 ********************************************************/
//
//	@GET
//	@Path("/getchromosomes")
//	public Response getChromosomes(@PathParam("species") String species, @Context UriInfo ui) {
//		return new Genomic(species, ui).getChromosomes2();
//	}
//	
	
}
