package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;


import comp490.handlers.JavaModel;

public class LoggingMethodDeclarationVisitor extends ASTVisitor{
	
	private int loggingMethodCount = 0;
	
	public boolean visit(MethodDeclaration node) {
		if(node.getName().getIdentifier().contains("log")) {
			loggingMethodCount += 1;
			int lineNbr = 0;
			ASTNode parentClass = node.getParent();
			ASTNode parentPackage = null;
			
			lineNbr = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
			
			if(node.getRoot().getClass() == CompilationUnit.class) {
				CompilationUnit root = (CompilationUnit) node.getRoot();
				if(root.getPackage() != null) {
					parentPackage = root.getPackage();
				}
			}
			
			JavaModel.getLoggingMethods().add(new JavaModel.LoggingMethod(lineNbr, node, parentClass, parentPackage));
		}
		return super.visit(node);
	}
	
	public int getMethodCount() {
		return loggingMethodCount;
	}
	
}
