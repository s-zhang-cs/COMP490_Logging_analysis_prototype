package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class InterfaceDeclarationVisitor extends ASTVisitor{

private int interfaceCount = 0;
	
	public boolean visit(TypeDeclaration node) {
		if(node.isInterface()) {
			interfaceCount += 1;
		}
		return super.visit(node);
	}
	
	public int getClassCount() {
		return interfaceCount;
	}
}
