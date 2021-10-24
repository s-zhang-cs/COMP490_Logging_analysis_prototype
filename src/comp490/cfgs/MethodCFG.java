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

import comp490.handlers.JavaModel.DefaultConstructor;

public class MethodCFG {
	
	private CFGNodeMethodDeclaration root;
	private DefaultConstructor rootConstructor;
	private CFGNode rootFirstStatement;
	private CFGNode rootLastStatement;
	private List<CFGNode> rootReturnStatements;
	
	public MethodCFG(MethodDeclaration node) {
		CFGNodeFactory.reset();
		root = new CFGNodeMethodDeclaration(node);
		rootReturnStatements = new ArrayList<>();
	}
	
	public MethodCFG(TypeDeclaration node) {
		CFGNodeFactory.reset();
		rootConstructor = new DefaultConstructor(node);
		rootReturnStatements = new ArrayList<>();
	}
	
	public CFGNode getRoot() {
		return root;
	}
	
	public DefaultConstructor getRootConstructor() {
		return rootConstructor;
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
		else if(rootConstructor != null) {
			
		}
	}
	
	public String makeDotForICFG() {
		if(root != null) {
			String dotString = trimDotContent(root.makeDot(new HashSet<CFGNode>()));
			dotString = "subgraph cluster_" + root.getName() + "{\n" + dotString;
			dotString += "label = " + root.getClassName() + "_" + root.getName() + "\n}\n";
			dotString += root.linkICFG(new HashSet<CFGNode>());
			return dotString;
		}
		if(rootConstructor != null) {
			String className = rootConstructor.getClassName();
			String dotString = "subgraph cluster_" + className + "{\n" + "label = " + className + "_" + className + "\n";
			dotString += rootConstructor.toString() + "\n}\n";
			//here to do something with default constructor:
			//	dotString += rootConstructor.linkICFG();
			return dotString;
		}
		return null;
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
			else if(rootConstructor != null) {
				//do nothing for default constructor (design choice)
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//this function is needed because during branching, different branch tails 
	//will converge to a same node. Current makeDotFile function  will then create
	//duplicate edges in output, which need to be trimmed.
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
