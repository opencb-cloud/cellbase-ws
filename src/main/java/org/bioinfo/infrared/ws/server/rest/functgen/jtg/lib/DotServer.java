package org.bioinfo.infrared.ws.server.rest.functgen.jtg.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bioinfo.formats.core.graph.dot.Dot;
import org.bioinfo.formats.core.graph.dot.Edge;
import org.bioinfo.formats.core.graph.dot.Node;
import org.bioinfo.infrared.core.biopax.v3.Complex;
import org.bioinfo.infrared.core.biopax.v3.Control;
import org.bioinfo.infrared.core.biopax.v3.Conversion;
import org.bioinfo.infrared.core.biopax.v3.Interaction;
import org.bioinfo.infrared.core.biopax.v3.Pathway;
import org.bioinfo.infrared.core.biopax.v3.PathwayStep;
import org.bioinfo.infrared.core.biopax.v3.PhysicalEntity;
import org.bioinfo.infrared.core.biopax.v3.Protein;
import org.bioinfo.infrared.core.biopax.v3.Term;

public class DotServer {

	private Dot dot = null;
	
	private int splitChunk = 13;
	private String splitSep = "\\n";
	
	private BioPaxServer bpServer = null;
	Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
	
	public DotServer() {
		bpServer = new BioPaxServer();
	}
	
	//=========================================================================
	//	generate DOT
	//=========================================================================
	
	public Dot generateDot(Pathway input) {
		dot = new Dot(bpServer.getFirstName(input.getBioEntity()), true);
		dot.setAttribute(Dot.BGCOLOR, "white");
		
		add(input);
		
		bpServer.getSession().clear();
		return dot;
	}
	
	public Dot generateDot(Complex input) {
		dot = new Dot(bpServer.getFirstName(input.getPhysicalEntity().getBioEntity()), true);
		dot.setAttribute(Dot.BGCOLOR, "white");
		
		add(input);
		
		bpServer.getSession().clear();
		return dot;
	}

	//=========================================================================
	//	Complex
	//=========================================================================

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

	//=========================================================================
	//	Physical Entities
	//=========================================================================

	private void addNode(PhysicalEntity input) {
				
		Node node = new Node("entity_" + input.getBioEntity().getPkEntity());
		
		String name = bpServer.getFirstName(input.getBioEntity());		
		//node.setAttribute(Node.LABEL, insertStringEvery(name, splitSep, splitChunk));
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
	}
	

	//=========================================================================
	//	Interactions
	//=========================================================================
	
	//-------------------------------------------------------------------------
	//	Control
	//-------------------------------------------------------------------------
	
	private String addEdges(Control input) {
		StringBuilder output = new StringBuilder();		
		
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
					//output.append(getDot(pe));
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

		String arrowhead = "none";
		if ("activation".equalsIgnoreCase(input.getControlType())) {
			if (bpServer.isCatalysis(input)) {
				arrowhead = "odot";				
			} else {
				arrowhead = "empty";
			}
		} else if ("inhibition".equalsIgnoreCase(input.getControlType())) {
			arrowhead = "tee";				
		}
		
		
		if (controlledIt!=null && controllerPes.size()>0) {
			Edge edge = null;
			for(PhysicalEntity pe: controllerPes) {
				edge = new Edge("entity_" + pe.getBioEntity().getPkEntity(), "entity_" + controlledIt.getBioEntity().getPkEntity(), true);
				edge.setAttribute(Edge.ARROWHEAD, arrowhead);
				dot.addEdge(edge);
			}
		} else {
//			if (controlledIt==null) {
//				System.out.println("!! controlled interaction is missing (interaction: " + name + ")");					
//			}
//			if (controllerNames.size()==0) {
//				System.out.println("!! controller names are missing (interaction: " + name + ")");
//			}
		}
		
		return output.toString();
	}

	//-------------------------------------------------------------------------
	//	Conversion
	//-------------------------------------------------------------------------
	
	private void addEdges(Conversion input) {
				
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
		String name = bpServer.getFirstName(input.getInteraction().getBioEntity());
		if (leftPes.size()>0 && rightPes.size()>0) {
			Node node = new Node("entity_" + input.getInteraction().getBioEntity().getPkEntity());
			//node.setAttribute(Node.LABEL, insertStringEvery(name, splitSep, splitChunk));
			node.setAttribute(Node.LABEL, name);
			node.setAttribute(Node.SHAPE, Node.SHAPE_VALUES.box.name());
			dot.addNode(node);
			
			Edge edge = null;
			for(PhysicalEntity l: leftPes) {
				edge = new Edge("entity_" + l.getBioEntity().getPkEntity(), "entity_" + input.getInteraction().getBioEntity().getPkEntity(), true);
				dot.addEdge(edge);
			}
			for(PhysicalEntity r: rightPes) {
				edge = new Edge("entity_" + input.getInteraction().getBioEntity().getPkEntity(), "entity_" + r.getBioEntity().getPkEntity(), true);
				dot.addEdge(edge);
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

		map.put(input.getPkPathway(), true);
		
		it = pathways.iterator();
		while (it.hasNext()) {
			pw = ((Pathway) it.next());
			if (!map.containsKey(pw.getPkPathway())) {
				add(pw);
			}
		}
	}
	
	//=========================================================================
	//	utils
	//=========================================================================
	
	private String insertStringEvery(String input, String sep, int splitChunk) {
		StringBuilder sb = new StringBuilder();
		
		String[] words = input.replace("\"", "'").split(" ");
		
		int count = 0;
		for(String w: words) {
			sb.append(w).append(" ");
			count += w.length();
			if (count>=splitChunk) {
				sb.append(sep);
				count = 0;
			}
		}
		
//		for(int i=0; i<input.length() ; i++) {
//			sb.append(input.charAt(i));
//			if (i>0 && i%splitChunk==0) {
//				sb.append(sep);
//			}
//		}

//		String aux = input;
//		while (aux.length()>splitChunk) {
//			sb.append(aux.substring(0, splitChunk)).append("\n");
//			aux.s
//		}
//		sb.append(aux);
		
		return sb.toString();
	}
	
}
