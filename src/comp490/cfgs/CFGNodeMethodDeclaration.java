package comp490.cfgs;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CFGNodeMethodDeclaration extends CFGNode{
	private String className;
	
	public CFGNodeMethodDeclaration(MethodDeclaration methodDeclaration) {
		super(methodDeclaration);
		ASTNode node = methodDeclaration;
		while(!(node instanceof TypeDeclaration)) {
			node = node.getParent();
		}
		this.name = methodDeclaration.getName().getIdentifier();
		this.className = ((TypeDeclaration)node).getName().getIdentifier();
	}
	
	public CFGNodeMethodDeclaration(MethodDeclaration methodDeclaration, String name) {
		super(methodDeclaration);
		ASTNode node = methodDeclaration;
		while(!(node instanceof TypeDeclaration)) {
			node = node.getParent();
		}
		this.name = methodDeclaration.getName().getIdentifier();
		this.className = ((TypeDeclaration)node).getName().getIdentifier();
	}
	
	public CFGNodeMethodDeclaration(MethodDeclaration methodDeclaration, String name, String className) {
		super(methodDeclaration);
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
	@Override
	public String toString() {
		return className + "_" + name + "_" + lineNbr;
	}
	
	
}
