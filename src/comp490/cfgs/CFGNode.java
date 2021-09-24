package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import comp490.handlers.JavaModel;

public class CFGNode {
	
	protected int lineNbr;
	protected String name;
	
	//all the ASTNodes encompassed within the CFGNode (eg. a sequence of expression statements)
	List<ASTNode> ASTNodes;
	//all the edges of the adjacency list associated with this CFGNode. (eg. ifExpression points to thenStatement and elsestatement in case of an 'if else')
	List<CFGNode> CFGNodes;
	
	public CFGNode(ASTNode astNode) {
		lineNbr = ((CompilationUnit) astNode.getRoot()).getLineNumber(astNode.getStartPosition());
		this.name = "Type" + astNode.getNodeType();
		ASTNodes = new ArrayList<>();
		CFGNodes = new ArrayList<>();
		ASTNodes.add(astNode);
	}
	
	public CFGNode(ASTNode astNode, String name) {
		lineNbr = ((CompilationUnit) astNode.getRoot()).getLineNumber(astNode.getStartPosition());
		this.name = name;
		ASTNodes = new ArrayList<>();
		CFGNodes = new ArrayList<>();
		ASTNodes.add(astNode);
	}
	
	//copy constructor
	public CFGNode(CFGNode source) {
		
	}

	public int getLineNbr() {
		return lineNbr;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ASTNode> getASTNodes() {
		return ASTNodes;
	}
	
	public List<CFGNode> getCFGNodes() {
		return CFGNodes;
	}
	
	public void addASTNode(ASTNode astNode) {
		ASTNodes.add(astNode);
	}
	
	public void addCFGNode(CFGNode cfgNode) {
		CFGNodes.add(cfgNode);
	}

	//to be overwritten in subclasses
	public void makeSequence(CFGNode cfgNode) {
		if(cfgNode != this) {
			addCFGNode(cfgNode);
		}
	}
	
	//to be overwritten in subclasses
	@Override
	public String toString() {
		return name + "_" + lineNbr;
	}
	
	public String makeDot(Set<CFGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		for(CFGNode cfgNode : CFGNodes) {
			str += this.toString() + " -> " + cfgNode.toString() + "\n";
		}
		for(CFGNode cfgNode : CFGNodes) {
			str += cfgNode.makeDot(traversed);
		}
		return str;
	}
	
	public String linkICFG(Set<CFGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		for(CFGNode cfgNode : CFGNodes) {
			//direct method calls 
			if(cfgNode instanceof CFGNodeExpressionStatement && ((CFGNodeExpressionStatement)cfgNode).getMethodInvocation() != null) {
				MethodInvocation methodInvocation = ((CFGNodeExpressionStatement)cfgNode).getMethodInvocation();
				MethodDeclaration methodDeclaration = JavaModel.getMethodDeclarationFromMethodInvocation(methodInvocation);
				//external methods
				if(methodDeclaration == null) {
					//do nothing, as they don't belong in the source code we analyze
				}
				else {
					MethodCFG methodCFG = new MethodCFG(methodDeclaration);
					methodCFG.extractMethodCFG();
					str += cfgNode.toString() + " -> " + methodCFG.getRoot().toString() + "\n";
					str += methodCFG.getRootLastStatement().toString() + " -> " + cfgNode.toString() + "\n";
				}
			}
			//variable declaration from methods 
			else if(cfgNode instanceof CFGNodeVariableDeclaration && ((CFGNodeVariableDeclaration)cfgNode).getMethodInvocation() != null) {
				MethodInvocation methodInvocation = ((CFGNodeVariableDeclaration)cfgNode).getMethodInvocation();
				MethodDeclaration methodDeclaration = JavaModel.getMethodDeclarationFromMethodInvocation(methodInvocation);
				//external methods
				if(methodDeclaration == null) {
					//do nothing, as they don't belong in the source code we analyze
				}
				else {
					MethodCFG methodCFG = new MethodCFG(methodDeclaration);
					methodCFG.extractMethodCFG();
					str += cfgNode.toString() + " -> " + methodCFG.getRoot().toString() + "\n";
					str += methodCFG.getRootLastStatement().toString() + " -> " + cfgNode.toString() + "\n";
				}
			}
			//variable declaration from constructors
			else if(cfgNode instanceof CFGNodeVariableDeclaration && ((CFGNodeVariableDeclaration)cfgNode).getClassInstanceCreation() != null) {
				ClassInstanceCreation classInstanceCreation = ((CFGNodeVariableDeclaration)cfgNode).getClassInstanceCreation();
				MethodDeclaration methodDeclaration = JavaModel.getConstructorMethodFromClassInstantiation(classInstanceCreation);
				//compiler generated default constructor
				if(methodDeclaration == null) {
					//do nothing here, it is handled inside default constructor class from javaModel
				}
				else {
					//still need to complete:
					// - find all the class instantiations within the parent class outside of constructors
					// - point to (first statement of) first class instantiation
					// - link all subsequent class instantiations 
					// - point from last statement of last class instantiation to first explicit statement in the constructor
					MethodCFG methodCFG = new MethodCFG(methodDeclaration);
					methodCFG.extractMethodCFG();
					str += cfgNode.toString() + " -> " + methodCFG.getRoot().toString() + "\n";
					str += methodCFG.getRootLastStatement().toString() + " -> " + cfgNode.toString() + "\n";
				}
			}
			
		}
		for(CFGNode cfgNode : CFGNodes) {
			str += cfgNode.linkICFG(traversed);
		}
		return str;
	}

}
