package comp490.callGraphs;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Provides CGNode based on JDT's MethodDeclaration. 1 to 1 relationship between
 * MethodDeclaration and CGNode. 
 *
 */
public class CGNodeFactory {
	
	private static Set<CGNode> methodsInCGChain = new HashSet<>();
	private static ASTMatcher astMatcher = new ASTMatcher();
	
	public static Set<CGNode> getMethodsInCGChain() {
		return methodsInCGChain;
	}
	
	//for non-logging methods 
	public static CGNode makeCGNode(MethodDeclaration node) {
		if(node == null) {
			return null;
		}
		for(CGNode method : methodsInCGChain) {
			if(astMatcher.match(method.getMethodDeclaration(), node)) {
				return method;
			}
		}
		CGNode newNode = new CGNode(node);
		methodsInCGChain.add(newNode);
		return newNode;
	}
	
	//for logging methods
	public static CGNode makeCGNode(MethodInvocation node) {
		if(node == null) {
			return null;
		}
		return new CGNode(node);
	}
	
	public static void reset() {
		methodsInCGChain.clear();
	}
	
}
