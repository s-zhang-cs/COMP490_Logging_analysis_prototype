package comp490.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SearchMethodDeclarationVisitor extends ASTVisitor {
	
	private String searchMethodName;
	private int searchMethodCount = 0;
	
	public SearchMethodDeclarationVisitor(String searchMethodName) {
		this.searchMethodName = searchMethodName;
	}
	
	public boolean visit(MethodDeclaration node) {
		if(node.getName().getIdentifier().equals(searchMethodName)) {
			searchMethodCount += 1;
		}
		return super.visit(node);
	}
	
	public int getMethodCount() {
		return searchMethodCount;
	}
}
