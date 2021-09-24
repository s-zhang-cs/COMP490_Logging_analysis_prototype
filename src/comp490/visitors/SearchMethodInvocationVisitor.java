package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class SearchMethodInvocationVisitor extends ASTVisitor{
	
	private String searchMethodName;
	private int searchMethodInvocationCount = 0;
	
	public SearchMethodInvocationVisitor(String searchMethodName) {
		this.searchMethodName = searchMethodName;
	}
	
	public boolean visit(MethodInvocation node) {
		if(node.getName().getIdentifier().equals(searchMethodName)) {
			searchMethodInvocationCount += 1;
		}
		return super.visit(node);
	}
	
	public int getMethodCount() {
		return searchMethodInvocationCount;
	}
}
