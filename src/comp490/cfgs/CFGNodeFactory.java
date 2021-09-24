package comp490.cfgs;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class CFGNodeFactory {
	
	//this class acts as a wrapper/pointer towards a certain CFGNode which groups 
	//sequential ASTNodes into one CFGNode, in order to avoid polluting the CFG.
	private static class ASTNodesBlock {
		private static CFGNode cfgNode;
		
		private static CFGNode getCfgNode() {
			return cfgNode;
		}
		
		private static void setCfgNode(CFGNode cfgNode) {
			ASTNodesBlock.cfgNode = cfgNode;
		}
		
		private static void reset() {
			cfgNode = null;
		}
	}
	
	public static void reset() {
		ASTNodesBlock.reset();
	}
	
	public static CFGNode makeCFGNode(ASTNode astNode) {
		if(astNode instanceof MethodDeclaration) {
			ASTNodesBlock.reset();
			return new CFGNodeMethodDeclaration((MethodDeclaration) astNode);
		}
		if(astNode instanceof VariableDeclarationStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeVariableDeclaration((VariableDeclarationStatement) astNode, "VariableDeclaration");
		}
		if(astNode instanceof IfStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeIfStatement((IfStatement) astNode, "ifStatement");
		}
		if(astNode instanceof ForStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeForStatement((ForStatement) astNode, "forStatement");
		}
		if(astNode instanceof WhileStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeWhileStatement((WhileStatement) astNode, "whileStatement");
		}
		if(astNode instanceof ExpressionStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeExpressionStatement((ExpressionStatement) astNode, "ExpressionStatement");
		}
		if(astNode instanceof ReturnStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeReturnStatement((ReturnStatement) astNode, "ReturnStatement");
		}
		if(ASTNodesBlock.getCfgNode() == null) {
			ASTNodesBlock.setCfgNode(new CFGNode(astNode));
			return ASTNodesBlock.getCfgNode();
		}
		else {
			ASTNodesBlock.getCfgNode().addASTNode(astNode);
			return ASTNodesBlock.getCfgNode();
		}
	}
	
	public static CFGNode makeCFGNode(ASTNode astNode, String name) {
		if(astNode instanceof MethodDeclaration) {
			ASTNodesBlock.reset();
			return new CFGNodeMethodDeclaration((MethodDeclaration) astNode, name);
		}
		if(astNode instanceof VariableDeclarationStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeVariableDeclaration((VariableDeclarationStatement) astNode, name);
		}
		if(astNode instanceof IfStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeIfStatement((IfStatement) astNode, name);
		}
		if(astNode instanceof ForStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeForStatement((ForStatement) astNode, name);
		}
		if(astNode instanceof WhileStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeWhileStatement((WhileStatement) astNode, name);
		}
		if(astNode instanceof ExpressionStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeExpressionStatement((ExpressionStatement) astNode, name);
		}
		if(astNode instanceof ReturnStatement) {
			ASTNodesBlock.reset();
			return new CFGNodeReturnStatement((ReturnStatement) astNode, name);
		}
		if(ASTNodesBlock.getCfgNode() == null) {
			ASTNodesBlock.setCfgNode(new CFGNode(astNode, name));
			return ASTNodesBlock.getCfgNode();
		}
		else {
			ASTNodesBlock.getCfgNode().addASTNode(astNode);
			return ASTNodesBlock.getCfgNode();
		}
	}
	
}
