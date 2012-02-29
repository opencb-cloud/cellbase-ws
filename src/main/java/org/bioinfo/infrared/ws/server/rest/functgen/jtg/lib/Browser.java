package org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib;


@Deprecated
public class Browser {
/*
	Session session = null;
	InteractionBrowser iBrowser = null;
	Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	List<String> fetchedLines = new ArrayList<String>();
	
	public Browser() {
		session = SessionManager.createSession();
		iBrowser = new InteractionBrowser();
	}

	//-------------------------------------------------------------
	
	public void displayPathway(String name, String dataSourceName) {
		try {
			String sql = "select pw.PK_Pathway as id from Pathway pw, nameEntity nm, DataSource ds, entity_dataSource eds  where pw.entity = nm.entity and eds.datasource = ds.PK_DataSource and eds.entity = pw.entity and nm.nameEntity = '" + name + "' and ds.name = '" + dataSourceName + "'";
			Query query = session.createSQLQuery(sql).addScalar("id", Hibernate.INTEGER);
			Object id =  query.uniqueResult(); 

			System.out.println("(name, id) = (" + name + ", " + id + ")");

			displayPathway((Integer) id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//-------------------------------------------------------------
	
	public void displayPathway(int id) {
		displayPathway((Pathway) session.get(Pathway.class, id));
	}

	//-------------------------------------------------------------
	
	public void displayPathway(Pathway pathway) {
		try {
			Iterator it = null;
			Set pathways = new HashSet();
			System.out.print("======================   ");
			System.out.print(((NameEntity) pathway.getBioEntity().getNameEntities().iterator().next()).getNameEntity());
			System.out.println("  ======================");
			
			//Pathway pathway = (Pathway) session.get(Pathway.class, id);

//			System.out.println("** pathway, comment :\n\t" + pathway.getBioEntity().getComment()); 
//			it = pathway.getBioEntity().getNameEntities().iterator();
//			System.out.println("** pathway, names :");
//			while (it.hasNext()) {
//				System.out.println("\t" + ((NameEntity) it.next()).getNameEntity());	
//			}

			Pathway pw = null;
			PathwayStep ps = null;
			Interaction inter = null;
			
			Set steps = pathway.getPathwaySteps_1();
//			System.out.println("** pathway, order: " +  steps.size() + " steps");
			it = steps.iterator();
			while (it.hasNext()) {
				ps = ((PathwayStep) it.next());
//				System.out.println("\n\tStep pk " + ps.getPkPathwayStep() );
//				
//				System.out.println("\t\tComponets: contains " + ps.getInteractions().size() + " interactions and " + ps.getPathwaies().size() + " pathways");
				Iterator it1 = ps.getInteractions().iterator();
				while (it1.hasNext()) {
					inter = ((Interaction) it1.next());
//					System.out.println("\t\t\tinteraction: pk " + inter.getPkInteraction()); // + ", " + ((NameEntity) inter.getBioEntity().getNameEntities().iterator().next()).getNameEntity());
					displayInteraction(inter);
				}
				
				pathways.addAll(ps.getPathwaies());
				
				it1 = ps.getPathwaies().iterator();
				while (it1.hasNext()) {
					pw = ((Pathway) it1.next());
//					System.out.println("\t\t\tpathway    pk : " + pw.getPkPathway() + ", " + ((NameEntity) pw.getBioEntity().getNameEntities().iterator().next()).getNameEntity());
				}
				
//				System.out.println("\t\tNext steps: contains " + ps.getPathwayStepsForNextPathwaystep().size() + " branches");
				it1 = ps.getPathwayStepsForNextPathwaystep().iterator();
				while (it1.hasNext()) {
					ps = ((PathwayStep) it1.next());
//					System.out.println("\t\t\tnext step  pk : " + ps.getPkPathwayStep());
				}
			}

			map.put(pathway.getPkPathway(), true);
			
			it = pathways.iterator();
			while (it.hasNext()) {
				pw = ((Pathway) it.next());
				if (!map.containsKey(pw.getPkPathway())) {
					displayPathway(pw);
				}
			}
//			System.out.println("** pathway, component: " +  pathway.getInteractions().size() + " interactions, " + pathway.getPathwaiesForPathwayComponent().size() + " pathways");
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------
	
	public void displayInteraction(Interaction interaction) {
		
		String name = Utils.getFirstName(interaction.getBioEntity());
//		System.out.println("\t\t\t\tpk   " + interaction.getPkInteraction());
//		System.out.println("\t\t\t\tname " + name);
		

		if (iBrowser.isControl(interaction)) {
			Control control = iBrowser.getControl(interaction);

//			System.out.println("\t\t\t\tControl");
//			System.out.println("\t\t\t\tControl type = " + control.getControlType());
			

			// controlled: 0..1, Interaction or Pathway
			//
			String controlledName = "";
			// Interaction
			if (control.getInteractionByControlledInteraction()!=null) {
				controlledName = Utils.getFirstName(control.getInteractionByControlledInteraction().getBioEntity());
				fetchedLines.add("interaction_" + control.getInteractionByControlledInteraction().getPkInteraction() + " [label=\"" + Utils.getFirstName(control.getInteractionByControlledInteraction().getBioEntity()) + "\"; shape=box];");

//				System.out.println("\t\t\t\tControlled interaction pk = " + control.getInteractionByControlledInteraction().getPkInteraction() + ", name = " + controlledName);
			// Pathway
			} else if (control.getPathway()!=null) {
//				System.out.println("\t\t\t\tControlled pathway pk = " + control.getPathway().getPkPathway());
				System.out.println("!! controlled pathway not implemented (pathway: " + control.getPathway().getPkPathway() + ", " + Utils.getFirstName(control.getPathway().getBioEntity())+ ")");
			}
			
			// controller: 0..*, PhysicalEntity or Pathway
			//
			List<String> controllerNames = new ArrayList<String>();
			// PhysicalEntity
			if (control.getPhysicalEntities()!=null && !control.getPhysicalEntities().isEmpty()) {
//				System.out.println("\t\t\t\tController physical entities: " + control.getPhysicalEntities().size());
				Iterator it = control.getPhysicalEntities().iterator();
				while (it.hasNext()) {
					PhysicalEntity pe = (PhysicalEntity) it.next();
//					System.out.println("\t\t\t\t\tpk   = " + pe.getPkPhysicalEntity() + ", name = " + Utils.getFirstName(pe.getBioEntity()));
					controllerNames.add(Utils.getFirstName(pe.getBioEntity()));
				}
			}
			// Pathway
			if (control.getPathwaies()!=null && !control.getPathwaies().isEmpty()) {
//				System.out.println("\t\t\t\tController pathwaies:" + control.getPathwaies().size());				
				Iterator it = control.getPathwaies().iterator();
				while (it.hasNext()) {
					Pathway pw = (Pathway) it.next();
					System.out.println("!! controller pathwaies not implemented (pathway: " + pw.getPkPathway() + ", " + Utils.getFirstName(pw.getBioEntity())+ ")");
				}
			}

			String arrowhead = "none";
			if ("activation".equalsIgnoreCase(control.getControlType())) {
				arrowhead = "normal";
			} else if ("inhibition".equalsIgnoreCase(control.getControlType())) {
				arrowhead = "tee";				
			}
			// save to sif lines
			if (controlledName!=null && controllerNames.size()>0) {
				for(String n: controllerNames) {
					fetchedLines.add(n.replace(" ", "_") + "->" + controlledName.replace(" ", "_") + "\"; arrowhead=" + arrowhead + "];");
					//fetchedLines.add(n.replace(" ", "_") + "\tpp\t" + controlledName.replace(" ", "_"));
				}
			} else {
				if (controlledName==null) {
					System.out.println("!! controlled name is missing (interaction: " + name + ")");					
				}
				if (controllerNames.size()==0) {
					System.out.println("!! controller names are missing (interaction: " + name + ")");
				}
			}
			
		} else if (iBrowser.isConversion(interaction)) {
			Conversion conversion = iBrowser.getConversion(interaction);
//			System.out.println("\t\t\t\tConversion");
//			System.out.println("\t\t\t\tConversion direction = " + conversion.getConversionDirection());
//			System.out.println("\t\t\t\tSpontaneous = " + conversion.getSpontaneous());
			
			// left: 0..*, PhysicalEntity
			List<String> leftNames = new ArrayList<String> ();
			if (conversion.getPhysicalEntities()!=null && !conversion.getPhysicalEntities().isEmpty()) {
//				System.out.println("\t\t\t\tLeft physical entities: " + conversion.getPhysicalEntities().size());
				Iterator it = conversion.getPhysicalEntities().iterator();
				while (it.hasNext()) {
					PhysicalEntity pe = (PhysicalEntity) it.next();
//					System.out.println("\t\t\t\t\tpk   = " + pe.getPkPhysicalEntity() + ", name = " + Utils.getFirstName(pe.getBioEntity()));
					fetchedLines.add(Utils.getFirstName(pe.getBioEntity()).replace(" ", "_") + " [label=\"" + Utils.getFirstName(pe.getBioEntity()) + "\"; shape=ellipse];");
					leftNames.add(Utils.getFirstName(pe.getBioEntity()));
				}				
			}
			
			// right: 0..*, PhysicalEntity
			List<String> rightNames = new ArrayList<String> ();
			if (conversion.getPhysicalEntities_1()!=null && !conversion.getPhysicalEntities_1().isEmpty()) {
//				System.out.println("\t\t\t\tRight physical entities: " + conversion.getPhysicalEntities_1().size());
				Iterator it = conversion.getPhysicalEntities_1().iterator();
				while (it.hasNext()) {
					PhysicalEntity pe = (PhysicalEntity) it.next();
//					System.out.println("\t\t\t\t\tpk   = " + pe.getPkPhysicalEntity() + ", name = " + Utils.getFirstName(pe.getBioEntity()));
					fetchedLines.add(Utils.getFirstName(pe.getBioEntity()).replace(" ", "_") + " [label=\"" + Utils.getFirstName(pe.getBioEntity()) + "\"; shape=ellipse];");
					rightNames.add(Utils.getFirstName(pe.getBioEntity()));
				}				
			}
			
			// save to sif lines
			if (leftNames.size()>0 && rightNames.size()>0) {
				for(String l: leftNames) {
					fetchedLines.add(l.replace(" ", "_") + "->" + name.replace(" ", "_") + "\"; arrowhead=none];");
//					fetchedLines.add(l.replace(" ", "_") + "\tpp\t" + name.replace(" ", "_"));
				}
				for(String r: rightNames) {
					fetchedLines.add(name.replace(" ", "_") + "->" + r.replace(" ", "_") + "\"; arrowhead=empty];");
//					fetchedLines.add(name.replace(" ", "_") + "\tpp\t" + r.replace(" ", "_"));
				}
			} else {
				if (leftNames.size()==0) {
					System.out.println("!! left names are missing (interaction: " + name + ")");
				}
				if (rightNames.size()==0) {
					System.out.println("!! right names are missing (interaction: " + name + ")");
				}
			}
		} else {
			System.out.println("!! interaction type not supported (interaction: " + name + ")");
		}
	}
*/
}
