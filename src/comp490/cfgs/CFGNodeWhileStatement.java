package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * Control flow node for while statement 
 *
 */
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
				statementsInBody.add(current);
				prev = current;
			}
		}
		
		else if(body instanceof Statement) {
			statementsInBody.add(CFGNodeFactory.makeCFGNode(body));
		}
		
		if(getLastStatementInBody() != null) {
			getLastStatementInBody().makeSequence(this);
		}
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
	public String makeDot(Set<CFGNode> traversed) {
		if(traversed.contains(this)) {
			return "";
		}
		traversed.add(this);
		String str = "";
		// add edge to next statement (outside of while loop)
		if(!CFGNodes.isEmpty()) {
			str += this.toString() + " -> " + CFGNodes.get(0).toString() + "\n";
			str += CFGNodes.get(0).makeDot(traversed);
		}
		// add edge to first statement (inside of while loop)
		if(getFirstStatementInBody() != null) {
			str += this.toString() + " -> " + getFirstStatementInBody().toString() + "\n";
		}
		for(CFGNode i : statementsInBody) {
			str += i.makeDot(traversed);
		}
		return str;
	} 
}
