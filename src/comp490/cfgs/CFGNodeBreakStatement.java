package comp490.cfgs;

import org.eclipse.jdt.core.dom.BreakStatement;

/**
 * Control flow node for break statement
 *
 */
public class CFGNodeBreakStatement extends CFGNode{
	public CFGNodeBreakStatement(BreakStatement astNode, String name) {
		super(astNode, name);
	}
}
