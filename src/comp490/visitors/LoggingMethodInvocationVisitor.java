package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

import comp490.logs.LoggingStatementFactory;

/**
 * Visitor used to count the number of logging methods in the source code
 *
 */
public class LoggingMethodInvocationVisitor extends ASTVisitor{
	
	private int invocationCount = 0;
	
	public boolean visit(MethodInvocation node) {
		if(LoggingStatementFactory.isLoggingStatement(node)) {
			invocationCount += 1;
		}
		return super.visit(node);
	}
	
	public int getInvocationCount() {
		return invocationCount;
	}
	
}
