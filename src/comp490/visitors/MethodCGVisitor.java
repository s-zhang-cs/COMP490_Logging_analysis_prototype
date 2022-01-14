package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import comp490.callGraphs.CGNode;
import comp490.callGraphs.CGNodeFactory;
import comp490.callGraphs.LoggingMethodCG;
import comp490.handlers.JavaModel;
import comp490.logs.LoggingStatementFactory;


/**
 * Visitor used to make call graph reaching all logging methods
 *
 */
public class MethodCGVisitor extends ASTVisitor{
	
	private LoggingMethodCG loggingMethods = new LoggingMethodCG();
	
	public boolean visit(MethodInvocation node) {

		MethodDeclaration caller = JavaModel.getParentMethodDeclarationForMethodInvocation(node);
		
		CGNode cgNode;
		//logging statements
		if(LoggingStatementFactory.isLoggingStatement(node)) {
			cgNode = CGNodeFactory.makeCGNode(node);
			loggingMethods.addRoot(cgNode);
		}
		//other method invocations
		else {
			cgNode = CGNodeFactory.makeCGNode(JavaModel.getMethodDeclarationFromMethodInvocation(node));
		}
		//skip external methods such as "System.out.println"
		if(cgNode != null) {
			cgNode.addCaller(CGNodeFactory.makeCGNode(caller));
		}
		return super.visit(node);
	}
		
	public LoggingMethodCG getLoggingMethods() {
		return loggingMethods;
	}
}
