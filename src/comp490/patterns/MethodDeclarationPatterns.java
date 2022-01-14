package comp490.patterns;

import org.eclipse.jdt.core.ICompilationUnit;

import comp490.cfgs.MethodCFG;
import comp490.handlers.JavaModel;
import comp490.visitors.MethodCFGVisitor;

public class MethodDeclarationPatterns {

	/**
	 * Make a control flow graph of dot format for each method in the AST.
	 * 
	 * The location of the resulting dot files is specified under "Config.java" under package "comp490.cfgs"
	 */
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
