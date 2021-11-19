package comp490.cfgs;

import org.eclipse.jdt.core.dom.BreakStatement;

public class CFGNodeBreakStatement extends CFGNode{
	public CFGNodeBreakStatement(BreakStatement astNode, String name) {
		super(astNode, name);
	}
}
