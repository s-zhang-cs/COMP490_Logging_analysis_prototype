package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import comp490.handlers.JavaModel;

public class LoggingMethodInvocationVisitor extends ASTVisitor{
	
	private int invocationCount = 0;
	
	public boolean visit(MethodInvocation node) {
		if(node.getName().getIdentifier().contains("log")) {
			invocationCount += 1;
			int lineNbr = 0;
			ASTNode parentClass = node.getParent();
			ASTNode parentPackage = null;
			
			while(!(parentClass instanceof TypeDeclaration)) {
				parentClass = parentClass.getParent();
			}
			
			lineNbr = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
			
			if(node.getRoot().getClass() == CompilationUnit.class) {
				CompilationUnit root = (CompilationUnit) node.getRoot();
				if(root.getPackage() != null) {
					parentPackage = root.getPackage();
				}
			}
			
			JavaModel.getLoggingMethodInvocations().add(new JavaModel.LoggingMethod(lineNbr, node, parentClass, parentPackage));
		}
		return super.visit(node);
	}
	
	public int getInvocationCount() {
		return invocationCount;
	}
	
}
