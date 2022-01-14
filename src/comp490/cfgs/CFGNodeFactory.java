package comp490.cfgs;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * Factory pattern used to generate control flow node based on the ASTNode 
 * provided by JDT parser
 *
 */
public class CFGNodeFactory {
	
	public static CFGNode makeCFGNode(ASTNode astNode) {
		if(astNode instanceof MethodDeclaration) {
			return new CFGNodeMethodDeclaration((MethodDeclaration) astNode, "methodDeclaration");
		}
		if(astNode instanceof VariableDeclarationStatement) {
			return new CFGNodeVariableDeclaration((VariableDeclarationStatement)astNode, "variableDeclaration");
		}
		if(astNode instanceof IfStatement) {
			return new CFGNodeIfStatement((IfStatement) astNode, "ifStatement");
		}
		if(astNode instanceof ForStatement) {
			return new CFGNodeForStatement((ForStatement) astNode, "forStatement");
		}
		if(astNode instanceof EnhancedForStatement) {
			return new CFGNodeEnhancedForStatement((EnhancedForStatement)astNode, "enhancedForStatement");
		}
		if(astNode instanceof WhileStatement) {
			return new CFGNodeWhileStatement((WhileStatement) astNode, "whileStatement");
		}
		if(astNode instanceof SwitchStatement) {
			return new CFGNodeSwitchStatement((SwitchStatement) astNode, "switchStatement");
		}
		if(astNode instanceof SwitchCase) {
			return new CFGNodeSwitchCase((SwitchCase) astNode, "switchCase");
		}
		if(astNode instanceof BreakStatement) {
			return new CFGNodeBreakStatement((BreakStatement) astNode, "breakStatement");
		}
		if(astNode instanceof ExpressionStatement) {
			return new CFGNodeExpressionStatement((ExpressionStatement) astNode, "expressionStatement");
		}
		if(astNode instanceof ReturnStatement) {
			return new CFGNodeReturnStatement((ReturnStatement) astNode, "ReturnStatement");
		}
		return new CFGNode(astNode);
	}
	
	public static CFGNode makeCFGNode(ASTNode astNode, String name) {
		if(astNode instanceof MethodDeclaration) {
			return new CFGNodeMethodDeclaration((MethodDeclaration) astNode, name);
		}
		if(astNode instanceof VariableDeclarationStatement) {
			return new CFGNodeVariableDeclaration((VariableDeclarationStatement)astNode, "variableDeclaration");
		}
		if(astNode instanceof IfStatement) {
			return new CFGNodeIfStatement((IfStatement) astNode, name);
		}
		if(astNode instanceof ForStatement) {
			return new CFGNodeForStatement((ForStatement) astNode, name);
		}
		if(astNode instanceof EnhancedForStatement) {
			return new CFGNodeEnhancedForStatement((EnhancedForStatement)astNode, name);
		}
		if(astNode instanceof WhileStatement) {
			return new CFGNodeWhileStatement((WhileStatement) astNode, name);
		}
		if(astNode instanceof SwitchStatement) {
			return new CFGNodeSwitchStatement((SwitchStatement) astNode, name);
		}
		if(astNode instanceof SwitchCase) {
			return new CFGNodeSwitchCase((SwitchCase) astNode, name);
		}
		if(astNode instanceof BreakStatement) {
			return new CFGNodeBreakStatement((BreakStatement) astNode, name);
		}
		if(astNode instanceof ExpressionStatement) {
			return new CFGNodeExpressionStatement((ExpressionStatement) astNode, name);
		}
		if(astNode instanceof ReturnStatement) {
			return new CFGNodeReturnStatement((ReturnStatement) astNode, name);
		}
		return new CFGNode(astNode);
	}
	
	/**
	 * Determine whether a cfgNode is a control flow node such as if statement, for loop, etc.
	 * 
	 * @param cfgNode cfgNode to determine if it is control flow node
	 * @return boolean true if it is control flow node, false otherwise
	 */
	public static boolean isControlFlowNode(CFGNode cfgNode) {
		return (cfgNode instanceof CFGNodeEnhancedForStatement || cfgNode instanceof CFGNodeForStatement ||
				cfgNode instanceof CFGNodeIfStatement || cfgNode instanceof CFGNodeSwitchStatement ||
				cfgNode instanceof CFGNodeWhileStatement || cfgNode instanceof CFGNodeSwitchCase);
	}
}
