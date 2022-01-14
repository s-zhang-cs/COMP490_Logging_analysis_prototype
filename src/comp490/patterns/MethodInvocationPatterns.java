package comp490.patterns;

import org.eclipse.jdt.core.ICompilationUnit;

import comp490.callGraphs.CGNode;
import comp490.handlers.JavaModel;
import comp490.visitors.MethodCGVisitor;
import comp490.visitors.LoggingMethodInvocationVisitor;

public class MethodInvocationPatterns {
	
	/**
	 * Count the number of logging methods present in the source code.
	 * Logging methods are methods from common Java logging frameworks specified 
	 * in "LoggingStatementFactory.java" under comp490.logs
	 * 
	 * @return int number of logging methods
	 */
	public int countNbrOfLoggingMethodInvocations() {
		LoggingMethodInvocationVisitor loggingMethodInvocationVisitor = new LoggingMethodInvocationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(loggingMethodInvocationVisitor);
		}
		return loggingMethodInvocationVisitor.getInvocationCount();
	}
	
	/**
	 * Make a call graph of dot format resulting from a specific node in the AST.
	 * 
	 * The location of the resulting dot files is specified under "Config.java" under package "comp490.cfgs"
	 */
	public void extractCG() {
		MethodCGVisitor callGraphVisitor = new MethodCGVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(callGraphVisitor);
		}
		callGraphVisitor.getLoggingMethods().makeCGDotFile();
	}
	
}
