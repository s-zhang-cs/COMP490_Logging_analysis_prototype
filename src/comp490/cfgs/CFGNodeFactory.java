package comp490.cfgs;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import comp490.logs.LoggingStatementFactory;

public class CFGNodeFactory {
	
	//this CFGNode groups sequential ASTNodes in the control flow into one CFGNode, in order to avoid polluting the CFG.
	private static CFGNode cfgNode;
	
	public static void reset() {
		cfgNode = null;
	}
	
	public static CFGNode makeCFGNode(ASTNode astNode) {
		if(astNode instanceof MethodDeclaration) {
			cfgNode = new CFGNodeMethodDeclaration((MethodDeclaration) astNode, "methodDeclaration");
			return cfgNode;
		}
		//variable declarations will be grouped with other non-control-flow nodes
		if(astNode instanceof VariableDeclarationStatement) {
			if(cfgNode == null || isControlFlowNode(cfgNode)) {
				cfgNode = new CFGNodeVariableDeclaration((VariableDeclarationStatement)astNode, "variableDeclaration");
				return cfgNode;
			}
			cfgNode.addASTNode(astNode);
			return cfgNode;
		}
		if(astNode instanceof IfStatement) {
			cfgNode = new CFGNodeIfStatement((IfStatement) astNode, "ifStatement");
			return cfgNode;
		}
		if(astNode instanceof ForStatement) {
			cfgNode = new CFGNodeForStatement((ForStatement) astNode, "forStatement");
			return cfgNode;
		}
		if(astNode instanceof EnhancedForStatement) {
			cfgNode = new CFGNodeEnhancedForStatement((EnhancedForStatement)astNode, "enhancedForStatement");
			return cfgNode;
		}
		if(astNode instanceof WhileStatement) {
			cfgNode = new CFGNodeWhileStatement((WhileStatement) astNode, "whileStatement");
			return cfgNode;
		}
		if(astNode instanceof SwitchStatement) {
			cfgNode = new CFGNodeSwitchStatement((SwitchStatement) astNode, "switchStatement");
			return cfgNode;
		}
		if(astNode instanceof SwitchCase) {
			cfgNode = new CFGNodeSwitchCase((SwitchCase) astNode, "switchCase");
			return cfgNode;
		}
		if(astNode instanceof BreakStatement) {
			cfgNode = new CFGNodeBreakStatement((BreakStatement) astNode, "breakStatement");
			return cfgNode;
		}
		//expression statements will be grouped with other non-control-flow nodes, unless if it is a logging statement
		if(astNode instanceof ExpressionStatement) {
			if(astNode instanceof MethodInvocation && LoggingStatementFactory.isLoggingStatement((MethodInvocation) astNode)) {
				cfgNode = new CFGNodeExpressionStatement((ExpressionStatement) astNode, "ExpressionStatement");
				return cfgNode;
			}
			if(astNode instanceof ClassInstanceCreation) {
				
			}
			if(cfgNode == null || isControlFlowNode(cfgNode)) {
				cfgNode = new CFGNodeExpressionStatement((ExpressionStatement)astNode, "expressionStatement");
				return cfgNode;
			}
			cfgNode.addASTNode(astNode);
			return cfgNode;
		}
		if(astNode instanceof ReturnStatement) {
			cfgNode = new CFGNodeReturnStatement((ReturnStatement) astNode, "ReturnStatement");
			return cfgNode;
		}
		//Do not group for unknown node types
		cfgNode = new CFGNode(astNode);
		return cfgNode;
	}
	
	public static CFGNode makeCFGNode(ASTNode astNode, String name) {
		if(astNode instanceof MethodDeclaration) {
			cfgNode = new CFGNodeMethodDeclaration((MethodDeclaration) astNode, name);
			return cfgNode;
		}
		//variable declarations will be grouped with other non-control-flow nodes
		if(astNode instanceof VariableDeclarationStatement) {
			if(cfgNode == null || isControlFlowNode(cfgNode)) {
				cfgNode = new CFGNodeVariableDeclaration((VariableDeclarationStatement)astNode, "variableDeclaration");
				return cfgNode;
			}
			cfgNode.addASTNode(astNode);
			return cfgNode;
		}
		if(astNode instanceof IfStatement) {
			cfgNode = new CFGNodeIfStatement((IfStatement) astNode, name);
			return cfgNode;
		}
		if(astNode instanceof ForStatement) {
			cfgNode = new CFGNodeForStatement((ForStatement) astNode, name);
			return cfgNode;
		}
		if(astNode instanceof EnhancedForStatement) {
			cfgNode = new CFGNodeEnhancedForStatement((EnhancedForStatement)astNode, name);
			return cfgNode;
		}
		if(astNode instanceof WhileStatement) {
			cfgNode = new CFGNodeWhileStatement((WhileStatement) astNode, name);
			return cfgNode;
		}
		if(astNode instanceof SwitchStatement) {
			cfgNode = new CFGNodeSwitchStatement((SwitchStatement) astNode, name);
			return cfgNode;
		}
		if(astNode instanceof SwitchCase) {
			cfgNode = new CFGNodeSwitchCase((SwitchCase) astNode, name);
			return cfgNode;
		}
		if(astNode instanceof BreakStatement) {
			cfgNode = new CFGNodeBreakStatement((BreakStatement) astNode, name);
			return cfgNode;
		}
		//expression statements will be grouped with other non-control-flow nodes, unless if it is a logging statement
		if(astNode instanceof ExpressionStatement) {
			if(astNode instanceof MethodInvocation && LoggingStatementFactory.isLoggingStatement((MethodInvocation) astNode)) {
				cfgNode = new CFGNodeExpressionStatement((ExpressionStatement) astNode, name);
				return cfgNode;
			}
			if(astNode instanceof ClassInstanceCreation) {
				
			}
			if(cfgNode == null || isControlFlowNode(cfgNode)) {
				cfgNode = new CFGNodeExpressionStatement((ExpressionStatement)astNode, "expressionStatement");
				return cfgNode;
			}
			cfgNode.addASTNode(astNode);
			return cfgNode;
		}
		if(astNode instanceof ReturnStatement) {
			cfgNode = new CFGNodeReturnStatement((ReturnStatement) astNode, name);
			return cfgNode;
		}
		//Do not group for unknown node types
		cfgNode = new CFGNode(astNode);
		return cfgNode;
	}
	
	public static boolean isControlFlowNode(CFGNode cfgNode) {
		return (cfgNode instanceof CFGNodeEnhancedForStatement || cfgNode instanceof CFGNodeForStatement ||
				cfgNode instanceof CFGNodeIfStatement || cfgNode instanceof CFGNodeSwitchStatement ||
				cfgNode instanceof CFGNodeWhileStatement || cfgNode instanceof CFGNodeSwitchCase);
	}
}
