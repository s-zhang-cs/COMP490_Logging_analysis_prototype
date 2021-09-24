package comp490.patterns;

import org.eclipse.jdt.core.ICompilationUnit;

import comp490.cfgs.MethodCFG;
import comp490.handlers.JavaModel;
import comp490.visitors.LoggingMethodDeclarationVisitor;
import comp490.visitors.MethodCFGVisitor;
import comp490.visitors.MethodDeclarationVisitor;
import comp490.visitors.SearchMethodDeclarationVisitor;

public class MethodDeclarationPatterns {

	public int countNbrOfMethods() {
		MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(methodVisitor);
		}
		return methodVisitor.getMethodCount();
	}
	
	public int countNbrOfMethods(String methodName) {
		SearchMethodDeclarationVisitor searchMethodDeclarationVisitor = new SearchMethodDeclarationVisitor(methodName);
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(searchMethodDeclarationVisitor);
		}
		return searchMethodDeclarationVisitor.getMethodCount();
	}
	
	public int countNbrOfLoggingMethods() {
		LoggingMethodDeclarationVisitor loggingMethodDeclarationVisitor = new LoggingMethodDeclarationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(loggingMethodDeclarationVisitor);
		}
		return loggingMethodDeclarationVisitor.getMethodCount();
	}
	
	public void extractCFG() {
		MethodCFGVisitor methodCFGVisitor = new MethodCFGVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(methodCFGVisitor);
		}
		for(MethodCFG methodCFG : methodCFGVisitor.getMethodCFGs()) {
			methodCFG.makeDotFile();
		}
	}
	
}
