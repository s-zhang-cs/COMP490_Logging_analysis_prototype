package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Base Control flow node, parent of more specific control flow nodes
 *
 */
public class CFGNode {
	
	/**
	 * Line number of the control flow node
	 */
	protected int lineNbr;
	/**
	 * Name (or type by default) of the control flow node
	 */
	protected String name;
	
	/**
	 * Associated ASTNode(s) of the control flow node
	 */
	List<ASTNode> ASTNodes;
	/**
	 * Control flow graph using adjacency list 
	 */
	List<CFGNode> CFGNodes;
	
	/**
	 * Constructor using type of ASTNode as CFGNode's name
	 * 
	 * @param astNode source ASTNode
	 */
	public CFGNode(ASTNode astNode) {
		lineNbr = ((CompilationUnit) astNode.getRoot()).getLineNumber(astNode.getStartPosition());
		this.name = "Type" + astNode.getNodeType();
		ASTNodes = new ArrayList<>();
		CFGNodes = new ArrayList<>();
		ASTNodes.add(astNode);
	}
	
	/**
	 * Constructor using custom name
	 * 
	 * @param astNode source ASTNode
	 * @param name custom name of the CFGNode
	 */
	public CFGNode(ASTNode astNode, String name) {
		lineNbr = ((CompilationUnit) astNode.getRoot()).getLineNumber(astNode.getStartPosition());
		this.name = name;
		ASTNodes = new ArrayList<>();
		CFGNodes = new ArrayList<>();
		ASTNodes.add(astNode);
	}

	public int getLineNbr() {
		return lineNbr;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ASTNode> getASTNodes() {
		return ASTNodes;
	}
	
	public List<CFGNode> getCFGNodes() {
		return CFGNodes;
	}
	
	public void addASTNode(ASTNode astNode) {
		ASTNodes.add(astNode);
	}
	
	public void addCFGNode(CFGNode cfgNode) {
		CFGNodes.add(cfgNode);
	}

	/**
	 * add a Control flow node to be adjacent to the current control flow node
	 * 
	 * @param cfgNode node adjacent to current node
	 */
	public void makeSequence(CFGNode cfgNode) {
		addCFGNode(cfgNode);
	}
	
	@Override
	public String toString() {
		return name + "_" + lineNbr;
	}
	
	/**
	 * Create a dot format String for the current control flow node's graph
	 * 
	 * @param traversed set keeping track of visited nodes to avoid infinite loop
	 * @return a String of dot format containing graph information
	 */
	public String makeDot(Set<CFGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		for(CFGNode cfgNode : CFGNodes) {
			str += this.toString() + " -> " + cfgNode.toString() + "\n";
		}
		for(CFGNode cfgNode : CFGNodes) {
			str += cfgNode.makeDot(traversed);
		}
		return str;
	}

}
