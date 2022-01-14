package comp490.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import comp490.cfgs.MethodCFG;

/**
 * Visitor used to make a control flow graph for each method in the source code
 *
 */
public class MethodCFGVisitor extends ASTVisitor{
	
	List<MethodCFG> methodCFGs;
	
	public MethodCFGVisitor() {
		methodCFGs = new ArrayList<>();
	}
	
	public boolean visit(MethodDeclaration node) {
		MethodCFG methodCFG = new MethodCFG(node);
		methodCFG.extractMethodCFG();
		methodCFGs.add(methodCFG);
		return super.visit(node);
	}
	
	public List<MethodCFG> getMethodCFGs() {
		return methodCFGs;
	}

}
