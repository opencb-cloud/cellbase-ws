package org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib;


@Deprecated
public class InteractionBrowser {
/*
	private static Session session = SessionManager.createSession();

	private static String PROTEIN_ID_QUERY = "select PK_Protein as id from Protein where physicalentity = "; 
	private static String COMPLEX_ID_QUERY = "select PK_Complex as id from Complex where physicalentity = "; 

	public boolean isComplex(PhysicalEntity physicalEntity) {
		return isComplex(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isComplex(int physicalEntityId) {
		int id = Utils.getId(COMPLEX_ID_QUERY + physicalEntityId);
		return (id!=-1);
	}

	public boolean isProtein(PhysicalEntity physicalEntity) {
		return isProtein(physicalEntity.getPkPhysicalEntity());
	}

	public boolean isProtein(int physicalEntityId) {
		int id = Utils.getId(PROTEIN_ID_QUERY + physicalEntityId);
		return (id!=-1);
	}

	//-------------------------------------------------------------------------

	public Protein getProtein(PhysicalEntity ph) {
		Protein protein = null;
		int proteinId = Utils.getId(PROTEIN_ID_QUERY + ph.getPkPhysicalEntity());
		return (Protein) session.get(Protein.class, proteinId);
	}	

	public Complex getComplex(PhysicalEntity ph) {
		Complex complex = null;
		int complexId = Utils.getId(COMPLEX_ID_QUERY + ph.getPkPhysicalEntity());
		return (Complex) session.get(Complex.class, complexId);
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
	
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------

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
		
	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------

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
				//System.out.println(ps.size() + " proteins in complex " + complex.getPkComplex());
				//Runtime.getRuntime().exit(-1);
				for(Protein p: ps) {
					if (!proteins.contains(p)) {
						proteins.add(p);
					}
				}
				//System.out.println("--------------> complex inside complex");
			}
		}
		return proteins;
	}


	
	public List<Protein> getProteins(List<PhysicalEntity> input) {

		Complex complex = null;
		Protein protein = null;
		List<Protein> proteins, output = new ArrayList<Protein>();

		for(PhysicalEntity ph: input) {
			if (isProtein(ph)) {
				protein = getProtein(ph);
				if (protein!=null && !output.contains(protein)) {
					output.add(protein);
				}
			} else if (isComplex(ph)) {
				complex = getComplex(ph);
				proteins = getProteins(complex);
				if (proteins.size()>0) {
					for(Protein p: proteins) {
						if (!output.contains(p)) {
							output.add(p);
						}
					}
				}
			}
		}
		return output;
	}



	//-------------------------------------------------------------------------
	//-------------------------------------------------------------------------


	private static String CONTROL_ID_QUERY = "select PK_Control as id from Control where interaction = "; 
	private static String CONVERSION_ID_QUERY = "select PK_Conversion as id from Conversion where interaction = "; 

	//-------------------------------------------------------------------------

	public boolean isControl(Interaction interaction) {
		return isControl(interaction.getPkInteraction());
	}

	public boolean isControl(int interactionId) {
		int id = Utils.getId(CONTROL_ID_QUERY + interactionId);
		return (id!=-1);
	}

	public boolean isConversion(Interaction interaction) {
		return isConversion(interaction.getPkInteraction());
	}

	public boolean isConversion(int interactionId) {
		int id = Utils.getId(CONVERSION_ID_QUERY + interactionId);
		return (id!=-1);
	}

	//-------------------------------------------------------------------------

	public Conversion getConversion(Interaction interaction) {
		Conversion conversion = null;
		int conversionId = Utils.getId(CONVERSION_ID_QUERY + interaction.getPkInteraction());
		return (Conversion) session.get(Conversion.class, conversionId);
	}

	public Control getControl(Interaction interaction) {
		Control control = null;
		int controlId = Utils.getId(CONTROL_ID_QUERY + interaction.getPkInteraction());
		return (Control) session.get(Control.class, controlId);
	}

	public List<Interaction> getInteractions(String dataSourceName) {
		List<Interaction> list = null;
		try {
			String sql = "select it.* from Interaction it, DataSource ds, entity_dataSource eds where it.entity = eds.entity and eds.datasource = ds.PK_DataSource and ds.name = '" + dataSourceName + "'";
			Query query = session.createSQLQuery(sql).addEntity(Interaction.class);
			list = (List<Interaction>) query.list();
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	public List<Integer> getInteractionIds(String dataSourceName) {
		List<Integer> list = null;
		try {
			String sql = "select it.PK_Interaction as id from Interaction it, DataSource ds, entity_dataSource eds where it.entity = eds.entity and eds.datasource = ds.PK_DataSource and ds.name = '" + dataSourceName + "'";
			Query query = session.createSQLQuery(sql).addScalar("id", Hibernate.INTEGER);
			list = (List<Integer>) query.list();
		} catch (Exception e) {
			list = null;
		}
		return list;
	}
*/
}
