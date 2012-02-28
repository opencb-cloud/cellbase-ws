package org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bioinfo.biopax.model.Complex;
import org.bioinfo.biopax.model.Control;
import org.bioinfo.biopax.model.Conversion;
import org.bioinfo.biopax.model.Interaction;
import org.bioinfo.biopax.model.Pathway;
import org.bioinfo.biopax.model.PathwayStep;
import org.bioinfo.biopax.model.PhysicalEntity;
import org.bioinfo.biopax.model.Term;
import org.bioinfo.biopax.server.BioPaxServer;
import org.bioinfo.biopax.server.ComplexComponent;
import org.bioinfo.commons.io.TextFileWriter;
import org.bioinfo.commons.io.utils.IOUtils;
import org.bioinfo.formats.core.graph.dot.Edge;
import org.bioinfo.formats.core.graph.dot.Node;

public class PatServer {

	private int splitChunk = 13;
	private String splitSep = "\\n";

	private BioPaxServer bpServer = null;
	Map<Integer, Boolean> pathwayMap = new HashMap<Integer, Boolean>();
	Map<Integer, Boolean> interactionMap = new HashMap<Integer, Boolean>();

	Map<Complex, Boolean> complexMap = new HashMap<Complex, Boolean>();
	
	List<String> controlLines = new ArrayList<String>();
	List<String> conversionLines = new ArrayList<String>();

	public PatServer() {
		bpServer = new BioPaxServer();
	}

	//=========================================================================
	//	generate PAT files in folder
	//=========================================================================

