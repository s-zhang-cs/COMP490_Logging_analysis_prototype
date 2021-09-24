package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class CFGNodeWhileStatement extends CFGNode{
	List<CFGNode> statementsInBody;
	
	public CFGNodeWhileStatement(WhileStatement whileStatement, String name) {
		super(whileStatement, name);
		Statement body = whileStatement.getBody();
		statementsInBody = new ArrayList<>();
		
		if(body instanceof Block) {
			CFGNode prev = null;
			for(Object i : ((Block) body).statements()) {
				CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode)i);
				if(prev != null) {
					prev.makeSequence(current);
				}
				//this check is needed due to CFGNodeFactory's automatic grouping of sequential statements
				if(prev != current) {
					statementsInBody.add(current);
				}
				prev = current;
			}
		}
		
		else if(body instanceof Statement) {
			statementsInBody.add(CFGNodeFactory.makeCFGNode(body));
		}
		
		if(getLastStatementInBody() != null) {
			getLastStatementInBody().makeSequence(this);
		}
		
		CFGNodeFactory.reset();
	}
	
	public CFGNode getFirstStatementInBody() {
		if(statementsInBody.size() != 0) {
			return statementsInBody.get(0);
		}
		return null;
	}

	public CFGNode getLastStatementInBody() {
		if(statementsInBody.size() != 0) {
			return statementsInBody.get(statementsInBody.size() - 1);
		}
		return null;
	}
	
	@Override
	public void makeSequence(CFGNode cfgNode) {
		addCFGNode(cfgNode);
		if(getLastStatementInBody() != null) {
			getLastStatementInBody().makeSequence(cfgNode);
		}
	}
	
	@Override
	public String makeDot(Set<CFGNode> traversed) {
		String str = "";
		if(CFGNodes.size() != 0) {
			str += this.toString() + " -> " + CFGNodes.get(0).toString() + "\n";
		}
		if(getFirstStatementInBody() != null) {
			str += this.toString() + " -> " + getFirstStatementInBody().toString() + "\n";
		}
		for(CFGNode i : statementsInBody) {
			if(traversed.contains(i)) {
				continue;
			}
			traversed.add(i);
			str += i.makeDot(traversed);
		}
		return str;
	} 
}
