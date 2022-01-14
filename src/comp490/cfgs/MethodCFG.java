package comp490.cfgs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Wrapper class for CFGNode. It allows to create a control flow graph 
 * for the method declaration itself, and output it as a dot file
 *
 */
public class MethodCFG {
	
	private CFGNodeMethodDeclaration root;
	private CFGNode rootFirstStatement;
	private CFGNode rootLastStatement;
	private List<CFGNode> rootReturnStatements;
	
	public MethodCFG(MethodDeclaration node) {
		root = new CFGNodeMethodDeclaration(node);
		rootReturnStatements = new ArrayList<>();
	}
	
	public MethodCFG(TypeDeclaration node) {
		rootReturnStatements = new ArrayList<>();
	}
	
	public CFGNode getRoot() {
		return root;
	}
	
	public CFGNode getRootFirstStatement() {
		return rootFirstStatement;
	}
	
	public CFGNode getRootLastStatement() {
		return rootLastStatement;
	}
	
	public List<CFGNode> getRootReturnStatements() {
		return rootReturnStatements;
	}
	
	public void setRoot(CFGNodeMethodDeclaration root) {
		this.root = root;
	}
	
	//Creates link between method's block statements in chronological order.
	public void extractMethodCFG() {
		if(root != null) {
			Block methodBody = ((MethodDeclaration) root.getASTNodes().get(0)).getBody();
			if(methodBody.statements().size() != 0) {
				CFGNode firstStatementInMethod = null;
				CFGNode prev = null;
				for(Object i : methodBody.statements()) {
					CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode) i);
					if(prev != null) {
						prev.makeSequence(current);
					}
					else {
						rootFirstStatement = current;
						firstStatementInMethod = current;
					}
					if(current instanceof CFGNodeReturnStatement) {
						rootReturnStatements.add(current);
					}
					prev = current;
				}
				rootLastStatement = prev;
				root.addCFGNode(firstStatementInMethod);
			}
		}
	}
	
	public void makeDotFile() {
		try {
			if(root != null) {
				String fileName = root.toString();
				File file = new File(Config.DOTFILEPATH + fileName);
				FileWriter fw = new FileWriter(file, true);
				String dotFileContent = root.makeDot(new HashSet<CFGNode>());
				fw.write("digraph G {\n" + trimDotContent(dotFileContent) + "}\n");
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//remove duplicate edges
	private String trimDotContent(String raw) {
		Set<String> uniqueStrs = new HashSet<>();
		for(String i : raw.split("\n")) {
			uniqueStrs.add(i);
		}
		String trimmed = "";
		for(String i : uniqueStrs) {
			trimmed += i + "\n";
		}
		return trimmed;
	}

}
