package comp490.cfgs;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class CFGNodeVariableDeclaration extends CFGNode{
	private MethodInvocation methodInvocation;
	private ClassInstanceCreation classInstanceCreation;
	
	public CFGNodeVariableDeclaration(VariableDeclarationStatement variableDeclarationStatement, String name) {
		super(variableDeclarationStatement, name);
		for(Object fragment : variableDeclarationStatement.fragments()) {
			Expression initializer = ((VariableDeclarationFragment)fragment).getInitializer();
			if(initializer instanceof MethodInvocation) {
				methodInvocation = (MethodInvocation) initializer;
				break;
			}
			else if(initializer instanceof ClassInstanceCreation) {
				classInstanceCreation = (ClassInstanceCreation) initializer;
				break;
			}
		}
		
	}
	
	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}
	
	public ClassInstanceCreation getClassInstanceCreation() {
		return classInstanceCreation;
	}
	
	public String toString() {
		if(methodInvocation != null) {
			return name + "_" + methodInvocation.getName().getIdentifier() + "_" + lineNbr;
		}
		if(classInstanceCreation != null) {
			return name + "_" + classInstanceCreation.resolveConstructorBinding().getName() + "_" + lineNbr;
		}
		return super.toString();
	}
}
