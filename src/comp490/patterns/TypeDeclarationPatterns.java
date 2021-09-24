package comp490.patterns;

import org.eclipse.jdt.core.ICompilationUnit;

import comp490.handlers.JavaModel;
import comp490.visitors.ClassDeclarationVisitor;
import comp490.visitors.InterfaceDeclarationVisitor;

public class TypeDeclarationPatterns {
	
	public int countNbrOfClasses() {
		ClassDeclarationVisitor classVisitor = new ClassDeclarationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(classVisitor);
		}
		return classVisitor.getClassCount();
	}
	
	public int countNbrOfInterfaces() {
		InterfaceDeclarationVisitor interfaceVisitor = new InterfaceDeclarationVisitor();
		for(ICompilationUnit iCompilationUnit : JavaModel.getICompilationUnits()) {
			org.eclipse.jdt.core.dom.CompilationUnit compilationUnit = JavaModel.parse(iCompilationUnit);
			compilationUnit.accept(interfaceVisitor);
		}
		return interfaceVisitor.getClassCount();
	}
	
}
