package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import util.exceptions.InvalidPathException;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.cha.ClassHierarchyException;

public class TestsUtils {
	// Only get those programs within mainTest package.
	public static ArrayList<CGNode> getApplicationMethods(CGBuilder builder) 
			throws InvalidPathException, IOException, ClassHierarchyException, 
				   IllegalArgumentException, CallGraphBuilderCancelException {
		CallGraph graph = builder.buildCG();
		
		Iterator<CGNode> it = graph.iterator();
		ArrayList<CGNode> nodes = new ArrayList<CGNode>();
		
		while(it.hasNext()) {
			CGNode node = it.next();
			String signature = node.getMethod().getSignature();
			
			if(signature.startsWith("mainTest")) {
				nodes.add(node);
			}
		}
		
		return nodes;
	}
	
	public static LinkedHashMap<String, ArrayList<String>> toStringGraph(CGBuilder builder) throws 
			InvalidPathException, IOException, ClassHierarchyException, IllegalArgumentException, CallGraphBuilderCancelException {
		ArrayList<CGNode> nodes = TestsUtils.getApplicationMethods(builder);
		LinkedHashMap<String, ArrayList<String>> graph = new LinkedHashMap<String, ArrayList<String>>();
		
		for(CGNode node : nodes) {
			ArrayList<String> adjMethods = new ArrayList<String>();
			Iterator<CallSiteReference> it = node.iterateCallSites();
			
			while(it.hasNext()) {
				CallSiteReference ref = it.next();
				String signature = ref.getDeclaredTarget().getSignature();
				
				// Only gets application methods references.
				if(signature.startsWith("mainTest")) {
					adjMethods.add(signature);
				}
			}
			
			graph.put(node.getMethod().getSignature(), adjMethods);
		}
		
		return graph;
	}
	
	public static String fromCGToString(CGBuilder builder)
			throws InvalidPathException, IOException, ClassHierarchyException,
				   IllegalArgumentException, CallGraphBuilderCancelException{
		LinkedHashMap<String, ArrayList<String>> strGraph = toStringGraph(builder);
		Set<String> keys = strGraph.keySet();
		StringBuffer sb = new StringBuffer();
		
		for(String key : keys) {
			sb.append(key + "--> {");
			ArrayList<String> adjMethods = strGraph.get(key);
			
			for(int i = 0; i < adjMethods.size(); ++i) {
				String method = adjMethods.get(i);
				
				if (i == adjMethods.size() - 1) {
					sb.append(method + "}\n");
				} else {
					sb.append(method + ", ");
				}
			}
		}
		
		return sb.toString();		
	}
}
