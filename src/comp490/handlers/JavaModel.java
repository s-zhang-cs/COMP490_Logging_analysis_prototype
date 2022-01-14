package comp490.handlers;

import java.lang.reflect.Array;
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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class JavaModel {
	
	/**
	 * Internal map mapping a string of form "packageName_ClassName_MethodName" to the corresponding MethodDeclaration
	 */
	public static Map<String, MethodDeclaration> methodDeclarationMap = new HashMap<>();
	
	/**
	 * Internal array of all the compilation units under the work space
	 */
	private static ICompilationUnit[] iCompilationUnits = getICompilationUnitsUnderIWorkspaceRoot();
	
	/**
	 * Getter of iCompilationUnits
	 * @return iCompilationUnits
	 */
	public static ICompilationUnit[] getICompilationUnits() {
		return iCompilationUnits;
	}
	
	/**
	 * Get associated MethodDeclaration of a MethodInvocation. The matching mechanism 
	 * is by comparing package name plus class name plus method name. 
	 * 
	 * If the method is external (not from parsed source code), return null.
	 * 
	 * @param node source MethodInvocation 
	 * @return associated MethodDeclaration
	 */
	public static MethodDeclaration getMethodDeclarationFromMethodInvocation(MethodInvocation node) {
		IMethodBinding binding = node.resolveMethodBinding();
		if(binding == null) {
			return null;
		}
		String methodName = binding.getName();
		String className = binding.getDeclaringClass().getQualifiedName();
		String key = className + "." + methodName;
		
		if(methodDeclarationMap.containsKey(key)) {
			return methodDeclarationMap.get(key);
		}
		
		ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor(IJavaElement.COMPILATION_UNIT);
		//for external methods such as "System.out.println()"
		if(unit == null) {
			return null;
		}
		MethodDeclaration methodDeclaration = (MethodDeclaration) JavaModel.parse(unit).findDeclaringNode(binding.getKey());
		className = methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName();
		methodName = methodDeclaration.getName().getFullyQualifiedName();
		key = className + "." + methodName;
		methodDeclarationMap.put(key, methodDeclaration);
		return methodDeclaration;
	}
	
	/**
	 * Get the scope (MethodDeclaration) of a method invocation
	 * 
	 * In case of a method invocation outside of a method declaration, return null
	 * eg. class MyClass {
	 * 			Object a = someMethod();
	 * 		}
	 * 
	 * @param node source method invocation
	 * @return MethodDeclaration scope of the source method invocation
	 */
	public static MethodDeclaration getParentMethodDeclarationForMethodInvocation(MethodInvocation node) {
		ASTNode parent = node.getParent();
		while(!(parent == null) && !(parent instanceof MethodDeclaration)) {
			parent = parent.getParent();
		}
		if(parent == null) return null;
		return (MethodDeclaration) parent;
	}
	
	/**
	 * Parse a compilation unit and generate AST
	 * 
	 * @param unit source compilation unit
	 * @return ASTNode root of the AST
	 */
	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS15);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		return (CompilationUnit) parser.createAST(null);
	}
	
	/**
	 * Get compilation units under a workspace as an array
	 *  
	 * @return ICompilationUnit[] 
	 */
	public static ICompilationUnit[] getICompilationUnitsUnderIWorkspaceRoot(){
		ICompilationUnit[] iCompilationUnits = null;
		IProject[] iProjects = getIProjectsUnderIWorkspaceRoot();
		
		for(IProject iProject : iProjects) {
			IPackageFragment[] iPackageFragments = getIPackageFragmentsUnderIProject(iProject);
			for(IPackageFragment iPackageFragment : iPackageFragments) {
				iCompilationUnits = concatenate(iCompilationUnits, getICompilationUnitsUnderIPackageFragment(iPackageFragment));
			}
		}
		return iCompilationUnits;
	}
	
	/**
	 * Get all the projects under a workspace as an array
	 * 
	 * @return IProject[]
	 */
	private static IProject[] getIProjectsUnderIWorkspaceRoot() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		return root.getProjects();
	}
	
	/**
	 * Get all the packages under a project as an array 
	 * 
	 * @param iProject source project
	 * @return IPackageFragment[]
	 */
	private static IPackageFragment[] getIPackageFragmentsUnderIProject(IProject iProject) {
		IPackageFragment[] packages = null;
		try {
			packages = JavaCore.create(iProject).getPackageFragments();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return packages;
	}
	
	/**
	 * Get all the compilation units under a package 
	 * 
	 * @param iPackageFragment source package
	 * @return ICompilationUnit[]
	 */
	private static ICompilationUnit[] getICompilationUnitsUnderIPackageFragment(IPackageFragment iPackageFragment) {
		ICompilationUnit[] iCompilationUnits = null;
		try {
			iCompilationUnits = iPackageFragment.getCompilationUnits();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return iCompilationUnits;
	}
	
	/**
	 * Generic method to concatenate two arrays of the same type 
	 * 
	 * @param <T> Generic Object Type
	 * @param a first array
	 * @param b second array
	 * @return T[] concatenated array containing both arrays
	 */
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
