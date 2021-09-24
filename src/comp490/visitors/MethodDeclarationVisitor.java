package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
	
	private int methodCount = 0;
	
	public boolean visit(MethodDeclaration node) {
		
		methodCount += 1;
		return super.visit(node);
	}
	
	public int getMethodCount() {
		return methodCount;
	}
}
