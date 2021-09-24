package comp490.cfgs;

import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class CFGNodeExpressionStatement extends CFGNode{
	private MethodInvocation methodInvocation;
	
	public CFGNodeExpressionStatement(ExpressionStatement expressionStatement, String name) {
		super(expressionStatement, name);
		if(expressionStatement.getExpression() instanceof MethodInvocation) {
			methodInvocation = (MethodInvocation) expressionStatement.getExpression();
		}
	}
	
	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}
	
	public String toString() {
		if(methodInvocation != null) {
			return name + "_" + methodInvocation.getName().getIdentifier() + "_" + lineNbr;
		}
		return super.toString();
	}
	
}
