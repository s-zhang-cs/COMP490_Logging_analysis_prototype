package comp490.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import comp490.cfgs.MethodCFG;

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
