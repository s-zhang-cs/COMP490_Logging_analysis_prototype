package comp490.handlers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JavaModel {
	
	public static class LoggingMethod {
		
		int lineNbr;
		ASTNode method;
		ASTNode parentClass;
		ASTNode parentPackage;
		
		public LoggingMethod(int lineNbr, ASTNode method, ASTNode parentClass, ASTNode parentPackage) {
			this.lineNbr = lineNbr;
			this.method = method;
			this.parentClass = parentClass;
			this.parentPackage = parentPackage;
		}
		
		ASTNode getMethod() {
			return method;
		}
		
		ASTNode getParentClass() {
			return parentClass;
		}
		
		ASTNode getParentPackage() {
			return parentPackage;
		}
		
		int getLineNbr() {
			return lineNbr;
		}
	}
	
	public static class DefaultConstructor {
		
		TypeDeclaration typeDeclaration;
		String className;
		String packageName;
		
		public DefaultConstructor(TypeDeclaration typeDeclaration) {
			this.typeDeclaration = typeDeclaration;
			this.className = typeDeclaration.getName().getIdentifier();
			this.packageName = ((CompilationUnit)typeDeclaration.getRoot()).getPackage().getName().getFullyQualifiedName();
		}
		
		public String getClassName() {
			return className;
		}
		
		public String getPackageName() {
			return packageName;
		}
		
		public TypeDeclaration getTypeDeclaration() {
			return typeDeclaration;
		}
		
		public String toString() {
			return className + "_" + className + "_default";
		}
		
		public String linkICFG() {
			//still need to complete:
			// - find all the class instantiations within the parent class outside of constructors
			// - point to (first statement of) first class instantiation
			// - link all subsequent class instantiations 
			// - point from last statement of last class instantiation back to default constructor
			return null;
		}
		
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(obj instanceof DefaultConstructor) {
				if(((DefaultConstructor)obj).getClassName().equals(className) &&
						((DefaultConstructor)obj).getPackageName().equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}
	
	private static ArrayList<LoggingMethod> loggingMethods = new ArrayList<>();
	private static ArrayList<LoggingMethod> loggingMethodInvocations = new ArrayList<>();
	
	//TODO:
	//still need to find a way to assess equality between different MethodInvocations and ClassInstanceCreations... 
	private static Map<MethodInvocation, MethodDeclaration> methodBindingMap = new HashMap<>();
	private static Map<ClassInstanceCreation, TypeDeclaration> classBindingMap = new HashMap<>();
	
	private static ICompilationUnit[] iCompilationUnits = getICompilationUnitsUnderIWorkspaceRoot();
	
	public static ArrayList<LoggingMethod> getLoggingMethods() {
		return loggingMethods;
	}
	
	public static ArrayList<LoggingMethod> getLoggingMethodInvocations() {
		return loggingMethodInvocations;
	}
	
	public static ICompilationUnit[] getICompilationUnits() {
		return iCompilationUnits;
	}
	
	public static MethodDeclaration getMethodDeclarationFromMethodInvocation(MethodInvocation node) {
		ASTMatcher astMatcher = new ASTMatcher();
        for (Map.Entry<MethodInvocation, MethodDeclaration> entry : methodBindingMap.entrySet()) {
        	if(astMatcher.match(entry.getKey(), node)) {
        		return entry.getValue();
        	}
        }
		IMethodBinding binding = node.resolveMethodBinding();
		ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor(IJavaElement.COMPILATION_UNIT);
		//for external methods such as "System.out.println()"
		if(unit == null) {
			return null;
		}
		MethodDeclaration methodDeclaration = (MethodDeclaration) JavaModel.parse(unit).findDeclaringNode(binding.getKey());
		methodBindingMap.put(node, methodDeclaration);
		return methodDeclaration;
	}
	
	public static TypeDeclaration getTypeDeclarationFromClassInstanceCreation(ClassInstanceCreation classInstanceCreation) {
		ASTMatcher astMatcher = new ASTMatcher();
        for (Map.Entry<ClassInstanceCreation, TypeDeclaration> entry : classBindingMap.entrySet()) {
        	if(astMatcher.match(entry.getKey(), classInstanceCreation)) {
        		return entry.getValue();
        	}
        }
		ITypeBinding binding = classInstanceCreation.resolveTypeBinding();
		ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor(IJavaElement.COMPILATION_UNIT);
		//for external classes
		if(unit == null) {
			return null;
		}
		TypeDeclaration typeDeclaration = (TypeDeclaration) JavaModel.parse(unit).findDeclaringNode(binding.getKey());
		classBindingMap.put(classInstanceCreation, typeDeclaration);
		return typeDeclaration;
	}
	
	
	public static MethodDeclaration getConstructorMethodFromClassInstantiation(ClassInstanceCreation node) {
		IMethodBinding binding = node.resolveConstructorBinding();
		//compiler generated default constructor
		if(binding.isDefaultConstructor()) {
			return null;
		}
		else {
			ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor(IJavaElement.COMPILATION_UNIT);
			MethodDeclaration methodDeclaration = (MethodDeclaration) JavaModel.parse(unit).findDeclaringNode(binding.getKey());
			return methodDeclaration;
		}
	}
	
	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS15);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		return (CompilationUnit) parser.createAST(null);
	}
	
	public static ICompilationUnit[] getICompilationUnitsUnderIWorkspaceRoot(){
		ICompilationUnit[] iCompilationUnits = null;
		IProject[] iProjects = getIProjectsUnderIWorkspaceRoot();
		//SampleHandler.printMessage(iProjects.toString());
		
		for(IProject iProject : iProjects) {
			IPackageFragment[] iPackageFragments = getIPackageFragmentsUnderIProject(iProject);
			//SampleHandler.printMessage(iPackageFragments.toString());
			for(IPackageFragment iPackageFragment : iPackageFragments) {
				iCompilationUnits = concatenate(iCompilationUnits, getICompilationUnitsUnderIPackageFragment(iPackageFragment));
				//SampleHandler.printMessage(iCompilationUnits.toString());
			}
		}
		return iCompilationUnits;
	}
	
	private static IProject[] getIProjectsUnderIWorkspaceRoot() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getProjects();
	}
	
	private static IPackageFragment[] getIPackageFragmentsUnderIProject(IProject iProject) {
		IPackageFragment[] packages = null;
		try {
			packages = JavaCore.create(iProject).getPackageFragments();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packages;
	}
	
	private static ICompilationUnit[] getICompilationUnitsUnderIPackageFragment(IPackageFragment iPackageFragment) {
		ICompilationUnit[] iCompilationUnits = null;
		try {
			iCompilationUnits = iPackageFragment.getCompilationUnits();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iCompilationUnits;
	}
	
	private static <T> T[] concatenate(T[] a, T[] b) {
		if(a == null && b == null) {
			return null;
		}
		else if(a == null) {
			return b;
		}
		else if(b == null) {
			return a;
		}
		
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
	
}
