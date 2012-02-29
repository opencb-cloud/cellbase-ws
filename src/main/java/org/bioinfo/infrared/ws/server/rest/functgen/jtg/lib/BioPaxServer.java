package org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bioinfo.biopax.core.SessionManager;
import org.bioinfo.biopax.model.BioEntity;
import org.bioinfo.biopax.model.Catalysis;
import org.bioinfo.biopax.model.Complex;
import org.bioinfo.biopax.model.Control;
import org.bioinfo.biopax.model.Conversion;
import org.bioinfo.biopax.model.DataSource;
import org.bioinfo.biopax.model.Dna;
import org.bioinfo.biopax.model.Dnaregion;
import org.bioinfo.biopax.model.GeneticInteraction;
import org.bioinfo.biopax.model.Interaction;
import org.bioinfo.biopax.model.MolecularInteraction;
import org.bioinfo.biopax.model.NameEntity;
import org.bioinfo.biopax.model.Pathway;
import org.bioinfo.biopax.model.PathwayStep;
import org.bioinfo.biopax.model.PhysicalEntity;
import org.bioinfo.biopax.model.Protein;
import org.bioinfo.biopax.model.ProteinReference;
import org.bioinfo.biopax.model.Rna;
import org.bioinfo.biopax.model.Rnaregion;
import org.bioinfo.biopax.model.SmallMolecule;
import org.bioinfo.biopax.model.TemplateReaction;
import org.bioinfo.biopax.model.Xref;
import org.bioinfo.commons.utils.ListUtils;
import org.bioinfo.formats.core.graph.dot.Edge;
import org.bioinfo.formats.core.graph.dot.Node;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

public class BioPaxServer {

	private Session session = SessionManager.createSession();

	//=========================================================================
	//	Physical Entities
	//=========================================================================

	//-------------------------------------------------------------------------
	// SQL queries
	//-------------------------------------------------------------------------

	private static String RNA_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_RNA as id from RNA where physicalentity = "; 
	private static String RNAREGION_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_RNARegion as id from RNARegion where physicalentity = "; 
	private static String DNA_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_DNA as id from DNA where physicalentity = "; 
	private static String DNAREGION_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_DNARegion as id from DNARegion where physicalentity = "; 
	private static String SMALLMOLECULE_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_SmallMolecule as id from SmallMolecule where physicalentity = "; 
	private static String PROTEIN_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_Protein as id from Protein where physicalentity = "; 
	private static String COMPLEX_ID_QUERY_BY_PHYSICALENTITY_ID = "select PK_Complex as id from Complex where physicalentity = "; 

	private static String PROTEIN_QUERY_BY_ID = "select * from Protein where PK_Protein = ";
	private static String COMPLEX_QUERY_BY_ID = "select * from Complex where PK_Complex = ";
	
	private static String PROTEIN_COUNTER_QUERY = "select count(*) as counter from Protein p, PhysicalEntity ph, entity_dataSource eds where p.physicalentity = ph.PK_PhysicalEntity and ph.entity = eds.entity and eds.datasource = "; 
	private static String COMPLEX_COUNTER_QUERY = "select count(*) as counter from Complex c, PhysicalEntity ph, entity_dataSource eds where c.physicalentity = ph.PK_PhysicalEntity and ph.entity = eds.entity and eds.datasource = "; 

	//-------------------------------------------------------------------------
	// Dna
	//-------------------------------------------------------------------------

