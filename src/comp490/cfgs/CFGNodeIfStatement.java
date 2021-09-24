package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

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
		for(Object i : ((Block)thenStatement).statements()) {
			CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode)i);
			if(prev != null) {
				prev.makeSequence(current);
			}
			//this check is needed due to CFGNodeFactory's automatic grouping of sequential statements
			if(prev != current) {
				statementsInThenBlock.add(current);
			}
			prev = current;
		}
		
		prev = null;
		//in case of "else"
		if(elseStatement instanceof Block) {
			CFGNodeFactory.reset();
			for(Object i : ((Block)elseStatement).statements()) {
				CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode)i);
				if(prev != null) {
					prev.makeSequence(current);
				}
				//this check is needed due to CFGNodeFactory's automatic grouping of sequential statements
				if(prev != current) {
					statementsInElseBlock.add(current);
				}
				prev = current;
			}
		}
		//in case of "else if"
		else if(elseStatement instanceof IfStatement) {
			CFGNode cfgNode = CFGNodeFactory.makeCFGNode(elseStatement, "ifStatement");
			statementsInElseBlock.add(cfgNode);
		}
		//in case of "if" only
		else if(elseStatement == null) {
			statementsInElseBlock.add(new CFGNode(astNode, "emptyElseStatement"));
		}
		
		CFGNodeFactory.reset();
	}
	
	public List<CFGNode> getStatementsInThenBlock() {
		return statementsInThenBlock;
	}
	
	public List<CFGNode> getStatementsInElseBlock() {
		return statementsInElseBlock;
	}

	public CFGNode getFirstStatementInThenBlock() {
		if(statementsInThenBlock.size() != 0) {
			return statementsInThenBlock.get(0);
		}
		return null;
	}

	public CFGNode getLastStatementInThenBlock() {
		if(statementsInThenBlock.size() != 0) {
			return statementsInThenBlock.get(statementsInThenBlock.size() - 1);
		}
		return null;
	}

	public CFGNode getFirstStatementInElseBlock() {
		if(statementsInElseBlock.size() != 0) {
			return statementsInElseBlock.get(0);
		}
		return null;
	}

	public CFGNode getLastStatementInElseBlock() {
		if(statementsInElseBlock.size() != 0) {
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
		String str = "";
		if(getFirstStatementInThenBlock() != null) {
			str += this.toString() + "->" + getFirstStatementInThenBlock().toString() + "\n";
		}
		if(getFirstStatementInElseBlock() != null) {
			str += this.toString() + "->" + getFirstStatementInElseBlock().toString() + "\n";
		}
		for(CFGNode i : statementsInThenBlock) {
			if(traversed.contains(i)) {
				continue;
			}
			traversed.add(i);
			str += i.makeDot(traversed);
		}
		for(CFGNode i : statementsInElseBlock) {
			if(traversed.contains(i)) {
				continue;
			}
			traversed.add(i);
			str += i.makeDot(traversed);
		}
		return str;
	}
}
