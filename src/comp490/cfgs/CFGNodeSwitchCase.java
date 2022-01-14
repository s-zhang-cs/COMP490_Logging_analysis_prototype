package comp490.cfgs;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * Control flow node for switch case 
 *
 */
public class CFGNodeSwitchCase extends CFGNode{
	public CFGNodeSwitchCase(ASTNode astNode, String name) {
		super(astNode, name);
	}
}
