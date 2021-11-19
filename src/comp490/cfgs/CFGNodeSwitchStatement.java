package comp490.cfgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

public class CFGNodeSwitchStatement extends CFGNode{
	
	List<List<CFGNode>> branches;

	public CFGNodeSwitchStatement(SwitchStatement astNode, String name) {
		super(astNode, name);
		branches = new ArrayList<>();
		branches.add(new ArrayList<CFGNode>());
		Expression expression = astNode.getExpression();
		List statements = astNode.statements();
		
		Object prev = null;
		for(Object i : statements) {
			if(i instanceof SwitchCase) {
				CFGNode cfgNode = CFGNodeFactory.makeCFGNode((ASTNode)i);
				if(prev instanceof BreakStatement) {
					ArrayList<CFGNode> newBranch = new ArrayList<>();
					newBranch.add(cfgNode);
					branches.add(newBranch);
					addCFGNode(cfgNode);
				}
				else {
					List<CFGNode> latestBranch = branches.get(branches.size() - 1);
					latestBranch.add(cfgNode);
					addCFGNode(cfgNode);
					//add link from previous case to new case (without break)
					if(latestBranch.size() >= 2) {
						latestBranch.get(latestBranch.size() - 2).makeSequence(cfgNode);
					}
				}
			}
			else {
				List<CFGNode> latestBranch = branches.get(branches.size() - 1);
				CFGNode latestNode = latestBranch.get(latestBranch.size() - 1);
				CFGNode cfgNode = CFGNodeFactory.makeCFGNode((ASTNode)i);
				latestBranch.add(cfgNode);
				latestNode.makeSequence(cfgNode);
			}
			prev = i;
		}
	}
	
	@Override
	public void makeSequence(CFGNode cfgNode) {
		for(List<CFGNode> branch : branches) {
			CFGNode latestNode = branch.get(branch.size() - 1);
			latestNode.makeSequence(cfgNode);
		}
	}

}
