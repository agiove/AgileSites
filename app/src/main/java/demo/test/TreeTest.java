package demo.test;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;
import demo.element.Tree;

// this test must be run by AgileSites TestRunnerElement
@AddIndex("demo/tests.txt")
public class TreeTest extends TestElement {

	final static Log log = Log.getLog(TreeTest.class);
	
	Tree it;
	
	@Before
	public void setUp() {
		it = new Tree();
	}

	@Test
	public void test() {
		parse(it.apply(env("")));
		odump(log);		
		// TODO: test the resuls
	}

}