	public void generatePat(Pathway input, String folder) {
		String name = bpServer.getFirstName(input.getBioEntity());

		add(input);

		try {
			IOUtils.write(new File(folder + "/" + name + "_control.txt"), controlLines);
			IOUtils.write(new File(folder + "/" + name + "_conversion.txt"), conversionLines);

			TextFileWriter writer = new TextFileWriter(folder + "/" + name + "_complex.txt");
			for(Complex c: complexMap.keySet()) {
				for(ComplexComponent cc: bpServer.getComplexComponents(c)) {
					if (cc.getType().equalsIgnoreCase(ComplexComponent.TYPE.Protein.toString())) {
						writer.writeLine(bpServer.getFirstName(c.getPhysicalEntity().getBioEntity()) + "\t" + (cc.getDbId()==null ? cc.getName() : cc.getDbName() + ":" + cc.getDbId()));
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		bpServer.getSession().clear();
	}

	//	public Dot generateDot(Complex input) {
	//		pat = new Pat(bpServer.getFirstName(input.getPhysicalEntity().getBioEntity()));
	//		
	//		add(input);
	//		
	//		bpServer.getSession().clear();
	//		return pat;
	//	}

	//=========================================================================
	//	Complex
	//=========================================================================
	/*
	private void add(Complex input) {

		Edge edge = null;
		Node child = null;
		Node parent = new Node("entity_" + input.getPhysicalEntity().getBioEntity().getPkEntity());

		parent.setAttribute(Node.LABEL, bpServer.getFirstName(input.getPhysicalEntity().getBioEntity()));
		parent.setAttribute(Node.ID, ""+input.getPkComplex());
		parent.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.octagon.name());
		parent.setAttribute(Node.FILLCOLOR, "#CCFFFF");
		parent.setAttribute(Node.STYLE, Node.STYLE_VALUES.filled.name());

		dot.addNode(parent);

		Iterator it = input.getPhysicalEntities().iterator();
		while (it.hasNext()) {
			child = null;
			PhysicalEntity ph = (PhysicalEntity) it.next();
			if (bpServer.isProtein(ph)) {
				child = new Node("entity_" + ph.getBioEntity().getPkEntity());

				Protein protein = bpServer.getProtein(ph);
				String name = bpServer.getFirstName(ph.getBioEntity());
				List<String> names = bpServer.getProteinReferenceNames(protein);
				if (names!=null && names.size()>0) {
					name = names.get(0);
				}

				child.setAttribute(Node.LABEL, name);
				child.setAttribute(Node.ID, ""+protein.getPkProtein());
				child.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.box.name());
				child.setAttribute(Node.FILLCOLOR, "#CCFFCC");
				child.setAttribute(Node.STYLE, Node.STYLE_VALUES.filled.name() + "," + Node.STYLE_VALUES.rounded.name());
			} else if (bpServer.isSmallMolecule(ph)) {
				child = new Node("entity_" + ph.getBioEntity().getPkEntity());

				child.setAttribute(Node.LABEL, bpServer.getFirstName(ph.getBioEntity()));				
				child.setAttribute(Node.ID, ""+bpServer.getSmallMolecule(ph).getPkSmallMolecule());
				child.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.ellipse.name());
				child.setAttribute(Node.FILLCOLOR, "#CCFFCC");
				child.setAttribute(Node.STYLE, Node.STYLE_VALUES.filled.name());
			} else if (bpServer.isComplex(ph)) {
				add(bpServer.getComplex(ph));
			} else {
				child = new Node("entity_" + ph.getBioEntity().getPkEntity());

				child.setAttribute(Node.LABEL, bpServer.getFirstName(ph.getBioEntity()));				
				child.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.egg.name());
				child.setAttribute(Node.FILLCOLOR, "#CCFFCC");
				child.setAttribute(Node.STYLE, Node.STYLE_VALUES.filled.name());
			}

			if (child!=null) {
				dot.addNode(child);
			}

			edge = new Edge("entity_" + ph.getBioEntity().getPkEntity(), "entity_" + input.getPhysicalEntity().getBioEntity().getPkEntity(), true);
			edge.setAttribute(Edge.ARROWHEAD, "empty");
			dot.addEdge(edge);
		}
	}
	 */
	//=========================================================================
	//	Physical Entities
	//=========================================================================

	private void addNode(PhysicalEntity input) {

		if (bpServer.isComplex(input)) {
			complexMap.put(bpServer.getComplex(input), true);
		}
/*
		Node node = new Node("entity_" + input.getBioEntity().getPkEntity());

		String name = bpServer.getFirstName(input.getBioEntity());		
		node.setAttribute(Node.LABEL, name);

		node.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.egg.name());
		node.setAttribute(Node.FILLCOLOR, "#CCFFCC");
		node.setAttribute(Node.STYLE, Node.STYLE_VALUES.filled.name());

		String group = null;
		try {
			group = ((Term) input.getCellularLocationVocabulary().getControlledVocabulary().getTerms().iterator().next()).getTerm();		
		} catch (Exception e) {
			group = null;
		}
		if (group!=null) {
			node.setAttribute(Node.GROUP, group);
		}

		if (bpServer.isProtein(input)) {
			node.setAttribute(Node.ID, ""+bpServer.getProtein(input).getPkProtein());
			node.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.box.name());
			node.setAttribute(Node.STYLE, Node.STYLE_VALUES.filled.name() + "," + Node.STYLE_VALUES.rounded.name());
		} else if (bpServer.isSmallMolecule(input)) {
			node.setAttribute(Node.ID, ""+bpServer.getSmallMolecule(input).getPkSmallMolecule());
			node.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.ellipse.name());
		} else if (bpServer.isComplex(input)) {
			node.setAttribute(Node.ID, ""+bpServer.getComplex(input).getPkComplex());
			node.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.octagon.name());
			node.setAttribute(Node.FILLCOLOR, "#CCFFFF");
		}

		dot.addNode(node);
*/
	}


	//=========================================================================
	//	Interactions
	//=========================================================================

	//-------------------------------------------------------------------------
	//	Control
	//-------------------------------------------------------------------------
	private void addEdges(Control input) {
		
		if (interactionMap.containsKey(input.getPkControl())) {
			return;
		}
		interactionMap.put(input.getPkControl(), true);

		String name = bpServer.getFirstName(input.getInteractionByInteraction().getBioEntity());
		
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
				if (!controllerPes.contains(pe)) {
					addNode(pe);
					controllerPes.add(pe);
				}
			}				
		}
		
		// Pathway
		if (input.getPathwaies()!=null && !input.getPathwaies().isEmpty()) {
			Iterator it = input.getPathwaies().iterator();
			while (it.hasNext()) {
				Pathway pw = (Pathway) it.next();
				System.out.println("!! controller pathwaies not implemented (pathway: " + pw.getPkPathway() + ", " + bpServer.getFirstName(pw.getBioEntity())+ ")");
			}
		}

		if (controlledIt!=null && controllerPes.size()>0) {
			for(PhysicalEntity pe: controllerPes) {
				System.out.println("CONTROL\t" + bpServer.getFirstName(pe.getBioEntity()) + "\t" + name + "\t" + bpServer.getFirstName(controlledIt.getBioEntity()));
				controlLines.add(bpServer.getFirstName(pe.getBioEntity()) + "\t" + name + "\t" + bpServer.getFirstName(controlledIt.getBioEntity()));
			}
		} else {
			if (controlledIt==null) {
				System.out.println("!! controlled interaction is missing (interaction: " + name + ")");					
			}
			if (controllerPes.size()==0) {
				System.out.println("!! controller names are missing (interaction: " + name + ")");
			}
		}
	}

	//-------------------------------------------------------------------------
	//	Conversion
	//-------------------------------------------------------------------------

	private void addEdges(Conversion input) {

		if (interactionMap.containsKey(input.getPkConversion())) {
			return;
		}
		interactionMap.put(input.getPkConversion(), true);

		String name = bpServer.getFirstName(input.getInteraction().getBioEntity());

		// left: 0..*, PhysicalEntity
		List<PhysicalEntity> leftPes = new ArrayList<PhysicalEntity> ();
		if (input.getPhysicalEntities()!=null && !input.getPhysicalEntities().isEmpty()) {
			Iterator it = input.getPhysicalEntities().iterator();
			while (it.hasNext()) {
				PhysicalEntity pe = (PhysicalEntity) it.next();
				if (!leftPes.contains(pe)) {
					addNode(pe);
					leftPes.add(pe);	
				}
			}				
		}

		// right: 0..*, PhysicalEntity
		List<PhysicalEntity> rightPes = new ArrayList<PhysicalEntity> ();
		if (input.getPhysicalEntities_1()!=null && !input.getPhysicalEntities_1().isEmpty()) {
			Iterator it = input.getPhysicalEntities_1().iterator();
			while (it.hasNext()) {
				PhysicalEntity pe = (PhysicalEntity) it.next();
				if (!rightPes.contains(pe)) {
					addNode(pe);
					rightPes.add(pe);
				}
			}				
		}

		// left -> right
		if (leftPes.size()>0 && rightPes.size()>0) {
			for(PhysicalEntity l: leftPes) {
				for(PhysicalEntity r: rightPes) {
					System.out.println("CONVERSION\t" + bpServer.getFirstName(l.getBioEntity()) + "\t" + name + "\t" + bpServer.getFirstName(r.getBioEntity()));
					conversionLines.add(bpServer.getFirstName(l.getBioEntity()) + "\t" + name + "\t" + bpServer.getFirstName(r.getBioEntity()));
				}
			}
		} else {
			if (leftPes.size()==0) {
				System.out.println("!! left physical entities are missing (interaction: " + name + ")");
			}
			if (rightPes.size()==0) {
				System.out.println("!! right physical entities are missing (interaction: " + name + ")");
			}
		}
	}

	//-------------------------------------------------------------------------
	//	Interaction
	//-------------------------------------------------------------------------

	private void add(Interaction input) {
		if (bpServer.isControl(input)) {
			addEdges(bpServer.getControl(input));
		} else if (bpServer.isConversion(input)) {
			addEdges(bpServer.getConversion(input));
			//		} else if (bpServer.isGeneticInteraction(input)) {
			//			output.append(getDot(bpServer.getGeneticInteraction(input)));
			//		} else if (bpServer.isMolecularInteraction(input)) {
			//			output.append(getDot(bpServer.getMolecularInteraction(input)));
			//		} else if (bpServer.isTemplateReaction(input)) {
			//			output.append(getDot(bpServer.getTemplateReaction(input)));
		}  else {
			System.out.println("Interacion not implemented yet !!");			
		}
	}


	//=========================================================================
	//	Pathway
	//=========================================================================

	private void add(Pathway input) {
		Pathway pw = null;
		PathwayStep ps = null;
		Interaction inter = null;

		Iterator it = null;
		Set pathways = new HashSet();		

		Set steps = input.getPathwaySteps_1();
		it = steps.iterator();
		while (it.hasNext()) {
			ps = ((PathwayStep) it.next());
			Iterator it1 = ps.getInteractions().iterator();
			while (it1.hasNext()) {
				inter = ((Interaction) it1.next());
				add(inter);
			}

			pathways.addAll(ps.getPathwaies());

			it1 = ps.getPathwaies().iterator();
			while (it1.hasNext()) {
				pw = ((Pathway) it1.next());
			}

			it1 = ps.getPathwayStepsForNextPathwaystep().iterator();
			while (it1.hasNext()) {
				ps = ((PathwayStep) it1.next());
			}
		}

		pathwayMap.put(input.getPkPathway(), true);

		it = pathways.iterator();
		while (it.hasNext()) {
			pw = ((Pathway) it.next());
			if (!pathwayMap.containsKey(pw.getPkPathway())) {
				add(pw);
			}
		}
	}

}
