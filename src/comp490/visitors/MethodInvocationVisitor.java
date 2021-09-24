package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor{
	
	private int invocationCount = 0;
	
	public boolean visit(MethodInvocation node) {
		invocationCount += 1;
		return super.visit(node);
	}
	
	public int getMethodCount() {
		return invocationCount;
	}
}
