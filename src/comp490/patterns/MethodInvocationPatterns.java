package comp490.patterns;

import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;

import comp490.callGraphs.CGNode;
import comp490.callGraphs.CGNodeFactory;
import comp490.handlers.JavaModel;
import comp490.visitors.MethodCGVisitor;
import comp490.visitors.LoggingMethodInvocationVisitor;
import comp490.visitors.MethodInvocationVisitor;
import comp490.visitors.SearchMethodInvocationVisitor;

public class MethodInvocationPatterns {
	
	public int countNbrOfMethodInvocations() {
		MethodInvocationVisitor methodVisitor = new MethodInvocationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(methodVisitor);
		}
		return methodVisitor.getMethodCount();
	}
	
	public int countNbrOfMethodInvocations(String methodName) {
		SearchMethodInvocationVisitor searchMethodDeclarationVisitor = new SearchMethodInvocationVisitor(methodName);
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(searchMethodDeclarationVisitor);
		}
		return searchMethodDeclarationVisitor.getMethodCount();
	}
	
	public int countNbrOfLoggingMethodInvocations() {
		LoggingMethodInvocationVisitor loggingMethodInvocationVisitor = new LoggingMethodInvocationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(loggingMethodInvocationVisitor);
		}
		return loggingMethodInvocationVisitor.getInvocationCount();
	}
	
	public void extractCG(String packageName, String className, String methodName) {
		MethodCGVisitor callGraphVisitor = new MethodCGVisitor(packageName, className, methodName);
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(callGraphVisitor);
		}
		callGraphVisitor.getRoot().makeCGDotFile();
	}
	
	public void extractICFG(String packageName, String className, String methodName) {
		MethodCGVisitor callGraphVisitor = new MethodCGVisitor(packageName, className, methodName);
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(callGraphVisitor);
		}
		callGraphVisitor.getRoot().makeICFGDotFile();
	}
	
}
