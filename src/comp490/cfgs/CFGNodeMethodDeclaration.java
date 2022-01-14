package comp490.cfgs;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import comp490.handlers.JavaModel;

/**
 * Control flow node for method declaration 
 *
 */
public class CFGNodeMethodDeclaration extends CFGNode{
	private String className;
	
	public CFGNodeMethodDeclaration(MethodDeclaration methodDeclaration) {
		super(methodDeclaration);

		this.name = methodDeclaration.getName().getIdentifier();
		this.className = methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName();
		
		String methodName = methodDeclaration.getName().getFullyQualifiedName();
		String key = this.className + "." + methodName;
		
		//add string and methodDeclaration mapping to java model, in order to facilitate
		//future references to the same methods by methodInvocation
		JavaModel.methodDeclarationMap.put(key, methodDeclaration);
	}
	
	public CFGNodeMethodDeclaration(MethodDeclaration methodDeclaration, String name) {
		super(methodDeclaration);

		this.name = name;
		this.className = methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName();
		
		String methodName = methodDeclaration.getName().getFullyQualifiedName();
		String key = this.className + "." + methodName;
		
		//add string and methodDeclaration mapping to java model, in order to facilitate
		//future references to the same methods by methodInvocation
		JavaModel.methodDeclarationMap.put(key, methodDeclaration);
	}
	
	public String getClassName() {
		return className;
	}
	
	@Override
	public String toString() {
		if(className.contains(".")) {
			return className.replace(".", "_") + "_" + name + lineNbr;
		}
		return className + "_" + name + "_" + lineNbr;
	}
	
}
