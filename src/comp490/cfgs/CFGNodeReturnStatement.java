package comp490.cfgs;

import org.eclipse.jdt.core.dom.ReturnStatement;

public class CFGNodeReturnStatement extends CFGNode{
	public CFGNodeReturnStatement(ReturnStatement node, String name) {
		super(node, name);
	}
}
