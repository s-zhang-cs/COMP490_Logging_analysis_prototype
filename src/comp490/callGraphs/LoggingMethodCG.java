package comp490.callGraphs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import comp490.cfgs.Config;

/**
 * Wrapper class for CGNode(s) of logging method(s). It allows to create a call graph 
 * concerning all the logging methods, and output it as a dot file
 *
 */
public class LoggingMethodCG {
	
	private Set<CGNode> roots = new HashSet<>();;
	
	public void addRoot(CGNode node) {
		roots.add(node);
	}
	
	public Set<CGNode> getRoots() {
		return roots;
	}

	public void makeCGDotFile() {
		try {
			String fileName = "Logging Methods Call Graph";
			File file = new File(Config.DOTFILEPATH + fileName);
			FileWriter fw = new FileWriter(file, true);
			String dotFileContent = "";
			Set<CGNode> traversed = new HashSet<>();
			//same traversed set is reused for different roots, in order to avoid repetition
			for(CGNode root : roots) {
				dotFileContent += root.makeDotCG(traversed);
			}
			fw.write("digraph G {\n" + dotFileContent + "}\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
