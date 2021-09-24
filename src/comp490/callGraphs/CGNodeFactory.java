package comp490.callGraphs;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CGNodeFactory {
	
	private static Set<CGNode> methodsInCGChain = new HashSet<>();
	private static ASTMatcher astMatcher = new ASTMatcher();
	
	public static Set<CGNode> getMethodsInCGChain() {
		return methodsInCGChain;
	}
	
	public static CGNode makeCGNode(MethodDeclaration node) {
		if(node == null) {
			return null;
		}
		for(CGNode method : methodsInCGChain) {
			if(method.getDefaultConstructor() != null) {
				continue;
			}
			if(astMatcher.match(method.getMethodDeclaration(), node)) {
				return method;
			}
		}
		CGNode newNode = new CGNode(node);
		methodsInCGChain.add(newNode);
		return newNode;
	}
	
	public static CGNode makeCGNode(TypeDeclaration typeDeclaration) {
		ASTMatcher astMatcher = new ASTMatcher();
		for(CGNode method : methodsInCGChain) {
			if(method.getMethodCFG() != null) {
				continue;
			}
			
			if(astMatcher.match(typeDeclaration, method.getDefaultConstructor().getTypeDeclaration())) {
				return method;
			}
		}
		CGNode newNode = new CGNode(typeDeclaration);
		methodsInCGChain.add(newNode);
		return newNode;
	}
	
	public static void reset() {
		methodsInCGChain.clear();
	}
	
}
