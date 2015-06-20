package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import util.exceptions.InvalidPathException;

import com.ibm.wala.core.tests.callGraph.CallGraphTestUtil;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.FileProvider;

public class CGBuilder {
	private String jarPath;
	
	public CGBuilder(String jarPath) {
		this.jarPath = jarPath;
	}
	
	public CallGraph buildCG()
			throws InvalidPathException, IOException, ClassHierarchyException, 
				   IllegalArgumentException, CallGraphBuilderCancelException {
		File exclusionsFile = new File(jarPath);
		
		if(!exclusionsFile.exists()) {
			throw new InvalidPathException();
		}
		
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String[] splitted = jarPath.split(pattern);
		String appJar = splitted[splitted.length - 1];
		AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar, (new FileProvider()).getFile(CallGraphTestUtil.REGRESSION_EXCLUSIONS));
		ClassHierarchy hierarchy = ClassHierarchy.make(scope);
		Iterable<Entrypoint> entrypoints = Util.makeMainEntrypoints(scope, hierarchy);
		AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
		CallGraphBuilder cgBuilder = Util.makeZeroCFABuilder(options, new AnalysisCache(), hierarchy, scope);
		CallGraph cg = cgBuilder.makeCallGraph(options, null);
		
		return cg; 
	}
}
