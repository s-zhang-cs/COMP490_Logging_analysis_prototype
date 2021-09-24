package comp490.callGraphs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import comp490.cfgs.Config;

public class MethodCG {
	
	private CGNode root;
	
	public MethodCG(MethodDeclaration node) {
		root = CGNodeFactory.makeCGNode(node);
	}
	
	public CGNode getRoot() {
		return root;
	}
	
	public void setRoot(CGNode root) {
		this.root = root;
	}
	
	public void makeCGDotFile() {
		try {
			String fileName = root.toString() + "(CG)";
			File file = new File(Config.DOTFILEPATH + fileName);
			FileWriter fw = new FileWriter(file, true);
			String dotFileContent = root.makeDotCG(new HashSet<CGNode>());
			fw.write("digraph G {\n" + dotFileContent + "}\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void makeICFGDotFile() {
		try {
			String fileName = root.toString() + "(ICFG)";
			File file = new File(Config.DOTFILEPATH + fileName);
			FileWriter fw = new FileWriter(file, true);
			String dotFileContent = root.makeDotICFG(new HashSet<CGNode>());
			fw.write("digraph G {\n" + dotFileContent + "}\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
