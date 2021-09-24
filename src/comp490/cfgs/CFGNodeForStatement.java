package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;

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
			//this check is needed due to CFGNodeFactory's automatic grouping of sequential statements
			if(prev != current) {
				initializers.add(current);
			}
			prev = current;
		}
		
		prev = null;
		CFGNodeFactory.reset();
		for(Object i : forStatement.updaters()) {
			CFGNode current = CFGNodeFactory.makeCFGNode((ASTNode) i, "updater");
			if(prev != null) {
				prev.makeSequence(current);
			}
			//this check is needed due to CFGNodeFactory's automatic grouping of sequential statements
			if(prev != current) {
				updaters.add(current);
			}
			prev = current;
		}
		
		CFGNodeFactory.reset();
		Statement body = forStatement.getBody();
		if(body instanceof Block) {
			prev = null;
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
				
		if(getFirstStatementInUpdaters() != null && getLastStatementInBody() != null) {
			getLastStatementInBody().makeSequence(getFirstStatementInUpdaters());
		}
		
		if(getFirstStatementInUpdaters() == null) {
			getLastStatementInBody().makeSequence(this);
		}
		else {
			getLastStatementInUpdaters().makeSequence(this);
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
	
	public CFGNode getFirstStatementInUpdaters() {
		if(updaters.size() != 0) {
			return updaters.get(0);
		}
		return null;
	}
	
	public CFGNode getLastStatementInUpdaters() {
		if(updaters.size() != 0) {
			return updaters.get(updaters.size() - 1);
		}
		return null;
	}
	
	@Override
	public void makeSequence(CFGNode cfgNode) {
		addCFGNode(cfgNode);
		if(getLastStatementInUpdaters() == null) {
			getLastStatementInBody().makeSequence(cfgNode);
		}
		else {
			getLastStatementInUpdaters().makeSequence(cfgNode);
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
		for(CFGNode i : updaters) {
			if(traversed.contains(i)) {
				continue;
			}
			traversed.add(i);
			str += i.makeDot(traversed);
		}
		return str;
	} 
		
}
