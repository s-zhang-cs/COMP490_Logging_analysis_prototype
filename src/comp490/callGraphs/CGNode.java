package comp490.callGraphs;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import comp490.cfgs.MethodCFG;

/**
 * Base call graph node.
 *
 */
public class CGNode {
	
	private boolean isLoggingMethod;
	private int loggingMethodLineNbr;
	private MethodCFG methodCFG;
	private Set<CGNode> callers;
	
	//for explicit methods (including constructors)
	public CGNode(MethodDeclaration methodDeclaration) {
		isLoggingMethod = false;
		methodCFG = new MethodCFG(methodDeclaration);
		callers = new HashSet<>();
	}
	
	//for default constructors
	public CGNode(TypeDeclaration typeDeclaration) {
		isLoggingMethod = false;
		methodCFG = new MethodCFG(typeDeclaration);
		callers = new HashSet<>();
	}
	
	//for logging methods
	public CGNode(MethodInvocation methodInvocation) {
		isLoggingMethod = true;
		loggingMethodLineNbr = ((CompilationUnit) methodInvocation.getRoot()).getLineNumber(methodInvocation.getStartPosition());
		callers = new HashSet<>();
	}
	
	public MethodCFG getMethodCFG() {
		return methodCFG;
	}
	
	public void addCaller(CGNode node) {
		callers.add(node);
	}	
		
	public MethodDeclaration getMethodDeclaration() {
		if(methodCFG == null || methodCFG.getRoot() == null) {
			return null;
		}
		return (MethodDeclaration) methodCFG.getRoot().getASTNodes().get(0);
	}
	
	public String makeDotCG(Set<CGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		for(CGNode caller : callers) {
			str += caller.toString() + " -> " + this.toString() + "\n";
		}
		for(CGNode caller : callers) {
			str += caller.makeDotCG(traversed);
		}
		return str;
	}
	
	@Override
	public String toString() {
		if(isLoggingMethod) {
			return "LoggingMethod_" + loggingMethodLineNbr;
		}
		if(methodCFG != null && methodCFG.getRoot() != null){
			return methodCFG.getRoot().toString();
		}
		return "";
	}
}
