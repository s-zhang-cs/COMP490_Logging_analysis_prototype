package comp490.cfgs;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * Control flow node for variable declarations
 * special cases:
 *  - variable is the return value of a method 
 *  - variable is a class instantiation
 */
public class CFGNodeVariableDeclaration extends CFGNode{
	String variableName;
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

}
