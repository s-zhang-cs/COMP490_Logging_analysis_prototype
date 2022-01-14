package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Control flow node for for statement 
 *
 */
public class CFGNodeForStatement extends CFGNode{
	
	List<CFGNode> initializers;
	List<CFGNode> updaters; 
	List<CFGNode> statementsInBody;
	
	public CFGNodeForStatement(ForStatement forStatement, String name) {
		super(forStatement, name);
		initializers = new ArrayList<>();
		updaters = new ArrayList<>();
		statementsInBody = new ArrayList<>();
		
		CFGNode prev = null;
		for(Object i : forStatement.initializers()) {
			CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode) i, "initializer");
			if(prev != null) {
				prev.makeSequence(current);
			}
			initializers.add(current);
			prev = current;
		}
		
		prev = null;
		for(Object i : forStatement.updaters()) {
			CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode) i, "updater");
			if(prev != null) {
				prev.makeSequence(current);
			}
			updaters.add(current);
			prev = current;
		}
		
		Statement body = forStatement.getBody();
		//for statement with brackets 
		//e.g. for(1 == 1) { doSomething; }
		if(body instanceof Block) {
			prev = null;
			for(Object i : ((Block) body).statements()) {
				CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode)i);
				if(prev != null) {
					prev.makeSequence(current);
				}
				statementsInBody.add(current);
				prev = current;
			}
		}
		//for statement with no brackets
		//e.g. for(1 == 1) doSomething;
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
	
	public CFGNode getFirstStatementInUpdaters() {
		if(!updaters.isEmpty()) {
			return updaters.get(0);
		}
		return null;
	}
	
	public CFGNode getLastStatementInUpdaters() {
		if(!updaters.isEmpty()) {
			return updaters.get(updaters.size() - 1);
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
		//add edge to following statement outside of for loop
		if(CFGNodes.size() != 0) {
			str += this.toString() + " -> " + CFGNodes.get(0).toString() + "\n";
			str += CFGNodes.get(0).makeDot(traversed);
		}
		//add edge to first statement inside of for loop
		if(getFirstStatementInBody() != null) {
			str += this.toString() + " -> " + getFirstStatementInBody().toString() + "\n";
		}
		for(CFGNode i : statementsInBody) {
			str += i.makeDot(traversed);
		}
		return str;
	} 
}
