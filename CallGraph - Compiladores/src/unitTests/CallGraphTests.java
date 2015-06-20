package unitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.ibm.wala.ipa.callgraph.CGNode;

import util.CGBuilder;
import util.TestsUtils;

public class CallGraphTests {
	final String USER_DIR = System.getProperty("user.dir");
	final String JAR_PATH = USER_DIR + File.separator + "MySample.jar";
	
	@Test
	public void successfulCGBuildTest() {
		try {
			CGBuilder builder = new CGBuilder(JAR_PATH);
			assertTrue(builder != null);
			builder.buildCG();			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void getApplicationMethodsTest() {
		try {
			CGBuilder builder = new CGBuilder(JAR_PATH);
			assertTrue(builder != null);
			ArrayList<CGNode> nodes = TestsUtils.getApplicationMethods(builder);
			assertFalse(nodes.isEmpty());
			
			// Every single sample is from this package (in this project, it only has one sample). 
			for(CGNode node : nodes) 
				assertTrue(node.getMethod().getSignature().startsWith("mainTest"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	// Testing Main.java methods.
	@Test
	public void checkMethodsSignaturesTest() {
		try {
			CGBuilder builder = new CGBuilder(JAR_PATH);
			assertTrue(builder != null);
			LinkedHashMap<String, ArrayList<String>> strGraph = TestsUtils.toStringGraph(builder);
			
			// Checking all nodes.
			assertTrue(strGraph.containsKey("mainTest.Main.main([Ljava/lang/String;)V"));
			assertTrue(strGraph.containsKey("mainTest.A.<init>(I)V"));
			assertTrue(strGraph.containsKey("mainTest.B.<init>(LmainTest/A;)V"));
			assertTrue(strGraph.containsKey("mainTest.A.inc()V"));
			assertTrue(strGraph.containsKey("mainTest.B.inc()V"));
			
			// Checking adjacent nodes.
			ArrayList<String> edges = strGraph.get("mainTest.Main.main([Ljava/lang/String;)V");
			assertFalse(edges.isEmpty());
			assertTrue(edges.contains("mainTest.A.<init>(I)V"));
			assertTrue(edges.contains("mainTest.B.<init>(LmainTest/A;)V"));
			assertTrue(edges.contains("mainTest.A.inc()V"));
			assertTrue(edges.contains("mainTest.B.inc()V"));
			
			edges = strGraph.get("mainTest.A.<init>(I)V");
			assertTrue(edges.isEmpty());
			
			edges = strGraph.get("mainTest.B.<init>(LmainTest/A;)V");
			assertTrue(edges.isEmpty());
			
			
			edges = strGraph.get("mainTest.A.inc()V");
			assertTrue(edges.isEmpty());
			
			
			edges = strGraph.get("mainTest.B.inc()V");
			assertFalse(edges.isEmpty());
			assertTrue(edges.contains("mainTest.A.inc()V"));			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
