package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassDeclarationVisitor extends ASTVisitor {
	
	private int classCount = 0;
	
	public boolean visit(TypeDeclaration node) {
		if(!node.isInterface()) {
			classCount += 1;
		}
		return super.visit(node);
	}
	
	public int getClassCount() {
		return classCount;
	}
}