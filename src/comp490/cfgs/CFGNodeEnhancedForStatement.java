package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Statement;

public class CFGNodeEnhancedForStatement extends CFGNode{
	
	List<CFGNode> statementsInBody;
	
	public CFGNodeEnhancedForStatement(EnhancedForStatement astNode, String name) {
		super(astNode, name);
		statementsInBody = new ArrayList<>();
		Statement body = astNode.getBody();
		
		//enhanced for statement with brackets 
		//e.g. for(boolean[] row : grid) { doSomething; }
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
		//enhanced for statement with no brackets
		//e.g. for(boolean[] row : grid) doSomething;
		else if(body instanceof Statement) {
			statementsInBody.add(CFGNodeFactory.makeCFGNode(body));
		}
		
		if(getLastStatementInBody() != null) {
			getLastStatementInBody().makeSequence(this);
		}
		
		CFGNodeFactory.reset();
	}
	
	public CFGNode getFirstStatementInBody() {
		if(!statementsInBody.isEmpty()) {
			return statementsInBody.get(0);
		}
		return null;
	}
	
	public CFGNode getLastStatementInBody() {
		if(!statementsInBody.isEmpty()) {
			return statementsInBody.get(statementsInBody.size() - 1);
		}
		return null;
	}
	
	@Override
	public void makeSequence(CFGNode cfgNode) {
		if(cfgNode != this) {
			addCFGNode(cfgNode);
		}
	}
	
	@Override
	public String makeDot(Set<CFGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		if(CFGNodes.size() != 0) {
			str += this.toString() + " -> " + CFGNodes.get(0).toString() + "\n";
		}
		if(getFirstStatementInBody() != null) {
			str += this.toString() + " -> " + getFirstStatementInBody().toString() + "\n";
		}
		for(CFGNode i : statementsInBody) {
			str += i.makeDot(traversed);
		}
		return str;
	} 
}
