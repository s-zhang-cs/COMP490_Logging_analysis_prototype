package comp490.callGraphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import comp490.cfgs.MethodCFG;
import comp490.handlers.JavaModel.DefaultConstructor;

public class CGNode {
	
	private MethodCFG methodCFG;
	private List<CGNode> callers;
	private ASTMatcher astMatcher;
	
	//for explicit methods (including constructors)
	public CGNode(MethodDeclaration methodDeclaration) {
		methodCFG = new MethodCFG(methodDeclaration);
		callers = new ArrayList<>();
		astMatcher = new ASTMatcher();
	}
	
	//for default constructors
	public CGNode(TypeDeclaration typeDeclaration) {
		methodCFG = new MethodCFG(typeDeclaration);
		callers = new ArrayList<>();
		astMatcher = new ASTMatcher();
	}
	
	public MethodCFG getMethodCFG() {
		return methodCFG;
	}
	
	public void addCaller(CGNode node) {
		for(CGNode caller : callers) {
			if(caller.getMethodDeclaration() != null && astMatcher.match(caller.getMethodDeclaration(), node)) {
				return;
			}
			if(caller.getDefaultConstructor() != null && caller.getDefaultConstructor().equals(node.getDefaultConstructor())) {
				return;
			}
		}
		callers.add(node);
	}
	
	public MethodDeclaration getMethodDeclaration() {
		if(methodCFG == null || methodCFG.getRoot() == null) {
			return null;
		}
		return (MethodDeclaration) methodCFG.getRoot().getASTNodes().get(0);
	}
	
	public DefaultConstructor getDefaultConstructor() {
		if(methodCFG == null || methodCFG.getRootConstructor() == null) {
			return null;
		}
		return methodCFG.getRootConstructor();
	}
	
	public String makeDotCG(Set<CGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		for(CGNode caller : callers) {
			str += caller.toString() + " -> " + toString() + "\n";
		}
		for(CGNode caller : callers) {
			str += caller.makeDotCG(traversed);
		}
		return str;
	}
	
	public String makeDotICFG(Set<CGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		if(methodCFG != null) {
			methodCFG.extractMethodCFG();
			str += methodCFG.makeDotForICFG();
		}
		for(CGNode caller : callers) {
			str += caller.makeDotICFG(traversed);
		}
		return str;
	}
	
	public String toString() {
		if(methodCFG != null && methodCFG.getRootConstructor() != null) {
			return methodCFG.getRootConstructor().toString();
		}
		else if(methodCFG != null && methodCFG.getRoot() != null){
			return methodCFG.getRoot().toString();
		}
		return null;
	}
}