	public boolean isDna(PhysicalEntity physicalEntity) {
		return isRna(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isDna(int physicalEntityId) {
		int id = getId(DNA_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public Dna getDna(PhysicalEntity ph) {
		int id = getId(DNA_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (Dna) session.get(Dna.class, id);
	}

	//-------------------------------------------------------------------------
	// Dnaregion
	//-------------------------------------------------------------------------

	public boolean isDnaregion(PhysicalEntity physicalEntity) {
		return isDnaregion(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isDnaregion(int physicalEntityId) {
		int id = getId(DNAREGION_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public Dnaregion getDnaregion(PhysicalEntity ph) {
		int id = getId(DNAREGION_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (Dnaregion) session.get(Dnaregion.class, id);
	}

	//-------------------------------------------------------------------------
	// Rna
	//-------------------------------------------------------------------------

	public boolean isRna(PhysicalEntity physicalEntity) {
		return isRna(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isRna(int physicalEntityId) {
		int id = getId(RNA_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public Rna getRna(PhysicalEntity ph) {
		int id = getId(RNA_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (Rna) session.get(Rna.class, id);
	}

	//-------------------------------------------------------------------------
	// Rnaregion
	//-------------------------------------------------------------------------

	public boolean isRnaregion(PhysicalEntity physicalEntity) {
		return isRnaregion(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isRnaregion(int physicalEntityId) {
		int id = getId(RNAREGION_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public Rnaregion getRnaregion(PhysicalEntity ph) {
		int id = getId(RNAREGION_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (Rnaregion) session.get(Rnaregion.class, id);
	}

	//-------------------------------------------------------------------------
	// SmallMolecule
	//-------------------------------------------------------------------------

	public boolean isSmallMolecule(PhysicalEntity physicalEntity) {
		return isSmallMolecule(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isSmallMolecule(int physicalEntityId) {
		int id = getId(SMALLMOLECULE_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public SmallMolecule getSmallMolecule(PhysicalEntity ph) {
		int id = getId(SMALLMOLECULE_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (SmallMolecule) session.get(SmallMolecule.class, id);
	}

	//-------------------------------------------------------------------------
	// Complex
	//-------------------------------------------------------------------------

	public int getNumberOfComplexes(DataSource ds) {
		return getCounter(COMPLEX_COUNTER_QUERY + ds.getPkDataSource());
	}
	
	public boolean isComplex(PhysicalEntity physicalEntity) {
		return isComplex(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isComplex(int physicalEntityId) {
		int id = getId(COMPLEX_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public Complex getComplex(PhysicalEntity ph) {
		int id = getId(COMPLEX_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (Complex) session.get(Complex.class, id);
	}

	public Complex getComplex(int complexId) {
		String sql = COMPLEX_QUERY_BY_ID + complexId;
		Query query = session.createSQLQuery(sql).addEntity(Complex.class);
		Complex output = (Complex) query.uniqueResult();
		return output;
	}
	
	public Complex getComplex(String complexName, String dataSourceName) {
		Complex output = null;
		try {
			String sql = "select c.* from Complex c, PhysicalEntity ph, nameEntity nm, DataSource ds, entity_dataSource eds where nm.nameEntity = '" + complexName + "' and c.physicalentity = ph.PK_PhysicalEntity and  ph.entity = nm.entity and eds.datasource = ds.PK_DataSource and eds.entity = ph.entity and ds.name = '" + dataSourceName + "'";
			Query query = session.createSQLQuery(sql).addEntity(Complex.class);
			output = (Complex) query.uniqueResult();
		} catch (Exception e) {
			output = null;
		}
		return output;
	}

	public List<ComplexComponent> getComplexComponents(String complexName, String dataSourceName) {
		return getComplexComponents(getComplex(complexName, dataSourceName));
	}
	
	public List<ComplexComponent> getComplexComponents(Complex input) {
		List<ComplexComponent> output = new ArrayList<ComplexComponent>();

		Iterator it = input.getPhysicalEntities().iterator();
		while (it.hasNext()) {
			PhysicalEntity ph = (PhysicalEntity) it.next();
			if (isProtein(ph)) {
				ComplexComponent cc = new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.Protein.name());
				List<String> names = getProteinReferenceNames(getProtein(ph));
				if (names!=null && names.size()>0) {
					String[] values = names.get(0).split(":");
					cc.setDbName(values[0]);
					cc.setDbId(values[1]);
				}
				output.add(cc);
			} else if (isSmallMolecule(ph)) {
				output.add(new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.SmallMollecule.name()));
			} else if (isDna(ph)) {
				output.add(new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.DNA.name()));
			} else if (isDnaregion(ph)) {
				output.add(new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.DNARegion.name()));
			} else if (isRna(ph)) {
				output.add(new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.RNA.name()));
			} else if (isRnaregion(ph)) {
				output.add(new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.RNARegion.name()));
			} else if (isComplex(ph)) {
				Complex complex = getComplex(ph);
				List<ComplexComponent> components = getComplexComponents(complex);
				
				output.addAll(components);
			} else {
				output.add(new ComplexComponent(getFirstName(ph.getBioEntity()), ComplexComponent.TYPE.Unknown.name()));				
			}
		}
		
		return ListUtils.unique(output);
	}
	
	public List<Pathway> getPathways(String complexName, String dataSourceName) {
		return getPathways(getComplex(complexName, dataSourceName));
	}
	
	public List<Interaction> getInteractions(Complex input) {
		return getInteractions(input.getPhysicalEntity());
	}
	
	public List<Pathway> getPathways(Complex input) {
		return getPathways(input.getPhysicalEntity());
	}
	
	public List<Protein> getProteins(Complex input) {
		Complex complex = null;
		Protein protein = null;
		List<Protein> ps, proteins = new ArrayList<Protein>();

		Iterator it = input.getPhysicalEntities().iterator();
		while (it.hasNext()) {
			PhysicalEntity ph = (PhysicalEntity) it.next();
			if (isProtein(ph)) {
				protein = getProtein(ph);
				if (!proteins.contains(protein)) {
					proteins.add(protein);
				}
			} else if (isComplex(ph)) {
				complex = getComplex(ph);
				ps = getProteins(complex);
				for(Protein p: ps) {
					if (!proteins.contains(p)) {
						proteins.add(p);
					}
				}
			}
		}
		return proteins;
	}
	
	//-------------------------------------------------------------------------
	
	public String toString(Complex input) {
		StringBuilder sb = new StringBuilder();
		sb.append(input.getPkComplex()).append("\t").append(getFirstName(input.getPhysicalEntity().getBioEntity()));
		return sb.toString();
	}
	
	public String toStringComplexes(List<Complex> input) {
		StringBuilder sb = new StringBuilder();
		sb.append("#id\tname\n");
		for(Complex item: input) {
			sb.append(toString(item)).append("\n");
		}		
		return sb.toString();
	}
	
	//-------------------------------------------------------------------------
	
	public String toJson(Complex input) {
		StringBuilder sb = new StringBuilder();		
		sb.append("{\"id\": ").append(input.getPkComplex()).append(", \"name\" : \"").append(getFirstName(input.getPhysicalEntity().getBioEntity())).append("\"}");		
		return sb.toString();
	}
	
	public String toJsonComplexes(List<Complex> input) {
		StringBuilder sb = new StringBuilder();		
		for(int i=0 ; i<input.size() ; i++) {
			sb.append((i==0 ? "" : ","));
			sb.append("[").append(toJson(input.get(i))).append("]");
		}
		return sb.toString();		
	}
	
	//-------------------------------------------------------------------------
	// Protein
	//-------------------------------------------------------------------------

	public int getNumberOfProteins(DataSource ds) {
		return getCounter(PROTEIN_COUNTER_QUERY + ds.getPkDataSource());
	}
	
	public boolean isProtein(PhysicalEntity physicalEntity) {
		return isProtein(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isProtein(int physicalEntityId) {
		int id = getId(PROTEIN_ID_QUERY_BY_PHYSICALENTITY_ID + physicalEntityId);
		return (id!=-1);
	}

	public Protein getProtein(PhysicalEntity ph) {
		Protein protein = null;
		int proteinId = getId(PROTEIN_ID_QUERY_BY_PHYSICALENTITY_ID + ph.getPkPhysicalEntity());
		return (Protein) session.get(Protein.class, proteinId);
	}	
	
	public Protein getProtein(int proteinId) {
		String sql = PROTEIN_QUERY_BY_ID + proteinId;
		Query query = session.createSQLQuery(sql).addEntity(Protein.class);
		Protein output = (Protein) query.uniqueResult();
		return output;
	}
	
	public Protein getProteinByXrefId(String proteinId, DataSource ds) {
		String sql = "select p.* from Protein p, protein_proteinReference_entityReference pprer, ProteinReference pr, EntityReference er, EntityReference_Xref erx, Xref x, PhysicalEntity ph, entity_dataSource eds where p.PK_Protein = pprer.protein and pprer.proteinreference = pr.PK_ProteinReference and pr.entityreference = er.PK_EntityReference and er.PK_EntityReference = erx.entityreference and erx.xref = x.PK_Xref and x.id = '" + proteinId + "' and p.physicalentity = ph.PK_PhysicalEntity and ph.entity = eds.entity and eds.datasource = " + ds.getPkDataSource();
		//String sql = "select p.PK_Protein as id from Protein p, protein_proteinReference_entityReference pprer, ProteinReference pr, EntityReference er, EntityReference_Xref erx, Xref x, PhysicalEntity ph, entity_dataSource eds where p.PK_Protein = pprer.protein and pprer.proteinreference = pr.PK_ProteinReference and pr.entityreference = er.PK_EntityReference and er.PK_EntityReference = erx.entityreference and erx.xref = x.PK_Xref and x.id = '" + proteinId + "' and p.physicalentity = ph.PK_PhysicalEntity and ph.entity = eds.entity and eds.datasource = " + ds.getPkDataSource();
		//int id = getId(sql);
		//System.out.println("************* " + sql);
		//System.out.println("************* getProteinByXrefId, input id = " + proteinId + ", output id = " + id);
		//return getProtein(id);
//		System.out.println("***--------------->>>> sql = " + sql);
		Query query = session.createSQLQuery(sql).addEntity(Protein.class);
		Protein output = (Protein) query.uniqueResult();
		return output;
	}
	
	public List<String> getProteinReferenceNames(Protein protein) {
		List<String> names = new ArrayList<String>();

		Iterator it = protein.getProteinReferences().iterator();
		while (it.hasNext()) {
			ProteinReference pReference = (ProteinReference) it.next();
			Iterator it1 = pReference.getEntityReference().getXrefs().iterator();
			while (it1.hasNext()) {
				Xref xref = (Xref) it1.next();
				names.add(xref.getDb() + ":" + xref.getId());
			}
		}
		return names;
	}
		
	public List<Complex> getComplexes(Protein input) {
		List<Complex> output = new ArrayList<Complex>();
		String sql = "select c.* from Complex c, complex_physicalEntity_component cphc where c.PK_Complex = cphc.complex and cphc.physicalentity = " + input.getPhysicalEntity().getPkPhysicalEntity();
		Query query = session.createSQLQuery(sql).addEntity(Complex.class);
		output = (List<Complex>) query.list();
		return output;
	}
	
	public List<Interaction> getInteractions(Protein input) {
		return getInteractions(input.getPhysicalEntity());
	}
	
	public List<Pathway> getPathways(Protein input) {
		return getPathways(input.getPhysicalEntity());
	}
	
	//=========================================================================
	//	Controls
	//=========================================================================

	//-------------------------------------------------------------------------
	// SQL queries
	//-------------------------------------------------------------------------

	private static String CATALYSIS_ID_QUERY_BY_CONTROL_ID = "select PK_Catalysis as id from Catalysis where control = "; 
	
	//-------------------------------------------------------------------------
	// Catalysis
	//-------------------------------------------------------------------------

	public boolean isCatalysis(Control control) {
		return isCatalysis(control.getPkControl());
	}
	
	public boolean isCatalysis(int controlId) {
		int id = getId(CATALYSIS_ID_QUERY_BY_CONTROL_ID + controlId);
		return (id!=-1);
	}
	
	public Catalysis getCatalysis(Control c) {
		int id = getId(CATALYSIS_ID_QUERY_BY_CONTROL_ID + c.getPkControl());
		return (Catalysis) session.get(Catalysis.class, id);
	}
	
	//=========================================================================
	//	Interactions
	//=========================================================================

	//-------------------------------------------------------------------------
	// SQL queries
	//-------------------------------------------------------------------------

	private static String CONTROL_ID_QUERY_BY_INTERACTION_ID = "select PK_Control as id from Control where interaction = "; 
	private static String CONVERSION_ID_QUERY_BY_INTERACTION_ID = "select PK_Conversion as id from Conversion where interaction = "; 
	private static String GENETICINTERACTION_ID_QUERY_BY_INTERACTION_ID = "select PK_GeneticInteraction as id from GeneticInteraction where interaction = "; 
	private static String MOLECULARINTERACTION_ID_QUERY_BY_INTERACTION_ID = "select PK_MolecularInteraction as id from MolecularReaction where interaction = "; 
	private static String TEMPLATEREACTION_ID_QUERY_BY_INTERACTION_ID = "select PK_TemplateReaction as id from TemplateReaction where interaction = "; 
		
	//-------------------------------------------------------------------------
	// Control
	//-------------------------------------------------------------------------
	
	public boolean isControl(Interaction interaction) {
		return isControl(interaction.getPkInteraction());
	}
	
	public boolean isControl(int interactionId) {
		int id = getId(CONTROL_ID_QUERY_BY_INTERACTION_ID + interactionId);
		return (id!=-1);
	}
	
	public Control getControl(Interaction ph) {
		int id = getId(CONTROL_ID_QUERY_BY_INTERACTION_ID + ph.getPkInteraction());
		return (Control) session.get(Control.class, id);
	}
	
	public List<Protein> getProteins(Control input) {
		List<Protein> output = new ArrayList<Protein>();
		
		// controlled: 0..1, Interaction or Pathway
		//
		Pathway controlledPw= input.getPathway();
		Interaction controlledIt = input.getInteractionByControlledInteraction();
				
		// controller: 0..*, PhysicalEntity or Pathway
		//
		List<PhysicalEntity> controllerPes = new ArrayList<PhysicalEntity>();
		List<Pathway> controllerPws = new ArrayList<Pathway>();

		// PhysicalEntity
		if (input.getPhysicalEntities()!=null && !input.getPhysicalEntities().isEmpty()) {
			Iterator it = input.getPhysicalEntities().iterator();
			while (it.hasNext()) {
				PhysicalEntity pe = (PhysicalEntity) it.next();
				if (isProtein(pe)) {
					output.add(getProtein(pe));
				} else if (isComplex(pe)) {
					output.addAll(getProteins(getComplex(pe)));
				}
			}				
		}
		// Pathway
		if (input.getPathwaies()!=null && !input.getPathwaies().isEmpty()) {
			Iterator it = input.getPathwaies().iterator();
			while (it.hasNext()) {
				Pathway pw = (Pathway) it.next();
				System.out.println("!! controller pathwaies not implemented (pathway: " + pw.getPkPathway() + ", " + getFirstName(pw.getBioEntity())+ ")");
			}
		}
		
		
		if (controlledIt!=null) {
			output.addAll(getProteins(controlledIt));
		}
		
		if (controlledPw!=null) {
			output.addAll(getProteins(controlledPw));
		}
		
		return ListUtils.unique(output);
	}
	
	//-------------------------------------------------------------------------
	// Conversion
	//-------------------------------------------------------------------------
	
	public boolean isConversion(Interaction interaction) {
		return isConversion(interaction.getPkInteraction());
	}
	
	public boolean isConversion(int interactionId) {
		int id = getId(CONVERSION_ID_QUERY_BY_INTERACTION_ID + interactionId);
		return (id!=-1);
	}

	public Conversion getConversion(Interaction ph) {
		int id = getId(CONVERSION_ID_QUERY_BY_INTERACTION_ID + ph.getPkInteraction());
		return (Conversion) session.get(Conversion.class, id);
	}
	
	public List<Protein> getProteins(Conversion input) {
		List<Protein> output = new ArrayList<Protein>();
		
		// left: 0..*, PhysicalEntity
		List<PhysicalEntity> leftPes = new ArrayList<PhysicalEntity> ();
		if (input.getPhysicalEntities()!=null && !input.getPhysicalEntities().isEmpty()) {
			Iterator it = input.getPhysicalEntities().iterator();
			while (it.hasNext()) {
				PhysicalEntity pe = (PhysicalEntity) it.next();
				if (isProtein(pe)) {
					output.add(getProtein(pe));
				} else if (isComplex(pe)) {
					output.addAll(getProteins(getComplex(pe)));
				}
			}				
		}
		
		// right: 0..*, PhysicalEntity
		List<PhysicalEntity> rightPes = new ArrayList<PhysicalEntity> ();
		if (input.getPhysicalEntities_1()!=null && !input.getPhysicalEntities_1().isEmpty()) {
			Iterator it = input.getPhysicalEntities_1().iterator();
			while (it.hasNext()) {
				PhysicalEntity pe = (PhysicalEntity) it.next();
				if (isProtein(pe)) {
					output.add(getProtein(pe));
				} else if (isComplex(pe)) {
					output.addAll(getProteins(getComplex(pe)));
				}
			}				
		}
				
		return ListUtils.unique(output);
	}
	
	//-------------------------------------------------------------------------
	// GeneticInteraction
	//-------------------------------------------------------------------------
	
	public boolean isGeneticInteraction(Interaction interaction) {
		return isGeneticInteraction(interaction.getPkInteraction());
	}
	
	public boolean isGeneticInteraction(int interactionId) {
		int id = getId(GENETICINTERACTION_ID_QUERY_BY_INTERACTION_ID + interactionId);
		return (id!=-1);
	}

	public GeneticInteraction getGeneticInteraction(Interaction ph) {
		int id = getId(GENETICINTERACTION_ID_QUERY_BY_INTERACTION_ID + ph.getPkInteraction());
		return (GeneticInteraction) session.get(GeneticInteraction.class, id);
	}

	//-------------------------------------------------------------------------
	// MolecularInteraction
	//-------------------------------------------------------------------------
	
	public boolean isMolecularInteraction(Interaction interaction) {
		return isConversion(interaction.getPkInteraction());
	}
	
	public boolean isMolecularInteraction(int interactionId) {
		int id = getId(MOLECULARINTERACTION_ID_QUERY_BY_INTERACTION_ID + interactionId);
		return (id!=-1);
	}

	public MolecularInteraction getMolecularInteraction(Interaction ph) {
		int id = getId(MOLECULARINTERACTION_ID_QUERY_BY_INTERACTION_ID + ph.getPkInteraction());
		return (MolecularInteraction) session.get(MolecularInteraction.class, id);
	}

	//-------------------------------------------------------------------------
	// TemplateReaction
	//-------------------------------------------------------------------------
	
	public boolean isTemplateReaction(Interaction interaction) {
		return isTemplateReaction(interaction.getPkInteraction());
	}
	
	public boolean isTemplateReaction(int interactionId) {
		int id = getId(TEMPLATEREACTION_ID_QUERY_BY_INTERACTION_ID + interactionId);
		return (id!=-1);
	}

	public TemplateReaction getTemplateReaction(Interaction ph) {
		int id = getId(TEMPLATEREACTION_ID_QUERY_BY_INTERACTION_ID + ph.getPkInteraction());
		return (TemplateReaction) session.get(TemplateReaction.class, id);
	}
	
	
	//=========================================================================
	//	Entities
	//=========================================================================

	//-------------------------------------------------------------------------
	// SQL queries
	//-------------------------------------------------------------------------
	
	private static String GENE_COUNTER_QUERY = "select count(*) as counter from Gene g, entity_dataSource eds where g.entity = eds.entity and eds.datasource = "; 
	private static String PHYSICALENTITY_COUNTER_QUERY = "select count(*) as counter from PhysicalEntity ph, entity_dataSource eds where ph.entity = eds.entity and eds.datasource = "; 
	private static String INTERACTION_COUNTER_QUERY = "select count(*) as counter from Interaction i, entity_dataSource eds where i.entity = eds.entity and eds.datasource = "; 
	private static String PATHWAY_COUNTER_QUERY = "select count(*) as counter from Pathway p, entity_dataSource eds where p.entity = eds.entity and eds.datasource = "; 
	
	//-------------------------------------------------------------------------
	// PhysicalEntity
	//-------------------------------------------------------------------------
	
	public int getNumberOfPhysicalEntities(DataSource ds) {
		return getCounter(PHYSICALENTITY_COUNTER_QUERY + ds.getPkDataSource());
	}
		
	public List<Interaction> getInteractions(PhysicalEntity input) {
		List<Interaction> output = new ArrayList<Interaction>();
		
		List<Interaction> aux = null;
		String sql = "select i.* from rightSide rs, Interaction i, Conversion cv where cv.PK_Conversion = rs.conversion and i.PK_Interaction = cv.interaction and rs.physicalentity = " + input.getPkPhysicalEntity();
		Query query = session.createSQLQuery(sql).addEntity(Interaction.class);
		aux = (List<Interaction>) query.list();
		output.addAll(aux);
		
		sql = "select i.* from leftSide ls, Interaction i, Conversion cv where cv.PK_Conversion = ls.conversion and i.PK_Interaction = cv.interaction and ls.physicalentity = " + input.getPkPhysicalEntity();
		query = session.createSQLQuery(sql).addEntity(Interaction.class);
		aux = (List<Interaction>) query.list();
		output.addAll(aux);

		sql = "select i.* from control_physicalEntity_controller cpc, Interaction i, Control ct where ct.PK_Control = cpc.control and (i.PK_Interaction = ct.controlledInteraction or i.PK_Interaction = ct.interaction) and cpc.physicalentity = " + input.getPkPhysicalEntity();
		query = session.createSQLQuery(sql).addEntity(Interaction.class);
		aux = (List<Interaction>) query.list();
		output.addAll(aux);

		for (Interaction inter: output) {
			System.out.println("interaction: " + inter.getPkInteraction() + ", name = " + getFirstName(inter.getBioEntity()));
		}
		
		return ListUtils.unique(output);
	}
	
	public List<Pathway> getPathways(PhysicalEntity input) {
		List<Pathway> output = new ArrayList<Pathway>();

		List<Interaction> interactions = getInteractions(input);
		
		for (Interaction inter: interactions) {
			Iterator it = inter.getPathwaies().iterator();
			while (it.hasNext()) {
				Pathway pw = (Pathway) it.next();
				output.add(pw);
				System.out.println("pathway: " + pw.getPkPathway() + ", name = " + getFirstName(pw.getBioEntity()));
			}
		}		
		return ListUtils.unique(output);
	}
	
	//-------------------------------------------------------------------------
	// Gene
	//-------------------------------------------------------------------------
	
	public int getNumberOfGenes(DataSource ds) {
		return getCounter(GENE_COUNTER_QUERY + ds.getPkDataSource());
	}
	
	//-------------------------------------------------------------------------
	// Interaction
	//-------------------------------------------------------------------------
	
	public int getNumberOfInteractions(DataSource ds) {
		return getCounter(INTERACTION_COUNTER_QUERY + ds.getPkDataSource());
	}
	
	//-------------------------------------------------------------------------
	
	public List<Protein> getProteins(Interaction input) {
		List<Protein> output = new ArrayList<Protein>();
		
		if (isControl(input)) {
			output.addAll(getProteins(getControl(input)));
		} else if (isConversion(input)) {
			output.addAll(getProteins(getConversion(input)));
		}

		return ListUtils.unique(output);
	}
	
	//-------------------------------------------------------------------------
	
	public String toString(Interaction input) {
		StringBuilder sb = new StringBuilder();
		sb.append(input.getPkInteraction()).append("\t").append(getFirstName(input.getBioEntity()));
		return sb.toString();
	}
	
	public String toStringInteractions(List<Interaction> input) {
		StringBuilder sb = new StringBuilder();
		sb.append("#id\tname\n");
		for(Interaction item: input) {
			sb.append(toString(item)).append("\n");
		}		
		return sb.toString();
	}
	
	//-------------------------------------------------------------------------
	
	public String toJson(Interaction input) {
		StringBuilder sb = new StringBuilder();		
		sb.append("{\"id\": ").append(input.getPkInteraction()).append(", \"name\" : \"").append(getFirstName(input.getBioEntity())).append("\"}");		
		return sb.toString();
	}
	
	public String toJsonInteractions(List<Interaction> input) {
		StringBuilder sb = new StringBuilder();		
		for(int i=0 ; i<input.size() ; i++) {
			sb.append((i==0 ? "" : ","));
			sb.append("[").append(toJson(input.get(i))).append("]");
		}
		return sb.toString();		
	}
	
	//-------------------------------------------------------------------------
	// Pathway
	//-------------------------------------------------------------------------
	
	public int getNumberOfPathways(DataSource ds) {
		return getCounter(PATHWAY_COUNTER_QUERY + ds.getPkDataSource());
	}

	public Pathway getPathway(String pathwayName, String dataSourceName) {
		Pathway output = null;
		try {
			String sql = "select pw.* from Pathway pw, nameEntity nm, DataSource ds, entity_dataSource eds  where pw.entity = nm.entity and eds.datasource = ds.PK_DataSource and eds.entity = pw.entity and nm.nameEntity = '" + pathwayName + "' and ds.name = '" + dataSourceName + "'";
			Query query = session.createSQLQuery(sql).addEntity(Pathway.class);
			output = (Pathway) query.uniqueResult();
		} catch (Exception e) {
			output = null;
		}
		return output;
	}
	
	public Pathway getPathway(int pathwayId) {
		Pathway output = null;
		try {
			String sql = "select * from Pathway where PK_Pathway = " + pathwayId;
			Query query = session.createSQLQuery(sql).addEntity(Pathway.class);
			output = (Pathway) query.uniqueResult();
		} catch (Exception e) {
			output = null;
		}
		return output;
	}

	public List<Pathway> getPathways(String dataSourceName) {
		return getPathways(dataSourceName, null, false);
	}
	
	public List<Pathway> getPathways(String dataSourceName, String search, boolean onlyTopLevel) {
		List<Pathway> output = null;
		try {
			String sql = "";
			String searchSql = "";
			if (search!=null && search.length()>0 && search.trim().length()>0) {
				searchSql = "ne.entity = eds.entity and ne.nameEntity like \"%" + search + "%\" and "; 
			}
			if (onlyTopLevel) {
				sql = "select distinct p.* from pathway_pathway_pathwayComponent pc, Pathway p, entity_dataSource eds, DataSource ds, nameEntity ne where " + searchSql + " pc.pathway = p.PK_Pathway and p.entity = eds.entity and eds.datasource = ds.PK_DataSource and ds.name = '" + dataSourceName + "' and pc.pathway not in (select pathwayComponent from pathway_pathway_pathwayComponent)";				
			} else {
				sql = "select pw.* from Pathway pw, DataSource ds, entity_dataSource eds, nameEntity ne where  " + searchSql + " eds.datasource = ds.PK_DataSource and eds.entity = pw.entity and ds.name = '" + dataSourceName + "'";
			}
			Query query = session.createSQLQuery(sql).addEntity(Pathway.class);
			output = (List<Pathway>) query.list();
		} catch (Exception e) {
			output = null;
		}
		return output;
	}
	
	public List<Interaction> getInteractions(Pathway input) {
		List<Interaction> output = new ArrayList<Interaction>();
		
		List<Pathway> pws, pathways = new ArrayList<Pathway>();
		
		Interaction inter = null;

		
		PathwayStep ps = null;
		Set steps = input.getPathwaySteps_1();
		Iterator it1, it = steps.iterator();
		while (it.hasNext()) {
			ps = ((PathwayStep) it.next());
			it1 = ps.getInteractions().iterator();
			while (it1.hasNext()) {
				inter = ((Interaction) it1.next());
				output.add(inter);
			}
			
			pathways.addAll(ps.getPathwaies());
			
			it1 = ps.getPathwaies().iterator();
			while (it1.hasNext()) {
				pathways.add((Pathway) it1.next());
			}
			
			it1 = ps.getPathwayStepsForNextPathwaystep().iterator();
			while (it1.hasNext()) {
				ps = ((PathwayStep) it1.next());
			}
		}

		pws = ListUtils.unique(pathways);
		for(Pathway pw: pathways) {
			output.addAll(getInteractions(pw));
		}
		
		return ListUtils.unique(output);
	}

	public List<Protein> getProteins(Pathway input) {
		List<Protein> output = new ArrayList<Protein>();
		
		List<Interaction> interactions = getInteractions(input);
		for (Interaction i: interactions) {
			output.addAll(getProteins(i));
		}
		
		return ListUtils.unique(output);
	}
	
	//-------------------------------------------------------------------------
	
	public String toString(Pathway input) {
		StringBuilder sb = new StringBuilder();
		sb.append(input.getPkPathway()).append(":::").append(getFirstName(input.getBioEntity()));
		return sb.toString();
	}
	
	public String toStringPathways(List<Pathway> input) {
		StringBuilder sb = new StringBuilder();
		//sb.append("#id\tname\n");
		for(int i=0 ; i<input.size() ; i++) {
			if (i>0) { 
				sb.append("///");
			}
			sb.append(toString(input.get(i)));
		}		
		return sb.toString();
	}
	
	//-------------------------------------------------------------------------
	
	public String toJson(Pathway input) {
		StringBuilder sb = new StringBuilder();		
		sb.append("{\"id\": ").append(input.getPkPathway()).append(", \"name\" : \"").append(getFirstName(input.getBioEntity())).append("\"}");		
		return sb.toString();
	}
	
	public String toJsonPathways(List<Pathway> input) {
		StringBuilder sb = new StringBuilder();		
		sb.append("[");
		for(int i=0 ; i<input.size() ; i++) {
			if (i>0) {
				sb.append(",");
			}
			sb.append(toJson(input.get(i)));
		}
		sb.append("]");
		return sb.toString();		
	}
	
	//=========================================================================
	//	Utilities
	//=========================================================================

	//-------------------------------------------------------------------------
	// SQL queries
	//-------------------------------------------------------------------------
	
	private static String CELLULARLOCATION_COUNTER_QUERY = "select count(*) as counter from CellularLocationVocabulary clv, ControlledVocabulary cv, utility_dataSource uds where clv.controlledvocabulary = cv.PK_ControlledVocabulary and cv.utility = uds.utility and uds.datasource = "; 
	private static String PUBLICATIONXREF_COUNTER_QUERY = "select count(*) as counter from PublicationXref px, Xref x, utility_dataSource uds where px.xref = x.PK_Xref and x.utility = uds.utility and uds.datasource = "; 
	
	//-------------------------------------------------------------------------
	// CellularLocation
	//-------------------------------------------------------------------------
	
	public int getNumberOfCellularLocations(DataSource ds) {
		return getCounter(CELLULARLOCATION_COUNTER_QUERY + ds.getPkDataSource());
	}
	
	//-------------------------------------------------------------------------
	// PublicationXref
	//-------------------------------------------------------------------------
	
	public int getNumberOfPublicationXrefs(DataSource ds) {
		return getCounter(PUBLICATIONXREF_COUNTER_QUERY + ds.getPkDataSource());
	}
	
	//=========================================================================
	//	DataSources
	//=========================================================================

	public List<DataSource> getDataSources() {
		List<DataSource> output = null;
		try {
			String sql = "select * from DataSource";
			Query query = session.createSQLQuery(sql).addEntity(DataSource.class);
			output = (List<DataSource>) query.list();
		} catch (Exception e) {
			output = null;
		}
		return output;
	}

	public DataSource getDataSource(String name) {
		DataSource output = null;
		try {
			String sql = "select * from DataSource where name = '" + name + "'";
			Query query = session.createSQLQuery(sql).addEntity(DataSource.class);
			output = (DataSource) query.uniqueResult();
		} catch (Exception e) {
			output = null;
		}
		return output;
	}
	
	public DataSourceStats getDataSourceStats(String dataSourceName) {
		DataSource ds = getDataSource(dataSourceName);
		return getDataSourceStats(ds);
	}
	
	public DataSourceStats getDataSourceStats(DataSource ds) {		
		DataSourceStats dsStats = null;
		
		if (ds!=null) {
			dsStats = new DataSourceStats();
			
			dsStats.setNbPathways(getNumberOfPathways(ds));
			dsStats.setNbInteractions(getNumberOfInteractions(ds));
			dsStats.setNbPhysicalEntities(getNumberOfPhysicalEntities(ds));
			dsStats.setNbGenes(getNumberOfGenes(ds));
			dsStats.setNbProteins(getNumberOfProteins(ds));
			dsStats.setNbComplexes(getNumberOfComplexes(ds));
			dsStats.setNbCellularLocations(this.getNumberOfCellularLocations(ds));
			dsStats.setNbPubXrefs(getNumberOfPublicationXrefs(ds));
		}
		
		return dsStats;		
	}

	//=========================================================================
	//=========================================================================
	
	private int getId(String sql) {
		int id = -1;
		try {
			Query query = session.createSQLQuery(sql).addScalar("id", Hibernate.INTEGER);
			Object res = query.uniqueResult();
			id = (res!=null ? ((Integer) res) : -1);
		} catch (Exception e) {
			id = -1;
		}
		return id;
	}
	
	private int getCounter(String sql) {
		int id = -1;
		try {
			Query query = session.createSQLQuery(sql).addScalar("counter", Hibernate.INTEGER);
			Object res = query.uniqueResult();
			id = (res!=null ? ((Integer) res) : -1);
		} catch (Exception e) {
			id = -1;
		}
		return id;
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

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
