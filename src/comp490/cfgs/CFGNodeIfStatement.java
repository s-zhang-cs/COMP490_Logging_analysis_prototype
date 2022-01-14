package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Control flow node for if statement 
 *
 */
public class CFGNodeIfStatement extends CFGNode{
	
	private List<CFGNode> statementsInThenBlock;
	private List<CFGNode> statementsInElseBlock;
	
	public CFGNodeIfStatement(IfStatement astNode, String name) {
		super(astNode, name);

		statementsInThenBlock = new ArrayList<>();
		statementsInElseBlock = new ArrayList<>();
		
		Statement thenStatement = astNode.getThenStatement();
		Statement elseStatement = astNode.getElseStatement();
		
		CFGNode prev = null;
		//if statements with brackets:
		//e.g. if(1 == 1) { doSomething; }
		if(thenStatement instanceof Block){
			for(Object i : ((Block)thenStatement).statements()) {
				CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode)i);
				if(prev != null) {
					prev.makeSequence(current);
				}
				statementsInThenBlock.add(current);
				prev = current;
			}
		}
		//if statements with no brackets:
		//e.g. if(1 == 1) doSomething;
		else if(thenStatement instanceof Statement) {
			CFGNode cfgNode = CFGNodeFactory.makeCFGNode(thenStatement);
			statementsInThenBlock.add(cfgNode);
		}
		
		prev = null;
		//in case of "else" with brackets:
		//e.g. else { doSomething; }
		if(elseStatement instanceof Block) {
			for(Object i : ((Block)elseStatement).statements()) {
				CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode)i);
				if(prev != null) {
					prev.makeSequence(current);
				}
				statementsInElseBlock.add(current);
				prev = current;
			}
		}
		//in case of "else if"
		//e.g. else if { doSomething; }
		else if(elseStatement instanceof IfStatement) {
			CFGNode cfgNode = CFGNodeFactory.makeCFGNode(elseStatement, "ifStatement");
			statementsInElseBlock.add(cfgNode);
		}
		//in case of "else" with no brackets:
		//e.g. else doSomething;
		else if(elseStatement instanceof Statement) {
			CFGNode cfgNode = CFGNodeFactory.makeCFGNode(elseStatement);
			statementsInElseBlock.add(cfgNode);
		}
		//in case of "if" only (no "else")
		//add dummy empty else statement
		else if(elseStatement == null) {
			statementsInElseBlock.add(new CFGNode(astNode, "emptyElseStatement"));
		}
	}
	
	public List<CFGNode> getStatementsInThenBlock() {
		return statementsInThenBlock;
	}
	
	public List<CFGNode> getStatementsInElseBlock() {
		return statementsInElseBlock;
	}

	public CFGNode getFirstStatementInThenBlock() {
		if(!statementsInThenBlock.isEmpty()) {
			return statementsInThenBlock.get(0);
		}
		return null;
	}

	public CFGNode getLastStatementInThenBlock() {
		if(!statementsInThenBlock.isEmpty()) {
			return statementsInThenBlock.get(statementsInThenBlock.size() - 1);
		}
		return null;
	}

	public CFGNode getFirstStatementInElseBlock() {
		if(!statementsInElseBlock.isEmpty()) {
			return statementsInElseBlock.get(0);
		}
		return null;
	}

	public CFGNode getLastStatementInElseBlock() {
		if(!statementsInElseBlock.isEmpty()) {
			return statementsInElseBlock.get(statementsInElseBlock.size() - 1);
		}
		return null;
	}
	
	@Override
	public void makeSequence(CFGNode cfgNode) {
		if(getLastStatementInThenBlock() != null) {
			getLastStatementInThenBlock().makeSequence(cfgNode);
		}
		if(getLastStatementInElseBlock() != null) {
			getLastStatementInElseBlock().makeSequence(cfgNode);
		}
	}
	
	@Override
	public String makeDot(Set<CFGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		if(getFirstStatementInThenBlock() != null) {
			str += this.toString() + "->" + getFirstStatementInThenBlock().toString() + "\n";
		}
		if(getFirstStatementInElseBlock() != null) {
			str += this.toString() + "->" + getFirstStatementInElseBlock().toString() + "\n";
		}
		for(CFGNode i : statementsInThenBlock) {
			str += i.makeDot(traversed);
		}
		for(CFGNode i : statementsInElseBlock) {
			str += i.makeDot(traversed);
		}
		return str;
	}
}
