package comp490.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import comp490.callGraphs.CGNode;
import comp490.callGraphs.CGNodeFactory;
import comp490.callGraphs.MethodCG;
import comp490.handlers.JavaModel;
import comp490.logs.LoggingStatement;
import comp490.logs.LoggingStatementFactory;

public class MethodCGVisitor extends ASTVisitor{
	
	private String packageName;
	private String className;
	private String methodName;
	//private MethodCG root;
	private Map<String, List<LoggingStatement>> roots;
	
	public MethodCGVisitor(String packageName, String className, String methodName) {
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
		
		roots = new HashMap<>();
	}
	
	public boolean visit(MethodInvocation node) {
//		if(root == null && node.resolveMethodBinding().getName().equals(methodName)) {
//			root = new MethodCG(JavaModel.getMethodDeclarationFromMethodInvocation(node));
//		}
		
		LoggingStatement loggingStatement = LoggingStatementFactory.getLoggingStatement(node);
		if(loggingStatement != null) {
			List<LoggingStatement> sameLevelLogs = roots.get(loggingStatement.getLoggingLevel());
			if(sameLevelLogs != null) {
				sameLevelLogs.add(loggingStatement);
			}
			else {
				List<LoggingStatement> newLevelLogs = new ArrayList<>();
				newLevelLogs.add(loggingStatement);
				roots.put(loggingStatement.getLoggingLevel(), newLevelLogs);
			}
		}
		
		MethodDeclaration caller = getParentMethodDeclarationForMethodInvocation(node);
		CGNode cgNode = CGNodeFactory.makeCGNode(JavaModel.getMethodDeclarationFromMethodInvocation(node));
		if(cgNode != null) {
			cgNode.addCaller(CGNodeFactory.makeCGNode(caller));
		}
		return super.visit(node);
	}
	
	public boolean visit(ClassInstanceCreation node) {	
		//class instantiated outside constructor
		if(classInstantiationOutsideConstructor(node)) {
			MethodDeclaration[] callers = getParentClassConstructorsForClassInstantiation(node);
			MethodDeclaration callee = JavaModel.getConstructorMethodFromClassInstantiation(node);
			//callee is a default constructor (from compiler)
			if(callee == null) {
				CGNode cgNode = CGNodeFactory.makeCGNode(JavaModel.getTypeDeclarationFromClassInstanceCreation(node));
				//caller only has default constructor (from compiler)
				if(callers.length == 0) {
					ASTNode parentClass = node.getParent();
					while(!(parentClass instanceof TypeDeclaration)) {
						parentClass = parentClass.getParent();
					}
					cgNode.addCaller(CGNodeFactory.makeCGNode((TypeDeclaration) parentClass));
				}
				//caller has user-specified constructors
				else {
					for(MethodDeclaration caller : callers) {
						cgNode.addCaller(CGNodeFactory.makeCGNode(caller));
					}
				}
			}
			//callee is a user-specified constructor
			else {
				CGNode cgNode = CGNodeFactory.makeCGNode(callee);
				//caller only has default constructor (from compiler)
				if(callers.length == 0) {
					ASTNode parentClass = node.getParent();
					while(!(parentClass instanceof TypeDeclaration)) {
						parentClass = parentClass.getParent();
					}
					cgNode.addCaller(CGNodeFactory.makeCGNode((TypeDeclaration) parentClass));
				}
				//caller has user-specified constructors
				else {
					for(MethodDeclaration caller : callers) {
						cgNode.addCaller(CGNodeFactory.makeCGNode(caller));
					}
				}
			}
		}
		//class instantiated inside a constructor
		else {
			MethodDeclaration caller = getParentConstructorForClassInstantiation(node);
			MethodDeclaration callee = JavaModel.getConstructorMethodFromClassInstantiation(node);
			//callee is default constructor (from compiler)
			if(callee == null) {
				CGNode cgNode = CGNodeFactory.makeCGNode(JavaModel.getTypeDeclarationFromClassInstanceCreation(node));
				cgNode.addCaller(CGNodeFactory.makeCGNode(caller));
			}
			//callee is user-specified constructor
			else {
				CGNode cgNode = CGNodeFactory.makeCGNode(callee);
				cgNode.addCaller(CGNodeFactory.makeCGNode(caller));
			}
		}
		return super.visit(node);
	}
	
	public MethodCG getRoot() {
		return root;
	}
	
	private MethodDeclaration getParentConstructorForClassInstantiation(ClassInstanceCreation classInstanceCreation) {
		ASTNode parent = classInstanceCreation.getParent();
		while(!(parent instanceof MethodDeclaration)) {
			parent = parent.getParent();
		}
		return (MethodDeclaration) parent;
	}
	
	private MethodDeclaration getParentMethodDeclarationForMethodInvocation(MethodInvocation node) {
		ASTNode parent = node.getParent();
		while(!(parent instanceof MethodDeclaration)) {
			parent = parent.getParent();
		}
		return (MethodDeclaration) parent;
	}
	
	//when a class is instantiated directly during attribute declaration of the parent class 
	//eg. class Parent {
	// 	      ChildClass childClass = new ChildClass();
	//    }
	private boolean classInstantiationOutsideConstructor(ClassInstanceCreation node) {
		ASTNode parent = node.getParent();
		while(!(parent instanceof MethodDeclaration || parent instanceof TypeDeclaration)) {
			parent = parent.getParent();
		}
		if(parent instanceof MethodDeclaration) {
			return false;
		}
		return true;
	}
	
	private MethodDeclaration[] getParentClassConstructorsForClassInstantiation(ClassInstanceCreation node) {
		ASTNode parent = node.getParent();
		while(!(parent instanceof TypeDeclaration)) {
			parent = parent.getParent();
		}
		TypeDeclaration type = (TypeDeclaration) parent;
		MethodDeclaration[] allMethods = type.getMethods();
		MethodDeclaration[] constructors = Arrays.stream(allMethods).filter(method -> method.resolveBinding().getName().equals(type.getName().getIdentifier())).toArray(MethodDeclaration[]::new);
		return constructors;
	}
		
}
